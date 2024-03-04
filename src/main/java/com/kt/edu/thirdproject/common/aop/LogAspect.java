package com.kt.edu.thirdproject.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Profile("prd")
@Slf4j
public class LogAspect {

    @Pointcut("within(com.kt.edu.thirdproject.employee..*)")
    private void all() {}

    @Pointcut("within(com.kt.edu.thirdproject.employee..controller..*)")
    public void controller() {}

    @Pointcut("within(com.kt.edu.thirdproject.employee.service..*)")
    public void service() {}

    @Before("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void transactionalAnnotation(JoinPoint joinPoint) {
        log.info("#### transactionalAnnotation #### [transaction] {} args = {} ", joinPoint.getSignature(), joinPoint.getArgs());
    }

    @Before("@annotation(Ktedu)")
    public void kteduAnnotation(JoinPoint joinPoint) {
        log.info("#### KteduAnnotation ####[Ktedu] {} args = {}", joinPoint.getSignature(), joinPoint.getArgs());
    }

    @Around("all()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        log.info("start = {}", joinPoint.toString());

        try {
            return joinPoint.proceed();
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            log.info("log = {}", joinPoint.getSignature());
            log.info("timeMs = {}", timeMs);
        }
    }

    @Before("controller() || service()")
    public void before(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        log.info("before = {}", method.getName());
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg != null) {
                log.info("type = {}", arg.getClass().getSimpleName());
                log.info("value = {}", arg);
            }
        }
    }

    @After("controller() || service()")
    public void after(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        log.info("after = {}", method.getName());
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg != null) {
                log.info("type = {}", arg.getClass().getSimpleName());
                log.info("value = {}", arg);
            }
        }
    }
}
