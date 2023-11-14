package tech.qiuweihong.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.qiuweihong.request.CartItemRequest;
import tech.qiuweihong.service.CartService;
import tech.qiuweihong.utils.JsonData;

@Api("Shopping cart")
@RestController
@RequestMapping("/api/cart/v1")
public class CartController {
    @Autowired
    private CartService cartService;

    @ApiOperation("add to cart")
    @PostMapping("/")
    public JsonData addToCart(@ApiParam("cart item") @RequestBody CartItemRequest cartItemRequest){
        cartService.addToCart(cartItemRequest);
        return JsonData.buildSuccess();

    }

    @ApiOperation("Clear Cart")
    @DeleteMapping("/clear")
    public JsonData clearCart(){
        cartService.clear();
        return JsonData.buildSuccess();
    }
}
