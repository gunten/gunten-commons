package org.tp.mix.aspect;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.tp.annotation.cache.TPCacheClean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

/**
 * TODO 缓存工具要用具体场景的替代掉
 */
@Aspect
@Component
@Slf4j
@Log4j
public class CreditCacheCleanAspect {

    @Around("@annotation(org.tp.annotation.cache.TPCacheClean)")
    public Object cacheCleanInvocation(ProceedingJoinPoint joinPoint) throws Throwable {
        TPCacheClean cache = getCacheAnnotation(joinPoint, TPCacheClean.class);

        String[] domains = cache.domains();
        boolean allEntries = cache.allEntries();
        boolean beforeInvocation = cache.beforeInvocation();
        Object result = ObjectUtils.NULL;

        if(beforeInvocation){
            result = joinPoint.proceed();
        }

        if( condition(joinPoint, cache) ) {
            String parsedKey = getKey(joinPoint, cache);
//            TODO
            /*CommonRedisUtil redisUtil = CommonRedisUtil.getInstance();

            if (allEntries) {
                redisUtil.del(domains);
            } else {
                Arrays.stream(domains).forEach(domain -> redisUtil.hdel(domain, parsedKey));
            }*/
        }

        if (!beforeInvocation) {
            result = joinPoint.proceed();
        }
        return result;
    }

    private boolean condition(ProceedingJoinPoint joinPoint, TPCacheClean cache) {
        return Boolean.TRUE.equals(parseExpression(cache.condition(), joinPoint, Boolean.class));
    }

    private String getKey(ProceedingJoinPoint joinPoint, TPCacheClean cache) {
        String parsedKey = parseExpression(cache.key(), joinPoint, String.class);
        if(StringUtils.isBlank(parsedKey)){
            String methodName = joinPoint.getSignature().getName();
            int argsHash = generateHashForArgs(joinPoint.getArgs());
            return  ":" + methodName + ":" + argsHash;
        }else{
            return  ":" + parsedKey;
        }
    }

    @Nullable
    private <T> T parseExpression(String expression, ProceedingJoinPoint joinPoint, Class<T> desiredResultType) {
        if (StringUtils.isBlank(expression)) {
            return null;
        }
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        String[] argumentNames = ((CodeSignature) signature).getParameterNames();
        Object[] argumentValues = joinPoint.getArgs();

        ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
        EvaluationContext context =new MethodBasedEvaluationContext(joinPoint.getTarget(),methodSignature.getMethod(),argumentValues,parameterNameDiscoverer);
        for (int i = 0; i < argumentNames.length; i++) {
            context.setVariable(argumentNames[i], argumentValues[i]);
        }

        ExpressionParser parser =new SpelExpressionParser();
        return parser.parseExpression(expression).getValue(context, desiredResultType);
    }

    private int generateHashForArgs(Object[] args) {
        return Objects.hash(args);
    }

    private <T extends Annotation> T getCacheAnnotation(ProceedingJoinPoint joinPoint, Class<T> clazz) throws NoSuchMethodException {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        Class<?>[] parameterTypes = signature.getMethod().getParameterTypes();
        Method method = joinPoint.getTarget().getClass().getMethod(methodName,parameterTypes);
        return method.getAnnotation(clazz);
    }

}