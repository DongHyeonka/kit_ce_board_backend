package com.creativedesignproject.kumoh_board_backend.auth.infrastructure.interceptor;

import org.springframework.util.Assert;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.creativedesignproject.kumoh_board_backend.auth.annotation.Authorization;
import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.authentication.Authentication;
import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.extractor.AuthenticationTokenExtractor;
import com.creativedesignproject.kumoh_board_backend.auth.infrastructure.context.AuthenticateContext;
import com.creativedesignproject.kumoh_board_backend.auth.presentation.HttpRequestTokenExtractor;
import com.creativedesignproject.kumoh_board_backend.common.exception.ErrorCode;
import com.creativedesignproject.kumoh_board_backend.common.exception.ForbiddenException;
import com.creativedesignproject.kumoh_board_backend.common.exception.UnauthorizedException;
import com.creativedesignproject.kumoh_board_backend.common.exception.UnexpectedException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AnnotationAuthorizationInterceptor implements HandlerInterceptor {
    private final HttpRequestTokenExtractor httpRequestTokenExtractor;
    private final AuthenticationTokenExtractor authenticationTokenExtractor;
    private final AuthenticateContext authenticateContext;

    public AnnotationAuthorizationInterceptor(
        HttpRequestTokenExtractor httpRequestTokenExtractor,
        AuthenticationTokenExtractor authenticationTokenExtractor,
        AuthenticateContext authenticateContext
    ) {
        Assert.notNull(httpRequestTokenExtractor, "httpRequestTokenExtractor must not be null");
        Assert.notNull(authenticationTokenExtractor, "authenticationTokenExtractor must not be null");
        Assert.notNull(authenticateContext, "authenticateContext must not be null");
        this.httpRequestTokenExtractor = httpRequestTokenExtractor;
        this.authenticationTokenExtractor = authenticationTokenExtractor;
        this.authenticateContext = authenticateContext;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Authorization authorization = handlerMethod.getMethodAnnotation(Authorization.class);
        if( authorization == null ) throw new UnexpectedException("Authorization annotation is not found");
        String token = httpRequestTokenExtractor.extract(request).orElseThrow(() -> new UnauthorizedException(ErrorCode.NEED_AUTH_TOKEN));
        Authentication authentication = authenticationTokenExtractor.extract(token);
        if(authentication.getRole() != authorization.role()) throw new ForbiddenException(ErrorCode.NO_PERMISSION);
        authenticateContext.setAuthentication(authentication);
        return true;
    }
}
