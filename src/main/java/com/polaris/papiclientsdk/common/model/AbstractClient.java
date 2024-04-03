package com.polaris.papiclientsdk.common.model;

import com.polaris.papiclientsdk.common.profile.HttpProfile;
import lombok.Data;

/**
 * @Author polaris
 * @Create 2024-04-02 11:07
 * @Version 1.0
 * ClassName AbstractClient
 * Package com.polaris.papiclientsdk.common.utils
 * Description
 */
@Data
public abstract class AbstractClient {
    private Credential credential;
    private String signMethod;
    private String SDKVersion;
    private String apiVersion;
    private HttpProfile httpProfile;
    private String GATEWAY_HOST;

    public AbstractClient (Credential credential){
        this.credential = credential;
    }
}
