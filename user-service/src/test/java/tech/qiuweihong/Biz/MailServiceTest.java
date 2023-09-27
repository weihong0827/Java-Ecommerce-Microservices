package tech.qiuweihong.Biz;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tech.qiuweihong.UserApplication;
import tech.qiuweihong.component.impl.MailServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserApplication.class)
@Slf4j
public class MailServiceTest {
    @Autowired
    private MailServiceImpl mailService;

    @Test
    public void sendMailTest() {
        mailService.sendMail("1176101021qiu@gmail.com", "test", "test");
    }

}
