package com.negongal.hummingbird.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class TimeTraceAop {

    @Around("execution(* com.negongal.hummingbird.domain..*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        Long startTime = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } finally {
            Long finishTime = System.currentTimeMillis();
            Long executeTime = finishTime - startTime;
            log.info("{}의 메소드 실행 시간 : {}ms", joinPoint.getSignature().getName(), executeTime);
        }
    }
}
