package chj.idler.util;

import chj.idler.response.BusinessException;
import chj.idler.response.EmBusinessError;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class PermissionAspect {
//    @Pointcut(value = "execution(* chj.idler.controller..VideoController.*(..))")
//    public void loginVerify(){};

    @Pointcut(value="@annotation(chj.idler.annotation.ValidatePermission)")
    public void permissonCut(){};

    @Before(value = "permissonCut()")
    public void beforeVideo()throws BusinessException, JsonProcessingException {
        ServletRequestAttributes attributes=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request=attributes.getRequest();
        String token=request.getHeader("token");
        if(StringUtils.isEmpty(token)||StringUtils.isEmpty(JedisClusterUtil.get(token)))throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        JedisClusterUtil.expire(token,600);
    }
}
