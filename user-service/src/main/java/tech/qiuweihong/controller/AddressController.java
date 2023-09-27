package tech.qiuweihong.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import tech.qiuweihong.Exception.BizException;
import tech.qiuweihong.enums.BizCodeEnum;
import tech.qiuweihong.model.AddressDO;
import tech.qiuweihong.service.AddressService;
import tech.qiuweihong.utils.JsonData;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Qiu Weihong
 * @since 2023-07-27
 */
@RestController
@Api(tags = "AddressController")
@RequestMapping("/api/address/v1")
public class AddressController {

    @Autowired
    private AddressService addressService;
    @GetMapping("/find/{address_id}")
    @ApiOperation(value = "Get address details")
    public Object details(@PathVariable("address_id") Long id){
        AddressDO addressDo = addressService.detail(id);
        return JsonData.buildSuccess(addressDo);

    }

}

