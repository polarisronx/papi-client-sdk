package com.polaris.papiclientsdk.basicapi.model.request;

import com.polaris.papiclientsdk.basicapi.model.response.GetNameByPostResponse;
import com.polaris.papiclientsdk.common.enums.RequestMethodEnum;
import com.polaris.papiclientsdk.common.model.AbstractRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author polaris
 * @Create 2024-04-02 11:01
 * @Version 1.0
 * ClassName GetUsernameRequest
 * Package com.polaris.papiclientsdk.basicapi.model.request
 * Description
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GetNameByPost2Request extends AbstractRequest<GetNameByPostResponse> {
    private String username;

    public GetNameByPost2Request(){

    }
    public GetNameByPost2Request(String method,String path, Map<String, Object> params){
        this.path=path;
        this.method=method;
        this.customizedParams=params;

    }
    @Override
    public String getMethod (){
        return RequestMethodEnum.POST.getMethod();
    }

    public void setCustomField (Map<String,Object> params){
        for (Map.Entry<String, Object> entry : params.entrySet()){
            if (entry.getKey().equals("username")){
                this.username=(String)entry.getValue();
            }
        }
    }

    @Override
    public String getPath (){
        return "/api/name/c";
    }

    @Override
    public Class<GetNameByPostResponse> getResponseClass (){
        return GetNameByPostResponse.class;
    }

    @Override
    public void toMap (HashMap<String, String> params, String prefix){
        this.setParamSimple(params, prefix + "username", this.username);
    }
}
