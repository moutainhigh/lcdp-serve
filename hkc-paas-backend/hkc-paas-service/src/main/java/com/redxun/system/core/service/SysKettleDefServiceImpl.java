
package com.redxun.system.core.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.common.utils.FileUtil;
import com.redxun.common.utils.SysPropertiesUtil;
import com.redxun.system.core.entity.SysFile;
import com.redxun.system.core.entity.SysKettleDbdef;
import com.redxun.system.core.entity.SysKettleDef;
import com.redxun.system.core.mapper.SysKettleDefMapper;
import com.redxun.system.operator.FileOperatorFactory;
import com.redxun.system.util.kettle.KRepository;
import com.redxun.system.util.kettle.KettleUtil;
import com.redxun.system.util.kettle.RepositoryTree;
import com.redxun.system.util.kettle.RepositoryUtil;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.repository.RepositoryDirectoryInterface;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
* [KETTLE定义]业务服务类
*/
@Service
public class SysKettleDefServiceImpl extends SuperServiceImpl<SysKettleDefMapper, SysKettleDef> implements BaseService<SysKettleDef> {

    @Resource
    private SysKettleDefMapper sysKettleDefMapper;

    @Autowired
    SysKettleDbdefServiceImpl sysKettleDbdefService;

    @Autowired
    SysFileServiceImpl sysFileServiceImpl;

    @Autowired
    FileOperatorFactory fileOperatorFactory;

    @Override
    public BaseDao<SysKettleDef> getRepository() {
        return sysKettleDefMapper;
    }

    public JsonResult getJobTree(String kettleDbdefId) {
        JsonResult jsonResult=new JsonResult().setShow(false).setMessage("获取成功!");
        List<RepositoryTree> allRepositoryTreeList =new ArrayList<>();
        SysKettleDbdef sysKettleDbdef = sysKettleDbdefService.get(kettleDbdefId);
        String pluginPath= SysPropertiesUtil.getString("kettlePluginPath");
        KRepository repo=new KRepository();
        repo.setDbType(sysKettleDbdef.getDbType())
                .setResUser(sysKettleDbdef.getResUser())
                .setResPwd(sysKettleDbdef.getResPwd())
                .setDatabaseName(sysKettleDbdef.getDatabase())
                .setPort(sysKettleDbdef.getPort())
                .setHost(sysKettleDbdef.getHost())
                .setUser(sysKettleDbdef.getUser())
                .setPassword(sysKettleDbdef.getPassword());
        JsonResult result= KettleUtil.connectToRepository(pluginPath,repo);
        KettleDatabaseRepository repository= (KettleDatabaseRepository) result.getData();
        try {
            allRepositoryTreeList= RepositoryUtil.getAllDirectoryTreeList(repository, "/", new ArrayList<>());
        } catch (KettleException e) {
            e.printStackTrace();
        }
        jsonResult.setData(allRepositoryTreeList);
        return jsonResult;
    }

    public JsonResult uploadFile(List<MultipartFile> files) throws IOException {
        JsonResult jsonResult=new JsonResult().setShow(false).setMessage("上传成功!");
        Iterator<MultipartFile> it = files.iterator();
        StringBuilder sb=new StringBuilder();
        sb.append("上传文件:");
        while (it.hasNext()) {
            String fileId = IdGenerator.getIdStr();
            SysFile file = new SysFile();
            file.setFileId(fileId);
            MultipartFile multipartFile=it.next();
            String oriFileName = multipartFile.getOriginalFilename();
            String extName = FileUtil.getFileExt(oriFileName);
            // 新文件名
            String newFileName = fileId + "." + extName;

            sb.append(fileId +",");

            InputStream is = multipartFile.getInputStream();
            byte[] bytes= FileUtil.input2byte(is);
            String relFilePath=createFile(newFileName, bytes);
            file.setPath(relFilePath);
            file.setFileSystem("file");
            file.setFileName(oriFileName);
            file.setTotalBytes(bytes.length);
            file.setExt(extName);
            file.setDelStatus("undeleted");
            file.setCreateUser(ContextUtil.getCurrentUser().getFullName());

            sysFileServiceImpl.save(file);
            file.setFileContent(bytes);
            jsonResult.setData(file);
        }
        return jsonResult;
    }

