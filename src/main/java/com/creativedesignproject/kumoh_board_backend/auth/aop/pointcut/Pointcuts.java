package com.creativedesignproject.kumoh_board_backend.auth.aop.pointcut;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {
    // com.creativedesignproject.kumoh_board_backend.auth 패키지와 하위 패키지
    @Pointcut("execution(* com.creativedesignproject.kumoh_board_backend.auth..*(..))")
    public void allAuth(){} // pointcut signature

    // 클래스 이름 패턴이 *Service
    @Pointcut("execution(* *..*Service.*(..))")
    public void allService(){}

    // allAuth && allService
    @Pointcut("allAuth() && allService()")
    public void authAndService() {}
}
