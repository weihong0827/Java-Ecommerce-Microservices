package tech.qiuweihong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tech.qiuweihong.enums.AddressStatusEnum;
import tech.qiuweihong.interceptor.LoginInterceptor;
import tech.qiuweihong.mapper.UserMapper;
import tech.qiuweihong.model.AddressDO;
import tech.qiuweihong.mapper.AddressMapper;
import tech.qiuweihong.model.LoginUser;
import tech.qiuweihong.model.UserDO;
import tech.qiuweihong.request.AddressAddRequest;
import tech.qiuweihong.service.AddressService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Qiu Weihong
 * @since 2023-07-27
 */
@Service
@Slf4j
public class AddressServiceImpl implements AddressService{
    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private UserMapper userMapper;
    @Override
    public AddressDO detail(Long id) {
        return addressMapper.selectOne(new QueryWrapper<AddressDO>().eq("user_id",id));
    }

    @Override
    public void add(AddressAddRequest addressAddRequest) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        AddressDO addressDO = new AddressDO();
        addressDO.setCreateTime(new Date());
        addressDO.setUserId(Long.valueOf(loginUser.getId()));
        BeanUtils.copyProperties(addressAddRequest,addressDO);

        if (addressDO.getDefaultStatus()==AddressStatusEnum.DEFAULT_STATUS.getStatus()){
            AddressDO defaultAddressDo = addressMapper.selectOne(new QueryWrapper<AddressDO>().eq("user_id",loginUser.getId()).eq("default_status", AddressStatusEnum.DEFAULT_STATUS.getStatus()));
            if (defaultAddressDo!=null){
                defaultAddressDo.setDefaultStatus(AddressStatusEnum.COMMON_STATUS.getStatus());
                addressMapper.update(defaultAddressDo,new QueryWrapper<AddressDO>().eq("id",defaultAddressDo.getId()));
            }

        }

        int row = addressMapper.insert(addressDO);
        log.info("added address: row={}, data={}",row,addressDO);

    }
}
