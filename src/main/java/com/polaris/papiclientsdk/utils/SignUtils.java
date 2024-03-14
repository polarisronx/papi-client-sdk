package com.polaris.papiclientsdk.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

/**
 * @Author polaris
 * @Create 2024-03-13 14:31
 * @Version 1.0
 * ClassName SignUtils
 * Package com.polaris.apiinterface.utils
 * Description 生成签名
 */
public class SignUtils {
    public static String genSign(String body,String secretKey){
        // 使用SHA25算法的Digester
        Digester digester = new Digester(DigestAlgorithm.SHA256);
        // 签名内容 body + 密钥 拼接
        // 也可以把 nonce timestamp 都加入到 签名中增加复杂度，但这些参数都是要一一获取校验的，因此为了简化，这里不加入这些参数
        String signContent = body +"."+ secretKey;
        return digester.digestHex(signContent);
    }
}
