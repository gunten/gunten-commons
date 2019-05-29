package org.tp.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 *  将Map类型的Key转换为下划线形式（需要要自定义实现，因配置的属性转换策略对Map这样的容器类型不适用）
 *  序列化只针对Map的key是String类型的（其他的默认不支持，如：自定义的对象类）
 *
 */
public class SnakeCaseKeySerializer extends JsonSerializer<String> {

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        String key = StringUtil.convertToSnakeCase(value);
        gen.writeFieldName(key);
    }
}