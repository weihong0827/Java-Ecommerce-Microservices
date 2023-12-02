package tech.qiuweihong.mapper;

import tech.qiuweihong.model.ProductDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Qiu Weihong
 * @since 2023-11-13
 */
public interface ProductMapper extends BaseMapper<ProductDO> {

    int lockProductStock(@Param("product_id")Long productId, @Param("buy_num")Integer count);

    void unlockProductStock(@Param("product_id") Long productId, @Param("buy_num")Integer buyNum);

    void updateStock(@Param("product_id") Long productId, @Param("buy_num")Integer buyNum);
}
