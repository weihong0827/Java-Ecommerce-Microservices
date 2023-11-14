package tech.qiuweihong.service.impl;

import com.alibaba.fastjson.JSON;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tech.qiuweihong.Exception.BizException;
import tech.qiuweihong.constants.CacheKey;
import tech.qiuweihong.enums.BizCodeEnum;
import tech.qiuweihong.interceptor.LoginInterceptor;
import tech.qiuweihong.model.LoginUser;
import tech.qiuweihong.request.CartItemRequest;
import tech.qiuweihong.service.CartService;
import tech.qiuweihong.service.ProductService;
import tech.qiuweihong.vo.CartItemVO;
import tech.qiuweihong.vo.ProductVO;

@Service
@Slf4j
public class CartServiceImpl implements CartService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ProductService productService;

    @Override
    public void addToCart(CartItemRequest cartItemRequest) {
        long productId = cartItemRequest.getProductId();
        int buyNum = cartItemRequest.getBuyNum();

        BoundHashOperations<String,Object,Object> cart = getMyCartOps();

        Object cacheObj = cart.get(productId);
        String result = "";
        if (cacheObj!=null){
            result = (String) cacheObj;
        }
        if (StringUtil.isBlank(result)){
            CartItemVO cartItemVO = new CartItemVO();
            ProductVO productVO = productService.getProduct(productId);
            if (productVO==null){
                throw new BizException(BizCodeEnum.CART_FAIL);
            }
            cartItemVO.setAmount(productVO.getAmount());
            cartItemVO.setBuyNum(buyNum);
            cartItemVO.setProductId(productId);
            cartItemVO.setProductImg(productVO.getCoverImg());
            cartItemVO.setProductTitle(productVO.getTitle());
            cart.put(productId, JSON.toJSONString(cartItemVO));
        }else {
            CartItemVO cartItem = JSON.parseObject(result,CartItemVO.class);
            cartItem.setBuyNum(cartItem.getBuyNum()+buyNum);
            cart.put(productId,JSON.toJSONString(cartItem));
        }
    }

    @Override
    public void clear() {
        String cartKey = getCartKey();
        redisTemplate.delete(cartKey);

    }

    private BoundHashOperations<String,Object,Object> getMyCartOps(){
        String cartKey = getCartKey();
        return redisTemplate.boundHashOps(cartKey);

    }

    private String getCartKey(){
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        return String.format(CacheKey.CartCodeKey,loginUser.getId());
    }
}
