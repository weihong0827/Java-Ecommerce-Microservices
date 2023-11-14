package tech.qiuweihong.service;

import tech.qiuweihong.request.CartItemRequest;
import tech.qiuweihong.vo.CartVO;

import java.util.List;

public interface CartService {
    void addToCart(CartItemRequest cartItemRequest);

    void clear();

    CartVO getCart();
}
