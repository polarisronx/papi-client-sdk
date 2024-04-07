package com.polaris.papiclientsdk.common.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import com.polaris.papiclientsdk.common.execption.PapiClientSDKException;
import com.polaris.papiclientsdk.common.model.Credential;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

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
    public static String getAuthorizationByHMACSHA256(String service, String timestamp, Credential credential,String canonicalRequest,String signedHeaders) throws PapiClientSDKException{
        String secretId = credential.getAccessKey();
        String secretKey = credential.getSecretKey();

        // 当前系统时间格式化
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String date = sdf.format(new Date(Long.parseLong(timestamp + "000")));// 2024-4-7 数据库中是精确到秒，统一

        String credentialScope = date + "/" + service + "/" + "v3_request";
        String hashedCanonicalRequest =
                SignUtils.sha256Hex(canonicalRequest.getBytes(StandardCharsets.UTF_8));
        String stringToSign =
                "papi3-HMAC-SHA256\n" + timestamp + "\n" + credentialScope + "\n" + hashedCanonicalRequest;

        byte[] secretDate = hmac256(("Papi3" + secretKey).getBytes(StandardCharsets.UTF_8), date);
        byte[] secretService = hmac256(secretDate, service);
        byte[] secretSigning = hmac256(secretService, "papi3_request");
        String signature =
                DatatypeConverter.printHexBinary(SignUtils.hmac256(secretSigning, stringToSign)).toLowerCase();
        return
                "papi3-HMAC-SHA256 "
                        + "Credential="
                        + secretId
                        + "/"
                        + credentialScope
                        + ", "
                        + "SignedHeaders="
                        + signedHeaders
                        + ", "
                        + "Signature="
                        + signature;
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
