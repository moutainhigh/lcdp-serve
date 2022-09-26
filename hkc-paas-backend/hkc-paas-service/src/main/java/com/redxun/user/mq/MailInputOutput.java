package com.redxun.user.mq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;


/**
 * 邮件消息收发。
 * @author ray
 */
public interface MailInputOutput {

    String MAILOUTPUT = "mailOut";
    String MAILINPUT = "mailInput";


    @Output(MAILOUTPUT)
    MessageChannel output();
    @Input(MAILINPUT)
    SubscribableChannel input();

}
