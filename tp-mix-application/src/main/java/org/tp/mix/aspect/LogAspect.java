package org.tp.mix.aspect;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.tp.annotation.autolog.AutoLog;
import org.tp.annotation.autolog.LogLevelEnum;
import org.tp.annotation.sensitive.SensitiveInfo;
import org.tp.annotation.sensitive.SensitiveInfoUtils;
import org.tp.annotation.sensitive.SensitiveType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * 系统脱敏日志打印
 *
 */
@Aspect
@Component
@Slf4j
public class LogAspect {

    /**
     * 方法参数名获取组件
     */
    @Autowired
    private ParameterNameDiscoverer parameterNameDiscoverer;

    /**
     * 拦截系统带@AutoLog标志方法，打印脱敏方法
     *
     * @param proceedingJoinPoint 切入点对象
     * @return 方法执行结果对象
     * @throws Throwable 目标方法可能抛出的异常
     */
    @Around(value = "within(org.tp.mix..*) && @annotation(org.tp.annotation.autolog.AutoLog)")
    public Object printAutoLogs(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        Signature signature = proceedingJoinPoint.getSignature();
        if (!(signature instanceof MethodSignature)) {
            return proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        }

        MethodSignature methodSignature = (MethodSignature) signature;

        Object[] args = proceedingJoinPoint.getArgs().clone();

        Method method = methodSignature.getMethod();

        String targetClassName = method.getDeclaringClass().getName();

        String targetMethodName = methodSignature.getName();

        AutoLog autoLog = method.getAnnotation(AutoLog.class);

        try {
            String inMessage = "begin invoke [" + targetClassName + "].[" + targetMethodName + "], args: " + generateMethodArgsLog(
                    method, args);

            printLog(inMessage, autoLog.level());
        } catch (Exception e) {
            log.warn("方法日志入参打印失败", e);
        }

        Object result = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());

        try {

            String returnResult = StringUtils.EMPTY;
            if (autoLog.isPrintReturnResult()) {
                returnResult = "result: " + generateMethodEndLog(result, autoLog);
            }

            String outMessage = "end invoke [" + targetClassName + "].[" + targetMethodName + "]. " + returnResult;

            printLog(outMessage, autoLog.level());
        } catch (Exception e) {
            log.warn("方法日志返回结果打印失败", e);
        }

        return result;
    }

    /**
     * 打印方法入参日志
     *
     * @param method 目标方法
     * @param args            参数
     */
    private String generateMethodArgsLog(Method method, Object[] args) {

        if (args.length == 0) {
            return "{}";
        }

        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        Map<Integer, SensitiveInfo> annotationData = new HashMap<>(5);

        for (int i = 0; i < parameterAnnotations.length; i++) {
            Annotation[] annotations = parameterAnnotations[i];
            if (annotations == null || annotations.length == 0) {
                continue;
            }

            Optional<SensitiveInfo> sensitiveInfo = Stream.of(annotations)
                                                          .filter(annotation -> annotation.annotationType() == SensitiveInfo.class)
                                                          .map(annotation -> (SensitiveInfo) annotation)
                                                          .findFirst();

            if (sensitiveInfo.isPresent()) {
                annotationData.put(i, sensitiveInfo.get());
            }
        }

        for (int i = 0; i < args.length; i++) {
            SensitiveInfo sensitiveInfo = annotationData.get(i);
            if (sensitiveInfo == null) {
                continue;
            }
            args[i] = SensitiveInfoUtils.convertSensitiveInfo(sensitiveInfo, args[i].toString());
        }

        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);

        Map<String, Object> paramData = new HashMap<>(5);

        for (int i = 0; i < parameterNames.length; i++) {
            paramData.put(parameterNames[i], args[i]);
        }

        return SensitiveInfoUtils.toJsonString(paramData);
    }

    /**
     * 打印方法结束日志
     *
     * @param result        方法返回对象
     * @param autoLog 注解对象
     */
    private String generateMethodEndLog(Object result, AutoLog autoLog) {

        if (result == null) {
            return "{}";
        }

        SensitiveType[] sensitiveTypes = autoLog.returnSensitiveType();
        if (sensitiveTypes.length == 0) {
            return SensitiveInfoUtils.toJsonString(result);
        }

        SensitiveType sensitiveType = sensitiveTypes[0];
        return SensitiveInfoUtils.convertSensitiveInfo(sensitiveType, result.toString());
    }

    /**
     * 根据日志级别打印日志
     *
     * @param message      消息
     * @param logLevelEnum 日志级别
     */
    private void printLog(String message, LogLevelEnum logLevelEnum) {
        switch (logLevelEnum) {
            case INFO:
                log.info(message);
                return;
            case DEBUG:
                log.debug(message);
                return;
            default:
                log.info(message);
        }
    }

}
