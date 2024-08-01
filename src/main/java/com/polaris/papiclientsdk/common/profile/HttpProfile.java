package com.polaris.papiclientsdk.common.profile;

import lombok.Data;

/**
 * @author polaris
 * @data 2024-04-02 11:36
 * @version 1.0
 * ClassName HttpProfile
 * Package com.polaris.papiclientsdk.utils.profile
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
    public HttpProfile (String endpoint, String path, String method,String protocol,String contentType,String charset){
        this.endpoint=endpoint;
        this.path=path;
        this.method=method;
        this.protocol=protocol;
        this.contentType=contentType;
        this.charset=charset;
    }
// todo 暂不支持代理   private String userAgent;

}
