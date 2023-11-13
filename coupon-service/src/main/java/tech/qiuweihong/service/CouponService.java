package tech.qiuweihong.service;

import tech.qiuweihong.enums.CouponCategoryEnum;
import tech.qiuweihong.model.CouponDO;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiuweihong.request.NewUserCouponRequest;
import tech.qiuweihong.utils.JsonData;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Qiu Weihong
 * @since 2023-11-11
 */
public interface CouponService  {
    Map<String,Object> pageCouponActivity(int page, int size);

    JsonData claimCoupon(long couponId, CouponCategoryEnum category);

    JsonData initNewUserCoupon(NewUserCouponRequest newUserCouponRequest);
}
