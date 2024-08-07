package com.polaris.papiclientsdk.common.enums;



/**
 * @author polaris
 * @data 2024-04-02 13:39
 * @version 1.0
 * ClassName RequestMethodEnum
 * Package com.polaris.papiclientsdk.utils.enums
 * Description 暂不支持除get和post以外的请求方式
 */

public enum RequestMethodEnum {
    GET("GET"),
    POST("POST");
    private final String method;
    RequestMethodEnum(String method){
        this.method=method;
    }
    public String getMethod(){
        return method;
    }
}
