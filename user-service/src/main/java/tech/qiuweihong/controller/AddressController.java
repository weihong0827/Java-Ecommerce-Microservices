package tech.qiuweihong.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import tech.qiuweihong.Exception.BizException;
import tech.qiuweihong.enums.BizCodeEnum;
import tech.qiuweihong.interceptor.LoginInterceptor;
import tech.qiuweihong.model.AddressDO;
import tech.qiuweihong.model.LoginUser;
import tech.qiuweihong.request.AddressAddRequest;
import tech.qiuweihong.service.AddressService;
import tech.qiuweihong.utils.JsonData;
import tech.qiuweihong.vo.AddressVO;

import java.util.List;

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

    @PostMapping("/")
    @ApiOperation(value = "Add a new address")
    public JsonData addAddress(@ApiParam("Address Object") @RequestBody AddressAddRequest addressAddRequest){
        addressService.add(addressAddRequest);
        return JsonData.buildSuccess();
    }

    @DeleteMapping("/{address_id}")
    public JsonData Delete(@ApiParam(value="address_id",required=true) @PathVariable("address_id") Long id){
        int rows = addressService.delete(id);
        return rows==1?JsonData.buildSuccess():JsonData.buildResult(BizCodeEnum.ADDRESS_DEL_FAIL);
    }
    @GetMapping("/")
    @ApiOperation(value = "Get all address for a user")
    public JsonData list(){
        List<AddressVO> addressVOList = addressService.list();
        return JsonData.buildSuccess(addressVOList);
    }

    @GetMapping("/{address_id}")
    @ApiOperation(value = "Get address details")
    public Object details(@PathVariable("address_id") Long id){
        AddressVO addressVO = addressService.detail(id);

        return addressVO==null?JsonData.buildResult(BizCodeEnum.ADDRESS_NO_EXITS):JsonData.buildSuccess(addressVO);

    }

}

