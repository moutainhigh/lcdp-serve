package com.redxun.form.core.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.redxun.common.base.entity.BaseExtEntity;
import com.redxun.common.tool.StringUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.JdbcType;

import java.lang.reflect.Field;

/**
 * 下面定义的数据列表EXT_JSON_字段中的扩展属性
 * 写法：
 *  @TableField(exist = false)
 *  private 字段类型 字段名;
 * @param <T>
 */
@Setter
@Getter
public abstract class FormBoListExt<T> extends BaseExtEntity<T> {
    /**
     * 主键字段
     */
    @TableField(exist = false)
    private String idField;
    /**
     * 显示字段(树)
     */
    @TableField(exist = false)
    private String textField;
    /**
     * 父ID(树)
     */
    @TableField(exist = false)
    private String parentField;
    /**
     * 仅可选择树节点
     */
    @TableField(exist = false)
    private String onlySelLeaf;
    /**
     * 是否多选择
     */
    @TableField(exist = false)
    private String multiSelect;
    /**
     * 是否显示左树
     */
    @TableField(exist = false)
    private String isLeftTree="NO";
    /**
     * 左树字段映射
     */
    @TableField(exist = false)
    private String leftTreeJson;
    /**
     * SQL语句
     */
    @TableField(exist = false)
    private String sql;
    /**
     * USE_COND_SQL_
     */
    @TableField(exist = false)
    private String useCondSql;
    /**
     * COND_SQLS_
     */
    @TableField(exist = false)
    private String condSqls;
    /**
     * 列字段JSON
     */
    @TableField(exist = false)
    private String fieldsJson;
    /**
     * 列的JSON
     */
    @TableField(exist = false)
    private String colsJson;
    /**
     * 搜索条件HTML
     */
    @TableField(exist = false)
    private String searchJson;
    /**
     * 绑定表单方案
     */
    @TableField(exist = false)
    private String formAlias;
    /**
     * 头部按钮配置
     */
    @TableField(exist = false)
    private String topBtnsJson;
    /**
     * 脚本JS
     */
    @TableField(exist = false)
    private String bodyScript;
    /**
     * 是否分页
     */
    @TableField(exist = false)
    private String isPage="NO";
    /**
     * 字段显隐
     */
    @TableField(exist = false)
    private String isFieldShow="YES";
    /**
     * 分页大小
     */
    @TableField(exist = false)
    private Integer pageSize=10;
    /**
     * 是否懒加载
     */
    @TableField(exist = false)
    private String isLazy;
    /**
     * 高
     */
    @TableField(exist = false)
    private Integer height;
    /**
     * 宽
     */
    @TableField(exist = false)
    private Integer width;
    /**
     * 是否全屏
     */
    @TableField(exist = false)
    private String isMax;
    /**
     * 是否显示遮罩
     */
    @TableField(exist = false)
    private String isShade;
    /**
     * 数据风格
     */
    @TableField(exist = false)
    private String dataStyle;
    /**
     * 行数据编辑
     */
    @TableField(exist = false)
    private String rowEdit;
    /**
     * 是否显示汇总行
     */
    @TableField(exist = false)
    private String showSummaryRow;

    /**
     * 是否显示总的统计函数。
     */
    @TableField(exist = false)
    private String showSummary;
    /**
     * 是否初始化数据
     */
    @TableField(exist = false)
    private String isInitData;
    /**
     * 明细表单
     */
    @TableField(exist = false)
    private String formDetailAlias;
    /**
     * 模版类型
     */
    @TableField(exist = false)
    private String templateType;
    /**
     * 表单方案(添加)
     */
    @TableField(exist = false)
    private String formAddAlias;
    /**
     * WEBREQ_KEY_
     */
    @TableField(exist = false)
    private String webreqKey;
    /**
     * WEBREQ_SCRIPT_
     */
    @TableField(exist = false)
    private String webreqScript;
    /**
     * WEBREQ_MAPPING_JSON_
     */
    @TableField(exist = false)
    private String webreqMappingJson;
    /**
     * INTERFACE_KEY_
     */
    @TableField(exist = false)
    private String interfaceKey;
    /**
     * INTERFACE_NAME_
     */
    @TableField(exist = false)
    private String interfaceName;
    /**
     * INTERFACE_MAPPING_JSON_
     */
    @TableField(exist = false)
    private String interfaceMappingJson;
    /**
     * FORM_NAME_
     */
    @TableField(exist = false)
    private String formName;
    /**
     * FORM_DETAIL_NAME_
     */
    @TableField(exist = false)
    private String formDetailName;
    /**
     * FORM_ADD_NAME_
     */
    @TableField(exist = false)
    private String formAddName;
    /**
     * 发布字段
     */
    @TableField(exist = false)
    private String publishConf;
    /**
     * 手机端配置
     * {
     *     mobileStyle:手机端列表模板风格
     *     mobileCols:手机端列表列头
     *     mobileTreeTab:手机端导航树
     *     mobileSearch:手机端搜索条件
     *     mobileIsSearchView:是否展示手机搜索条件
     *     mobileIsShowView:是否展示手机查询视图
     *     mobileIsViewHeader:手机端查询视图风格
     *     mobileView:手机端查询视图
     *     mobileButton:手机端按钮
     *     mobileJs:手机端js
     * }
     */
    @TableField(exist = false)
    private String mobileConf;
    /**
     * 选择框配置
     */
    @TableField(exist = false)
    private String isCheckboxProps;
    /**
     * 树形默认展开等级
     */
    @TableField(exist = false)
    private String expandLevel;

