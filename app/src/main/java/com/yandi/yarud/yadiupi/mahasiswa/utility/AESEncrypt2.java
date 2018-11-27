package com.yandi.yarud.yadiupi.mahasiswa.utility;

import android.annotation.SuppressLint;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AESEncrypt2 {
    private String enkripsivalue;
//    private String dekripsivalue;
    private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        return cipher.doFinal(clear);
    }

//    private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
//        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
//        @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance("AES");
//        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
//        return cipher.doFinal(encrypted);
//    }

    private static String byte2Hex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b: bytes) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                hex = "0" + hex;
            }
            sb.append(hex);
        }
        return sb.toString();
    }

//    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
//    public static String bytesToHex(Byte[] bytes) {
//        char[] hexChars = new char[bytes.length * 2];
//        for ( int j = 0; j < bytes.length; j++ ) {
//            int v = bytes[j] & 0xFF;
//            hexChars[j * 2] = hexArray[v >>> 4];
//            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
//        }
//        return new String(hexChars);
//    }

    public void tes(String nim) throws Exception {
        byte[] keyStart = "yandiapriandi".getBytes();
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed(keyStart);
        kgen.init(128, sr);
        SecretKey skey = kgen.generateKey();
        byte[] key = skey.getEncoded();

        byte[] encryptedData = encrypt(key,nim.getBytes());
        enkripsivalue=byte2Hex(encryptedData);

//        byte[] decryptedData = decrypt(key,encryptedData);
//        dekripsivalue=byte2Hex(decryptedData);

    }

    public String getEnkripsivalue(){
        return enkripsivalue;
    }

//    public String getDekripsivalue(){
//        return dekripsivalue;
//    }
}
