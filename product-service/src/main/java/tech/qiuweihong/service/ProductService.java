package tech.qiuweihong.service;

import org.springframework.stereotype.Service;
import tech.qiuweihong.model.ProductDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Qiu Weihong
 * @since 2023-11-13
 */
public interface ProductService  {


    Map<String, Object> listProducts(int page, int size);
}
