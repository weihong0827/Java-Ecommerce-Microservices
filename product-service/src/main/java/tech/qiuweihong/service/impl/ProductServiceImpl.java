package tech.qiuweihong.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tech.qiuweihong.model.ProductDO;
import tech.qiuweihong.mapper.ProductMapper;
import tech.qiuweihong.service.ProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import tech.qiuweihong.vo.ProductVO;

import java.util.HashMap;
import java.util.Map;
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

    private ProductVO beanProcess(ProductDO productDO){
        ProductVO productVO = new ProductVO();
        BeanUtils.copyProperties(productDO,productVO);
        productVO.setStock((productDO.getStock()-productDO.getLockStock()));

        return productVO;

    }
}
