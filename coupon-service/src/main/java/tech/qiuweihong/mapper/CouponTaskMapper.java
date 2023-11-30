package tech.qiuweihong.mapper;

import org.apache.ibatis.annotations.Param;
import tech.qiuweihong.model.CouponTaskDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Qiu Weihong
 * @since 2023-11-29
 */
public interface CouponTaskMapper extends BaseMapper<CouponTaskDO> {

    int insertBatch(@Param("couponTaskList")List<CouponTaskDO> couponTaskDOS);
}
