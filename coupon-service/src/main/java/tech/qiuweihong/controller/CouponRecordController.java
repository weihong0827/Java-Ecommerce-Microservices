package tech.qiuweihong.controller;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import tech.qiuweihong.enums.BizCodeEnum;
import tech.qiuweihong.request.LockCouponRecordRequest;
import tech.qiuweihong.service.CouponRecordService;
import tech.qiuweihong.utils.JsonData;
import tech.qiuweihong.vo.CouponRecordVO;

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
@RequestMapping("/api/coupon_record/v1")
public class CouponRecordController {

    @Autowired
    private CouponRecordService couponRecordService;
    @ApiOperation("Paginated personal coupon check")
    @GetMapping("/")
    public JsonData details(
            @ApiParam(value="Current page")  @RequestParam(value = "page",defaultValue = "1") int page,
            @ApiParam(value="Page size") @RequestParam(value="size",defaultValue = "20") int size
    ){
        Map<String,Object> details = couponRecordService.detail(page, size);
        return JsonData.buildSuccess(details);
    }
    @ApiOperation("Get Coupon Record Detail")
    @GetMapping("/{coupon_record_id}")
    public JsonData coupon_detail(
            @ApiParam(value="coupon record id") @PathVariable("coupon_record_id") int coupon_record_id
    ){
        CouponRecordVO couponRecordVO = couponRecordService.findById(coupon_record_id);
        return couponRecordVO==null ? JsonData.buildResult(BizCodeEnum.COUPON_NO_EXITS):JsonData.buildSuccess(couponRecordVO);

    }
    @ApiOperation("rpc - lock coupon record")
    @PostMapping("/lock_records")
    public JsonData lockCouponRecord(@ApiParam("Lock Coupon Model")@RequestBody LockCouponRecordRequest lockCouponRecordRequest){
        return couponRecordService.lockCouponRecord(lockCouponRecordRequest);

    }
}

