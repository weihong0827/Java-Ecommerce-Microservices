package tech.qiuweihong.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.qiuweihong.service.ProductService;
import tech.qiuweihong.utils.JsonData;

import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Qiu Weihong
 * @since 2023-11-13
 */
@RestController
@Api("product module")
@RequestMapping("/api/product/v1")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/")
    @ApiOperation("paginated products")
    public JsonData pageProductList(
            @ApiParam(value="Current page")  @RequestParam(value = "page",defaultValue = "1") int page,
            @ApiParam(value="Page size") @RequestParam(value="size",defaultValue = "10") int size
    ){
        Map<String,Object> pageResult = productService.listProducts(page,size);
        return JsonData.buildSuccess(pageResult);
    }

}

