package com.polaris.papiclientsdk.basicapi.client;

import com.polaris.papiclientsdk.basicapi.model.request.GetNameByPost1Request;
import com.polaris.papiclientsdk.basicapi.model.request.GetNameByPost2Request;
import com.polaris.papiclientsdk.basicapi.model.response.GetNameByPostResponse;
import com.polaris.papiclientsdk.common.execption.ErrorCode;
import com.polaris.papiclientsdk.common.execption.PapiClientSDKException;
import com.polaris.papiclientsdk.common.model.AbstractClient;
import com.polaris.papiclientsdk.common.model.AbstractRequest;
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
    private String sdkVersion = "0.0.2-2024-04";
    private String gatewayHost = "http://localhost:8090";
    private static final HashMap<String, AbstractRequest> request = new HashMap<>();

    static {
        request.put("GetNameByPost2", new GetNameByPost2Request());
        request.put("GetNameByPost1", new GetNameByPost1Request());
    }
    public PapiClient (Credential credential, HttpProfile httpProfile){
        super(credential, httpProfile);
    }

    @Override
    public AbstractRequest getRequest (String action) throws PapiClientSDKException{
        for (String key : request.keySet()){
            if (key.equals(action)) {
                return request.get(key);
            }
        }
        throw new PapiClientSDKException("没有这种Request类型", ErrorCode.PARAMS_ERROR);
    }

    public GetNameByPostResponse getNameByGet(GetNameByPost2Request request) throws PapiClientSDKException{
        return call(request, "getNameByGet");
    }

    public GetNameByPostResponse getNameByPost1(GetNameByPost2Request request) throws PapiClientSDKException{
        return call(request, "getNameByPost1");
    }

    public GetNameByPostResponse getNameByPost2(GetNameByPost2Request request) throws PapiClientSDKException{
        return call(request, "getNameByPost2");
    }

}

