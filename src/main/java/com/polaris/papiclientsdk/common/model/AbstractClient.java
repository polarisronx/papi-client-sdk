package com.polaris.papiclientsdk.common.model;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.polaris.papiclientsdk.common.enums.RequestMethodEnum;
import com.polaris.papiclientsdk.common.execption.ErrorCode;
import com.polaris.papiclientsdk.common.execption.PapiClientSDKException;
import com.polaris.papiclientsdk.common.profile.HttpProfile;
import com.polaris.papiclientsdk.common.utils.SignUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import static com.polaris.papiclientsdk.common.utils.SignUtils.SIGN_SHA1;
import static com.polaris.papiclientsdk.common.utils.SignUtils.genSign;

/**
 * @author polaris
 * @create 2024-04-02 11:07
 * @version 1.0
 * ClassName AbstractClient
 * Package com.polaris.papiclientsdk.common.utils
 * Description
 */
@Data
@Slf4j
public abstract class AbstractClient {
    private Credential credential;
    private String signMethod;
    private String SDKVersion;
    private String apiVersion;
    private HttpProfile httpProfile;
    private String GATEWAY_HOST;

    public AbstractClient (Credential credential,HttpProfile httpProfile){
        this.credential = credential;
        this.httpProfile = httpProfile;
    }




    //    private String formatRequestData(String action, Map<String, String> param)
//            throws PapiClientSDKException{
//        param.put("Action", action);
//        param.put("RequestClient", this.sdkVersion);
//        param.put("Nonce", String.valueOf(Math.abs(new SecureRandom().nextInt())));
//        param.put("Timestamp", String.valueOf(System.currentTimeMillis() / 1000));
//        param.put("Version", this.apiVersion);
//
//        if (this.credential.getSecretId() != null && (!this.credential.getSecretId().isEmpty())) {
//            param.put("SecretId", this.credential.getSecretId());
//        }
//
//        if (this.region != null && (!this.region.isEmpty())) {
//            param.put("Region", this.region);
//        }
//
//        if (this.profile.getSignMethod() != null && (!this.profile.getSignMethod().isEmpty())) {
//            param.put("SignatureMethod", this.profile.getSignMethod());
//        }
//
//        if (this.credential.getToken() != null && (!this.credential.getToken().isEmpty())) {
//            param.put("Token", this.credential.getToken());
//        }
//
//        if (null != this.profile.getLanguage()) {
//            param.put("Language", this.profile.getLanguage().getValue());
//        }
//
//        String endpoint = this.getEndpoint();
//
//        String sigInParam =
//                Sign.makeSignPlainText(
//                        new TreeMap<String, String>(param),
//                        this.profile.getHttpProfile().getReqMethod(),
//                        endpoint,
//                        this.path);
//        String sigOutParam =
//                Sign.sign(this.credential.getSecretKey(), sigInParam, this.profile.getSignMethod());
//
//        String strParam = "";
//        try {
//            for (Map.Entry<String, String> entry : param.entrySet()) {
//                strParam +=
//                        (URLEncoder.encode(entry.getKey(), "utf-8")
//                                + "="
//                                + URLEncoder.encode(entry.getValue(), "utf-8")
//                                + "&");
//            }
//            strParam += ("Signature=" + URLEncoder.encode(sigOutParam, "utf-8"));
//        } catch (UnsupportedEncodingException e) {
//            throw new PapiClientSDKException("", e);
//        }
//        return strParam;
//    }






    /**
     * 获取请求头
     *
     * @return {@link HashMap}<{@link String},{@link String}>
     */
//    private HashMap<String, String> getHeaders(Credential credential, String body){
//        String secretKey = credential.getSecretKey();
//        String accessKey = credential.getAccessKey();
//        HashMap<String, String> headers = new HashMap<>();
//        headers.put("accessKey", accessKey);
//        // 添加4位数字的随机数
//        headers.put("nonce", RandomUtil.randomNumbers(4));
//        // 添加包含用户参数的请求体内容
//        headers.put("body", body);
//        // 添加当前时间戳
//        headers.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
//        headers.put("secretKey", secretKey);
//        // 获取签名，并把签名也放入请求头
//        // 其他参数会添加到请求头中直接发送，但是密钥不能直接在服务器间传递。
//        headers.put("sign", genSign(body, secretKey));
//        return headers;
//    }




    public <T extends CommonResponse> T call(AbstractRequest<T> request, String actionName) throws PapiClientSDKException{
        try {
            return callByHttp(request,actionName);
        } catch (Exception e) {
            throw new PapiClientSDKException(e.getMessage(), ErrorCode.OPERATION_ERROR);
        }
    }

    private <T extends CommonResponse> T callByHttp (AbstractRequest<T> request,String actionName) throws PapiClientSDKException, IOException{
        T okRsp = null;
        String endpoint = httpProfile.getEndpoint();
        Map<String, Object> customizedParams = request.getCustomizedParams();

        if (!customizedParams.isEmpty() || signMethod.equals(SIGN_SHA1)) {
            okRsp = doRequestWithTC3(endpoint, request, actionName);
        }
        else {
            throw new PapiClientSDKException(
                    "Signature method " + signMethod + " is invalid or not supported yet.");
        }
        if (okRsp.getCode() != CommonResponse.HTTP_RSP_OK) {
            String msg = "response code is " + okRsp.getCode() + ", not 200";
            log.info(msg);
            throw new PapiClientSDKException(msg, "", "ServerSideError");
        }
        return okRsp;
    }


