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
    private String protocol;
    private String contentType;
    private String charset;
    private String userAgent;
    private String cookie;
    private String GATEWAY_HOST;
}
