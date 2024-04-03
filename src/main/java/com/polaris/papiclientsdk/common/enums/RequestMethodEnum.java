package com.polaris.papiclientsdk.common.enums;

import lombok.Getter;

/**
 * @Author polaris
 * @Create 2024-04-02 13:39
 * @Version 1.0
 * ClassName RequestMethodEnum
 * Package com.polaris.papiclientsdk.common.enums
 * Description 暂不支持除get和post以外的请求方式
 */
@Getter
public enum RequestMethodEnum {
    GET("GET"),
    POST("POST");

    RequestMethodEnum(String method){
        this.method=method;
    }
    private final String method;
}