    /**
     * 头部显示搜索
     */
    @TableField(exist = false)
    private String isSearchHeader;
    /**
     * 搜索条件布局（YES:垂直 NO:水平）
     */
    @TableField(exist = false)
    private String isSearchLayout;
    /**
     * 是否开启搜索条件
     */
    @TableField(exist = false)
    private String isShowSearch;
    /**
     * 调试模式
     */
    @TableField(exist = false)
    private String isTest;
    /**
     * 最大显示按钮数
     */
    @TableField(exist = false)
    private Integer buttonMax;

    /**
     * 配置展示行
     */
    @TableField(exist = false)
    private String isExpandRow;
    /**
     * 展示行JSON
     */
    @TableField(exist = false)
    private String expandRowJson;
    /**
     * excel配置JSON
     */
    @TableField(exist = false)
    private String excelConfJson;
    /**
     * 机构列头配置
     */
    @TableField(exist = false)
    private String instColumnConfig;
    /**
     * 业务方案
     */
    @TableField(exist = false)
    private String busSolution;
    /**
     * 搜索最大显示数
     */
    @TableField(exist = false)
    private Integer searchMax;
    /**
     * 是否为机构使用
     */
    @TableField(exist = false)
    private String isTenant;
    /**
     * 文本位置
     */
    @TableField(exist = false)
    private String fontPosition;
    /**
     * 行高
     */
    @TableField(exist = false)
    private Integer lineHeight;
    /**
     * 表格风格
     */
    @TableField(exist = false)
    private Integer tableStyle;
    /**
     * 查询视图风格
     */
    @TableField(exist = false)
    private String isSearchView;
    /**
     * 是否开启视图
     */
    @TableField(exist = false)
    private String isShowView;
    /**
     * 查询视图
     */
    @TableField(exist = false)
    private String editSearchViewJson;
    /**
     * 导出限制数量
     */
    @TableField(exist = false)
    private Integer restriction;



    /**
     * 扩展属性
     */
    @TableField(value = "EXT_JSON_",jdbcType = JdbcType.VARCHAR)
    private String extJson;


    public String getExtJson() {
        Class clazz = getClass().getSuperclass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            try {
                if (field.isAnnotationPresent(TableField.class) &&
                        !field.getAnnotation(TableField.class).exist()) {
                    field.setAccessible(true);
                    Object value = field.get(this);
                    if (value == null) {
                        continue;
                    }
                    setExtJsonValueByKey(field.getName(), value);
                }
            } catch (Exception e) {
            }
        }
        return this.extJson;
    }

    public void setExtJson(String extJson){
        this.extJson=extJson;
        if(StringUtils.isNotEmpty(this.extJson)) {
            JSONObject extJsonObj=JSONObject.parseObject(this.extJson);
            Class clazz = getClass().getSuperclass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                try {
                    if (field.isAnnotationPresent(TableField.class) &&
                            !field.getAnnotation(TableField.class).exist()) {
                        field.setAccessible(true);
                        if(extJsonObj.containsKey(field.getName())){
                            field.set(this, extJsonObj.get(field.getName()));
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    public void setExtJsonValueByKey(String key,Object value) {
        JSONObject extJsonObj=new JSONObject();
        if(StringUtils.isNotEmpty(extJson)){
            extJsonObj=JSONObject.parseObject(extJson);
        }
        extJsonObj.put(key,value);
        this.extJson=extJsonObj.toJSONString();
    }

}
