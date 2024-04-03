package com.polaris.papiclientsdk.common.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import com.polaris.papiclientsdk.common.execption.PapiClientSDKException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Author polaris
 * @Create 2024-03-13 14:31
 * @Version 1.0
 * ClassName SignUtils
 * Package com.polaris.apiinterface.utils
 * Description 生成签名
 */
public class SignUtils {

    private static final Charset UTF8 = StandardCharsets.UTF_8;
    /**
     * Signature process version 1, with HmacSHA1.
     */
    public static final String SIGN_SHA1 = "HmacSHA1";

    /**
     * Signature process version 1, with HmacSHA256.
     */
    public static final String SIGN_SHA256 = "HmacSHA256";

    /**
     * Signature process version 3.
     */
    public static final String SIGN_TC3_256 = "TC3-HMAC-SHA256";
    public static String genSign(String body,String secretKey){



        // todo 选择加密算法
        // 使用SHA25算法的Digester
        Digester digester = new Digester(DigestAlgorithm.SHA256);
        // 签名内容 body + 密钥 拼接
        // 也可以把 nonce timestamp 都加入到 签名中增加复杂度，但这些参数都是要一一获取校验的，因此为了简化，这里不加入这些参数
        String signContent = body +"."+ secretKey;
        return digester.digestHex(signContent);
    }

    public static String sha256Hex(String s) throws PapiClientSDKException{
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new PapiClientSDKException("SHA-256 is not supported." + e.getMessage());
        }
        byte[] d = md.digest(s.getBytes(UTF8));
        return DatatypeConverter.printHexBinary(d).toLowerCase();
    }

    public static String sha256Hex(byte[] b) throws PapiClientSDKException {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new PapiClientSDKException("SHA-256 is not supported." + e.getMessage());
        }
        byte[] d = md.digest(b);
        return DatatypeConverter.printHexBinary(d).toLowerCase();
    }

    public static byte[] hmac256(byte[] key, String msg) throws PapiClientSDKException {
        Mac mac;
        try {
            mac = Mac.getInstance("HmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            throw new PapiClientSDKException("HmacSHA256 is not supported." + e.getMessage());
        }
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, mac.getAlgorithm());
        try {
            mac.init(secretKeySpec);
        } catch (InvalidKeyException e) {
            throw new PapiClientSDKException(e.getClass().getName() + "-" + e.getMessage());
        }
        return mac.doFinal(msg.getBytes(UTF8));
    }
    
}
