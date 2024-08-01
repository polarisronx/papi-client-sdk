package com.polaris.papiclientsdk.basicapi.model.response;

import com.polaris.papiclientsdk.common.model.CommonResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * @author polaris
 * @version 1.0
 * ClassName RomanToIntResponse
 * Package com.polaris.papiclientsdk.basicapi.model.response
 * Description
 * @create 2024-05-29 19:30
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RomanToIntResponse extends CommonResponse {
    private Integer num;
    public RomanToIntResponse(Integer num){
        this.num=num;
    }
    public RomanToIntResponse(){}
    @Override
    public void setCustomData(Map<String,Object> data){
        num= (Integer)data.get("num");
    }
}
