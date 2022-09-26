
/**
 * <pre>
 *
 * 描述：接口项目表实体类定义
 * 表:SYS_INTERFACE_PROJECT
 * 作者：ray
 * 邮箱: ray@redxun.cn
 * 日期:2021-05-18 15:34:23
 * 版权：广州红迅软件
 * </pre>
 */
package com.redxun.system.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.redxun.common.base.entity.BaseExtEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang.StringUtils;


@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "SYS_INTERFACE_PROJECT")
public class SysInterfaceProject  extends BaseExtEntity<String> {

    @JsonCreator
    public SysInterfaceProject() {
    }

    //项目ID
    @TableId(value = "PROJECT_ID_",type = IdType.INPUT)
	private String projectId;

    //项目别名
    @TableField(value = "PROJECT_ALIAS_")
    private String projectAlias;
    //项目名称
    @TableField(value = "PROJECT_NAME_")
    private String projectName;
    //分类ID
    @TableField(value = "TREE_ID_")
    private String treeId;
    //描述
    @TableField(value = "DESCRIPTION_")
    private String description;
    //接口通讯协议
    @TableField(value = "DOMAIN_TCP_")
    private String domainTcp;
    //接口域名路径
    @TableField(value = "DOMAIN_PATH_")
    private String domainPath;
    //接口基本路径
    @TableField(value = "BASE_PATH_")
    private String basePath;
    //项目状态
    @TableField(value = "STATUS_")
    private String status;
    //全局请求头
    @TableField(value = "GLOBAL_HEADERS_")
    private String globalHeaders;



    @Override
    public String getPkId() {
        return projectId;
    }

    @Override
    public void setPkId(String pkId) {
        this.projectId=pkId;
    }

    /**
     * 完整域名路径 http://localhost/api
     * @return
     */
    public String getDomainFullPath(){
        return this.domainTcp+this.domainPath+ (StringUtils.isEmpty(this.basePath)?"":this.basePath);
    }

}



