package com.polaris.papiclientsdk.common.model;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.polaris.papiclientsdk.common.enums.RequestMethodEnum;
import lombok.Data;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author polaris
 * @Create 2024-04-02 11:07
 * @Version 1.0
 * ClassName AbstactModel
 * Package com.polaris.papiclientsdk.common.utils
 * Description
 */
@Data
public abstract class AbstractRequest<T extends CommonResponse> {
    public Map<String,String> header = new HashMap<>();
    private HashMap<String, Object> customizedParams = new HashMap<>();
    private boolean skipSign = false;
    private boolean isUnsignedPayload;

    public static <O extends AbstractRequest> String toJsonString (O obj){
        return toJsonObject(obj).toString();
    }
    private static <O extends AbstractRequest> JsonObject toJsonObject(O obj) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        JsonObject joall = new JsonObject();
        JsonObject joadd = gson.toJsonTree(obj.getCustomizedParams()).getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : joadd.entrySet()) {
            joall.add(entry.getKey(), entry.getValue());
        }
        // jopublic will override joadd if key conflict exists
        JsonObject jopublic = gson.toJsonTree(obj).getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : jopublic.entrySet()) {
            Object fo = null;
            try {
                Field f = obj.getClass().getDeclaredField(entry.getKey());
                f.setAccessible(true);
                fo = f.get(obj);
            } catch (Exception e) {
                // this should never happen
                e.printStackTrace();
            }
            if (fo instanceof AbstractRequest) {
                joall.add(entry.getKey(), toJsonObject((AbstractRequest) fo));
            } else {
                joall.add(entry.getKey(), entry.getValue());
            }
        }
        return joall;
    }

    /**
     * 请求方法
     *
     * @return {@link RequestMethodEnum}
     */
    public abstract String getMethod();
    /**
     * 获取路径
     *
     * @return {@link String}
     */
    public abstract String getPath();

    /**
     * 获取响应类
     *
     * @return {@link Class}<{@link T}>
     */
    public abstract Class<T> getResponseClass();

    @JsonAnyGetter
    public Map<String, Object> getCustomizedParams() {
        return customizedParams;
    }

    public void toMap (HashMap<String, String> params, String prefix){

    }

    public boolean getSkipSign (){
        return  this.skipSign;
    }
    /*
     *   设置请求参数
     *
     */
//    public void setRequestParams(O params) {
//
//        this.customizedParams = new Gson().fromJson(JSONUtil.toJsonStr(params), new TypeToken<Map<String, Object>>() {
//        }.getType());
//    }

}
