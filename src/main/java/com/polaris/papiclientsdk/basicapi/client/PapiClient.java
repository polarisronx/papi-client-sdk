package com.polaris.papiclientsdk.basicapi.client;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;

import com.polaris.papiclientsdk.basicapi.model.request.GetUsernameRequest;
import com.polaris.papiclientsdk.basicapi.model.response.GetUsernameResponse;
import com.polaris.papiclientsdk.common.execption.PapiClientSDKException;
import com.polaris.papiclientsdk.common.model.AbstractClient;
import com.polaris.papiclientsdk.common.model.Credential;
import com.polaris.papiclientsdk.common.profile.HttpProfile;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;


/**
 * @Author polaris
 * @Create 2024-03-08 15:24
 * @Version 1.0
 * ClassName ApiClient
 * Package com.polaris.apiinterface.client
 * Description
 */
@Slf4j
public class PapiClient extends AbstractClient {
    private String SDKVersion = "0.0.2-2024-04";
    private final String GATEWAY_HOST = "http://localhost:8090";

    public PapiClient (Credential credential, HttpProfile httpProfile){
        super(credential, httpProfile);
    }






    /*
     * 构造请求头的私有方法
     * @return headers
     * @author polaris
     * @create 2024/3/13
     **/

//    private HashMap<String, String> getHeaders(Credential credential,String body) {
//        String accessKey = credential.getAccessKey();
//        String secretKey = credential.getSecretKey();
//        HashMap<String, String> headers = new HashMap<>();
//        headers.put("accessKey", accessKey);
//        // 添加4位数字的随机数
//        headers.put("nonce", RandomUtil.randomNumbers(4));
//        // 添加包含用户参数的请求体内容
//        headers.put("body",body);
//        // 添加当前时间戳
//        headers.put("timestamp",String.valueOf(System.currentTimeMillis()/1000));
//        headers.put("secretKey", secretKey);
//        // 获取签名，并把签名也放入请求头
//        // 其他参数会添加到请求头中直接发送，但是密钥不能直接在服务器间传递。
//        headers.put("sign",genSign(body,secretKey));
//        return headers;
//    }


    public GetUsernameResponse getNameByGet(GetUsernameRequest request) throws PapiClientSDKException{
        return call(request, "getNameByGet");


//        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
//        HashMap<String, Object> paramMap = new HashMap<>();
//        paramMap.put("name", request.getUsername());
//        String result = HttpUtil.get(GATEWAY_HOST+"/api/name/a", paramMap);
//        log.info("get请求结果：{}", result);
//        return result;
    }

    public GetUsernameResponse getNameByPost1(GetUsernameRequest request) throws PapiClientSDKException{
        return call(request, "getNameByPost1");


        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
//        HashMap<String, Object> paramMap = new HashMap<>();
//        paramMap.put("name", request.getUsername());
//        String result = HttpUtil.post(GATEWAY_HOST+"/api/name/b", paramMap);
//        log.info("post1请求结果：{}", result);
//        return result;
    }

    public GetUsernameResponse getNameByPost2(GetUsernameRequest request) throws PapiClientSDKException{
        return call(request, "getNameByPost2");


//        // 将User对象转换为JSON格式的字符串
//        String jsonStr = JSONUtil.toJsonStr(request);
//        // 使用HttpRequest库发送POST请求，并获取服务器的响应
//        HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST+"/api/name/c")
//                .body(jsonStr)// 将json字符串设置为请求体
//                .addHeaders(getHeaders(this.getCredential(),jsonStr))// 添加请求头，携带AK和SK
//                .execute();// 执行请求
//        int status = httpResponse.getStatus();
//        String result = httpResponse.body();
//        log.info("status:{}",status);
//        log.info("post2请求结果：{}",result);
//        return result;
    }

}

