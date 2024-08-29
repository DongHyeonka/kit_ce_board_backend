package com.creativedesignproject.kumoh_board_backend.common.interceptor;

import static java.util.stream.Collectors.toUnmodifiableSet;

import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

/**
 * HandlerInterceptor를 확장하여 HTTP 요청을 처리하는데 특정 HTTP 메서드들에 대해서만 다른 인터셉터를 위임해 동작하도록 하는 역할
 * 우선 HandlerInterceptor의 역할은 Spring MVC에서 요청이 컨트롤러에 도달하기 전, 후, 끝 후에 커스텀된 로직을 수행하기 위해 사용
 * allowMethods : 허용된 HTTP 메서드들을 저장하는 Set이다. 이 메서드들에 대해 요청이 오면 다른 인터셉터로 위임된다.
 * Interceptor : 실제로 위임될 interceptor임 특정 HTTP 메서드에 대해서만 이 인터셉터가 동작하도록 할 것임.
 * 생성자 : 외부에서 직접 사용하지 않도록 제한 하며 빌더 패턴을 통해서 객체 생성
 * preHandle : 컨트롤러가 실행되기 전 호출됨 요청에서 allowMethods에 포함되어 있는지 확인 포함되어 있으면 
 * 지정된 interceptor로 preHandle 메서드를 호출해서 그 결과를 반환
 * 포함되어 있지 않으면 true를 반환하여 요청을 계속 진행
 * allowMethod : 이 메서드는 허용할 HTTP 메서드를 설정 가변 인자를 사용해서 한 번에 여러 메서드를 추가할 수 있음.
 * -> 빌더 객체를 반환해서 메서드 체이닝을 가능하게 한다.
 * build : HttpMethodDelegateInterceptor 객체를 생성하고 허용된 HTTP 메서드의 이름을 String 로 바꾸고 불변 set으로 저장 (toUnmodifiableSet)
 */

public class HttpMethodDelegateInterceptor implements HandlerInterceptor {
    private final Set<String> allowMethods;
    private final HandlerInterceptor interceptor;

    protected HttpMethodDelegateInterceptor(Set<String> allowMethods, HandlerInterceptor interceptor) {
        this.allowMethods = allowMethods;
        this.interceptor = interceptor;
    }

    public static HttpMethodDelegateInterceptorBuilder builder() {
        return new HttpMethodDelegateInterceptorBuilder();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        if (allowMethods.contains(request.getMethod())) {
            return interceptor.preHandle(request, response, handler);
        }
        return true;
    }

    public static class HttpMethodDelegateInterceptorBuilder {

        private final Set<HttpMethod> allowMethod = new HashSet<>();
        private HandlerInterceptor interceptor;

        public HttpMethodDelegateInterceptorBuilder allowMethod(HttpMethod... httpMethods) {
            allowMethod.addAll(Arrays.asList(httpMethods));
            return this;
        }

        public HttpMethodDelegateInterceptorBuilder interceptor(HandlerInterceptor interceptor) {
            this.interceptor = interceptor;
            return this;
        }

        public HttpMethodDelegateInterceptor build() {
            Set<String> methods = allowMethod.stream()
                .map(HttpMethod::name)
                .collect(toUnmodifiableSet());
            return new HttpMethodDelegateInterceptor(methods, interceptor);
        }
    }
}
