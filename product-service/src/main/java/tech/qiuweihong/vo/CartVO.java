package tech.qiuweihong.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

public class CartVO {

    public int getTotalItem() {
        if(this.cartItems!=null){
            return cartItems.stream().mapToInt(CartItemVO::getBuyNum).sum();
        }
        return 0;
    }

    public BigDecimal getTotalAmount() {
        BigDecimal amount = new BigDecimal("0");
        if(this.cartItems!=null){
            for(CartItemVO cartItemVO:cartItems){
                BigDecimal itemTotalAmount = cartItemVO.getTotalAmount();
                amount = amount.add(itemTotalAmount);
            }
        }

        return amount;
    }

    public List<CartItemVO> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItemVO> cartItems) {
        this.cartItems = cartItems;
    }

    public BigDecimal getDiscountedAmount() {
        return discountedAmount;
    }

    @JsonProperty("cart_item")
    private List<CartItemVO> cartItems;

    @JsonProperty("total_item")
    private int totalItem;

    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    @JsonProperty("discounted_amount")
    private BigDecimal discountedAmount;
}
