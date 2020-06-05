package chj.idler.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
public class LogAspect {

    private final Logger logger = LoggerFactory.getLogger(LogAspect.class);
    //public方法-任意返回值类型-所切包名(..)当前包及子包-任意类名-任何方法(..)任意参数
    @Pointcut("execution(public * chj.idler.controller..*.*(..))")
    public void controllerLog(){};


    @Before(value = "controllerLog()")
    public void beforeCon(JoinPoint joinPoint){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        logger.info("################URL : " + request.getRequestURL().toString());
        logger.info("################HTTP_METHOD : " + request.getMethod());
        logger.info("################IP : " + request.getRemoteAddr());
        logger.info("################THE ARGS OF THE CONTROLLER : " + Arrays.toString(joinPoint.getArgs()));
        logger.info("################CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
    }
    @AfterReturning(value = "controllerLog()",returning="returnObj")
    public void returnCon(JoinPoint joinPoint, Object returnObj) {
        logger.info("################RETURN_DATA : "+returnObj.toString());
    }

    @AfterThrowing(value = "controllerLog()",throwing = "throwable")
    public void throwCon(Throwable throwable){
        logger.error(throwable.getMessage(),throwable);
    }

    @AfterThrowing(pointcut = "execution(public * chj.idler.util..*.*(..))",throwing = "throwable")
    public void throwC(Throwable throwable){
        logger.error(throwable.getMessage(),throwable);
    }
}
