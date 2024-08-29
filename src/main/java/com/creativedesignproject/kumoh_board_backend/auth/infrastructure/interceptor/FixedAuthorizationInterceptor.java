package com.creativedesignproject.kumoh_board_backend.auth.infrastructure.interceptor;

import org.springframework.util.Assert;
import org.springframework.web.servlet.HandlerInterceptor;

import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.Role;
import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.authentication.Authentication;
import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.extractor.AuthenticationTokenExtractor;
import com.creativedesignproject.kumoh_board_backend.auth.infrastructure.context.AuthenticateContext;
import com.creativedesignproject.kumoh_board_backend.auth.presentation.HttpRequestTokenExtractor;
import com.creativedesignproject.kumoh_board_backend.common.exception.ErrorCode;
import com.creativedesignproject.kumoh_board_backend.common.exception.ForbiddenException;
import com.creativedesignproject.kumoh_board_backend.common.exception.UnauthorizedException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FixedAuthorizationInterceptor implements HandlerInterceptor {
    private final HttpRequestTokenExtractor httpRequestTokenExtractor;
    private final AuthenticationTokenExtractor authenticationTokenExtractor;
    private final AuthenticateContext authenticateContext;
    private final Role role;
    
    public FixedAuthorizationInterceptor(
        HttpRequestTokenExtractor httpRequestTokenExtractor,
        AuthenticationTokenExtractor authenticationTokenExtractor,
        AuthenticateContext authenticateContext,
        Role role
    ) {
        Assert.notNull(httpRequestTokenExtractor, "httpRequestTokenExtractor must not be null");
        Assert.notNull(authenticationTokenExtractor, "authenticationTokenExtractor must not be null");
        Assert.notNull(authenticateContext, "authenticateContext must not be null");
        Assert.notNull(role, "role must not be null");
        this.httpRequestTokenExtractor = httpRequestTokenExtractor;
        this.authenticationTokenExtractor = authenticationTokenExtractor;
        this.authenticateContext = authenticateContext;
        this.role = role;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = httpRequestTokenExtractor.extract(request).orElseThrow(() -> new UnauthorizedException(ErrorCode.NEED_AUTH_TOKEN));
        Authentication authentication = authenticationTokenExtractor.extract(token);
        if (authentication.getRole() != role) throw new ForbiddenException(ErrorCode.NO_PERMISSION);
        authenticateContext.setAuthentication(authentication);
        return true;
	}
}
