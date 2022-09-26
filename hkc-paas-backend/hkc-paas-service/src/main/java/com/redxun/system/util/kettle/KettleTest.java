package com.redxun.system.util.kettle;

import com.redxun.common.base.entity.JsonResult;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.repository.RepositoryDirectoryInterface;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;

public class KettleTest {
    public static void main(String[] args) throws KettleException {
        KRepository repo=new KRepository();
        repo.setDbType("MYSQL")
                .setResUser("admin")
                .setResPwd("admin")
                .setDatabaseName("demo")
                .setPort("3306")
                .setHost("localhost")
                .setUser("root")
                .setPassword("root");
        String pluginPath="D:/Redxun/kettle/data-integration/plugins";
        JsonResult result=KettleUtil.connectToRepository(pluginPath,repo);
        if(result.isSuccess() ){
            KettleDatabaseRepository repository= (KettleDatabaseRepository) result.getData();
            RepositoryDirectoryInterface directory = repository.loadRepositoryDirectoryTree().findDirectory("/");
            //根据任务名称执行
            JsonResult rtn= KettleUtil.runJob(repository,directory,"job1",LogLevel.DEBUG,null);

            //根据任务路径执行
            //JsonResult rtn= KettleUtil.runJob("C:\\Users\\gjh\\Desktop\\job1.kjb",pluginPath,new HashMap<>(),LogLevel.DEBUG);

            //根据转换名称执行
            //JsonResult rtn= KettleUtil.runTranslation(repository,directory,"demo1",LogLevel.DEBUG,null);

            //根据转换文件执行
            //JsonResult rtn= KettleUtil.runTranslation("C:\\Users\\gjh\\Desktop\\demo1.ktr",pluginPath,null,LogLevel.DEBUG);

            System.err.println(rtn.getMessage());
        }

    }






}
