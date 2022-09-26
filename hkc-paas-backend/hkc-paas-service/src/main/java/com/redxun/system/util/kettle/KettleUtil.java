package com.redxun.system.util.kettle;

import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.utils.ExceptionUtil;
import org.pentaho.di.base.AbstractMeta;
import org.pentaho.di.core.ProgressMonitorListener;
import org.pentaho.di.core.ProgressNullMonitorListener;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.KettleLogStore;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.core.logging.LoggingBuffer;
import org.pentaho.di.core.parameters.UnknownParamException;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.repository.RepositoryDirectoryInterface;
import org.pentaho.di.repository.RepositoryMeta;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;
import org.pentaho.di.repository.kdr.KettleDatabaseRepositoryMeta;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * kettle 执行job，translation工具类。
 * @author ray
 */
public class KettleUtil {

    /**
     * 连接到资源库
     * 如果成功则data 的数据为资源库对象。
     * @param pluginPath
     * @param repo
     * @return
     */
    public static JsonResult connectToRepository(String pluginPath, KRepository repo){
        try{
            KettleInit.init(pluginPath);

            DatabaseMeta databaseMeta = new DatabaseMeta(null, repo.getDbType(), "Native", repo.getHost(),
                    repo.getDatabaseName(), repo.getPort(), repo.getUser(), repo.getPassword());
            KettleDatabaseRepositoryMeta repositoryInfo = new KettleDatabaseRepositoryMeta();
            repositoryInfo.setConnection(databaseMeta);
            KettleDatabaseRepository repository = new KettleDatabaseRepository();
            repository.init((RepositoryMeta)repositoryInfo);
            repository.connect(repo.getResUser(), repo.getResPwd());
            return JsonResult.Success().setData(repository);
        }
        catch (Exception ex){
            JsonResult jsonResut= handException(ex);
            return  jsonResut;
        }
    }

    /**
     * 执行kettle 的 Translation
     * @param repository
     * @param directory
     * @param transName
     * @param logLevel
     * @param params
     * @return
     * @throws KettleException
     */
    public static JsonResult runTranslation(KettleDatabaseRepository repository,
                                   RepositoryDirectoryInterface directory, String transName,
                                   LogLevel logLevel,Map<String ,String> params) throws KettleException {

        try{
            TransMeta transMeta = repository.loadTransformation(transName, directory, (ProgressMonitorListener)new ProgressNullMonitorListener(), true, "1.0");
            transMeta.setCapturingStepPerformanceSnapShots(true);

            initMeta(transMeta,params);

            Trans trans = new Trans(transMeta);

            JsonResult result=runTrans(trans,logLevel);
            return result;
        }
        catch (Exception ex){
            JsonResult jsonResut= handException(ex);
            return  jsonResut;
        }
    }

    /**
     * 执行translation。
     * @param trans
     * @param logLevel
     * @return
     * @throws KettleException
     */
    private static JsonResult runTrans(Trans trans,LogLevel logLevel) throws KettleException {
        trans.setLogLevel(logLevel);
        trans.setMonitored(true);
        trans.setInitializing(true);
        trans.setPreparing(true);
        trans.setRunning(true);
        trans.setSafeModeEnabled(true);

        trans.execute(null);
        trans.waitUntilFinished();
        JsonResult result=trans.isFinished()?JsonResult.Success():JsonResult.Fail();
        String logText=getLogText(trans.getLogChannelId());
        result.setMessage(logText);
        return result;
    }


    /**
     * 执行资源库的JOB
     * @param repository
     * @param directory
     * @param jobName
     * @param logLevel
     * @param params
     * @return
     * @throws KettleException
     */
    public static JsonResult runJob(KettleDatabaseRepository repository,
                              RepositoryDirectoryInterface directory,
                              String jobName,
                              LogLevel logLevel, Map<String,String> params) throws KettleException {
        try{
            JobMeta jobMeta = repository.loadJob(jobName, directory, (ProgressMonitorListener)new ProgressNullMonitorListener(), null);

            initMeta(jobMeta,params);

            Job job = new Job((Repository)repository, jobMeta);
            JsonResult result=runJob(job,logLevel);
            return result;
        }
        catch (Exception ex){
            JsonResult jsonResut= handException(ex);
            return  jsonResut;
        }
    }

