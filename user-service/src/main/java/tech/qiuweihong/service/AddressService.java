package tech.qiuweihong.service;

import tech.qiuweihong.model.AddressDO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Qiu Weihong
 * @since 2023-07-27
 */

public interface AddressService {
    AddressDO detail(Long id);
}