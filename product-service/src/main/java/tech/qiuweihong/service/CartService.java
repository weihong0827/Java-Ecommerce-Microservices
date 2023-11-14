package tech.qiuweihong.service;

import tech.qiuweihong.request.CartItemRequest;

public interface CartService {
    void addToCart(CartItemRequest cartItemRequest);

    void clear();
}
