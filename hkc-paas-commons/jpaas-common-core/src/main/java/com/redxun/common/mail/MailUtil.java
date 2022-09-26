package com.redxun.common.mail;

import com.redxun.common.tool.BeanUtil;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;


/**
 * 发送邮件工具。
 */
@Component
public class MailUtil {

    @Resource
    JavaMailSender javaMailSender;

    /**
     * 发送邮件。
     * @param from      发送人
     * @param receiver  接收人
     * @param subject   主题
     * @param content   内容
     * @param files     附件
     * @throws MessagingException
     */
    public void sendMail(String from,
                         String[] receiver,
                         String subject,
                         String content,
                         List<File> files) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setSubject(subject);
        helper.setFrom(from);
        helper.setText(content,true);
        helper.setTo(receiver);

        if(BeanUtil.isNotEmpty(files)){
            for(File file:files){
                helper.addAttachment(file.getName(),file);
            }
        }
        javaMailSender.send(mimeMessage);
    }

    /**
     * 发送邮件。
     * @param from          发送人
     * @param receivers     接收人
     * @param subject       主题
     * @param content       内容
     * @throws MessagingException
     */
    public void sendMail(String from, String[] receivers, String subject, String content) throws MessagingException {
       sendMail( from,  receivers,  subject,  content, null);
    }

    /**
     * 发送邮件。
     * @param from          发件人
     * @param receiver      收件人
     * @param subject       发送主题
     * @param content       邮件内容。
     * @throws MessagingException
     */
    public void sendMail(String from, String receiver, String subject, String content) throws MessagingException {
        String[] reveivers=new String[]{receiver};
        sendMail( from,  reveivers,  subject,  content, null);
    }
}
