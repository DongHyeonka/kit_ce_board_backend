package com.creativedesignproject.kumoh_board_backend.auth.infrastructure.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.authentication.Authentication;
import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.authentication.UserAuthentication;
import com.creativedesignproject.kumoh_board_backend.auth.infrastructure.context.AuthenticateContext;
import com.creativedesignproject.kumoh_board_backend.common.exception.UnexpectedException;

public class UserAuthenticationArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthenticateContext authenticateContext;

    public UserAuthenticationArgumentResolver(AuthenticateContext authenticateContext) {
        Assert.notNull(authenticateContext, "The authenticateContext must not be null");
        this.authenticateContext = authenticateContext;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserAuthentication.class);
    }

    @Override
    public UserAuthentication resolveArgument(
        MethodParameter parameter, 
        ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, 
        WebDataBinderFactory binderFactory
    ) {
        Authentication authentication = authenticateContext.getAuthentication();
        if (authentication instanceof UserAuthentication userAuthentication) {
            return userAuthentication;
        }
        throw new UnexpectedException("인가된 권한이 인자의 권한과 맞지 않습니다.");
    }
    
}
