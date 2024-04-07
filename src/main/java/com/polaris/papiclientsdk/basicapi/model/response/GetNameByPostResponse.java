package com.polaris.papiclientsdk.basicapi.model.response;

import com.polaris.papiclientsdk.common.model.CommonResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author polaris
 * @Create 2024-04-02 11:01
 * @Version 1.0
 * ClassName getUsernameResponse
 * Package com.polaris.papiclientsdk.basicapi.model.response
 * Description
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GetNameByPostResponse extends CommonResponse {
    private String username;
}
