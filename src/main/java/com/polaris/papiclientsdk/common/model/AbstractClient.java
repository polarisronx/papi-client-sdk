package com.polaris.papiclientsdk.common.model;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
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
import static com.polaris.papiclientsdk.common.utils.SignUtils.getAuthorizationByHMACSHA256;

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
    private String sdkVersion;
    private String apiVersion;
    private HttpProfile httpProfile;
    private String gatewayHost;

    public AbstractClient (Credential credential,HttpProfile httpProfile){
        this.credential = credential;
        this.httpProfile = httpProfile;
    }


    public abstract AbstractRequest getRequest(String action) throws PapiClientSDKException;

    public <T extends CommonResponse> T call(AbstractRequest<T> request, String actionName) throws PapiClientSDKException{
        try {
            return callByHttp(request,actionName);
        } catch (Exception e) {
            throw new PapiClientSDKException(e.getMessage(), ErrorCode.OPERATION_ERROR);
        }
    }

    private <T extends CommonResponse> T callByHttp (AbstractRequest<T> request,String actionName) throws PapiClientSDKException, IOException{
        T okRsp = null;
        Map<String, Object> customizedParams = request.getCustomizedParams();

        if (!customizedParams.isEmpty() || signMethod.equals(SIGN_SHA1)) {
            okRsp = doRequestWithTC3(request, actionName);
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


    private <T extends CommonResponse> T doRequestWithTC3(AbstractRequest<T> request, String actionName)
            throws PapiClientSDKException{
        String endpoint = httpProfile.getEndpoint();
        String httpRequestMethod = request.getMethod();
        if (httpRequestMethod == null) {
            throw new PapiClientSDKException(
                    "Request method should not be null, can only be GET or POST");
        }
        String canonicalHeaders;

        // Post 请求设置请求体、请求正文（请求参数）
        String contentType= "application/json; charset=utf-8";
        byte[] requestPayload = "".getBytes(StandardCharsets.UTF_8);
        if (httpRequestMethod.equals(RequestMethodEnum.POST.getMethod())) {
            requestPayload = AbstractRequest.toJsonString(request).getBytes(StandardCharsets.UTF_8);//http总是设置charset，即使我们没有指定它，为了确保签名正确，我们也需要在这里设置它
            canonicalHeaders = "content-type:" + contentType + "\nhost:" + endpoint + "\n";
        }else {
            canonicalHeaders = "host:" + endpoint + "\n";
        }
        String canonicalUri = "/";
        // get请求的查询参数
        HashMap<String, String> params = new HashMap<>();
        request.toMap(params, "");
        // 构造规范请求
        String canonicalQueryString = this.getCanonicalQueryString(params, httpRequestMethod);// 规范的请求参数
        String signedHeaders = "content-type;host";// 签名请求头
        String hashedRequestPayload = "";
        if (request.getIsUnsignedPayload()) {
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
        // 获取系统当前时间
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);//当前时间的秒数
        String service = "papi";// 现在暂时没有二级域名，二级域名用来表示服务类型 endpoint.split("\\.")[0]

        // 构造 认证信息
        boolean skipSign = request.getSkipSign();
        String authorization;
        if (skipSign) {
            authorization = "SKIP";
        } else {
            authorization = getAuthorizationByHMACSHA256(service, timestamp, credential, canonicalRequest, signedHeaders);
        }

        // 构造请求头
        HashMap<String, String> header = getHeader(request, actionName, contentType, authorization, timestamp);

        // 构造请求地址
        String protocol = httpProfile.getProtocol();
        String url = protocol + endpoint + request.getPath();
        if (null != gatewayHost) {// 有网关打到网关
            url = protocol + gatewayHost;
            header.put("Host", gatewayHost);
        }

        // 构造Get或Post请求
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
            return response;
        } catch (Exception e) {
            throw new PapiClientSDKException(e.getMessage(),ErrorCode.OPERATION_ERROR );
        }

    }

    private <T extends CommonResponse> HashMap<String, String> getHeader (AbstractRequest<T> request, String actionName, String contentType, String authorization, String timestamp){
        // 添加请求头
        HashMap<String, String> header = new HashMap<>();
        String requestMethod = request.getMethod();
        if(requestMethod.equals(RequestMethodEnum.POST.getMethod())) header.put("Content-Type", contentType);
        header.put("Host", httpProfile.getEndpoint());
        header.put("Authorization", authorization);
        header.put("Action", actionName);
        header.put("Timestamp", timestamp);
        header.put("Version", apiVersion);
        header.put("RequestClient", sdkVersion);
        if (request.getIsUnsignedPayload()) {
            header.put("Content-SHA256", "UNSIGNED-PAYLOAD");
        }
        if (null != request.getHeader()) {
            header.putAll(request.getHeader());
        }
        return header;
    }

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
