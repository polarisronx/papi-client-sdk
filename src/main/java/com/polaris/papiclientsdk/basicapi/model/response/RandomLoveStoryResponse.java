package com.polaris.papiclientsdk.basicapi.model.response;

import com.polaris.papiclientsdk.common.model.CommonResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * @author polaris
 * @version 1.0
 * ClassName RandomLoveStoryResponse
 * Package com.polaris.papiclientsdk.basicapi.model.response
 * Description
 * @create 2024-07-09 20:12
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RandomLoveStoryResponse extends CommonResponse {
    private String loveStory;
    public RandomLoveStoryResponse(String loveStory){
        this.loveStory=loveStory;
    }
    public RandomLoveStoryResponse(){}
    @Override
    public void setCustomData(Map<String,Object> data){
        loveStory= (String)data.get("loveStory");
    }
}
