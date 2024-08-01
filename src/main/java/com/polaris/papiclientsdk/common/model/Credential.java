package com.polaris.papiclientsdk.common.model;

import lombok.Data;

/**
 * @author polaris
 * @data 2024-04-02 11:38
 * @version 1.0
 * ClassName Credential
 * Package com.polaris.papiclientsdk.utils
 * Description 用户登录凭证信息
 */
@Data
public class Credential {
    private String accessKey;
    private String secretKey;

    public Credential (String accessKey, String secretKey){
        this.accessKey=accessKey;
        this.secretKey=secretKey;
    }
}
