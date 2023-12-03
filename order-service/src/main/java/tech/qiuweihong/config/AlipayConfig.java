package tech.qiuweihong.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlipayConfig {
    static Dotenv dotenv = Dotenv.load();

    public static final String APPID = dotenv.get("ALIPAY_APPID");

    public static final String APP_PRI_KEY = dotenv.get("ALIPAY_PRI_KEY");

    public static final String ALIPAY_PUB_KEY = dotenv.get("ALIPAY_PUB_KEY");

    public static final String SIGN_TYPE = "RSA2";

    public static final String CHARSET = "UTF-8";
    public static final String FORMAT = "json";

    public static final String GATEWAY_URL = dotenv.get("ALIPAY_GATEWAY");

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
