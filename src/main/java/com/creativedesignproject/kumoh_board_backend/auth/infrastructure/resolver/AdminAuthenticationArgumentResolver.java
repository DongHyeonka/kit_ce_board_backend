package com.creativedesignproject.kumoh_board_backend.auth.infrastructure.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.authentication.AdminAuthentication;
import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.authentication.Authentication;
import com.creativedesignproject.kumoh_board_backend.auth.infrastructure.context.AuthenticateContext;
import com.creativedesignproject.kumoh_board_backend.common.exception.UnexpectedException;

/**
 * AdminAuthenticationArgumentResolver : Spring MVC에서 컨트롤러 메서드 인자로 AdminAuthentication 객체를 자동으로 주입하기 위한 클래스임
 * authenticateContext : 인증 정보를 가져오기 위한 변수 이는 변경이 되면 안되기 때문에 final로 선언함
 * 생성자를 통해서 null인지 아닌지 확인후 초기화 진행
 * Assert.notNull : 객체가 null인지 확인하고 null인 경우 IllegalArgumentException 예외 던짐 -> 간단하게 확인하기 좋은 듯 프론트가 몰라도 되는 경우
 * supportsParameter : 주어진 메서드 인자가 AdminAuthentication 타입인지 확인하는 메서드
 * resolveArgument : 인증 정보를 가져온 후 AdminAuthentication 객체를 반환하는 메서드 그게 아니면 예외 반환
 */
public class AdminAuthenticationArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthenticateContext authenticateContext;

    public AdminAuthenticationArgumentResolver(AuthenticateContext authenticateContext) {
        Assert.notNull(authenticateContext, "The authenticateContext must not be null");
        this.authenticateContext = authenticateContext;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(AdminAuthentication.class);
    }

    @Override
    public AdminAuthentication resolveArgument(
        MethodParameter parameter, 
        ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, 
        WebDataBinderFactory binderFactory
        ) {
        Authentication authentication = authenticateContext.getAuthentication();
        if (authentication instanceof AdminAuthentication adminAuthentication) {
            return adminAuthentication;
        }
        throw new UnexpectedException("인가된 권한이 인자의 권한과 맞지 않습니다.");
    }  
}
