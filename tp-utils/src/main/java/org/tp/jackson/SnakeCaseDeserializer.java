package org.tp.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 将Map类型的Key反序列化转换为驼峰形式（需要要自定义实现，因配置的属性转换策略对Map这样的容器类型不适用）
 * 反序列化只针对Map的key是String类型的（其他的默认不支持，如：自定义的对象类）
 *
 */
public class SnakeCaseDeserializer extends JsonDeserializer<Map<String,String>> {

    @Override
    public Map<String,String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        Map<String,String> map = new HashMap<>();
        JsonNode jsonNode =  p.getCodec().readTree(p);
        Iterator<String> iterator =  jsonNode.fieldNames();
        while (iterator.hasNext()){
            String key = iterator.next();
            map.put(StringUtil.convertToCamelCase(key),JacksonTool.getStringValueFromNode(jsonNode.get(key)));
        }
        return map;
    }
}