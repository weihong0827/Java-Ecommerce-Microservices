package tech.qiuweihong.service.impl;

import tech.qiuweihong.model.UserDO;
import tech.qiuweihong.mapper.UserMapper;
import tech.qiuweihong.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

}
