package tech.qiuweihong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tech.qiuweihong.mapper.BannerMapper;
import tech.qiuweihong.model.BannerDO;
import tech.qiuweihong.service.BannerService;
import org.springframework.stereotype.Service;
import tech.qiuweihong.vo.BannerVO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Qiu Weihong
 * @since 2023-11-13
 */
@Service
public class BannerServiceImpl implements BannerService {
    @Autowired
    private BannerMapper bannerMapper;

    @Override
    public List<BannerVO> listBanner() {
        List<BannerDO> bannerDOList = bannerMapper.selectList(new QueryWrapper<BannerDO>().orderByAsc("weight"));
        List<BannerVO> bannerVOList = bannerDOList.stream().map(obj->{
            BannerVO bannerVO = new BannerVO();
            BeanUtils.copyProperties(obj,bannerVO);
            return bannerVO;
        }).collect(Collectors.toList());
        return bannerVOList;
    }
}
