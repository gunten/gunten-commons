package org.tp.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.tp.threadcontext.ThreadContextTool;
import org.tp.enums.DesensitizationEnum;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 脱敏处理类
 *
 */

@Slf4j
public class DesensitizationTool {
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static final String TYPE_INCORRECT = "The basic Type is incorrect for transform.";
    private static final String NO_SUCH_METHOD = "no such method found";
    private static volatile DesensitizationTool desensitizationTool ;
    private static final List<String> DESENSITIZATION_VALUE_LIST = Stream.of(DesensitizationEnum.values())
                                                                       .map(DesensitizationEnum::getCode)
                                                                       .collect(Collectors.toList());

    public static  DesensitizationTool getInstance() {
        DesensitizationTool desensitization = desensitizationTool;
        if (desensitization == null) {
            synchronized (ThreadContextTool.class) {
                desensitization = desensitizationTool;
                if (desensitization == null) {
                    desensitizationTool = desensitization = new DesensitizationTool();
                }
            }
        }
        return desensitization;
    }

    /**
     * json字符串脱敏，传入json字符串 返回脱敏后json字符串
     *
     * @param json
     * @return
     */
    public static String jsonDesensitization(String json) {
        JsonNode ret = null;
        try {
            ret = objectMapper.readTree(json);
        } catch (Exception e) {
            log.warn(TYPE_INCORRECT);
        }
        JsonNode transformed = replace(ret);
        return JacksonTool.parseToJsonString(transformed, objectMapper);
    }

    /**
     * map脱敏，传入map,返回脱敏后的字符串
     * @param map
     * @return
     */
    public static String mapDesensitization(Map<String, Object> map){
        String json = JacksonTool.parseToJsonString(map);
        return jsonDesensitization(json);
    }

    /**
     * 将jsonNode里面的参数转换成脱敏jsonNode
     *
     * @param rootNode
     * @return
     */
    public static JsonNode replace(JsonNode rootNode) {
        if (null == rootNode) {
            return rootNode;
        }
        if (rootNode.isObject()) {
            ObjectNode objNode = objectMapper.createObjectNode();
            Iterator<Map.Entry<String, JsonNode>> it = rootNode.fields();
            while (it.hasNext()) {
                Map.Entry<String, JsonNode> nodeEntry = it.next();
                JsonNode entryValue = nodeEntry.getValue();
                String entryKey = nodeEntry.getKey();
                if (entryValue.isObject() || entryValue.isArray()) {
                    objNode.putPOJO(entryKey, replace(entryValue));
                } else {
                    if (DESENSITIZATION_VALUE_LIST.contains(entryKey)) {
                        String retValue = basicDesensitization(entryKey, entryValue.asText());
                        objNode.put(entryKey, retValue);
                    } else {
                        objNode.put(entryKey, entryValue.asText());
                    }
                }
            }
            return objNode;
        } else if (rootNode.isArray()) {
            ArrayNode arrNode = objectMapper.createArrayNode();
            Iterator<JsonNode> it = rootNode.elements();
            while (it.hasNext()) {
                JsonNode jsNode = it.next();
                arrNode.add(replace(jsNode));
            }
            return arrNode;
        } else {
            return new TextNode(rootNode.asText());
        }

    }

    /**
     * 基本对象脱敏方法
     *
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public static <T> String basicDesensitization(String key, T value) {
        DesensitizationEnum stringType = DesensitizationEnum.find(key);
        String desensitionedValue = null;
        //如果字段不在枚举里，返回原字段
        if (Objects.isNull(stringType)) {
            return valueToString(value);
        }
        //如果字段在枚举里
        else {
            switch (stringType) {
                case NAME:
                    return nameDesensitization(value);
                case BANKCARD:
                    return bankCardDesensitization(value);
                case IDCARD:
                    return idCardDesensitization(value);
                default:
                    return valueToString(value);
            }
        }
    }

    /**
     * 对应值转换成string
     *
     * @param value
     * @param <T>
     * @return
     */
    private static <T> String valueToString(T value) {
        String outString = null;
        try {
            outString = String.valueOf(value);
        } catch (Exception e) {
            log.warn(TYPE_INCORRECT);
        }
        return outString;
    }

    /**
     * 姓名脱敏
     *
     * @param value
     * @param <T>
     * @return
     */
    private static <T> String nameDesensitization(T value) {
        String name = null;
        try {
            name = String.valueOf(value);
        } catch (Exception e) {
            log.warn(TYPE_INCORRECT);
        }
        return paddingRight(name, 1);
    }

    /**
     * 银行卡脱敏
     *
     * @param value
     * @param <T>
     * @return
     */
    private static <T> String bankCardDesensitization(T value) {
        String bankcard = null;
        try {
            bankcard = String.valueOf(value);
        } catch (Exception e) {
            log.warn(TYPE_INCORRECT);
        }
        return paddingAround(bankcard, 0, 4);
    }

    /**
     * 身份证脱敏
     *
     * @param value 姓名
     * @param <T>
     * @return
     */
    private static <T> String idCardDesensitization(T value) {
        String idCard = null;
        try {
            idCard = String.valueOf(value);
        } catch (Exception e) {
            log.warn(TYPE_INCORRECT);
        }
        return paddingAround(idCard, 4, 4);
    }

    /**
     * 将右边N位数字转换成*号
     *
     * @param inputString
     * @param index
     * @return
     */
    private static String paddingRight(String inputString, int index) {
        if (StringUtils.isEmpty(inputString)) {
            return StringUtils.EMPTY;
        }
        String prefix = StringUtils.left(inputString, StringUtils.length(inputString) - index);
        return StringUtils.rightPad(prefix, StringUtils.length(inputString), "*");
    }

    /**
     * 将除前几位和后几位的字符转成*号
     *
     * @param inputString
     * @param index
     * @param end
     * @return
     */
    private static String paddingAround(String inputString, int index, int end) {
        if (StringUtils.isEmpty(inputString)) {
            return StringUtils.EMPTY;
        }
        int len = StringUtils.length(inputString);
        String prefix = StringUtils.left(inputString, len - end);
        String suffix = StringUtils.right(inputString, end);
        return paddingRight(prefix, len - index - end).concat(suffix);
    }

    public static void main(String[] args) {
        String jsonStr = "{\"bankCard\": 510411199405022219, \"userName\": \"吕温韦\", \"gender\": \"male\", \"address\": \"四川省攀枝花市仁和区\", \"family\": {\"children\": [{\"bankCard\":{\"bankCard\":2222222222222222222222222222222222}}]}}";
        bankCardDesensitization("510411199405022219");
        basicDesensitization("bankCard", "510411199405022219");
        String out = jsonDesensitization(jsonStr);
        System.out.println(out);
    }

}

