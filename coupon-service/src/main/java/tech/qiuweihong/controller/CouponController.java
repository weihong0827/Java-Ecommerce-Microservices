package tech.qiuweihong.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import springfox.documentation.spring.web.json.Json;
import tech.qiuweihong.enums.CouponCategoryEnum;
import tech.qiuweihong.request.NewUserCouponRequest;
import tech.qiuweihong.service.CouponService;
import tech.qiuweihong.utils.JsonData;

import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Qiu Weihong
 * @since 2023-11-11
 */
@RestController
@Api("Coupon")
@RequestMapping("/api/coupon/v1")
public class CouponController {
    @Autowired
    private CouponService couponService;
    @GetMapping("/page_coupon")
    @ApiOperation("Pagniated coupon search")
    public JsonData pageCouponList(
            @ApiParam(value="Current page")  @RequestParam(value = "page",defaultValue = "1") int page,
            @ApiParam(value="Page size") @RequestParam(value="size",defaultValue = "10") int size
    ){
        Map<String,Object> pageMap = couponService.pageCouponActivity(page,size);
        return JsonData.buildSuccess(pageMap);
    }
    @PostMapping("/claim/promotion/{coupon_id}")
    public JsonData claimPromotionCoupon(
            @ApiParam(value = "coupon id",required = true)
            @PathVariable("coupon_id") long couponId
    ){
        return couponService.claimCoupon(couponId, CouponCategoryEnum.PROMOTION);
    }

    @ApiOperation("RPC-New user registration claim coupon")
    @PostMapping("/new_user_coupon")
    public JsonData claimNewUserCoupon(@ApiParam("User Object") @RequestBody NewUserCouponRequest newUserCouponRequest){
        JsonData jsonData = couponService.initNewUserCoupon(newUserCouponRequest);
        return jsonData;

    }

}

