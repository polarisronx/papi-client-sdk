package com.polaris.papiclientsdk.basicapi.model.response;

import com.polaris.papiclientsdk.common.model.CommonResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * @author polaris
 * @version 1.0
 * ClassName IntToRomanResponse
 * Package com.polaris.papiclientsdk.basicapi.model.response
 * Description
 * @create 2024-05-28 20:21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class IntToRomanResponse extends CommonResponse {
    private String roman;
    public IntToRomanResponse(String roman){
        this.roman=roman;
    }
    public IntToRomanResponse(){}
    @Override
    public void setCustomData(Map<String,Object> data){
        roman= (String)data.get("roman");
    }
}
