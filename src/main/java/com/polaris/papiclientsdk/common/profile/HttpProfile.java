package com.polaris.papiclientsdk.common.profile;

import lombok.Data;

/**
 * @Author polaris
 * @Create 2024-04-02 11:36
 * @Version 1.0
 * ClassName HttpProfile
 * Package com.polaris.papiclientsdk.common.profile
 * Description
 */
@Data
public class HttpProfile {
    private String endpoint;
    private int port;
    private String path;
    private String method;
    private String protocol="http";
    private String contentType="application/json";
    private String charset="UTF-8";

    public HttpProfile (String endpoint, String path, String method){
        this.endpoint=endpoint;
        this.path=path;
        this.method=method;
    }
// todo 暂不支持代理   private String userAgent;

}
