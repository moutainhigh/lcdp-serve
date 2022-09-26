package com.redxun.system.messagehandler;

import com.redxun.common.base.entity.IUser;
import com.redxun.common.engine.FtlEngine;
import com.redxun.common.engine.FtlUtil;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.SpringUtil;
import com.redxun.common.utils.SysPropertiesUtil;
import com.redxun.dto.user.OsUserDto;
import com.redxun.fieldrender.RenderUtil;
import com.redxun.mq.MessageModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateHashModel;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;

/***
 * 消息处理工具类
 */
@Slf4j
public class MessageUtil {

    /**
     * 获取消息标题。
     *
     * @param model
     * @return
     * @throws IOException
     * @throws TemplateException
     */
    public static String getInformSubject(MessageModel model, OsUserDto receiver){
        String subject = model.getSubject();
        if(StringUtils.isNotEmpty(subject)){
            return subject;
        }
        //构建变量。
        IUser sender=model.getSender();
        StringBuilder subjectStr=new StringBuilder();
        subjectStr.append("【"+receiver.getAccount()+"】"+receiver.getFullName());
        subjectStr.append("您收到一封来自【"+sender.getFullName()+"】"+sender.getAccount()+"的信息：");
        return subjectStr.toString();
    }

    /**
     * 获取消息内容。
     * <pre>
     *     1.获取模板消息类型内容，如果有内容则使用消息内容。
     *     2.如果没有则根据消息类型，获取模板，使用模板引擎对数据进行渲染。
     * </pre>
     *
     *
     * @param model
     * @param msgType
     * @return
     * @throws IOException
     * @throws TemplateException
     */
    public static String getContent(MessageModel model, OsUserDto receiver, String msgType) throws IOException, TemplateException {
        String content = model.getContent();
        if(StringUtils.isNotEmpty(content)){
            return content;
        }
        FtlEngine freemarkEngine = SpringUtil.getBean(FtlEngine.class);

        Map<String,Object> vars=model.getVars();
        IUser sender=model.getSender();
        vars.put("senderObj",sender);
        vars.put("sender",sender.getFullName());
        vars.put("senderAccount",sender.getAccount());

        vars.put("subject",model.getSubject());

        vars.put("receiverObj",receiver);
        vars.put("receiverAccount",receiver.getAccount());
        vars.put("receiver",receiver.getFullName());

        String serverUrl= SysPropertiesUtil.getString("serverAddress")+ SysPropertiesUtil.getString("ctxPath");
        vars.put("serverUrl",serverUrl);

        Map<String, String> templateVars = model.getTemplateVars();
        if(BeanUtil.isNotEmpty(templateVars) && templateVars.containsKey(msgType) ){

            TemplateHashModel util = FtlUtil.generateStaticModel(RenderUtil.class);
            vars.put("util", util);
            //表单数据。
            if(BeanUtil.isNotEmpty(model.getBoDataMap())){
                vars.putAll(model.getBoDataMap());
            }
            String template=templateVars.get(msgType);
            //对模板进行整理。
            template=RenderUtil.constructPDFTemp(template);

            return  freemarkEngine.parseByStringTemplate(vars,template);
        }
        return "";

    }



}
