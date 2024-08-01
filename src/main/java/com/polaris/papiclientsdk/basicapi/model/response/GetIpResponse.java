package com.polaris.papiclientsdk.basicapi.model.response;

import com.polaris.papiclientsdk.common.model.CommonResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * @author polaris
 * @version 1.0
 * ClassName GetIpResponse
 * Package com.polaris.papiclientsdk.basicapi.model.response
 * Description
 * @create 2024-07-09 20:29
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GetIpResponse extends CommonResponse {
    private String ip;
    private String country;
    private String prov;
    private String city;
    private String isp;
    public GetIpResponse(String ip,String country,String prov,String city,String isp){
        this.ip=ip;
        this.country=country;
        this.prov=prov;
        this.city=city;
        this.isp=isp;
    }



    public GetIpResponse(){}
    @Override
    public void setCustomData(Map<String,Object> data){
        ip= (String)data.get("ip");
        country= (String)data.get("country");
        prov= (String)data.get("prov");
        city= (String)data.get("city");
        isp= (String)data.get("isp");

    }
}
