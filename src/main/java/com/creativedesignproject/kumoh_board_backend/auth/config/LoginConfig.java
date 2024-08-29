package com.creativedesignproject.kumoh_board_backend.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.creativedesignproject.kumoh_board_backend.auth.annotation.Authorization;
import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.Role;
import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.extractor.AuthenticationTokenExtractor;
import com.creativedesignproject.kumoh_board_backend.auth.infrastructure.context.AuthenticateContext;
import com.creativedesignproject.kumoh_board_backend.auth.infrastructure.extractor.CompositeHttpRequestTokenExtractor;
import com.creativedesignproject.kumoh_board_backend.auth.infrastructure.extractor.HeaderHttpRequestTokenExtractor;
import com.creativedesignproject.kumoh_board_backend.auth.infrastructure.interceptor.AnnotationAuthorizationInterceptor;
import com.creativedesignproject.kumoh_board_backend.auth.infrastructure.interceptor.FixedAuthorizationInterceptor;
import com.creativedesignproject.kumoh_board_backend.auth.infrastructure.resolver.AdminAuthenticationArgumentResolver;
import com.creativedesignproject.kumoh_board_backend.auth.infrastructure.resolver.RoleArgumentResolver;
import com.creativedesignproject.kumoh_board_backend.auth.infrastructure.resolver.UserAuthenticationArgumentResolver;
import com.creativedesignproject.kumoh_board_backend.common.interceptor.AnnotationDelegateInterceptor;
import com.creativedesignproject.kumoh_board_backend.common.interceptor.HttpMethodDelegateInterceptor;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class LoginConfig implements WebMvcConfigurer {
    private final AuthenticationTokenExtractor userAuthenticationTokenExtractor;
    private final AuthenticationTokenExtractor adminAuthenticationTokenExtractor;
    private final AuthenticationTokenExtractor compositeAuthenticationTokenExtractor;
    private final AuthenticateContext authenticateContext;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new RoleArgumentResolver(Role.USER, authenticateContext));
        resolvers.add(new RoleArgumentResolver(Role.ADMIN, authenticateContext));
        resolvers.add(new UserAuthenticationArgumentResolver(authenticateContext));
        resolvers.add(new AdminAuthenticationArgumentResolver(authenticateContext));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(HttpMethodDelegateInterceptor.builder()
                .allowMethod(HttpMethod.GET, HttpMethod.POST, HttpMethod.DELETE, HttpMethod.PUT, HttpMethod.PATCH)
                .interceptor(adminFixedAuthorizationInterceptor())
                .build())
            .addPathPatterns("/api/admin/**")
            .excludePathPatterns("/api/admin/v1/login", "/api/admin/v1/initialize");
        registry.addInterceptor(HttpMethodDelegateInterceptor.builder()
                .allowMethod(HttpMethod.GET, HttpMethod.POST, HttpMethod.DELETE, HttpMethod.PUT, HttpMethod.PATCH)
                .interceptor(userFixedAuthorizationInterceptor())
                .build())
            .addPathPatterns("/api/user/**")
            .excludePathPatterns("/api/user/v1/login");
        registry.addInterceptor(AnnotationDelegateInterceptor.builder()
                .annotation(Authorization.class)
                .interceptor(annotationAuthorizationInterceptor())
                .build())
            .addPathPatterns("/api/**");
    }

    @Bean
    public FixedAuthorizationInterceptor adminFixedAuthorizationInterceptor() {
        return new FixedAuthorizationInterceptor(
            compositeHttpRequestTokenExtractor(),
            adminAuthenticationTokenExtractor,
            authenticateContext,
            Role.ADMIN
        );
    }

    @Bean
    public FixedAuthorizationInterceptor userFixedAuthorizationInterceptor() {
        return new FixedAuthorizationInterceptor(
            compositeHttpRequestTokenExtractor(),
            userAuthenticationTokenExtractor,
            authenticateContext,
            Role.USER
        );
    }

    @Bean
    public AnnotationAuthorizationInterceptor annotationAuthorizationInterceptor() {
        return new AnnotationAuthorizationInterceptor(
            compositeHttpRequestTokenExtractor(),
            compositeAuthenticationTokenExtractor,
            authenticateContext
        );
    }

    @Bean
    public CompositeHttpRequestTokenExtractor compositeHttpRequestTokenExtractor() {
        return new CompositeHttpRequestTokenExtractor(
            List.of(
                new HeaderHttpRequestTokenExtractor()
            )
        );
    }
}
