package org.tp;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Base64;

@Slf4j
public class SerializeUtil {

    private SerializeUtil(){}

    public static String serializeToString(Object object) throws NotSerializableException {
        byte[] bytes = serialize(object);
        return new String(Base64.getEncoder().encode(bytes));
    }

    public static Object unserializeFromString(String base64String) {
        byte[] bytes = Base64.getDecoder().decode(base64String);
        return unserialize(bytes);
    }

    private static byte[] serialize(Object object) throws NotSerializableException {
        ObjectOutputStream oos;
        ByteArrayOutputStream baos;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new NotSerializableException(
                    "Class: " + e.getMessage() + " does not implement Serializable interface");
        }
    }

    private static Object unserialize(byte[] bytes) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            return ois.readObject();
        } catch (Exception e) {
            log.warn("反序列化失败，失败原因：{}", e.toString(), e);
        }
        return null;
    }

}

