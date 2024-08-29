package com.creativedesignproject.kumoh_board_backend.auth.presentation.command;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.creativedesignproject.kumoh_board_backend.common.exception.BadRequestException;
import com.creativedesignproject.kumoh_board_backend.common.exception.ErrorCode;
import com.creativedesignproject.kumoh_board_backend.common.util.PasswordValidator;
import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.Certification;
import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.PasswordResetToken;
import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.RefreshToken;
import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.User;
import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.VerificationCode;
import com.creativedesignproject.kumoh_board_backend.auth.domain.repository.CertificationRepository;
import com.creativedesignproject.kumoh_board_backend.auth.domain.repository.PasswordResetTokenRepository;
import com.creativedesignproject.kumoh_board_backend.auth.domain.repository.RefreshTokenRepository;
import com.creativedesignproject.kumoh_board_backend.auth.domain.repository.UserRepository;
import com.creativedesignproject.kumoh_board_backend.auth.dto.request.ChangeNicknameRequestDto;
import com.creativedesignproject.kumoh_board_backend.auth.dto.request.ChangePasswordRequestDto;
import com.creativedesignproject.kumoh_board_backend.auth.dto.request.EmailCertificationRequestDto;
import com.creativedesignproject.kumoh_board_backend.auth.dto.request.LoginRequestDto;
import com.creativedesignproject.kumoh_board_backend.auth.dto.request.SignUpRequestDto;
import com.creativedesignproject.kumoh_board_backend.auth.dto.request.UserIdCheckRequestDto;
import com.creativedesignproject.kumoh_board_backend.auth.dto.response.LoginResponseDto;
import com.creativedesignproject.kumoh_board_backend.auth.infrastructure.dto.response.TokenResponse;
import com.creativedesignproject.kumoh_board_backend.auth.infrastructure.provider.JwtTokenProvider;
import com.creativedesignproject.kumoh_board_backend.auth.service.MailClient;
import com.creativedesignproject.kumoh_board_backend.auth.service.VerificationCodeProvider;

import lombok.RequiredArgsConstructor;

