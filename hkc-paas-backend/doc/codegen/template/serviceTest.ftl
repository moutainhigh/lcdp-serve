<#import "function.ftl" as func>
<#assign package=model.variables.package>
<#assign class=model.variables.class>
<#assign classVar=model.variables.classVar>
<#assign comment=model.tabComment>
<#assign system=vars.system>
<#assign domain=vars.domain>
<#assign sub=model.sub>
<#assign foreignKey=func.convertUnderLine(model.foreignKey)>
<#assign pkType=func.getPkType(model)>
<#assign fkType=func.getFkType(model)>

package ${domain}.${system}.${package};

import com.baomidou.mybatisplus.core.metadata.IPage;
import ${domain}.common.base.search.QueryFilter;
import ${domain}.common.ribbon.annotation.EnableFeignInterceptor;
import ${domain}.${system}.${package}.entity.${class};
import ${domain}.${system}.${package}.service.${class}ServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * ${class}服务测试用例
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@EnableFeignInterceptor
@EnableFeignClients
public class ${class}ServiceTest {

    @Resource
    ${class}ServiceImpl ${classVar}Service;

    /**
     * 测试获取记录
     */
    @Test
    public void testGet(){
        String id="1001";
        ${class} ${classVar}=${classVar}Service.get(id);
        Assert.assertNotNull(${classVar});
    }

    /**
     * 测试获取所有记录
     */
    @Test
    public void testGetAll(){
        List<${class}> list=${classVar}Service.getAll();
        Assert.assertTrue(list.size()>0);
    }

    /**
     * 测试按条件进行过滤并分页返回
     */
    @Test
    public void testQueryAll() {
        QueryFilter filter=new QueryFilter();
        filter.addQueryParam("Q_name__S_LK","表单");
        IPage<${class}> page=${classVar}Service.query(filter);
        Assert.assertTrue(page.getRecords().size()>0);
    }

    /**
     * 查询添厍
     */
    @Test
    public void testAddAndUpdate(){
<#--        String randId=String.valueOf(new Double(10000*Math.random()).intValue());-->
<#--        ${class} ${classVar}=new ${class}();-->
<#--        ${classVar}.setCatId(IdGenerator.getIdStr());-->
<#--        ${classVar}.setName("name_" + randId);-->
<#--        ${classVar}.setKey("key_" + randId);-->
<#--        ${classVar}.setDescp("descp_" + randId);-->
<#--        ${classVar}.setSn(1);-->
<#--        ${classVar}Service.insert(${classVar});-->
<#--        String randId2=String.valueOf(new Double(10000*Math.random()).intValue());-->
<#--        ${classVar}.setName("name_" + randId2 );-->
<#--        ${classVar}Service.update(${classVar});-->
    }

    /**
     * 测试删除记录
     */
    @Test
    public void testDel(){
        String id="1200733561548824578";
        ${classVar}Service.delete(id);
    }

}
