package com.redxun.common.base.entity;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 模块：jpaas
 * 包名：com.redxun.common.base.entity
 * 功能描述：返回的数据分页记录值合
 *
 * @author：think
 * @date:2019/8/3
 */
@Data
public class JsonPage implements Serializable {
    /**
     * 分页大小
     */
    private Long pageSize;
    /**
     * 分页编码
     */
    private Long pageNo;
    /**
     * 分页总记录数
     */
    private Long totalCount;
    /**
     * 总页数
     */
    private Long totalPage;
    /**
     * 返回的记录集
     */
    private List data;

    JsonPage (){

    }

    public JsonPage(IPage ipage) {
        this.pageSize = ipage.getSize();
        this.pageNo = ipage.getCurrent();
        this.totalCount = ipage.getTotal();
        this.totalPage = ipage.getPages();
        this.data = ipage.getRecords();
    }

    public JsonPage(Long totalPage,Long pageSize,Long totalCount){
        super();
        this.pageSize=pageSize;
        this.totalCount=totalCount;
        this.totalPage=computePageNo(totalPage);
    }

    public JsonPage(Long pageSize, Long pageNo, Long totalCount, Long totalPage, List data) {
        this.pageSize = pageSize;
        this.pageNo = pageNo;
        this.totalCount = totalCount;
        this.totalPage = totalPage;
        this.data = data;
    }

    /**
     * 根据总记录数计算分页页数
     * @param totalPage
     * @return
     */
    protected Long computePageNo(Long totalPage){
        return computePageNumber(totalPage,pageSize,totalCount);
    }

    /**
     * 计算有效页码
     * @param totalPage 总页数
     * @param pageSize 页大小
     * @param totalCount 总记录数
     * @return
     */
    private static Long computePageNumber(Long totalPage,Long pageSize,Long totalCount){
        if(totalPage <= 1){
            return Long.valueOf(1);
        }
        if(Long.MAX_VALUE == totalPage || totalPage > computeLastPageNumber(totalCount,pageSize)){
            return computeLastPageNumber(totalCount,pageSize);
        }
        return totalPage;
    }

    /**
     * 计算最后的页码值
     * @param totalCount
     * @param pageSize
     * @return
     */
    private static Long computeLastPageNumber(Long totalCount,Long pageSize){
        if(pageSize <= 0){
            return Long.valueOf(1);
        }
        Long result = (totalCount % pageSize ==0 ? totalCount / pageSize : totalCount /pageSize + 1);
        if(result <= 1){
            result = Long.valueOf(1);
        }
        return result;
    }
}
