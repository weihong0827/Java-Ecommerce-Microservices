package tech.qiuweihong.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlipayConfig {

    public static final String APPID = System.getenv("ALIPAY_APPID");

    public static final String APP_PRI_KEY = System.getenv("ALIPAY_PRI_KEY");

    public static final String ALIPAY_PUB_KEY = System.getenv("ALIPAY_PUB_KEY");

    public static final String SIGN_TYPE = "RSA2";

    public static final String CHARSET = "UTF-8";
    public static final String FORMAT = "json";

    public static final String GATEWAY_URL = System.getenv("ALIPAY_GATEWAY");

    private AlipayConfig(){

    }

    private volatile  static AlipayClient instance = null;

    public static AlipayClient getInstance(){

        if (instance==null){
            synchronized (AlipayConfig.class){
                if (instance==null){
                    instance = new DefaultAlipayClient(GATEWAY_URL,APPID,APP_PRI_KEY,FORMAT,CHARSET,ALIPAY_PUB_KEY,SIGN_TYPE);
                }
            }
        }
        return instance;

    }


}
