package com.polaris.papiclientsdk.common.model;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.polaris.papiclientsdk.common.enums.RequestMethodEnum;
import com.polaris.papiclientsdk.common.execption.ErrorCode;
import com.polaris.papiclientsdk.common.execption.PapiClientSDKException;
import com.polaris.papiclientsdk.common.profile.HttpProfile;
import com.polaris.papiclientsdk.common.utils.SignUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.polaris.papiclientsdk.common.utils.SignUtils.*;

/**
 * @author polaris
 * @create 2024-04-02 11:07
 * @version 2.0
 * ClassName AbstractClient
 * Package com.polaris.papiclientsdk.common.utils
 * Description
 */
@Data
@Slf4j
public abstract class AbstractClient {
    private Credential credential;
    private String signMethod;
    private String sdkVersion;
    private String apiVersion;
    private HttpProfile httpProfile;
    private String gatewayHost;

    public AbstractClient (){
    }




    public <T extends CommonResponse> T call(AbstractRequest<T> request, String actionName) throws PapiClientSDKException{
        try {
            return callByHttp(request,actionName);
        } catch (Exception e) {
            throw new PapiClientSDKException(e.getMessage(), ErrorCode.OPERATION_ERROR);
        }
    }

    private <T extends CommonResponse> T callByHttp (AbstractRequest<T> request,String actionName) throws PapiClientSDKException{
        T okRsp = null;
        Map<String, Object> customizedParams = request.getCustomizedParams();

        if (!customizedParams.isEmpty() || signMethod.equals(SIGN_SHA1)) {
            okRsp = doRequestV3(request, actionName);
        }
        else {
            throw new PapiClientSDKException(
                    "签名方法" + signMethod + "暂不支持",ErrorCode.TYPE_ERROR);
        }
        if (okRsp.getCode() != CommonResponse.HTTP_RSP_OK) {
            String msg = "response code is " + okRsp.getCode() + ", not 200";
            log.info(msg);
            throw new PapiClientSDKException(msg,ErrorCode.SYSTEM_ERROR);
        }
        return okRsp;
    }


    private <T extends CommonResponse> T doRequestV3(AbstractRequest<T> request, String actionName)
            throws PapiClientSDKException{
        String endpoint = httpProfile.getEndpoint();
        String requestMethod = request.getMethod();

        // 获取系统当前时间
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);//当前时间的秒数
        String service = "papi";// 现在暂时没有二级域名，二级域名用来表示服务类型 endpoint.split("\\.")[0]

        // Post请求的请求体设置
        String contentType;
        byte[] requestPayload = "".getBytes(StandardCharsets.UTF_8);
        if (Objects.equals(requestMethod, RequestMethodEnum.POST.name())) {
            contentType= "application/json;charset=utf-8";
            requestPayload = AbstractRequest.toJsonString(request).getBytes(StandardCharsets.UTF_8);//http总是设置charset，即使我们没有指定它，为了确保签名正确，我们也需要在这里设置它
        }else if(Objects.equals(requestMethod, RequestMethodEnum.GET.name())){
            contentType= "-";
        }else {
            throw new PapiClientSDKException("Request method should be GET or POST",ErrorCode.TYPE_ERROR);
        }

        // 获取查询参数
        HashMap<String, String> params = new HashMap<>();
        request.toMap(params, "");

        // 构造规范请求
        String canonicalQueryString = getCanonicalQueryString(params, requestMethod);// 规范的请求参数
        Map<String, String> canonicalHeaderMap = getCanonicalHeader(requestMethod, timestamp, contentType,endpoint);// 规范的请求头
        Map<String, String> canonicalRequestMap = getCanonicalRequest(request, canonicalHeaderMap);
        String canonicalRequest = canonicalRequestMap.get("canonicalRequest");

        // 构造 认证信息
        boolean skipSign = request.getSkipSign();
        String authorization;
        if (skipSign) {
            authorization = "SKIP";
        } else {
            authorization = getAuthorization(service, timestamp, credential, canonicalRequest,canonicalHeaderMap.get("signedHeaders"));
        }


        // 构造请求头
        String hashedRequestPayload = canonicalRequestMap.get("hashedRequestPayload");
        HashMap<String, String> header = getHeader(request, actionName, contentType, authorization, timestamp,hashedRequestPayload);

        // 构造请求地址
        String protocol = httpProfile.getProtocol();
        String url = protocol +"://"+ endpoint + request.getPath();
        if (null != gatewayHost) {// 有网关打到网关
            url = protocol + "://"+ gatewayHost + request.getPath();
            header.put("Host", gatewayHost);
        }

