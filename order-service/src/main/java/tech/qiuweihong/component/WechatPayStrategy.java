package tech.qiuweihong.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.qiuweihong.vo.PayInfoVO;

@Service
@Slf4j
public class WechatPayStrategy implements PayStrategy{
    @Override
    public String unifiedOrder(PayInfoVO payInfoVO) {
        return null;
    }

    @Override
    public String queryPaymentStatus(PayInfoVO payInfoVO) {
        return PayStrategy.super.queryPaymentStatus(payInfoVO);
    }
}
