package tech.qiuweihong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.spring.web.json.Json;
import tech.qiuweihong.Exception.BizException;
import tech.qiuweihong.config.RabbitMQConfig;
import tech.qiuweihong.enums.BizCodeEnum;
import tech.qiuweihong.enums.CouponUseStateEnum;
import tech.qiuweihong.enums.OrderStatus;
import tech.qiuweihong.enums.StockTaskStateEnum;
import tech.qiuweihong.feign.ProductOrderFeign;
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
    private CouponTaskMapper couponTaskMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RabbitMQConfig rabbitMQConfig;

    @Autowired
    private ProductOrderFeign productOrderFeign;

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
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
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
        log.info("Lock coupon record :{}",couponTaskDOS);

        int insertRows = couponTaskMapper.insertBatch(couponTaskDOS);
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

    /**
     * Check task existance
     * query order status
     * @param recordMessage
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public boolean releaseCouponRecord(CouponRecordMessage recordMessage) {
        CouponTaskDO couponTaskDO = couponTaskMapper.selectOne(new QueryWrapper<CouponTaskDO>().eq("id",recordMessage.getTaskId()));
        if (couponTaskDO==null){
            log.warn("task null, message:{}",recordMessage);

            return true; //processed message
        }
        if (couponTaskDO.getLockState().equals(StockTaskStateEnum.LOCK.name())){
            JsonData jsonData = productOrderFeign.queryProductOrderStatus(recordMessage.getOutTradeNo());
            if (jsonData.getCode()==0){
                String state = jsonData.getData().toString();
                if (OrderStatus.NEW.name().equalsIgnoreCase(state)){
                    log.warn("Order status NEW return to MQ:{}",recordMessage);
                    return false;
                }
                if(OrderStatus.PAID.name().equalsIgnoreCase(state)){
                    couponTaskDO.setLockState(StockTaskStateEnum.FINISH.name());
                    couponTaskMapper.update(couponTaskDO,new QueryWrapper<CouponTaskDO>().eq("id",recordMessage.getTaskId()));
                    log.info("Order Paid, changing tasks to finish state:{}",recordMessage);
                    return true;
                }

            }
            // order does not exist or order cancelled, restore coupon use state to NEW

            log.warn("order does not exist or order cancelled, restore coupon use state to NEW,message:{}",recordMessage);
            couponTaskDO.setLockState(StockTaskStateEnum.CANCEL.name());
            couponTaskMapper.update(couponTaskDO,new QueryWrapper<CouponTaskDO>().eq("id",recordMessage.getTaskId()));
            couponRecordMapper.updateState(couponTaskDO.getCouponRecordId(),CouponUseStateEnum.NEW.name());
            return true;


        }else{
            log.warn("task state is not LOCK, state={}, message={}",couponTaskDO.getLockState(),recordMessage);
            return true;
        }

    }

    private CouponRecordVO beanProcess(CouponRecordDO couponRecordDO){
        CouponRecordVO couponRecordVO = new CouponRecordVO();
        BeanUtils.copyProperties(couponRecordDO,couponRecordVO);
        return couponRecordVO;
    }
}