        // 构造Get或Post请求
        HttpRequest httpRequest;
        switch (requestMethod) {
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

        // 处理请求响应信息
        Map<String, Object> data = new HashMap<>();
        try (HttpResponse httpResponse = httpRequest.execute()) {
            Class<T> responseClass = request.getResponseClass();
            T response = responseClass.newInstance();
            response.setCode(httpResponse.getStatus());
            String body = httpResponse.body();
            if (response.getCode()!=200){
                data.put("errorMessage", "响应失败");
            }
            // 尝试通过JSON解析为map
            data = parseJson(data,body);
            response.setData(data);
            response.setCustomData(data);
            return response;
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new PapiClientSDKException(e.getMessage(),ErrorCode.OPERATION_ERROR );
        }
    }

    /**
     * @Description 获取规范的请求
     * 客户端与云服务API网关使用相同的请求规范，可以确保同一个HTTP请求的前后端得到相同的签名结果，从而完成身份校验。
     * CanonicalRequest =
     *   HTTPRequestMethod + '\n' +    // ·1 http方法名，全大写
     *   CanonicalURI + '\n' +         // ·2 规范化URI
     *      即URL的资源路径部分经过编码得到，资源路径部分指URL中host与查询字符串之间的部分，包含host之后的/但不包含查询字符串前的?。用户发起请求时的URI应使用规范化URI，编码方式使用UTF-8字符集按照RFC3986的规则对URI中的每一部分（即被/分割开的字符串）进行编码
     *   CanonicalQueryString + '\n' + // ·3 规范化查询字符串
     *   CanonicalHeaders + '\n' +     // ·4 规范化消息头
     *      请求中出现的以x-papi-为前缀的参与签名的请求头信息
     *   SignedHeaders + '\n' +        // ·5已签名消息头
     *      用于说明此次请求有哪些消息头参与了签名，与CanonicalHeaders中包含的消息头是一一对应的
     *   HashedRequestPayload          // ·6 摘要编码后请求正文
     * @author polaris
     * @create 2024/4/8
     * @return {@link String}
     */
    private static <T extends CommonResponse>  Map<String,String> getCanonicalRequest (AbstractRequest<T> request,Map<String, String> canonicalHeaderMap) throws PapiClientSDKException{
        String canonicalHeaders;
        String signedHeaders;
        String requestMethod = request.getMethod();//1
        String canonicalURI = request.getPath();//2
        // Post 请求设置请求体、请求正文（请求参数）
        byte[] requestPayload = "".getBytes(StandardCharsets.UTF_8);
        if (requestMethod.equals(RequestMethodEnum.POST.getMethod())) {
            requestPayload = AbstractRequest.toJsonString(request).getBytes(StandardCharsets.UTF_8);//http总是设置charset，即使我们没有指定它，为了确保签名正确，我们也需要在这里设置它
        }

        canonicalHeaders = canonicalHeaderMap.get("canonicalHeaders");
        signedHeaders = canonicalHeaderMap.get("signedHeaders");
        // get请求的查询参数
        HashMap<String, String> params = new HashMap<>();
        request.toMap(params, "");
        String canonicalQueryString = null;//3 规范的请求参数
        String hashedRequestPayload = null;

        // 构造规范请求
        canonicalQueryString = getCanonicalQueryString(params, requestMethod);

        hashedRequestPayload = "";
        if (request.getIsUnsignedPayload()) {
            hashedRequestPayload = SignUtils.sha256Hex("UNSIGNED-PAYLOAD".getBytes(StandardCharsets.UTF_8));
        } else {
            hashedRequestPayload = SignUtils.sha256Hex(requestPayload);
        }

        String canonicalRequest;

        canonicalRequest=
            canonicalQueryString.isEmpty()?
                    requestMethod              // http方法名，全大写
                    + "\n"
                    + canonicalURI             // 规范化查询字符串
                    + "\n"
                    + canonicalHeaders         // 规范化消息头
                    + "\n"
                    + signedHeaders            // 已签名消息头
                    + "\n"
                    + hashedRequestPayload    // 编码后请求正文
                    :
                    requestMethod              // http方法名，全大写
                    + "\n"
                    + canonicalURI             // 规范化查询字符串
                    + "\n"
                    + canonicalQueryString     // 规范化URI   可能为空
                    + "\n"
                    + canonicalHeaders         // 规范化消息头
                    + "\n"
                    + signedHeaders            // 已签名消息头
                    + "\n"
                    + hashedRequestPayload;    // 编码后请求正文
        HashMap<String, String> canonicalRequestMap = new HashMap<>();
        canonicalRequestMap.put("canonicalRequest", canonicalRequest);
        canonicalRequestMap.put("canonicalURI", canonicalURI);
        canonicalRequestMap.put("canonicalQueryString", canonicalQueryString);
        canonicalRequestMap.put("canonicalHeaders", canonicalHeaders);
        canonicalRequestMap.put("signedHeaders", signedHeaders);
        canonicalRequestMap.put("hashedRequestPayload", hashedRequestPayload);
        return canonicalRequestMap;
    }
    public static <T extends CommonResponse>  String getCanonicalRequest (String requestMethod,String path, Map<String,String> canonicalHeaderMap,String hashedRequestPayload,HashMap<String,String> params) throws PapiClientSDKException{
        String canonicalHeaders;
        String signedHeaders;
        String canonicalURI = path;
        canonicalHeaders = canonicalHeaderMap.get("canonicalHeaders");
        signedHeaders = canonicalHeaderMap.get("signedHeaders");
        // 规范请求参数
        String canonicalQueryString = getCanonicalQueryString(params, requestMethod);//3 规范的请求参数
        return  canonicalQueryString.isEmpty()?
                requestMethod              // http方法名，全大写
                + "\n"
                + canonicalURI             // 规范化查询字符串
                + "\n"
                + canonicalHeaders         // 规范化消息头
                + "\n"
                + signedHeaders            // 已签名消息头
                + "\n"
                + hashedRequestPayload    // 编码后请求正文
        :       requestMethod              // http方法名，全大写
                + "\n"
                + canonicalURI             // 规范化查询字符串
                + "\n"
                + canonicalQueryString     // 规范化URI   可能为空
                + "\n"
                + canonicalHeaders         // 规范化消息头
                + "\n"
                + signedHeaders            // 已签名消息头
                + "\n"
                + hashedRequestPayload;    // 编码后请求正文
    }
    /**
     * @Description
     * 获取消息头
     * @author polaris
     * @create 2024/4/8
     * @return {@link HashMap<String,String>}
     */
    private <T extends CommonResponse> HashMap<String, String> getHeader (AbstractRequest<T> request, String actionName, String contentType, String authorization, String timestamp, String hashedRequestPayload){
        // 添加请求头
        HashMap<String, String> header = new HashMap<>();
        String requestMethod = request.getMethod();
        if(requestMethod.equals(RequestMethodEnum.POST.getMethod())) header.put("Content-Type", contentType);
        header.put("Authorization", authorization);
        header.put("Action", actionName);
        //todo 集成redis后添加 随机数 token
        header.put("nonce", RandomUtil.randomNumbers(14));
        header.put("AccessKey",credential.getAccessKey());
        header.put("Timestamp", timestamp);
        header.put("ApiVersion", apiVersion);
        header.put("SdkVersion", sdkVersion);
        // host 请求体随后添加
        if (request.getIsUnsignedPayload()) {
            header.put("Content-SHA256", "UNSIGNED-PAYLOAD");
        } else {
            header.put("Content-SHA256", hashedRequestPayload);
        }
        if (null != request.getHeader()) {
            header.putAll(request.getHeader());
        }
        return header;
    }

