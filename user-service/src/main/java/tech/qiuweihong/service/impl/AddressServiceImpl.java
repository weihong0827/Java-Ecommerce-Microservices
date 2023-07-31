package tech.qiuweihong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import tech.qiuweihong.model.AddressDO;
import tech.qiuweihong.mapper.AddressMapper;
import tech.qiuweihong.service.AddressService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Qiu Weihong
 * @since 2023-07-27
 */
@Service
public class AddressServiceImpl implements AddressService{
    @Autowired
    private AddressMapper addressMapper;
    @Override
    public AddressDO detail(Long id) {
        return addressMapper.selectOne(new QueryWrapper<AddressDO>().eq("user_id",id));
    }
}
