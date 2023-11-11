package tech.qiuweihong.service;

import tech.qiuweihong.request.AddressAddRequest;
import tech.qiuweihong.vo.AddressVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Qiu Weihong
 * @since 2023-07-27
 */

public interface AddressService {
    AddressVO detail(Long id);

    void add(AddressAddRequest addressAddRequest);

    int delete(Long id);

    List<AddressVO> list();
}