package com.polaris.papiclientsdk.common.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author polaris
 * @data 2024-04-02 14:24
 * @version 1.0
 * ClassName CommonRequest
 * Package com.polaris.papiclientsdk.utils.model
 * Description
 */
@Data
public class CommonRequest extends AbstractRequest<CommonResponse>{
    private String method;
    private String path;

    @Override
    public void setCustomField (Map<String, Object> params){

    }

    @Override
    public String getMethod (){
        return method;
    }

    @Override
    public String getPath (){
        return path;
    }

    @Override
    public Class<CommonResponse> getResponseClass (){
        return CommonResponse.class;
    }

    @Override
    public void toMap (HashMap<String, String> params, String prefix){

    }
}
