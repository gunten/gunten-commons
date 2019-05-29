package org.tp.annotation.sensitive;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.ValueFilter;
import org.apache.commons.lang3.StringUtils;
import org.tp.jackson.DesensitizationTool;

import java.lang.reflect.Field;

/**
 * 日志敏感信息脱敏工具
 *
 * @version V1.0.0
 */
public class SensitiveInfoUtils {

    private SensitiveInfoUtils(){}

    /**
     * [中文姓名] 只显示第一个汉字，其他隐藏为2个星号<例子：李**>
     */

    public static String chineseName(final String fullName) {

        if (StringUtils.isBlank(fullName)) {

            return "";

        }

        final String name = StringUtils.left(fullName, 1);

        return StringUtils.rightPad(name, StringUtils.length(fullName), "*");

    }

    /**
     * [中文姓名] 只显示第一个汉字，其他隐藏为2个星号<例子：李**>
     */

    public static String chineseName(final String familyName, final String givenName) {

        if (StringUtils.isBlank(familyName) || StringUtils.isBlank(givenName)) {

            return "";

        }

        return chineseName(familyName + givenName);

    }

    /**
     * [身份证号] 显示最后四位，其他隐藏。共计18位或者15位。<例子：*************5762>
     */

    public static String idCardNum(final String id) {

        if (StringUtils.isBlank(id)) {

            return "";

        }

        return StringUtils.left(id, 3).concat(StringUtils

                                                      .removeStart(StringUtils.leftPad(StringUtils.right(id, 3),
                                                                                       StringUtils.length(id), "*"),

                                                                   "***"));

    }

    public static String certCodeNum(final String code) {
        if (StringUtils.isBlank(code)) {
            return "";
        }

        return StringUtils.left(code, 3).concat(StringUtils

                                                        .removeStart(StringUtils.leftPad(StringUtils.right(code, 3),
                                                                                         StringUtils.length(code), "*"),

                                                                     "***"));
    }

    /**
     * [固定电话] 后四位，其他隐藏<例子：****1234>
     */

    public static String fixedPhone(final String num) {

        if (StringUtils.isBlank(num)) {

            return "";

        }

        return StringUtils.leftPad(StringUtils.right(num, 4), StringUtils.length(num), "*");

    }

    /**
     * [手机号码] 前三位，后四位，其他隐藏<例子:138******1234>
     */

    public static String mobilePhone(final String num) {

        if (StringUtils.isBlank(num)) {

            return "";

        }

        return StringUtils.left(num, 2).concat(StringUtils

                                                       .removeStart(StringUtils.leftPad(StringUtils.right(num, 2),
                                                                                        StringUtils.length(num), "*"),

                                                                    "***"));

    }

    /**
     * [地址] 只显示到地区，不显示详细地址；我们要对个人信息增强保护<例子：北京市海淀区****>
     *
     * @param sensitiveSize 敏感信息长度
     */

    public static String address(final String address, final int sensitiveSize) {

        if (StringUtils.isBlank(address)) {

            return "";

        }

        final int length = StringUtils.length(address);

        return StringUtils.rightPad(StringUtils.left(address, length - sensitiveSize), length, "*");

    }

    /**
     * [电子邮箱] 邮箱前缀仅显示第一个字母，前缀其他隐藏，用星号代替，@及后面的地址显示<例子:g**@163.com>
     */

    public static String email(final String email) {

        if (StringUtils.isBlank(email)) {

            return "";

        }

        final int index = StringUtils.indexOf(email, "@");

        if (index <= 1) {

            return email;

        } else {

            return StringUtils.rightPad(StringUtils.left(email, 1), index, "*")

                              .concat(StringUtils.mid(email, index, StringUtils.length(email)));

        }

    }

    /**
     * [银行卡号] 前六位，后四位，其他用星号隐藏每位1个星号<例子:6222600**********1234>
     */

    public static String bankCard(final String cardNum) {

        if (StringUtils.isBlank(cardNum)) {

            return "";

        }

        return StringUtils.left(cardNum, 6).concat(StringUtils.removeStart(

                StringUtils.leftPad(StringUtils.right(cardNum, 4), StringUtils.length(cardNum), "*"),

                "******"));

    }

    /**
     * [公司开户银行联号] 公司开户银行联行号,显示前两位，其他用星号隐藏，每位1个星号<例子:12********>
     */

    public static String cnapsCode(final String code) {

        if (StringUtils.isBlank(code)) {

            return "";

        }

        return StringUtils.rightPad(StringUtils.left(code, 2), StringUtils.length(code), "*");

    }

    /**
     * 针对对象属性处理脱敏
     *
     * @param object
     * @return
     */
    public static String toJsonString(Object object) {

        return JSON.toJSONString(object, getValueFilter());
    }

    private static final ValueFilter getValueFilter() {
        //obj-对象  key-字段名  value-字段值
        return (obj, key, value) -> {
            try {
                Field field = obj.getClass().getDeclaredField(key);
                SensitiveInfo annotation = field.getAnnotation(SensitiveInfo.class);
                if (value instanceof String) {
                    return convertSensitiveInfo(annotation, (String) value);
                }
            } catch (NoSuchFieldException e) {
                //找不到的field对功能没有影响,空处理
            }
            return value;
        };
    }

    public static String convertSensitiveInfo(SensitiveInfo annotation, String value) {
        if (null == annotation || StringUtils.isBlank(value)) {
            return value;
        }

        return convertSensitiveInfo(annotation.type(), value);
    }

    public static String convertSensitiveInfo(SensitiveType sensitiveType, String value) {
        switch (sensitiveType) {

            case CHINESE_NAME:

                return SensitiveInfoUtils.chineseName(value);
            case ID_CARD:

                return SensitiveInfoUtils.idCardNum(value);
            case FIXED_PHONE:

                return SensitiveInfoUtils.fixedPhone(value);
            case MOBILE_PHONE:

                return SensitiveInfoUtils.mobilePhone(value);
            case ADDRESS:

                return SensitiveInfoUtils.address(value, 4);
            case EMAIL:

                return SensitiveInfoUtils.email(value);
            case BANK_CARD:

                return SensitiveInfoUtils.bankCard(value);
            case CNAPS_CODE:

                return SensitiveInfoUtils.cnapsCode(value);
            case JSON:
                //TODO 需要本地业务化改造  有待测试正确性
                return DesensitizationTool.jsonDesensitization(value);
            default:

                return value;
        }
    }

}