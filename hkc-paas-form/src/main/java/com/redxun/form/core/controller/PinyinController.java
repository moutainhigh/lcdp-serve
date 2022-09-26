package com.redxun.form.core.controller;

import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.PinyinUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/form/share/pinyin")
@Api(tags = "拼音转换")
public class PinyinController {

    @ApiOperation(value="获取拼音", notes = "根据汉字返回拼音的大小写。")
    @GetMapping("getPinyin")
    public String getPinyin(@RequestParam(name = "chinese") String chinese,@RequestParam(name ="lower", defaultValue = "false") boolean lower) throws Exception {
        if(StringUtils.isEmpty(chinese)) {
            return "";
        }
        String rtn="";
        if(lower){
            rtn=  PinyinUtil.getFirstLettersLo(chinese);
        }
        else{
            rtn=  PinyinUtil.getFirstLettersUp(chinese);
        }
        return  rtn;

    }

    public static void main(String[] args) {
        String pinyin=PinyinUtil.getFirstLettersUp("中文");
        System.err.println(pinyin);
    }
}
