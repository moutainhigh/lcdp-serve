package com.redxun.common.mail;

import lombok.Data;

import java.io.File;
import java.util.List;

/**
 * 发送邮件对象。
 */
@Data
public class Mail {
    /**
     * 邮件发送人
     */
    private  String from="";
    /**
     * 邮件接收人
     */
    private String[] receiver;
    /**
     * 邮件标题
     */
    private String subject;
    /**
     * 邮件内容
     */
    private String content;
    /**
     * 邮件附件
     */
    private List<File> files;

}
