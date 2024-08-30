package com.creativedesignproject.kumoh_board_backend.auth.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(1) // AOP 적용 순서
public class AuthLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(AuthLoggingAspect.class);

    @Before("execution(* com.creativedesignproject.kumoh_board_backend.auth..*(..))")
    public void logBeforeMethod(JoinPoint joinPoint) {
        logger.info("메서드 진입 : {}", joinPoint.getSignature().toShortString());
        logger.info("인자: {}", joinPoint.getArgs());
    }

    @AfterReturning(pointcut = "execution(* com.creativedesignproject.kumoh_board_backend.auth..*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        logger.info("메서드 종료: {}", joinPoint.getSignature().toShortString());
        logger.info("반환 값: {}", result);
    }

    @AfterThrowing(pointcut = "execution(* com.creativedesignproject.kumoh_board_backend.auth..*(..))", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        logger.error("예외 메서드: {}", joinPoint.getSignature().toShortString(), exception);
    }

    @After("execution(* com.creativedesignproject.kumoh_board_backend.auth..*(..))")
    public void doAfter(JoinPoint joinPoint) {
        logger.info("[후처리] {}", joinPoint.getSignature());
    }
}
