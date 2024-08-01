package com.polaris.papiclientsdk.common.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.polaris.papiclientsdk.common.enums.RequestMethodEnum;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author polaris
 * @data 2024-04-02 11:07
 * @version 1.0
 * ClassName AbstactModel
 * Package com.polaris.papiclientsdk.utils.utils
 * Description
 */
@Data
public abstract class AbstractRequest<T extends CommonResponse> {
    public Map<String,String> header = new HashMap<>();
    public Map<String, Object> customizedParams = new HashMap<>();
    public Map<String,MultipartFile> files = new HashMap<>();
    public String info;// 用于必要时传递一些参数
    private boolean skipSign = false;
    private boolean isUnsignedPayload;
    public String method; // 全大写
    public String path;


    public static <O extends AbstractRequest> String toJsonString (O obj){
        return toJsonObject(obj).toString();
    }


    public abstract void setCustomField (Map<String,Object> params);

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

    public abstract void toMap (HashMap<String, String> params, String prefix);
    protected <V> void setParamSimple(HashMap<String, String> map, String key, V value) {
        if (value != null) {

//            key = key.substring(0, 1).toUpperCase() + key.substring(1);
            key = key.replace("_", ".");
            map.put(key, String.valueOf(value));
        }
    }
    public boolean getIsUnsignedPayload(){
        return this.isUnsignedPayload;
    }
    public boolean getSkipSign (){
        return  this.skipSign;
    }

    /**
     * @Description 用于标记哪些参数是multipart类型，需要转为二进制类型参数
     * @author polaris
     * @create 2024/6/18
     * @return {@link String[]}
     */

    public String[] getBinaryParams (){
        return new String[0];
    }

    /**
     * 当存在时设置multipart请求对象.
     */
    public HashMap<String, byte[]> getMultipartRequestParams() {
        return new HashMap<String, byte[]>();
    }


}
