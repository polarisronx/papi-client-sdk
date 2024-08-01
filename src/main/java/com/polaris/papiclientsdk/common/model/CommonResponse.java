package com.polaris.papiclientsdk.common.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author polaris
 * @data 2024-04-02 12:06
 * @version 1.0
 * ClassName AbstractResponse
 * Package com.polaris.papiclientsdk.utils
 * Description
 */
@Data
public class CommonResponse implements Serializable {
    public static final int HTTP_RSP_OK = 200;
    private int code;
    private static final long serialVersionUID = 2759257295792653L;
    private Map<String, Object> data = new HashMap<>();


    @JsonAnyGetter
    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
    public void setCustomData(Map<String,Object> data){

    }

}
