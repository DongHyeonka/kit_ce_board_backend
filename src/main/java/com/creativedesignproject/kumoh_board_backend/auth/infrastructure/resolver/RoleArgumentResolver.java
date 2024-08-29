package com.creativedesignproject.kumoh_board_backend.auth.infrastructure.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.Role;
import com.creativedesignproject.kumoh_board_backend.auth.infrastructure.context.AuthenticateContext;
import com.creativedesignproject.kumoh_board_backend.common.exception.UnexpectedException;

public class RoleArgumentResolver implements HandlerMethodArgumentResolver {

    private final Role role;
    private final AuthenticateContext authenticateContext;

    public RoleArgumentResolver(Role role, AuthenticateContext authenticateContext) {
        Assert.notNull(authenticateContext, "The authenticateContext must not be null");
        Assert.notNull(role, "The role must not be null");
        this.role = role;
        this.authenticateContext = authenticateContext;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Long.class) && parameter.hasParameterAnnotation(
            role.getAnnotation());
    }

    @Override
    public Long resolveArgument(
        MethodParameter parameter, 
        ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, 
        WebDataBinderFactory binderFactory
        ) {
        if (authenticateContext.getRole() != this.role)
            throw new UnexpectedException("인가된 권한이 인자의 권한과 맞지 않습니다.");
        return authenticateContext.getId();
    }
    
}
