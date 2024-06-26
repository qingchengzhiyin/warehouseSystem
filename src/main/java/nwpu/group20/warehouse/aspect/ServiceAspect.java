package nwpu.group20.warehouse.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nwpu.group20.warehouse.http.HttpResult;
import nwpu.group20.warehouse.service.impl.UserServiceImpl;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 服务的日志切面，用于记录现在进入哪一个方法，方便后期排错
 * 也可以计时，后期优化用
 */
@Component
@Aspect
@Slf4j
public class ServiceAspect {
    @Autowired
    private ObjectMapper objectMapper;
    @Around("execution(* nwpu.group20.warehouse.service.impl..*(..))")
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable{
        log.info("进入方法 <类>{},<方法>{},<参数>{}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature(),
                objectMapper.writeValueAsString(joinPoint.getArgs()));
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        log.info("(离开方法)<类>{}<方法>{}<出参>{}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                objectMapper.writeValueAsString(result));
        log.info("用时{} <类>{} <方法>{}",
                String.valueOf(endTime - startTime),
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName());
        return result;
    }
}
