package tech.qiuweihong.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.qiuweihong.config.CallBackUrlConfig;
import tech.qiuweihong.vo.PayInfoVO;

@Service
@Slf4j
public class AlipayStrategy implements PayStrategy{
    @Autowired
    private CallBackUrlConfig callBackUrlConfig;

    @Override
    public String unifiedOrder(PayInfoVO payInfoVO) {
        return null;
    }

    @Override
    public String queryPaymentStatus(PayInfoVO payInfoVO) {
        return PayStrategy.super.queryPaymentStatus(payInfoVO);
    }
}