    public String createFile(String fileName, byte[] bytes) {
        // 上传的相对路径
        String tempPath=new SimpleDateFormat("yyyyMM").format(new Date());
        String	relFilePath=tempPath;
        IUser curUser = ContextUtil.getCurrentUser();
        if(curUser!=null){
            String account = curUser.getFullName();
            relFilePath = account + "/" + tempPath  ;
        }
        else{
            relFilePath =new SimpleDateFormat("yyyyMM").format(new Date());
        }

        String uploadPath = SysPropertiesUtil.getString("kettleUploadPath");
        String fullPath = uploadPath+ relFilePath;

        File dirFile = new File(fullPath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        String filePath=fullPath + "/" +fileName;
        try {
            FileUtil.writeByte(filePath, bytes);
            return filePath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 运行Kettle
     * @param kettleDefId 主键
     * @return
     */
    public JsonResult runKettle(String kettleDefId,String loglevel) {
        SysKettleDef sysKettleDef = sysKettleDefMapper.selectById(kettleDefId);
        String storeSetting = sysKettleDef.getStoreSetting();
        JSONObject jsonObject = JSON.parseObject(storeSetting);
        //资源库
        if("database".equals(sysKettleDef.getStoreType())){
            return runKettleByDatabase(sysKettleDef,jsonObject,loglevel);
        }else{
            //文件
            return runKettleByFile(sysKettleDef,jsonObject,loglevel);
        }
    }

    /**
     * 根据资源库运行Kettle
     * @param sysKettleDef Kettle定义
     * @param storeSetting 存储配置
     * @return
     */
    public JsonResult runKettleByDatabase(SysKettleDef sysKettleDef,JSONObject storeSetting,String loglevel) {
        String database = storeSetting.getString("database");
        String job = storeSetting.getString("job");
        String fileDirectory = storeSetting.getString("directory");
        if(StringUtils.isEmpty(database)){
            return new JsonResult().setSuccess(false).setMessage("未配置资源库");
        }
        Map<String ,String> params=new HashMap<>();
        String parameters = sysKettleDef.getParameters();
        if(StringUtils.isNotEmpty(parameters)){
            JSONArray jsonArray = JSON.parseArray(parameters);
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                params.put(jsonObject.getString("name"),jsonObject.getString("value"));
            }
        }
        JsonResult jsonResult = new JsonResult();
        SysKettleDbdef sysKettleDbdef = sysKettleDbdefService.get(database);
        KRepository repo=new KRepository();
        repo.setDbType(sysKettleDbdef.getDbType())
                .setResUser(sysKettleDbdef.getResUser())
                .setResPwd(sysKettleDbdef.getResPwd())
                .setDatabaseName(sysKettleDbdef.getDatabase())
                .setPort(sysKettleDbdef.getPort())
                .setHost(sysKettleDbdef.getHost())
                .setUser(sysKettleDbdef.getUser())
                .setPassword(sysKettleDbdef.getPassword());

        String pluginPath = SysPropertiesUtil.getString("kettlePluginPath");
        JsonResult result=KettleUtil.connectToRepository(pluginPath,repo);
        if(result.isSuccess() ){
            LogLevel level = LogLevel.getLogLevelForCode(loglevel);
            try {
                KettleDatabaseRepository repository= (KettleDatabaseRepository) result.getData();
                RepositoryDirectoryInterface directory = repository.loadRepositoryDirectoryTree().findDirectory(fileDirectory);
                //转换
                if("transformation".equals(sysKettleDef.getType())){
                    jsonResult= KettleUtil.runTranslation(repository,directory,job,level,params);
                }
                //任务
                else {
                    jsonResult= KettleUtil.runJob(repository,directory,job, level,params);
                }
            }catch (Exception ex){
                jsonResult.setSuccess(false);
                jsonResult.setMessage(ExceptionUtil.getExceptionMessage(ex));
            }
        }
        return jsonResult.setShow(false);
    }

    /**
     * 根据文件运行Kettle
     * @param sysKettleDef Kettle定义
     * @param storeSetting 存储配置
     * @return
     */
    public JsonResult runKettleByFile(SysKettleDef sysKettleDef,JSONObject storeSetting,String loglevel) {
        JsonResult jsonResult = new JsonResult();
        String fileId = storeSetting.getString("fileId");
        SysFile sysFile = sysFileServiceImpl.get(fileId);
        String pluginPath = SysPropertiesUtil.getString("kettlePluginPath");
        LogLevel level = LogLevel.getLogLevelForCode(loglevel);
        //转换
        if("transformation".equals(sysKettleDef.getType())){
            jsonResult= KettleUtil.runTranslation(sysFile.getPath(),pluginPath,null,level);
        }
        //任务
        else {
            jsonResult=KettleUtil.runJob(sysFile.getPath(),pluginPath,new HashMap<>(),level);
        }
        return jsonResult.setShow(false);
    }

}
