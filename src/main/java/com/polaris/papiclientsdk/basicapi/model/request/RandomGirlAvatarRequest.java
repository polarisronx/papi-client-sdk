package com.polaris.papiclientsdk.basicapi.model.request;

import com.polaris.papiclientsdk.basicapi.model.response.RandomBoyAvatarResponse;
import com.polaris.papiclientsdk.basicapi.model.response.RandomGirlAvatarResponse;
import com.polaris.papiclientsdk.common.enums.RequestMethodEnum;
import com.polaris.papiclientsdk.common.model.AbstractRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author polaris
 * @version 1.0
 * ClassName RandomBoyAvatarRequest
 * Package com.polaris.papiclientsdk.basicapi.model.request
 * Description
 * @create 2024-07-09 20:41
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class RandomGirlAvatarRequest extends AbstractRequest<RandomGirlAvatarResponse> {
    public RandomGirlAvatarRequest (){

    }
    public RandomGirlAvatarRequest (String method, String path, Map<String, Object> params){
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
        return "/api/v2/avatar/girl";
    }

    @Override
    public Class getResponseClass (){
        return RandomGirlAvatarResponse.class;
    }

    @Override
    public void toMap (HashMap<String, String> params, String prefix){
    }
}
