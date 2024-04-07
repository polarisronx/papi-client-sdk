package com.polaris.papiclientsdk.common.model;

import lombok.Data;

import java.util.HashMap;

/**
 * @Author polaris
 * @Create 2024-04-02 14:24
 * @Version 1.0
 * ClassName CommonRequest
 * Package com.polaris.papiclientsdk.common.model
 * Description
 */
@Data
public class CommonRequest extends AbstractRequest<CommonResponse>{
    private String method;
    private String path;

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
