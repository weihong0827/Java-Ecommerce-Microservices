package tech.qiuweihong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tech.qiuweihong.Exception.BizException;
import tech.qiuweihong.config.RabbitMQConfig;
import tech.qiuweihong.enums.BizCodeEnum;
import tech.qiuweihong.enums.CouponUseStateEnum;
import tech.qiuweihong.enums.StockTaskStateEnum;
import tech.qiuweihong.interceptor.LoginInterceptor;
import tech.qiuweihong.mapper.CouponTaskMapper;
import tech.qiuweihong.model.*;
import tech.qiuweihong.mapper.CouponRecordMapper;
import tech.qiuweihong.request.LockCouponRecordRequest;
import tech.qiuweihong.service.CouponRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import tech.qiuweihong.utils.JsonData;
import tech.qiuweihong.vo.CouponRecordVO;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    @Autowired
    private CouponTaskMapper couponTaskMapperMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RabbitMQConfig rabbitMQConfig;

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

    @Override
    public JsonData lockCouponRecord(LockCouponRecordRequest recordRequest) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        String orderOutTradeNo = recordRequest.getOrderOutTradeNo();
        List<Long> couponList = recordRequest.getLockCouponRecodIds();
        int updatedRows = couponRecordMapper.lockCouponState(loginUser.getId(), CouponUseStateEnum.USED.name(),couponList);
        List<CouponTaskDO> couponTaskDOS = couponList.stream().map(obj->{
                CouponTaskDO couponTaskDO = new CouponTaskDO();
                couponTaskDO.setCreateTime(new Date());
                couponTaskDO.setOutTradeNo(orderOutTradeNo);
                couponTaskDO.setCouponRecordId(obj);
                couponTaskDO.setLockState(StockTaskStateEnum.LOCK.name());
                return couponTaskDO;
        }).collect(Collectors.toList());
        int insertRows = couponTaskMapperMapper.insertBatch(couponTaskDOS);
        log.info("Lock coupon record rows={}",updatedRows);
        log.info("Insert coupon task record rows={}",insertRows);
        if(couponList.size()==insertRows &&insertRows==updatedRows){
            // send to message queue
            for (CouponTaskDO couponTaskDO:couponTaskDOS){
                CouponRecordMessage couponRecordMessage = new CouponRecordMessage();
                couponRecordMessage.setOutTradeNo(orderOutTradeNo);
                couponRecordMessage.setTaskId(couponTaskDO.getId());
                rabbitTemplate.convertAndSend(rabbitMQConfig.getEventExchange(),rabbitMQConfig.getCouponReleaseDelayRoutingKey(),couponRecordMessage);
                log.info("lock coupon message sent{}",couponRecordMessage.toString());
            }
            return JsonData.buildSuccess();
        }else {
            throw new BizException(BizCodeEnum.COUPON_RECORD_LOCK_FAIL);
        }

    }

    private CouponRecordVO beanProcess(CouponRecordDO couponRecordDO){
        CouponRecordVO couponRecordVO = new CouponRecordVO();
        BeanUtils.copyProperties(couponRecordDO,couponRecordVO);
        return couponRecordVO;
    }
}

