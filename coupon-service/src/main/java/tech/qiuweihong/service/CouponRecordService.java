package tech.qiuweihong.service;

import tech.qiuweihong.model.CouponRecordDO;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiuweihong.request.LockCouponRecordRequest;
import tech.qiuweihong.utils.JsonData;
import tech.qiuweihong.vo.CouponRecordVO;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Qiu Weihong
 * @since 2023-11-11
 */
public interface CouponRecordService  {
    Map<String,Object> detail(int page, int size);

    CouponRecordVO findById(int recordId);

    JsonData lockCouponRecord(LockCouponRecordRequest lockCouponRecordRequest);
}
