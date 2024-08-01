package com.polaris.papiclientsdk.basicapi.model.response;

import com.polaris.papiclientsdk.common.model.CommonResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * @author polaris
 * @version 1.0
 * ClassName RandomBoyAvatarResponse
 * Package com.polaris.papiclientsdk.basicapi.model.response
 * Description
 * @create 2024-07-09 20:42
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class RandomBoyAvatarResponse extends CommonResponse {
    private String avatarUrl;
    public RandomBoyAvatarResponse(String avatarUrl){
        this.avatarUrl=avatarUrl;
    }
    public RandomBoyAvatarResponse(){}
    @Override
    public void setCustomData(Map<String,Object> data){
        avatarUrl= (String)data.get("avatarUrl");
    }
}
