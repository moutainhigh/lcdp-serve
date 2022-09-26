package com.redxun.common.bean;

import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ClassLoaderUtil;
import com.redxun.common.utils.FileUtil;
import com.redxun.common.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class InterfaceBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) beanFactory;
        //构建SpringUtil实例
        defaultListableBeanFactory.getBean(SpringUtil.class);

        try{
            loadClass(beanFactory);
        }catch (Exception e){
            log.error("----InterfaceBeanFactoryPostProcessor.postProcessBeanFactory is error---message="+e.getMessage());
        }
    }

    /**
     * 扩展接口列表
     * @return
     */
    private List<Class> initClass() {
        List<Class> list = new ArrayList<>();
        String classes = SpringUtil.getProperty("plugin.interface");
        if (StringUtils.isEmpty(classes)) {
            return list;
        }
        String[] classList = classes.split(",");
        for (String className : classList) {
            try {
                list.add(Class.forName(className));
            } catch (Exception e) {
                log.error("----InterfaceBeanFactoryPostProcessor.initClass CLASS_NAME:"+className+" is error---message=" + e.getMessage());
            }
        }
        return list;
    }

    /**
     * 加载扩展接口
     * @param beanFactory bean工厂
     * @throws IllegalAccessException
     */
    private void loadClass(ConfigurableListableBeanFactory beanFactory) throws IllegalAccessException {
        //扩展接口jar包储存地址
        File pluginsFile = new File(System.getProperty("user.dir") + "\\plugins");
        if (!pluginsFile.exists()) {
            return;
        }
        File[] files = pluginsFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if(!"jar".equals(FileUtil.getFileExt(file))){
                continue;
            }
            for (Class clazz : initClass()) {
                registerClass(beanFactory, file.getPath(), clazz);
            }
        }
    }

    /**
     * 注册扩展接口
     * @param beanFactory bean工厂
     * @param jarUrl jar包路径
     * @param clazz 接口字节
     * @param <T> 接口类型
     * @throws IllegalAccessException
     */
    private <T> void registerClass(ConfigurableListableBeanFactory beanFactory,String jarUrl,Class<T> clazz) throws IllegalAccessException{
        List<T> list= ClassLoaderUtil.loadClassByInterface("file:/"+jarUrl, clazz);
        for(T obj:list) {
            //获取类所有字段
            Field[] fields = obj.getClass().getDeclaredFields();
            for(Field field:fields){
                //判断是否有Resource和Autowired注解
                if(field.isAnnotationPresent(Resource.class) || field.isAnnotationPresent(Autowired.class)){
                    String beanName=null;
                    Resource resource = field.getAnnotation(Resource.class);
                    if(resource!=null){
                        //取resource注解名称
                        beanName=resource.name();
                    }
                    field.setAccessible(true);
                    //字段类类名
                    String fieldClassName=field.getType().getSimpleName();
                    //默认类名首字母小写
                    Object bean = beanFactory.getBean(StringUtils.makeFirstLetterLowerCase(fieldClassName));
                    if(StringUtils.isNotEmpty(beanName)) {
                        bean = beanFactory.getBean(beanName);
                    }
                    field.set(obj,bean);
                }
            }
            //接口实现类类名
            String className=obj.getClass().getSimpleName();
            //注册bean实例
            beanFactory.registerSingleton(StringUtils.makeFirstLetterLowerCase(className), obj);
        }
    }
}