    /**
     * 运行JOB
     * @param job
     * @param logLevel
     * @return
     */
    private static JsonResult runJob(Job job,LogLevel logLevel){
        job.setDaemon(true);
        job.setLogLevel(logLevel);

        job.run();
        job.waitUntilFinished();

        JsonResult result=job.isFinished()?JsonResult.Success(): JsonResult.Fail();

        String logChannelId = job.getLogChannelId();
        String log=getLogText(logChannelId);
        result.setMessage(log);

        return result;
    }


    private static String getLogText(String logChannelId){
        LoggingBuffer appender = KettleLogStore.getAppender();
        String logText = appender.getBuffer(logChannelId, true).toString();
        return logText;
    }

    /**
     * 执行文件JOB
     * @param jobPath
     * @param params
     * @return
     */
    public static JsonResult runJob(String jobPath,String  pluginPath,Map<String,String> params,LogLevel logLevel ) {
        try {

            KettleInit.init(pluginPath);

            JobMeta jobMeta = new JobMeta(jobPath, null);



            initMeta(jobMeta,params);

            Job job = new Job(null, jobMeta);

            JsonResult result= runJob(job,logLevel);

            return result;
        } catch (Exception ex) {
            JsonResult jsonResut= handException(ex);
            return  jsonResut;
        }
    }



    /**
     * 执行文件的转换。
     * @param ktrPath
     * @param params
     * @param logLevel
     * @return
     */
    public static JsonResult runTranslation(String ktrPath,String  pluginPath, Map<String,String> params,LogLevel logLevel ) {
        try {
            KettleInit.init(pluginPath);

            TransMeta transMeta = new TransMeta(ktrPath);

            initMeta(transMeta,params);

            // 转换
            Trans trans = new Trans(transMeta);
            JsonResult result=runTrans(trans,logLevel);
            return result;
        }
        catch (Exception ex){
            JsonResult jsonResut= handException(ex);
            return  jsonResut;
        }
    }

    /**
     * 设置变量。
     * @param meta
     * @param params
     * @throws UnknownParamException
     */
    private static void initMeta(AbstractMeta meta, Map<String,String> params) throws UnknownParamException {
        Set<Map.Entry<String, String>> entries = params.entrySet();
        for(Iterator<Map.Entry<String,String>> it = entries.iterator(); it.hasNext();){
            Map.Entry<String, String> next = it.next();
            meta.setParameterValue(next.getKey(),next.getValue());
        }
    }



    /**
     * 对错误统一处理
     * @param ex
     * @return
     */
    private static JsonResult handException(Exception ex){
        JsonResult jsonResut= JsonResult.Fail();
        String errMsg= ExceptionUtil.getExceptionMessage(ex);
        jsonResut.setMessage(errMsg);
        return  jsonResut;
    }

    public static void main(String[] args) {
        Map<String,String> params=new HashMap<>();
        params.put("path","d:\\abc");
        params.put("ext","txt");
        params.put("conf","mydemo");
        params.put("ktr","C:/Users/ASUS/Desktop/kettle/demo1.ktr");

//            String[] args1=new String[]{"d:\\params"};
//            KettleUtil.runTranslation("C:\\Users\\ASUS\\Desktop\\kettle\\demo1.ktr",
//                    "D:\\software\\kettle\\pdi-ce-9.1.0.0-324\\data-integration\\plugins",
//                    params,LogLevel.BASIC
//                    );

        KettleUtil.runJob("C:\\Users\\ASUS\\Desktop\\kettle\\demo.kjb",
                "D:\\software\\kettle\\pdi-ce-9.1.0.0-324\\data-integration\\plugins",params,LogLevel.DEBUG);
    }
}
