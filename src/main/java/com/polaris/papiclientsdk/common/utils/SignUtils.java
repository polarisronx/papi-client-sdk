package com.polaris.papiclientsdk.common.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import com.polaris.papiclientsdk.common.execption.ErrorCode;
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
 * @author polaris
 * @data 2024-03-13 14:31
 * @version 1.0
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
     * @Description
     * 获取签名认证信息
     * ·1 待签名字符串 stringToSign
     *          签名算法 + \n + 哈希摘要后的规范请求
     * ·2 用户密钥
     * ·3 时间
     * @author polaris
     * @date 2024/4/8
     * @return {@link String}
     */


    public static String getAuthorization(String service, String timestamp, Credential credential,String canonicalRequest,String signedHeader) throws PapiClientSDKException{
        String accessKey = credential.getAccessKey();
        String secretKey = credential.getSecretKey();

        // 当前系统时间格式化
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String date = sdf.format(new Date(Long.parseLong(timestamp + "000")));// 2018-01-01T12:00:00Z 数据库中是精确到秒，统一

        String hashedCanonicalRequest =
                SignUtils.sha256Hex(canonicalRequest.getBytes(StandardCharsets.UTF_8));
        // 待签名字符串
        String stringToSign =
                "papi3-HMAC-SHA256\n" + timestamp + "\n" + hashedCanonicalRequest;

        byte[] secretDate = hmac256(("Papi3" + secretKey).getBytes(StandardCharsets.UTF_8), date);
        byte[] secretService = hmac256(secretDate, service);
        byte[] secretSigning = hmac256(secretService, "papi3_request");
        String signature =
                DatatypeConverter.printHexBinary(SignUtils.hmac256(secretSigning, stringToSign)).toLowerCase();
        return
                "papi-HMAC-SHA256 "
                        + "Credential="
                        + accessKey
                        + ","
                        + "SignedHeaders="
                        + signedHeader
                        + ","
                        + "Signature="
                        + signature;
    }

    public static String sha256Hex(String s) throws PapiClientSDKException{
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new PapiClientSDKException("SHA-256 is not supported." + e.getMessage(), ErrorCode.NO_SUPPORT_ERROR);
        }
        byte[] d = md.digest(s.getBytes(UTF8));
        return DatatypeConverter.printHexBinary(d).toLowerCase();
    }

    public static String sha256Hex(byte[] b) throws PapiClientSDKException {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new PapiClientSDKException("SHA-256 is not supported." + e.getMessage(), ErrorCode.NO_SUPPORT_ERROR);
        }
        byte[] d = md.digest(b);
        return DatatypeConverter.printHexBinary(d).toLowerCase();
    }

    public static byte[] hmac256(byte[] key, String msg) throws PapiClientSDKException {
        Mac mac;
        try {
            mac = Mac.getInstance("HmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            throw new PapiClientSDKException("HmacSHA256 is not supported." + e.getMessage(), ErrorCode.NO_SUPPORT_ERROR);
        }
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, mac.getAlgorithm());
        try {
            mac.init(secretKeySpec);
        } catch (InvalidKeyException e) {
            throw new PapiClientSDKException(e.getClass().getName() + "-" + e.getMessage(), ErrorCode.NO_SUPPORT_ERROR);
        }
        return mac.doFinal(msg.getBytes(UTF8));
    }
    
}
