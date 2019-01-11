package com.ncrb.samapre.myapplication;

import android.content.Context;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;


public class MCoCoRy {

    String coco_seed_encd = "";


    static {
        System.loadLibrary("keys");
    }

    public native String getCocoSeed();

    private static String IV = "IV_VALUE_16_BYTE";

    private static String SALT = "SALT_VALUE";

    Singleton singleton = Singleton.getInstance();

    // this seed will be enter by user only for auth
    private static String COCO_SEED = "SEED";



    public String encryptAndEncode(String raw) {

        try {
            Cipher c = getCipher(Cipher.ENCRYPT_MODE);
            byte[] encryptedVal = c.doFinal(getBytes(raw));
            String s = Base64.encodeToString(encryptedVal, Base64.DEFAULT);
            //String s = getString(Base64.encodeToString(encryptedVal,Base64.DEFAULT));
            return s;
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public String decodeAndDecrypt(String encrypted) throws Exception {


        //byte[] decodedValue = Base64.decodeBase64(getBytes(encrypted));
        byte[] decodedValue = Base64.decode(encrypted, Base64.DEFAULT);
        Cipher c = getCipher(Cipher.DECRYPT_MODE);
        byte[] decValue = c.doFinal(decodedValue);
        return new String(decValue);
    }

    private String getString(byte[] bytes) throws UnsupportedEncodingException {
        return new String(bytes, "UTF-8");
    }

    private byte[] getBytes(String str) throws UnsupportedEncodingException {
        return str.getBytes("UTF-8");
    }

    private Cipher getCipher(int mode) throws Exception {

        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] iv = getBytes(IV);
        c.init(mode, generateKey(), new IvParameterSpec(iv));
        return c;
    }

    private Key generateKey() throws Exception {

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        Utils.printv("singleton.coco_seed_cd " +  singleton.coco_seed_cd);

        String cocoseed = new String(Base64.decode(getCocoSeed(),Base64.DEFAULT));

        char[] coco_seed = cocoseed.toCharArray(); //COCO_SEED.toCharArray();

        byte[] salt = getBytes(SALT);

        KeySpec spec = new PBEKeySpec(coco_seed, salt, 65536, 128);

        SecretKey tmp = factory.generateSecret(spec);

        byte[] encoded = tmp.getEncoded();

        return new SecretKeySpec(encoded, "AES");
    }

    public String ThreadToSecureDetail(Context context, String coco_seed, String encode_decode) {


        final String final_coco_seed = coco_seed;
        final String final_fun_value = encode_decode;

        Utils.printv("ThreadToSecureDetail....");

        try {

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

                    try {

                        if(final_fun_value == "ENCODE")  {
                            coco_seed_encd = encryptAndEncode(final_coco_seed);


                        }
                        if(final_fun_value == "DECODE") {
                            coco_seed_encd = decodeAndDecrypt(final_coco_seed);


                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }


                }});

            t.start(); // spawn thread
            t.join();  // wait for thread to finish

        } catch (Exception e) {
            return "";
            //e.printStackTrace();
        }

        return coco_seed_encd;

    }// end thread to secure

}// end main class
