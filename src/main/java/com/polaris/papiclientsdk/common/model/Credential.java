package com.polaris.papiclientsdk.common.model;

import lombok.Data;

/**
 * @Author polaris
 * @Create 2024-04-02 11:38
 * @Version 1.0
 * ClassName Credential
 * Package com.polaris.papiclientsdk.common
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
