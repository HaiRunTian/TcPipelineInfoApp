package com.app.pipelinesurvey.utils;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author HaiRun
 * @time 2019/10/11.11:34
 */
public class AESUtils {
    /**
     * 加密用的Key 可以用26个字母和数字组成
     * 此处使用AES-128-CBC加密模式，key需要为16位。
     */
    private String sKey;
    private String ivParameter = "1234567890123456";
    private static AESUtils instance = null;

    private AESUtils(String sKey) {
        this.sKey = sKey;
    }

    public static AESUtils getInstance(String sKey) {
        if (instance == null) {
            instance = new AESUtils(sKey);
        }
        return instance;
    }
    /**
     * 加密
     *
     * @Params :
     * @author :HaiRun
     * @date :2019/10/11  11:35
     */
    public static String encrypt(String encData, String secretKey, String vector) throws Exception {

        if (secretKey == null) {
            return null;
        }

        if (secretKey.length() != 16) {
            return null;
        }

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] raw = secretKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        IvParameterSpec iv = new IvParameterSpec(vector.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(encData.getBytes("utf-8"));
        // 此处使用BASE64做转码。（处于android.util包）
        return Base64.encodeToString(encrypted, Base64.DEFAULT);
    }


    /**
     * 加密
     *
     * @Params :
     * @author :HaiRun
     * @date :2019/10/11  11:37
     */
    public String encrypt(String sSrc) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] raw = sKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
        // 此处使用BASE64做转码。
        return Base64.encodeToString(encrypted, Base64.DEFAULT);
    }

    /**
     * 解密
     *
     * @Params :
     * @author :HaiRun
     * @date :2019/10/11  11:36
     */
    public String decrypt(String sSrc) throws Exception {
        try {
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            // 先用base64解密
            byte[] encrypted1 = Base64.decode(sSrc, Base64.DEFAULT);
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, "utf-8");
            return originalString;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 解密
     *
     * @Params :
     * @author :HaiRun
     * @date :2019/10/11  11:36
     */
    public String decrypt(String sSrc, String key) throws Exception {
        try {
            byte[] raw = key.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            // 先用base64解密
            byte[] encrypted1 = Base64.decode(sSrc, Base64.DEFAULT);
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, "utf-8");
            return originalString;
        } catch (Exception ex) {
            return null;
        }
    }

}
