package com.redxun.system.startup;

import com.alibaba.fastjson.JSONObject;
import com.redxun.system.util.OpenOfficeUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 程序启动时，自动启动OPENOFFICE服务。
 */
@Component
public class OpenOfficeRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws IOException {
        JSONObject config = OpenOfficeUtil.getOpenOfficeConfig();
        OpenOfficeUtil.startService(config);
    }
}
