
package com.redxun.system.core.entity;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * @author LWong
 * @date 2020/03/06
 */
public class ExcelListener extends AnalysisEventListener {

    private final static Logger LOGGER = LoggerFactory.getLogger(ExcelListener.class);

    private static final int BATCH_COUNT = 5;

    private List<Object> datas = Lists.newArrayList();

    @Override
    public void invoke(Object o, AnalysisContext analysisContext) {
        LOGGER.info("解析到一条数据:{}", JSON.toJSONString(o));
        //数据存储到list，
        datas.add(o);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (datas.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            datas.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        saveData();
        LOGGER.info("所有数据解析完成！");
    }

    /**
     * 入库
     */
    private void saveData() {
        LOGGER.info("{}条数据，开始存储数据库！", datas.size());
        //这个方法自己实现  能完成保存数据入库即可
        LOGGER.info("存储数据库成功！");
    }

    public List<Object> getDatas() {
        return datas;
    }

    public void setDatas(List<Object> datas) {
        this.datas = datas;
    }
}