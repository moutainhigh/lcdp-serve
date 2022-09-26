package com.redxun.user.mq;


import com.redxun.common.mail.Mail;
import com.redxun.common.mail.MailUtil;
import com.redxun.common.utils.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 邮件消息处理类
 */
@Service
@Slf4j
public class MailConsumer {

    @Resource
    private MailUtil mailUtil;

    /**
     * 处理mail文件转换。
     */
    @StreamListener(MailInputOutput.MAILINPUT)
    public void handMail(Mail mail) {
        try {
            mailUtil.sendMail(mail.getFrom(),mail.getReceiver(),mail.getSubject(),mail.getContent());
        }
        catch (Exception ex){
            log.error(ExceptionUtil.getExceptionMessage(ex));
        }
    }
}