    /**
     * @Description 获取规范的请求头
     * @author polaris
     * @create 2024/4/8
     * @return {@link Map< String, String>}
     */
    public static Map<String,String> getCanonicalHeader(String method,String timestamp,String contentType,String endpoint){
        String canonicalHeaders;
        String signedHeaders;
        if (method.equals(RequestMethodEnum.POST.getMethod())) {
            // 规范化请求头
            canonicalHeaders = "content-type:" + contentType + "\nhost:" + endpoint + "\ntimestamp:" + timestamp;
            signedHeaders = "content-type;host;timestamp";
        }else {
            canonicalHeaders = "host:" + endpoint;
            signedHeaders = "host;timestamp";
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("canonicalHeaders", canonicalHeaders);
        map.put("signedHeaders", signedHeaders);
        return map;
    }

    /**
     * @Description 尝试将响应体解析为map
     * @author polaris
     * @create 2024/4/8
     * @return {@link Map< String, Object>}
     */
    private Map<String, Object> parseJson(Map<String, Object> data,String body){
        try {
            data = new Gson().fromJson(body, new TypeToken<Map<String, Object>>() {
            }.getType());
            return data;
        } catch (JsonSyntaxException e) {
            // 解析失败，将body作为普通字符串处理
            data.put("value", body);
            return data;
        }
    }

    /**
     * @Description 获取规范查询参数
     * @author polaris
     * @create 2024/4/8
     * @return {@link String}
     */
    private static String getCanonicalQueryString (HashMap<String, String> params, String method)
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
                throw new PapiClientSDKException("UTF8编码失败", ErrorCode.PARAMS_ERROR);
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