/**
 * ApplicationEventPublisher : Spring 프레임워크에서 이벤트를 발행하는 역할을 하는 인터페이스인데 이를 통해서 애플리
 * 케이션 내에 이벤트 기반의 비동기 통신을 구현할 수 있음 이를 사용하면 이벤트 기반의 비동기 통신을 구현할 수 있다.
 * 또한 이를 사용하면 특정 작업이 완료된 후에 이벤트를 발행해서 다른 컴포넌트가 이를 처리할 수 있도록 할 수 있다.
 */

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final Clock clock;
    private final CertificationRepository certificationRepository;
    private final MailClient mailClient;
    private final VerificationCodeProvider verificationCodeProvider;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository passwordResetTokenRepository;


    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final long LOCK_TIME_DURATION = 15 * 60 * 1000; // 15분
    

    @Transactional
    public void emailCertification(EmailCertificationRequestDto dto) {
        Certification certification = createCode(dto.getEmail());
        sendMail(certification);
        certificationRepository.save(certification);
    }

    private Certification createCode(String email) {
        VerificationCode verificationCode = verificationCodeProvider.provide();
        userRepository.findByEmail(email).ifPresent(user -> { throw new BadRequestException(ErrorCode.EMAIL_DUPLICATED); });

        Certification certification = Certification.builder()
                .email(email)
                .verificationCode(verificationCode)
                .build();

        return certification;
    }

    private void sendMail(Certification certification) {
        mailClient.sendMail(mail -> {
            mail.setTo(certification.getEmail());
            mail.setSubject("[금오 보드] 이메일 인증 코드");
            mail.setText("""
                금오 보드 이메일 인증 코드입니다.
                Code는 다음과 같습니다.
                %s
                """.formatted(certification.getVerificationCode()));
        });
    }

    @Transactional
    public void signUp(SignUpRequestDto dto) {
        String userId = dto.getUserId();
        String email = dto.getEmail();
        String certificationNumber = dto.getCertificationNumber();
        String nickName = dto.getNickName();
        String password = dto.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        String profileImage = dto.getProfileImage();

        if(userRepository.findByUserId(userId) != null)
            throw new BadRequestException(ErrorCode.USER_ID_DUPLICATED);

        if (userRepository.findByNickname(nickName) != null)
            throw new BadRequestException(ErrorCode.NICKNAME_DUPLICATED);

        Certification certification = certificationRepository.findByEmail(email);

        if (certification == null) throw new BadRequestException(ErrorCode.CERTIFICATION_FAIL);

        boolean isMatched = certification.getEmail().equals(email)
                && certification.getVerificationCode().equals(certificationNumber);
        if (!isMatched) throw new BadRequestException(ErrorCode.CERTIFICATION_MISSMATCHING);

        User user = User.builder()
            .nickname(nickName)
            .userId(userId)
            .password(encodedPassword)
            .email(email)
            .role("ROLE_USER")
            .profileImage(profileImage)
            .build();
        
        userRepository.save(user);
        certificationRepository.deleteByEmail(email);
    }

    @Transactional
    public LoginResponseDto signIn(LoginRequestDto dto) {
        String userId = dto.userId();
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new BadRequestException(ErrorCode.USER_NOT_FOUND));

        if (user.isAccountLocked()) {
            if (unlockWhenTimeExpired(user)) {
                user.resetFailedAttempts();
            } else {
                throw new BadRequestException(ErrorCode.ACCOUNT_LOCKED);
            }
        }

        String password = dto.password();
        String encodedPassword = user.getPassword();
        boolean isMatched = passwordEncoder.matches(password, encodedPassword);
        
        if (!isMatched) {
            user.increaseFailedAttempts();
            if (user.getFailedAttempts() >= MAX_FAILED_ATTEMPTS) {
                user.lockAccount();
            }
            throw new BadRequestException(ErrorCode.PASSWORD_NOT_MATCHED);
        }

        user.resetFailedAttempts();
        
        // TokenResponse tokenResponse = jwtTokenProvider.provide(60L, jwtBuilder -> jwtBuilder
        //     .setSubject(user.getId().toString())
        //     .claim("role", user.getRole())
        // );
        RefreshToken refreshToken = saveRefreshToken(user.getId());
        return new LoginResponseDto(
            user.getId(),
            user.getNickname(),
            user.getProfileImage(),
            refreshToken.getId(),
            refreshToken.getExpiredAt()
        );
    }

    private RefreshToken saveRefreshToken(Long userId) {
        return refreshTokenRepository.save(RefreshToken.of(userId, LocalDateTime.now(clock)));
    }

    public void logout(Long Id, UUID refreshTokenId) {
        refreshTokenRepository.findById(refreshTokenId)
            .ifPresent(refreshToken -> {
                if(refreshToken.isOwner(Id)) {
                    refreshTokenRepository.deleteById(refreshTokenId);
                }
            });
    }

    private boolean unlockWhenTimeExpired(User user) {
        long lockTimeInMillis = user.getLockTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long currentTimeInMillis = System.currentTimeMillis();

        if (lockTimeInMillis + LOCK_TIME_DURATION < currentTimeInMillis) {
            user.setAccountLocked(false);
            return true;
        }
        return false;
    }

    public void checkUserId(UserIdCheckRequestDto dto) {
        if (userRepository.findByUserId(dto.getUserId()) != null)
            throw new BadRequestException(ErrorCode.USER_ID_DUPLICATED);
    }

    @Transactional
    public void changePassword(String userId, ChangePasswordRequestDto dto) {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new BadRequestException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new BadRequestException(ErrorCode.PASSWORD_NOT_MATCHED);
        }

        if (passwordEncoder.matches(dto.getNewPassword(), user.getPassword())) {
            throw new BadRequestException(ErrorCode.PASSWORD_REUSED);
        }

        if (!PasswordValidator.isValidPassword(dto.getNewPassword())) {
            throw new BadRequestException(ErrorCode.WEAK_PASSWORD);
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        sendPasswordChangeEmail(user.getEmail()); // TODO : 비밀번호 변경 후 사용자에게 이메일 전송 기능 개선 필요
        // TODO : 비밀번호 변경 후 사용자 세션 처리
    }

    private void sendPasswordChangeEmail(String email) {
        mailClient.sendMail(mail -> {
            mail.setTo(email);
            mail.setSubject("비밀번호 변경 알림");
            mail.setText("비밀번호가 성공적으로 변경되었습니다.");
        });
    }

    @Transactional
    public void changeNickname(String userId, ChangeNicknameRequestDto dto) {
        User nickname = userRepository.findByNickname(dto.getNewNickname());
        if (nickname != null) throw new BadRequestException(ErrorCode.NICKNAME_DUPLICATED);

        User user = userRepository.findByUserId(userId).orElseThrow(() -> new BadRequestException(ErrorCode.USER_NOT_FOUND));

        user.setNickname(dto.getNewNickname());
    }

    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BadRequestException(ErrorCode.USER_NOT_FOUND));
        
        String token = generateToken();
        PasswordResetToken resetToken = new PasswordResetToken(token, user);
        passwordResetTokenRepository.save(resetToken);

        String resetLink = "https://yourapp.com/reset-password?token=" + token;
        sendPasswordResetEmail(user.getEmail(), resetLink);
    }

    private void sendPasswordResetEmail(String email, String resetLink) {
        mailClient.sendMail(mail -> {
            mail.setTo(email);
            mail.setSubject("[금오 보드] 비밀번호 재설정 요청");
            mail.setText("비밀번호 재설정 링크: " + resetLink);
        });
    }

    private String generateToken() {
        return UUID.randomUUID().toString(); // 토큰 생성
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token);
        if (resetToken == null || resetToken.isExpired()) {
            throw new BadRequestException(ErrorCode.INVALID_OR_EXPIRED_TOKEN);
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));

        passwordResetTokenRepository.delete(resetToken); // 토큰 사용 후 삭제
    }
}
