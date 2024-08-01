package com.polaris.papiclientsdk.basicapi.model.request;

import com.polaris.papiclientsdk.basicapi.model.response.GetNameByPostResponse;
import com.polaris.papiclientsdk.common.enums.RequestMethodEnum;
import com.polaris.papiclientsdk.common.model.AbstractRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author polaris
 * @data 2024-04-02 11:01
 * @version 1.0
 * ClassName GetUsernameRequest
 * Package com.polaris.papiclientsdk.basicapi.model.request
 * Description
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GetNameByPost1Request extends AbstractRequest<GetNameByPostResponse> {
    private String username;

    @Override
    public void setCustomField (Map<String, Object> params){
        for (Map.Entry<String, Object> entry : params.entrySet()){
            if (entry.getKey().equals("username")){
                this.username=(String)entry.getValue();
            }
        }
    }

    @Override
    public String getMethod (){
        return RequestMethodEnum.GET.getMethod();
    }

    @Override
    public String getPath (){
        return "/name/a";
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
