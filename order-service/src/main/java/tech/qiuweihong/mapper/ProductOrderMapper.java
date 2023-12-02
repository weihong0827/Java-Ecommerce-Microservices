package tech.qiuweihong.mapper;

import org.apache.ibatis.annotations.Param;
import tech.qiuweihong.model.ProductOrderDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.qiuweihong.model.ProductOrderItemDO;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Qiu Weihong
 * @since 2023-11-18
 */
public interface ProductOrderMapper extends BaseMapper<ProductOrderDO> {

}
