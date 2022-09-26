package com.redxun.constvar.impl;

import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.constant.CommonConstant;
import com.redxun.common.utils.HttpContextUtil;
import com.redxun.constvar.ConstVarType;
import com.redxun.constvar.IConstVarService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Service
public class AuthContextImpl implements IConstVarService {



    @Override
    public ConstVarType getType() {
        return new ConstVarType("[AUTH]","认证信息");
    }

    @Override
    public Object getValue(Map<String,Object> vars) {
        HttpServletRequest request = HttpContextUtil.getRequest();
        if(BeanUtil.isEmpty(request)){
            return null;
        }
        return request.getHeader(CommonConstant.TOKEN_HEADER);
    }

}
