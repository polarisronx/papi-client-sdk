package com.polaris.clienttest;

import com.polaris.papiclientsdk.basicapi.client.PapiClient;
import com.polaris.papiclientsdk.basicapi.model.request.IntToRomanRequest;
import com.polaris.papiclientsdk.basicapi.model.response.IntToRomanResponse;
import com.polaris.papiclientsdk.common.execption.PapiClientSDKException;
import com.polaris.papiclientsdk.common.model.Credential;
import com.polaris.papiclientsdk.common.profile.HttpProfile;
import com.polaris.papiclientsdk.common.utils.http.HttpConnection;

/**
 * @author polaris
 * @version 1.0
 * ClassName com.polaris.clienttest.InterfaceTest
 * Package PACKAGE_NAME
 * Description
 * @create 2024-07-10 13:32
 */
public class InterfaceTest {
    public static void main (String[] args){
        try {
            // 01 配置开发密钥
            Credential credential = new Credential("polaris", "abcdefgh");
            // 02 配置接口和请求参数
            HttpProfile httpProfile = new HttpProfile("localhost:8123", "/api/v1/roman/intToRoman", "GET");
            HttpConnection httpConnection = new HttpConnection();
            // 03 创建papi客户端
            PapiClient papi = new PapiClient(credential, httpProfile,httpConnection);
            // 04 发起请求（请确保您的开发密钥正确且积分充足）
            IntToRomanResponse intToRomanResponse = papi.intToRoman(new IntToRomanRequest("9"));
            // 05 打印结果
            System.out.println(intToRomanResponse.getRoman());
        }catch (PapiClientSDKException e){
            System.out.println(e.getMessage());
        }
    }
}
