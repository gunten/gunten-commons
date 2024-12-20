package algorithm;

import cn.hutool.core.util.HexUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

public class AESTest {

    public static void main(String[] args) throws Exception {
//        String content = "yqfzxtznbb!24,1720517096115";        // 原文内容
        String content = "yqfzxtznbb!24," + System.currentTimeMillis();        // 原文内容
        String key = "aes_yqfzxt@2024";                  // AES加密/解密用的原始密码

        // 加密数据, 返回密文
        byte[] cipherBytes = encrypt(content.getBytes(), key.getBytes());
        char[] chars = HexUtil.encodeHex(cipherBytes);
        for (int i = 0; i < chars.length; i++) {
            chars[i] = Character.toUpperCase(chars[i]);
        }

        System.out.println("PSW:" + String.valueOf(chars));

        String basicAuth = HttpUtil.buildBasicAuth("API_YQFZXT", String.valueOf(chars), StandardCharsets.UTF_8);
        System.out.println("basic auth:" + basicAuth);
        String result = HttpRequest.post("http://10.150.124.64/yqxt/business/API/znbb/bbRgql").auth(basicAuth).body("{\"wd\":\"\",\"createStrDate\":\"2024-07-11\",\"createEndDate\":\"2024-07-12\"}").execute().body();
        System.out.println("result: " + result);
    }

    /**
     * 生成密钥对象
     */
    private static SecretKey generateKey(byte[] key) throws Exception {
        // 根据指定的 RNG 算法, 创建安全随机数生成器
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        // 设置 密钥key的字节数组 作为安全随机数生成器的种子
        random.setSeed(key);

        // 创建 AES算法生成器
        KeyGenerator gen = KeyGenerator.getInstance("AES");
        // 初始化算法生成器
        gen.init(128, random);

        // 生成 AES密钥对象, 也可以直接创建密钥对象: return new SecretKeySpec(key, ALGORITHM);
        return gen.generateKey();
    }

    /**
     * 数据加密: 明文 -> 密文
     */
    public static byte[] encrypt(byte[] plainBytes, byte[] key) throws Exception {
        // 生成密钥对象
        SecretKey secKey = generateKey(key);

        // 获取 AES 密码器
        Cipher cipher = Cipher.getInstance("AES");
        // 初始化密码器（加密模型）
        cipher.init(Cipher.ENCRYPT_MODE, secKey);

        // 加密数据, 返回密文
        return cipher.doFinal(plainBytes);
    }

    /**
     * 数据解密: 密文 -> 明文
     */
    public static byte[] decrypt(byte[] cipherBytes, byte[] key) throws Exception {
        // 生成密钥对象
        SecretKey secKey = generateKey(key);

        // 获取 AES 密码器
        Cipher cipher = Cipher.getInstance("AES");
        // 初始化密码器（解密模型）
        cipher.init(Cipher.DECRYPT_MODE, secKey);

        // 解密数据, 返回明文
        return cipher.doFinal(cipherBytes);
    }

}

