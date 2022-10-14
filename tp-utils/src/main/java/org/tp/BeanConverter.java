package org.tp;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.tp.exception.SysConstants;
import org.tp.jackson.DateTimeFormat;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * beanToMap2 的Object 并非Bean类型，多数是LinkedHashMap
 *
 */
public class BeanConverter {
    /**
     * 用gson来操作bean to map 支持Date， 默认日期格式为yyyy-MM-dd HH:mm:ss
     * 全部转换为String, 一般用作post调用前的参数转换
     *
     * @param bean
     * @return
     */
    public static Map<String, String> beanToMap(Object bean) {
        return beanToMap(bean, DateTimeFormat.DATE_PATTERN_LONG);
    }

    /**
     * 用gson来操作bean to map 支持Date， 日期格式可指定
     * 全部转换为String, 一般用作post调用前的参数转换
     *
     * @param bean
     * @return
     */
    public static Map<String, String> beanToMap(Object bean, String dateFormat) {
        Gson gson = new GsonBuilder().setDateFormat(dateFormat)
                .create();
        Map<String, String> paramsMap = gson.fromJson(gson.toJson(bean),
                new TypeToken<Map<String, String>>() {
                }.getType());
        return paramsMap;
    }

    /**
     * 用gson来操作bean to map 支持Date， 默认日期格式为yyyy-MM-dd HH:mm:ss
     * 全部转换为String, 一般用作post调用前的参数转换
     *
     * @param bean
     * @return
     */
    public static Map<String, Object> beanToMap2(Object bean) {
        return beanToMap2(bean, DateTimeFormat.DATE_PATTERN_LONG);
    }

    /**
     * 用gson来操作bean to map 支持Date， 日期格式可指定
     * 全部转换为String, 一般用作post调用前的参数转换
     *
     * @param bean
     * @return
     */
    public static Map<String, Object> beanToMap2(Object bean, String dateFormat) {
        Gson gson = new GsonBuilder().setDateFormat(dateFormat)
                .create();
        Map<String, Object> paramsMap = gson.fromJson(gson.toJson(bean),
                new TypeToken<Map<String, Object>>() {
                }.getType());
        return paramsMap;
    }

    /**
     * BeanUtils.populate注册了DateConverter，支持Date
     * 目前只支持yyyy-MM-dd HH:mm:ss，扩展时可修改tf56.common.web.utils.DateConverter
     * @param bean
     * @param paramsMap
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static void mapToBean(Object bean, Map<String, Object> paramsMap)
            throws IllegalAccessException, InvocationTargetException {
        ConvertUtils.register(new DateConverter(), java.util.Date.class);
        BeanUtils.populate(bean, paramsMap);
    }

    /**
     * bean转json，支持Date
     * @param bean
     * @return
     */
    public static String beanToJson(Object bean){
        return new GsonBuilder().setDateFormat(DateTimeFormat.DATE_PATTERN_LONG)
                .create().toJson(bean);
    }
}