package tech.qiuweihong.Biz;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tech.qiuweihong.UserApplication;
import tech.qiuweihong.model.AddressDO;
import tech.qiuweihong.service.AddressService;
import tech.qiuweihong.vo.AddressVO;

@SpringBootTest(classes = {UserApplication.class})
@RunWith(SpringRunner.class)
@Slf4j
public class AddressTest {
    @Autowired
    private AddressService addressService;

    @Test
    public void testAddressDetail(){
        AddressVO addressVO = addressService.detail(1L);
        log.info("addressDO:{}",addressVO);
        Assert.assertNotNull(addressVO);
    }
}
