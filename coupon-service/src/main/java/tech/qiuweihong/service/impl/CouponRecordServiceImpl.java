package tech.qiuweihong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tech.qiuweihong.interceptor.LoginInterceptor;
import tech.qiuweihong.model.CouponDO;
import tech.qiuweihong.model.CouponRecordDO;
import tech.qiuweihong.mapper.CouponRecordMapper;
import tech.qiuweihong.model.LoginUser;
import tech.qiuweihong.service.CouponRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import tech.qiuweihong.vo.CouponRecordVO;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Qiu Weihong
 * @since 2023-11-11
 */
@Service
@Slf4j
public class CouponRecordServiceImpl implements CouponRecordService {
    @Autowired
    private CouponRecordMapper couponRecordMapper;

    @Override
    public Map<String, Object> detail(int page, int size) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        Page<CouponRecordDO> pageinfo = new Page<>(page,size);
        IPage<CouponRecordDO> recordDOIpage = couponRecordMapper.selectPage(pageinfo,new QueryWrapper<CouponRecordDO>()
                .eq("user_id",loginUser.getId())
                .orderByDesc("create_time")
        );
        Map<String,Object> pageMap = new HashMap<>(3);
        pageMap.put("total_record",recordDOIpage.getTotal());
        pageMap.put("total_page",recordDOIpage.getPages());
        pageMap.put("current_data",recordDOIpage.getRecords().stream().map(
                obj->beanProcess(obj)
        ).collect(Collectors.toList()));


        return pageMap;
    }

    @Override
    public CouponRecordVO findById(int recordId) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        CouponRecordDO couponDO = couponRecordMapper.selectOne(new QueryWrapper<CouponRecordDO>()
                .eq("id",recordId)
                .eq("user_id",loginUser.getId())
        );
        if (couponDO==null){return null;}
        return beanProcess(couponDO);
    }

    private CouponRecordVO beanProcess(CouponRecordDO couponRecordDO){
        CouponRecordVO couponRecordVO = new CouponRecordVO();
        BeanUtils.copyProperties(couponRecordDO,couponRecordVO);
        return couponRecordVO;
    }
}

