package tech.qiuweihong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tech.qiuweihong.Exception.BizException;
import tech.qiuweihong.enums.BizCodeEnum;
import tech.qiuweihong.enums.StockTaskStateEnum;
import tech.qiuweihong.mapper.ProductTaskMapper;
import tech.qiuweihong.model.ProductDO;
import tech.qiuweihong.mapper.ProductMapper;
import tech.qiuweihong.model.ProductTaskDO;
import tech.qiuweihong.request.LockProductRequest;
import tech.qiuweihong.request.OrderItemRequest;
import tech.qiuweihong.service.ProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import tech.qiuweihong.utils.JsonData;
import tech.qiuweihong.vo.ProductVO;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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
public class ProductServiceImpl  implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductTaskMapper productTaskMapper;
    @Override
    public Map<String, Object> listProducts(int page, int size) {
        Page<ProductDO> pageInfo = new Page<>(page,size);

        IPage<ProductDO> productDOIPage = productMapper.selectPage(pageInfo,null);

        Map<String,Object> pageMap = new HashMap<>(3);
        pageMap.put("total_record",productDOIPage.getTotal());
        pageMap.put("total_page",productDOIPage.getPages());
        pageMap.put("current_data",productDOIPage.getRecords().stream().map(obj->beanProcess(obj)).collect(Collectors.toList()));
        return pageMap;

    }

    @Override
    public ProductVO getProduct(long id) {
        ProductDO productDO = productMapper.selectById(id);
        return beanProcess(productDO);
    }

    @Override
    public List<ProductVO> findProductByIdBatch(List<Long> productIdList) {
        List<ProductDO>productDOList = productMapper.selectList(new QueryWrapper<ProductDO>().in("id",productIdList));
        return productDOList.stream().map(obj->beanProcess(obj)).collect(Collectors.toList());
    }

    @Override
    public JsonData lockProductStock(LockProductRequest request) {
        String outTradeNo = request.getOrderOutTradeNo();

        List<OrderItemRequest> orderItemRequests = request.getOrderItemList();
        List<Long> idList = orderItemRequests.stream().map(OrderItemRequest::getProductId).collect(Collectors.toList());

        List<ProductVO> productVoList = this.findProductByIdBatch(idList);
        Map<Long,ProductVO> productVOMap = productVoList.stream().collect(Collectors.toMap(ProductVO::getId, Function.identity()));

        for (OrderItemRequest item:orderItemRequests){
            int rows = productMapper.lockProductStock(item.getProductId(),item.getCount());
            if (rows!=1){
                throw new BizException(BizCodeEnum.ORDER_CONFIRM_LOCK_PRODUCT_FAIL);
            }else{
                ProductTaskDO taskDO = new ProductTaskDO();
                ProductVO productVO = productVOMap.get(item.getProductId());
                taskDO.setProductId(productVO.getId());
                taskDO.setLockState(StockTaskStateEnum.LOCK.name());
                taskDO.setBuyNum(item.getCount());
                taskDO.setProductName(productVO.getTitle());
                taskDO.setCreateTime(new Date());
                taskDO.setOutTradeNo(outTradeNo);
                productTaskMapper.insert(taskDO);
            }

        }
        return JsonData.buildSuccess();


    }

    private ProductVO beanProcess(ProductDO productDO){
        ProductVO productVO = new ProductVO();
        BeanUtils.copyProperties(productDO,productVO);
        productVO.setStock((productDO.getStock()-productDO.getLockStock()));

        return productVO;

    }
}
