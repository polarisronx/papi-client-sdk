package com.polaris.papiclientsdk.basicapi.client;

import com.polaris.papiclientsdk.basicapi.model.request.*;
import com.polaris.papiclientsdk.basicapi.model.response.*;
import com.polaris.papiclientsdk.common.execption.PapiClientSDKException;
import com.polaris.papiclientsdk.common.model.AbstractClient;
import com.polaris.papiclientsdk.common.model.AbstractRequest;
import com.polaris.papiclientsdk.common.model.CommonRequest;
import com.polaris.papiclientsdk.common.model.Credential;
import com.polaris.papiclientsdk.common.profile.HttpProfile;
import com.polaris.papiclientsdk.common.utils.http.HttpConnection;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;


/**
 * @author polaris
 * @data 2024-03-08 15:24
 * @version 1.0
 * ClassName ApiClient
 * Package com.polaris.apiinterface.client
 * Description
 */
@Slf4j
public class PapiClient extends AbstractClient {
    public static final String SDK_VERSION = "2.0.0-2024-04";
    public static final String GATEWAY_HOST = "127.0.0.1:8090";
//    106.15.79.18:8090
    public static final String SIGN_METHOD = "HmacSHA1";


    public PapiClient (Credential credential, HttpProfile httpProfile, HttpConnection httpConnection){
        super();
        this.setCredential(credential);
        this.setHttpProfile(httpProfile);
        this.setSdkVersion(SDK_VERSION);
        this.setGatewayHost(GATEWAY_HOST);
        this.setSignMethod(SIGN_METHOD);
        this.setHttpConnection(httpConnection);
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
    public IntToRomanResponse intToRoman(CommonRequest commonRequest) throws PapiClientSDKException{
        IntToRomanRequest intToRomanRequest = new IntToRomanRequest(commonRequest.getMethod(), commonRequest.getPath(), commonRequest.getCustomizedParams());
        intToRomanRequest.setCustomField(commonRequest.getCustomizedParams());
        return call(intToRomanRequest,"intToRoman");
    }
    public IntToRomanResponse intToRoman(IntToRomanRequest intToRomanRequest) throws PapiClientSDKException{
        return call(intToRomanRequest,"intToRoman");
    }
    public LstmPredictResponse lstmPredict(CommonRequest commonRequest) throws PapiClientSDKException, IOException{
        LstmPredictRequest lstmPredictRequest = new LstmPredictRequest(commonRequest.getMethod(), commonRequest.getPath(), commonRequest.getCustomizedParams());
        lstmPredictRequest.setCustomField(commonRequest.getCustomizedParams());
        lstmPredictRequest.setInfo(commonRequest.getInfo());
        lstmPredictRequest.setFiles(commonRequest.files);
        lstmPredictRequest.setFile(commonRequest.files.get(commonRequest.getInfo()).getBytes());
        return call(lstmPredictRequest,"lstmPredict");
    }
    public LstmPredictResponse lstmPredict(LstmPredictRequest lstmPredictRequest) throws PapiClientSDKException{
        return call(lstmPredictRequest,"lstmPredict");
    }
    public RandomLoveStoryResponse randomLoveStory(RandomLoveStoryRequest randomLoveStoryRequest) throws PapiClientSDKException{
        return call(randomLoveStoryRequest,"randomLoveStory");
    }
    public RandomLoveStoryResponse randomLoveStory(CommonRequest commonRequest) throws PapiClientSDKException{
        RandomLoveStoryRequest randomLoveStoryRequest = new RandomLoveStoryRequest(commonRequest.getMethod(), commonRequest.getPath(), commonRequest.getCustomizedParams());
        return call(randomLoveStoryRequest,"randomLoveStory");
    }
    public GetIpResponse getIp(GetIpRequest getIpRequest) throws PapiClientSDKException{
        return call(getIpRequest,"getIp");
    }
    public GetIpResponse getIp(CommonRequest commonRequest) throws PapiClientSDKException{
        GetIpRequest getIpRequest = new GetIpRequest(commonRequest.getMethod(), commonRequest.getPath(), commonRequest.getCustomizedParams());
        return call(getIpRequest,"getIp");
    }
    public RandomBoyAvatarResponse randomBoyAvatar(RandomBoyAvatarRequest randomBoyAvatarRequest) throws PapiClientSDKException{
        return call(randomBoyAvatarRequest,"randomBoyAvatar");
    }
    public RandomBoyAvatarResponse randomBoyAvatar(CommonRequest commonRequest) throws PapiClientSDKException{
        RandomBoyAvatarRequest randomBoyAvatarRequest = new RandomBoyAvatarRequest(commonRequest.getMethod(), commonRequest.getPath(), commonRequest.getCustomizedParams());
        return call(randomBoyAvatarRequest,"randomBoyAvatar");
    }
    public RandomGirlAvatarResponse randomGirlAvatar(RandomGirlAvatarRequest randomGirlAvatarRequest) throws PapiClientSDKException{
        return call(randomGirlAvatarRequest,"randomGirlAvatar");
    }
    public RandomGirlAvatarResponse randomGirlAvatar(CommonRequest commonRequest) throws PapiClientSDKException{
        RandomGirlAvatarRequest randomGirlAvatarRequest = new RandomGirlAvatarRequest(commonRequest.getMethod(), commonRequest.getPath(), commonRequest.getCustomizedParams());
        return call(randomGirlAvatarRequest,"randomGirlAvatar");
    }
}

