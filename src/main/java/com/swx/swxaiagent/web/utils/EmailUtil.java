package com.swx.swxaiagent.web.utils;

import cn.hutool.core.util.RandomUtil;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: swx-ai-agent
 * @ClassName: EmailUtil
 * @description:
 * @author:
 * @create: 2025/6/18 22:22
 */
@Component
public class EmailUtil {
    @Resource
    private JavaMailSenderImpl mailSender;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public Boolean sendEmail(String email) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        //生成随机验证码
        String code = RandomUtil.randomNumbers(4);
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        //内容
        helper.setText("<p style='color: blue'>欢迎使用MCompanion。你的验证码为：" + code + "(有效期为一分钟)</p>", true);
        //主题
        helper.setSubject("MCompanion验证码");
        //收件人
        helper.setTo(email);
        //发送人
        helper.setFrom("mcompanion@163.com");
        //存入redis
        redisTemplate.opsForValue().set(email, code, Duration.ofMinutes(1));
        //TODO 改为异步
        mailSender.send(mimeMessage);
        return true;

    }

    // 通用邮箱正则（兼容用户名、域名、多级域名）
    private static final String EMAIL_REGEX =
            "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";

    public  boolean isValid(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
