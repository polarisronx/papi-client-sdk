package com.polaris.papiclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;

import com.polaris.papiclientsdk.model.User;
import lombok.extern.slf4j.Slf4j;
import java.util.HashMap;

import static com.polaris.papiclientsdk.utils.SignUtils.genSign;


/**
 * @Author polaris
 * @Create 2024-03-08 15:24
 * @Version 1.0
 * ClassName ApiClient
 * Package com.polaris.apiinterface.client
 * Description
 */
@Slf4j
public class PapiClient {

    // ak 和 sk 由平台提供
    private String accessKey;
    private String secretKey;

    public PapiClient (String accessKey, String secretKey){
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    /*
     * 构造请求头的私有方法
     * @return headers
     * @author polaris
     * @create 2024/3/13
     **/

    private HashMap<String, String> getHeaders(String body) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("accessKey", accessKey);
        // 添加4位数字的随机数
        headers.put("nonce", RandomUtil.randomNumbers(4));
        // 添加包含用户参数的请求体内容
        headers.put("body",body);
        // 添加当前时间戳
        headers.put("timestamp",String.valueOf(System.currentTimeMillis()/1000));
        headers.put("secretKey", secretKey);
        // 获取签名，并把签名也放入请求头
        // 其他参数会添加到请求头中直接发送，但是密钥不能直接在服务器间传递。
        headers.put("sign",genSign(body,secretKey));
        return headers;
    }


    public String getNameByGet(String name){
        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        String result = HttpUtil.get("http://localhost:8123/api/name/a", paramMap);
        log.info("get请求结果：{}", result);
        return result;
    }

    public String getNameByPost1(String name){
        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        String result = HttpUtil.post("http://localhost:8123/api/name/b", paramMap);
        log.info("post1请求结果：{}", result);
        return result;
    }

    public String getNameByPost2(User user){
        // 将User对象转换为JSON格式的字符串
        String jsonStr = JSONUtil.toJsonStr(user);
        // 使用HttpRequest库发送POST请求，并获取服务器的响应
        HttpResponse httpResponse = HttpRequest.post("http://localhost:8123/api/name/c")
                .body(jsonStr)// 将json字符串设置为请求体
                .addHeaders(getHeaders(jsonStr))// 添加请求头，携带AK和SK
                .execute();// 执行请求
        int status = httpResponse.getStatus();
        String result = httpResponse.body();
        log.info("status:{}",status);
        log.info("post2请求结果：{}",result);
        return result;
    }
}
