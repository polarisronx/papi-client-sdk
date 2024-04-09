package com.polaris.papiclientsdk.basicapi.client;

import com.polaris.papiclientsdk.basicapi.model.request.GetNameByPost1Request;
import com.polaris.papiclientsdk.basicapi.model.request.GetNameByPost2Request;
import com.polaris.papiclientsdk.basicapi.model.response.GetNameByPostResponse;
import com.polaris.papiclientsdk.common.execption.PapiClientSDKException;
import com.polaris.papiclientsdk.common.model.AbstractClient;
import com.polaris.papiclientsdk.common.model.AbstractRequest;
import com.polaris.papiclientsdk.common.model.CommonRequest;
import com.polaris.papiclientsdk.common.model.Credential;
import com.polaris.papiclientsdk.common.profile.HttpProfile;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;


/**
 * @Author polaris
 * @Create 2024-03-08 15:24
 * @Version 1.0
 * ClassName ApiClient
 * Package com.polaris.apiinterface.client
 * Description
 */
@Slf4j
public class PapiClient extends AbstractClient {
    public static final String SDK_VERSION = "2.0.0-2024-04";
    public static final String GATEWAY_HOST = "localhost:8090";

    private  static final HashMap<String, AbstractRequest> requestReady = new HashMap<>();

    static {
        requestReady.put("GetNameByPost2", new GetNameByPost2Request());
        requestReady.put("GetNameByPost1", new GetNameByPost1Request());
    }
    public PapiClient (Credential credential, HttpProfile httpProfile){
        super();
        this.setCredential(credential);
        this.setHttpProfile(httpProfile);
        this.setSdkVersion(SDK_VERSION);
        this.setGatewayHost(GATEWAY_HOST) ;
    }


    public GetNameByPost1Request getNameByPost1Request(){
        return new GetNameByPost1Request();
    }
    public GetNameByPost2Request getNameByPost2Request(){
        return new GetNameByPost2Request();
    }

    public GetNameByPostResponse getNameByGet(GetNameByPost2Request request) throws PapiClientSDKException{
        return call(request, "getNameByGet");
    }
    public GetNameByPostResponse getNameByGet(String method,String path,HashMap<String,Object> map) throws PapiClientSDKException{
        return call(new GetNameByPost1Request(), "getNameByGet");
    }

    public GetNameByPostResponse getNameByPost1(GetNameByPost2Request request) throws PapiClientSDKException{
        return call(request, "getNameByPost1");
    }

    public GetNameByPostResponse getNameByPost2(GetNameByPost2Request request) throws PapiClientSDKException{
        return call(request, "getNameByPost2");
    }
    public GetNameByPostResponse getNameByPost2(CommonRequest commonRequest) throws PapiClientSDKException{
        GetNameByPost2Request getNameByPost2Request = new GetNameByPost2Request(commonRequest.getMethod(),commonRequest.getPath(),commonRequest.getCustomizedParams());
        getNameByPost2Request.setCustomField(commonRequest.getCustomizedParams());
        return call(getNameByPost2Request, "getNameByPost2");
    }

}

