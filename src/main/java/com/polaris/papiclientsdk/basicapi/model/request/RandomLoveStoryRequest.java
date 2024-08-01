package com.polaris.papiclientsdk.basicapi.model.request;

import com.polaris.papiclientsdk.basicapi.model.response.IntToRomanResponse;
import com.polaris.papiclientsdk.basicapi.model.response.RandomLoveStoryResponse;
import com.polaris.papiclientsdk.common.enums.RequestMethodEnum;
import com.polaris.papiclientsdk.common.model.AbstractRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author polaris
 * @version 1.0
 * ClassName RandomLoveStoryRequest
 * Package com.polaris.papiclientsdk.basicapi.model.request
 * Description
 * @create 2024-07-09 20:08
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RandomLoveStoryRequest extends AbstractRequest<RandomLoveStoryResponse> {
    public RandomLoveStoryRequest(){

    }
    public RandomLoveStoryRequest(String method,String path, Map<String, Object> params){
        this.path=path;
        this.method=method;
        this.customizedParams=params;

    }
    @Override
    public String getMethod (){
        return RequestMethodEnum.GET.getMethod();
    }
    @Override
    public void setCustomField (Map<String,Object> params){
    }

    @Override
    public String getPath (){
        return "/api/v1/useful-tool/loveStory";
    }

    @Override
    public Class getResponseClass (){
        return RandomLoveStoryResponse.class;
    }

    @Override
    public void toMap (HashMap<String, String> params, String prefix){
    }
}
