package com.app.pipelinesurvey.utils;

import android.content.ClipboardManager;
import android.content.Context;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author HaiRun
 * @time 2019/9/2.16:46
 */
public class EncryptionAlgorithm {
    public final String PASSWORD = "gztc2004";

    /**
     * 复制粘贴
     * @Params :
     * @author :HaiRun
     * @date   :2019/9/24  10:54
     */
    public static void copy(String content, Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    private String parseByte2HexStr(byte[] paramArrayOfByte) {
        StringBuffer localStringBuffer = new StringBuffer();
        for (int i = 0; i < paramArrayOfByte.length; i++) {
            String str = Integer.toHexString(0xFF & paramArrayOfByte[i]);
            if (str.length() == 1) {
                str = '0' + str;
            }
            localStringBuffer.append(str.toUpperCase());
        }
        return localStringBuffer.toString();
    }

    public String AES(String paramString) {
        try {
            KeyGenerator localKeyGenerator = KeyGenerator.getInstance("AES");
            localKeyGenerator.init(128, new SecureRandom("gztc2004".getBytes()));
            SecretKeySpec localSecretKeySpec = new SecretKeySpec(localKeyGenerator.generateKey().getEncoded(), "AES");
            Cipher localCipher = Cipher.getInstance("AES");
            byte[] arrayOfByte = paramString.getBytes("utf-8");
            localCipher.init(1, localSecretKeySpec);
            String str = parseByte2HexStr(localCipher.doFinal(arrayOfByte));
            return str;
        } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
            localNoSuchAlgorithmException.printStackTrace();
            return null;
        } catch (NoSuchPaddingException localNoSuchPaddingException) {
            for (; ; ) {
                localNoSuchPaddingException.printStackTrace();
            }
        } catch (InvalidKeyException localInvalidKeyException) {
            for (; ; ) {
                localInvalidKeyException.printStackTrace();
            }
        } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
            for (; ; ) {
                localUnsupportedEncodingException.printStackTrace();
            }
        } catch (IllegalBlockSizeException localIllegalBlockSizeException) {
            for (; ; ) {
                localIllegalBlockSizeException.printStackTrace();
            }
        } catch (BadPaddingException localBadPaddingException) {
            for (; ; ) {
                localBadPaddingException.printStackTrace();
            }
        }
    }

    public String MD5(String paramString) {
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramString.getBytes());
            byte[] arrayOfByte = localMessageDigest.digest();
            StringBuffer localStringBuffer = new StringBuffer("");
            for (int i = 0; i < arrayOfByte.length; i++) {
                int j = arrayOfByte[i];
                if (j < 0) {
                    j += 256;
                }
                if (j < 16) {
                    localStringBuffer.append("0");
                }
                localStringBuffer.append(Integer.toHexString(j));
            }

            String str = localStringBuffer.toString().substring(8, 24);
            return str;
        } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
            localNoSuchAlgorithmException.printStackTrace();
        }
        return "";
    }


}
