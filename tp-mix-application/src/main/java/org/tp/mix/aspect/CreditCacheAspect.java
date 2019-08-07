package org.tp.mix.aspect;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
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
import org.springframework.stereotype.Component;
import org.tp.SerializeUtil;
import org.tp.annotation.cache.TPCacheClean;
import org.tp.annotation.cache.TPCacheable;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.io.NotSerializableException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * TODO 注解属性还不够丰富，缓存工具要用具体场景的替代掉
 */
@Aspect
@Component
@Slf4j
public class CreditCacheAspect {

    @Around("@annotation(org.tp.annotation.cache.TPCacheable)")
    public Object cacheableInvocation(ProceedingJoinPoint joinPoint) throws Throwable {
        TPCacheable cache = getCacheAnnotation(joinPoint, TPCacheable.class);

        List<String> keyList = getKeyList(joinPoint, cache);

        List<Object> cachedObject = keyList.stream()
                                            .map(this::findCached)
                                            .filter(x -> x != null)
                                            .collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(cachedObject)) {
            log.info("get Cache Info, key: " + keyList + ", cachedObject: " + cachedObject);
            return cachedObject.get(0);
        }

        Object result = joinPoint.proceed();

        updateCache(keyList, result, cache.expireTime());

        return result;
    }

    private List<String> getKeyList(ProceedingJoinPoint joinPoint, TPCacheable cache) {
        String[] cacheDomains = cache.domains();
        String parsedKey = parseKey(cache.key(), joinPoint);
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        int argsHash = generateHashForArgs(joinPoint.getArgs());

        return generateKey(cacheDomains, parsedKey, className, methodName, String.valueOf(argsHash));
    }


    private Object findCached(String fullKey) {
        String key = fullKey.split(":")[0];
        String field = fullKey.substring(fullKey.indexOf(":"));
//        TODO
//        String value = CommonRedisUtil.getInstance().hget(key, field);
        String value = null;
        if (value == null) {
            return null;
        }

        return SerializeUtil.unserializeFromString(value);
    }


    private void updateCache(List<String> keyList, Object value, int expireTime) {
        keyList.stream().forEach(k -> updateCacheByKey(k, value, expireTime));
    }

    private void updateCacheByKey(String fullKey, Object result, int expireTime) {
        if (result == null) {
            return;
        }

        try {
            String key = fullKey.split(":")[0];
            String field = fullKey.substring(fullKey.indexOf(":"));
            String targetCachedValue = SerializeUtil.serializeToString(result);
//            TODO
//            CommonRedisUtil.getInstance().hset(key, field, targetCachedValue, expireTime);
        } catch (NotSerializableException e) {
            log.warn("Failed to serialize Object", e);
        }
    }

    private List<String> generateKey(String[] cacheDomains, String cacheKey, String className, String methodName, String argsHash) {
        if (cacheDomains.length == 0) {
            return Arrays.asList(StringUtils.isBlank(cacheKey) ?
                    className + ":" + methodName + ":" + argsHash :
                    className + ":" + cacheKey);
        }

        return Arrays.asList(cacheDomains).stream()
                .map(domain -> StringUtils.isBlank(cacheKey) ?
                        domain + ":" + methodName + ":" + argsHash :
                        domain + ":" + cacheKey)
                .collect(Collectors.toList());
    }

    private String parseKey(String cacheKey, ProceedingJoinPoint joinPoint) {
        if (StringUtils.isBlank(cacheKey)) {
            return "";
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
        return parser.parseExpression(cacheKey).getValue(context, String.class);
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