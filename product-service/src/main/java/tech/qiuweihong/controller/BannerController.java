package tech.qiuweihong.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import tech.qiuweihong.service.BannerService;
import tech.qiuweihong.utils.JsonData;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Qiu Weihong
 * @since 2023-11-13
 */
@RestController
@Api("Carousel Module")
@RequestMapping("/api/banner/v1")
public class BannerController {
    @Autowired
    private BannerService bannerService;
    @GetMapping("/")
    @ApiOperation(("Carousel list"))
    public JsonData list(){

        return JsonData.buildSuccess(bannerService.listBanner());

    }

}

