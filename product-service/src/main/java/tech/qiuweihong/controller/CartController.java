package tech.qiuweihong.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;
import tech.qiuweihong.request.CartItemRequest;
import tech.qiuweihong.service.CartService;
import tech.qiuweihong.utils.JsonData;
import tech.qiuweihong.vo.CartVO;

import java.util.List;

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

    @ApiOperation("View cart")
    @GetMapping("/")
    public JsonData viewCart(){
        CartVO cartVO = cartService.getCart();
        return JsonData.buildSuccess(cartVO);
    }

    @ApiOperation("delete cart item")
    @DeleteMapping("/{item_id}")
    public JsonData deleteCartItem(
            @ApiParam(value = "Item id to be deleted",required = true)
            @PathVariable("item_id") long item_id
    ){
        cartService.deleteItem(item_id);
        return JsonData.buildSuccess();
    }

    @ApiOperation("Change Item Num")
    @PostMapping("/change")
    public JsonData changeItemNum(@ApiParam("cart item") @RequestBody CartItemRequest cartItemRequest){
        cartService.changeItemNum(cartItemRequest);
        return JsonData.buildSuccess();

    }

}
