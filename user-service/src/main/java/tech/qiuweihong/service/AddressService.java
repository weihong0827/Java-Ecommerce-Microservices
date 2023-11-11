package tech.qiuweihong.service;

import tech.qiuweihong.model.AddressDO;
import tech.qiuweihong.request.AddressAddRequest;

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

    void add(AddressAddRequest addressAddRequest);
}