    private <T extends CommonResponse> T doRequestWithTC3(String endpoint, AbstractRequest<T> request, String actionName)
            throws PapiClientSDKException, IOException {

        String httpRequestMethod = httpProfile.getMethod();
        if (httpRequestMethod == null) {
            throw new PapiClientSDKException(
                    "Request method should not be null, can only be GET or POST");
        }
        String contentType = "application/x-www-form-urlencoded";
        byte[] requestPayload = "".getBytes(StandardCharsets.UTF_8);
        HashMap<String, String> params = new HashMap<>();
        request.toMap(params, "");
        if (httpRequestMethod.equals(RequestMethodEnum.POST.getMethod())) {
            requestPayload = AbstractRequest.toJsonString(request).getBytes(StandardCharsets.UTF_8);
            // okhttp always set charset even we don't specify it,
            // to ensure signature be correct, we have to set it here as well.
            contentType = "application/json; charset=utf-8";
        }
        String canonicalUri = "/";
        String canonicalQueryString = this.getCanonicalQueryString(params, httpRequestMethod);
        String canonicalHeaders = "content-type:" + contentType + "\nhost:" + endpoint + "\n";
        String signedHeaders = "content-type;host";

        String hashedRequestPayload = "";
        if (request.isUnsignedPayload()) {
            hashedRequestPayload = SignUtils.sha256Hex("UNSIGNED-PAYLOAD".getBytes(StandardCharsets.UTF_8));
        } else {
            hashedRequestPayload = SignUtils.sha256Hex(requestPayload);
        }
        String canonicalRequest =
                httpRequestMethod
                        + "\n"
                        + canonicalUri
                        + "\n"
                        + canonicalQueryString
                        + "\n"
                        + canonicalHeaders
                        + "\n"
                        + signedHeaders
                        + "\n"
                        + hashedRequestPayload;

        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String date = sdf.format(new Date(Long.parseLong(timestamp + "000")));
        String service = endpoint.split("\\.")[0];
        String credentialScope = date + "/" + service + "/" + "tc3_request";
        String hashedCanonicalRequest =
                SignUtils.sha256Hex(canonicalRequest.getBytes(StandardCharsets.UTF_8));
        String stringToSign =
                "TC3-HMAC-SHA256\n" + timestamp + "\n" + credentialScope + "\n" + hashedCanonicalRequest;
        boolean skipSign = request.getSkipSign();
        String authorization = "";
        if (skipSign) {
            authorization = "SKIP";
        } else {
            String secretId = credential.getAccessKey();
            String secretKey = credential.getSecretKey();
            byte[] secretDate = SignUtils.hmac256(("TC3" + secretKey).getBytes(StandardCharsets.UTF_8), date);
            byte[] secretService = SignUtils.hmac256(secretDate, service);
            byte[] secretSigning = SignUtils.hmac256(secretService, "tc3_request");
            String signature =
                    DatatypeConverter.printHexBinary(SignUtils.hmac256(secretSigning, stringToSign)).toLowerCase();
            authorization =
                    "TC3-HMAC-SHA256 "
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
        HashMap<String, String> header = new HashMap<>();
        header.put("Content-Type", contentType);
        header.put("Host", endpoint);
        header.put("Authorization", authorization);
        header.put("X-TC-Action", actionName);
        header.put("X-TC-Timestamp", timestamp);
        header.put("X-TC-Version", apiVersion);
        header.put("X-TC-RequestClient", SDKVersion);
        if (null != request.getHeader()) {
            header.putAll(request.getHeader());
        }

//        String token = this.credential.getToken();
//        if (token != null && !token.isEmpty()) {
//            hb.add("X-TC-Token", token);
//        }
//        if (this.profile.isUnsignedPayload()) {
//            hb.add("X-TC-Content-SHA256", "UNSIGNED-PAYLOAD");
//        }
//        if (null != this.profile.getLanguage()) {
//            hb.add("X-TC-Language", this.profile.getLanguage().getValue());
//        }

        String protocol = httpProfile.getProtocol();
        String url = protocol + endpoint + request.getPath();

        if (null != GATEWAY_HOST) {
            url = protocol + GATEWAY_HOST;
            header.put("Host", GATEWAY_HOST);
        }

        HttpRequest httpRequest;
        switch (httpRequestMethod) {
            case "GET": {
                httpRequest = HttpRequest.get(url + "?" + canonicalQueryString);
                break;
            }
            case "POST": {
                httpRequest = HttpRequest.post(url);
                break;
            }
            default: {
                throw new PapiClientSDKException("不支持该请求",ErrorCode.OPERATION_ERROR);
            }
        }
        httpRequest = httpRequest.addHeaders(header).body(requestPayload);


        try (HttpResponse httpResponse = httpRequest.execute()) {
            Class<T> responseClass = request.getResponseClass();
            T response = responseClass.newInstance();
            response.setCode(httpResponse.getStatus());
            response.setData(httpResponse.body());
            return response;
        } catch (Exception e) {
            throw new PapiClientSDKException(e.getMessage(),ErrorCode.OPERATION_ERROR );
        }

    }

    private String getCanonicalQueryString(HashMap<String, String> params, String method)
            throws PapiClientSDKException {
        if (method != null && method.equals(RequestMethodEnum.POST.getMethod())) {
            return "";
        }
        StringBuilder queryString = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String v;
            try {
                v = URLEncoder.encode(entry.getValue(), "UTF8");
            } catch (UnsupportedEncodingException e) {
                throw new PapiClientSDKException("UTF8 is not supported.", e);
            }
            queryString.append("&").append(entry.getKey()).append("=").append(v);
        }
        if (queryString.length() == 0) {
            return "";
        } else {
            return queryString.substring(1);
        }
    }
}
