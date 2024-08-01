package com.polaris.papiclientsdk.basicapi.model.request;

import com.polaris.papiclientsdk.basicapi.model.response.GetNameByPostResponse;
import com.polaris.papiclientsdk.basicapi.model.response.IntToRomanResponse;
import com.polaris.papiclientsdk.common.enums.RequestMethodEnum;
import com.polaris.papiclientsdk.common.model.AbstractRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author polaris
 * @version 1.0
 * ClassName IntToRomanRequest
 * Package com.polaris.papiclientsdk.basicapi.model.request
 * Description
 * @create 2024-05-28 20:18
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class IntToRomanRequest extends AbstractRequest<IntToRomanResponse> {
    private String num;

    public IntToRomanRequest(){

    }
    public IntToRomanRequest(String num){
        this.num=num;
    }
    public IntToRomanRequest(String method,String path, Map<String, Object> params){
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
        for (Map.Entry<String, Object> entry : params.entrySet()){
            if (entry.getKey().equals("num")){
                this.num=(String)entry.getValue();
            }
        }
    }

    @Override
    public String getPath (){
        return "/api/v1/roman/intToRoman";
    }

    @Override
    public Class getResponseClass (){
        return IntToRomanResponse.class;
    }

    @Override
    public void toMap (HashMap<String, String> params, String prefix){
        this.setParamSimple(params, prefix + "num", this.num);
    }
}
