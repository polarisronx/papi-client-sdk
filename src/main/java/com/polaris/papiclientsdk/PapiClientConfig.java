package com.polaris.papiclientsdk;

import com.polaris.papiclientsdk.client.PapiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Author polaris
 * @Create 2024-03-14 21:20
 * @Version 1.0
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
    @Bean
    public PapiClient papiClient(){
        // 传入AK和SK 创建一个 PApi 客户端
        return new PapiClient(accessKey,secretKey);
    }
}
