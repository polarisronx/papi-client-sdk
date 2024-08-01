package com.polaris.papiclientsdk.basicapi.model.request;


import cn.hutool.core.io.FileUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.polaris.papiclientsdk.basicapi.model.response.LstmPredictResponse;
import com.polaris.papiclientsdk.common.enums.RequestMethodEnum;
import com.polaris.papiclientsdk.common.model.AbstractRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * @author polaris
 * @version 1.0
 * ClassName LstmPredictRequest
 * Package com.polaris.apiinterface.model
 * Description
 * @create 2024-06-12 19:24
 */
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(value = { "file","fd","inputStream"})
@Data
public class LstmPredictRequest extends AbstractRequest<LstmPredictResponse> {
    private int timeStep;
    private double ratio;
    private int epoch;
    private int batchSize;
//    @SerializedName(value = "excelData",alternate = "csvData")
//    @Expose
    private byte[] file;
    public LstmPredictRequest(){

    }
    public LstmPredictRequest(int timeStep,double ratio,int epoch,int batchSize,byte[] file){
        this.timeStep=timeStep;
        this.ratio=ratio;
        this.epoch=epoch;
        this.batchSize=batchSize;
        this.file=file;
    }
    public LstmPredictRequest(String method,String path, Map<String, Object> params){
        this.path=path;
        this.method=method;
        this.customizedParams=params;
    }
    @Override
    public void setCustomField (Map<String, Object> params){
        for (Map.Entry<String, Object> entry : params.entrySet()){
            if (entry.getKey().equals("timeStep")) {
                this.timeStep = Integer.parseInt((String)entry.getValue());
            } else if (entry.getKey().equals("ratio")) {
                this.ratio = Double.parseDouble((String)entry.getValue());
            } else if (entry.getKey().equals("epoch")) {
                this.epoch = Integer.parseInt((String)entry.getValue());
            } else if (entry.getKey().equals("batchSize")) {
                this.batchSize = Integer.parseInt((String)entry.getValue());
            }
        }
    }

    @Override
    public String getMethod (){
        return RequestMethodEnum.POST.getMethod();
    }

    @Override
    public String getPath (){
        return "/api/v2/dl/lstm";
    }

    @Override
    public Class<LstmPredictResponse> getResponseClass (){
        return LstmPredictResponse.class;
    }



    public String [] getBinaryParams() {
        return new String [] {"excelData","csvData"};
    }
    public HashMap<String, byte []> getMultipartRequestParams() {
        HashMap<String, byte []> map = new HashMap<>();
        if (this.file != null) {
            if(FileUtil.getSuffix(this.info).equals("xlsx")) map.put("data.xlsx", this.file);
            else if (FileUtil.getSuffix(this.info).equals("csv")) map.put("data.csv", this.file);
        }
        return map;
    }


    @Override
    public void toMap (HashMap<String, String> params, String prefix){
        this.setParamSimple(params, prefix + "timeStep", this.timeStep);
        this.setParamSimple(params, prefix + "ratio", this.ratio);
        this.setParamSimple(params, prefix + "epoch", this.epoch);
        this.setParamSimple(params, prefix + "batchSize", this.batchSize);
        this.setParamSimple(params, prefix + "file", this.file);
    }
}
