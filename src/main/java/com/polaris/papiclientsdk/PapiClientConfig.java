package com.polaris.papiclientsdk;

import com.polaris.papiclientsdk.basicapi.client.PapiClient;
import com.polaris.papiclientsdk.common.model.Credential;
import com.polaris.papiclientsdk.common.profile.HttpProfile;
import com.polaris.papiclientsdk.common.utils.http.HttpConnection;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author polaris
 * @data 2024-03-14 21:20
 * @version 1.0
 * ClassName PapiClientConfig
 * Package com.polaris.papiclientsdk
 * Description
 */
@Configuration
@ConfigurationProperties("papi.client")
@Data
@ComponentScan
public class PapiClientConfig {
    private String accessKey;
    private String secretKey;
    private String endpoint;
    private String path;
    private String method;
    private int readTimeout;
    private int connTimeout;
    private int writeTimeout;
    @Bean
    @ConfigurationProperties(prefix = "papi.client.http")
    public PapiClient papiClient(){
        // 传入AK和SK 创建一个 PApi 客户端
        return new PapiClient(new Credential(accessKey,secretKey),new HttpProfile(endpoint,path,method),new HttpConnection(connTimeout,readTimeout,writeTimeout));
    }
}
