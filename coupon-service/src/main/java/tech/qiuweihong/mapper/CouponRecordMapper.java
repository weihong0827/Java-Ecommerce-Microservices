package tech.qiuweihong.mapper;

import org.apache.ibatis.annotations.Param;
import tech.qiuweihong.model.CouponRecordDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Qiu Weihong
 * @since 2023-11-11
 */
public interface CouponRecordMapper extends BaseMapper<CouponRecordDO> {


    int lockCouponState(@Param("userId")Integer id, @Param("useState")String useState, @Param("couponList")List<Long> couponList);

    void updateState(@Param("couponRecordId") Long couponRecordId,@Param("state") String name);
}
