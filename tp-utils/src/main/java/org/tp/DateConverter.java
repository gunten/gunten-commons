package org.tp;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang3.StringUtils;
import org.tp.jackson.DateTimeFormat;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 */
public class DateConverter implements Converter {
    @Override
    @SuppressWarnings({ "rawtypes" })
    public Object convert(Class type, Object value){
        if(value == null){
            return null;
        }else if(type == Timestamp.class){
            return convertToDate(type, value);
        }else if(type == Date.class){
            return convertToDate(type, value);
        }else if(type == String.class){
            return convertToString(type, value);
        }

        throw new ConversionException("不能转换 " + value.getClass().getName() + " 为 " + type.getName());
    }

    @SuppressWarnings("rawtypes")
    protected Object convertToDate(Class type, Object value) {
        if(value instanceof String){
            String pattern;
            if(((String) value).length()==(DateTimeFormat.DATE_PATTERN_LONG.length())){
                pattern = DateTimeFormat.DATE_PATTERN_LONG;
            }else  if(((String) value).length()==(DateTimeFormat.DATE_PATTERN_LONG_COMPRESS.length())){
                pattern = DateTimeFormat.DATE_PATTERN_LONG_COMPRESS;
            }else{
                return null;
            }
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            try{
                if(StringUtils.isEmpty(value.toString())){
                    return null;
                }
                Date date = sdf.parse((String) value);
                if(type.equals(Timestamp.class)){
                    return new Timestamp(date.getTime());
                }
                return date;
            }catch(Exception pe){
                return null;
            }
        }else if(value instanceof Date){
            return value;
        }

        throw new ConversionException("不能转换 " + value.getClass().getName() + " 为 " + type.getName());
    }

    @SuppressWarnings("rawtypes")
    protected Object convertToString(Class type, Object value) {
        if(value instanceof Date){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            if (value instanceof Timestamp) {
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            }

            try{
                return sdf.format(value);
            }catch(Exception e){
                throw new ConversionException("日期转换为字符串时出错！");
            }
        }else{
            return value.toString();
        }
    }
}