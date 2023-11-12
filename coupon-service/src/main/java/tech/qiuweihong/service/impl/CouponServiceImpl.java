package tech.qiuweihong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import tech.qiuweihong.Exception.BizException;
import tech.qiuweihong.enums.BizCodeEnum;
import tech.qiuweihong.enums.CouponCategoryEnum;
import tech.qiuweihong.enums.CouponPublishEnum;
import tech.qiuweihong.enums.CouponUseStateEnum;
import tech.qiuweihong.interceptor.LoginInterceptor;
import tech.qiuweihong.mapper.CouponMapper;
import tech.qiuweihong.mapper.CouponRecordMapper;
import tech.qiuweihong.model.CouponDO;
import tech.qiuweihong.model.CouponRecordDO;
import tech.qiuweihong.model.LoginUser;
import tech.qiuweihong.service.CouponService;
import tech.qiuweihong.utils.CommonUtils;
import tech.qiuweihong.utils.JsonData;
import tech.qiuweihong.vo.CouponVO;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Qiu Weihong
 * @since 2023-11-11
 */
@Service
@Slf4j
public class CouponServiceImpl implements CouponService {

    @Autowired
    private CouponMapper couponMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private CouponRecordMapper couponRecordMapper;

    @Override
    public Map<String, Object> pageCouponActivity(int page, int size) {
        Page<CouponDO> pageInfo = new Page<>(page, size);
        IPage<CouponDO> coupondoIPage = couponMapper.selectPage(pageInfo,
                new QueryWrapper<CouponDO>()
                        .eq("publish", CouponPublishEnum.PUBLISH)
                        .eq("category", CouponCategoryEnum.PROMOTION)
                        .orderByDesc("create_time")

        );
        Map<String, Object> pageMap = new HashMap<>(3);
        pageMap.put("total_record", coupondoIPage.getTotal());
        pageMap.put("total_page", coupondoIPage.getPages());
        pageMap.put("current_data", coupondoIPage.getRecords()
                .stream()
                .map(this::beanProcess)
                .collect(Collectors.toList()));
        return pageMap;
    }

    /**
     * Claim token end point
     * 1. Check coupon exists
     * 2. Check validity of coupon(claimable, stock, time, exceed limit)
     * 3. deduct stock
     * 4. save record
     *
     * @param couponId
     * @param category
     * @return
     */
    @Override
    public JsonData claimCoupon(long couponId, CouponCategoryEnum category) {
        String uuid = CommonUtils.generateUUID();
        String lockKey = "lock:coupon:"+couponId;
        RLock rlock = redissonClient.getLock(lockKey);
        try {
            LoginUser loginUser = LoginInterceptor.threadLocal.get();
            CouponDO couponDO = couponMapper.selectOne(new QueryWrapper<CouponDO>()
                    .eq("id", couponId)
                    .eq("category", category.name())
            );

            checkCoupon(couponDO, loginUser.getId());

            CouponRecordDO couponRecordDO = new CouponRecordDO();
            BeanUtils.copyProperties(couponDO, couponRecordDO);
            couponRecordDO.setCreateTime(new Date());
            couponRecordDO.setUseState(CouponUseStateEnum.NEW.name());
            couponRecordDO.setUserId(Long.valueOf(loginUser.getId()));
            couponRecordDO.setUserName(loginUser.getName());
            couponRecordDO.setId(null);
            couponRecordDO.setCouponId(couponId);

            int rows = couponMapper.reduceStock(couponId);
            if (rows == 1) {
                couponRecordMapper.insert(couponRecordDO);
            } else {
                log.warn("Claim token failed:{}, user:{}", couponDO, loginUser);
                throw new BizException(BizCodeEnum.COUPON_GET_FAIL);
            }
        }finally {
            rlock.unlock();
        }
        return JsonData.buildSuccess();
    }

    private CouponVO beanProcess(CouponDO obj) {
        CouponVO couponVO = new CouponVO();
        BeanUtils.copyProperties(obj, couponVO);
        return couponVO;
    }

    private void checkCoupon(CouponDO couponDO, Integer id) {
        if (couponDO.getStock() <= 0) {
            throw new BizException(BizCodeEnum.COUPON_NO_STOCK);
        }
        if (couponDO == null) {
            throw new BizException(BizCodeEnum.COUPON_NO_EXITS);
        }
        if (!couponDO.getPublish().equals(CouponPublishEnum.PUBLISH.name())) {
            throw new BizException(BizCodeEnum.COUPON_GET_FAIL);
        }
        long time = System.currentTimeMillis();
        long start = couponDO.getStartTime().getTime();
        long end = couponDO.getEndTime().getTime();
        if (time < start || time > end) {
            throw new BizException(BizCodeEnum.COUPON_OUT_OF_TIME);
        }
        int recordNum = couponRecordMapper.selectCount(new QueryWrapper<CouponRecordDO>()
                .eq("coupon_id", couponDO.getId())
                .eq("user_id", id));

        if (recordNum >= couponDO.getUserLimit()) {
            throw new BizException(BizCodeEnum.COUPON_OUT_OF_LIMIT);
        }

    }
}
