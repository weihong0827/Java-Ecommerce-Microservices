package tech.qiuweihong.component.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import tech.qiuweihong.component.MailService;

@Service
@Slf4j
public class MailServiceImpl implements MailService{
    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String from;
    @Override
    public void sendMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to); // 接收地址
        message.setSubject(subject); // 标题
        message.setText(content); // 内容
        mailSender.send(message);

        System.out.println("发送邮件");
    }
}
