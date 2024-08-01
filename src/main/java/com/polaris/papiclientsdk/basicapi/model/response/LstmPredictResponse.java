package com.polaris.papiclientsdk.basicapi.model.response;

import com.polaris.papiclientsdk.common.model.CommonResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author polaris
 * @version 1.0
 * ClassName LstmPredictResponse
 * Package com.polaris.papiclientsdk.basicapi.model.response
 * Description
 * @create 2024-06-12 19:29
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LstmPredictResponse extends CommonResponse {
    private String predictResult;
    @Override
    public void setCustomData(Map<String,Object> data){
        predictResult= (String)data.get("predictResult");
    }
}
