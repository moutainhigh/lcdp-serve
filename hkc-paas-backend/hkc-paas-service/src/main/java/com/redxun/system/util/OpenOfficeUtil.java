package com.redxun.system.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.exception.NacosException;
import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.common.utils.FileUtil;
import com.redxun.utils.OtherConfigUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;

/**
 * OpenOffice工具类。
 *
 */
@Slf4j
public class OpenOfficeUtil {

	private static final String OFFICE_CONFIG = "openOfficeConfig";
	private static final String DEFAULT_GROUP = "DEFAULT_GROUP";

	public static final String SERVICE_IP="ip";

	public static final String SERVICE_PORT="port";

	public static final String INSTALL_PATH="installPath";

	public static final String INSTALL_ENABLED="enable";

	public static final String AUTO_START="autoStart";

	public static OpenOfficeConnection connection;

	public static DocumentConverter converter;


	public synchronized static void  newOpenOfficeConnection(String opId,Integer opPort) throws Exception{
		if(connection!=null && connection.isConnected()){
			log.info("---OpenOfficeUtil.newOpenOfficeConnection disconnect begin--");
			connection.disconnect();
			log.info("---OpenOfficeUtil.newOpenOfficeConnection disconnect end--");
		}
		connection = new SocketOpenOfficeConnection(opId,opPort);
		log.info("---OpenOfficeUtil.newOpenOfficeConnection connection begin--");
		connection.connect();
		log.info("---OpenOfficeUtil.newOpenOfficeConnection connection end--");
		log.info("---OpenOfficeUtil.newOpenOfficeConnection converter new begin--");
		converter = new OpenOfficeDocumentConverter(connection);
		log.info("---OpenOfficeUtil.newOpenOfficeConnection converter new end--");
	}

	public static Boolean isConnection(){
		if(BeanUtil.isEmpty(connection) || BeanUtil.isEmpty(converter)){
			return false;
		}
		return connection.isConnected();
	}

	/**
	 * 是否允许office转换。
	 * @return
	 */
	public static boolean isOpenOfficeEnabled() throws NacosException {
		JSONObject configJson = getOpenOfficeConfig();
		if(BeanUtil.isEmpty(configJson)){
			return false;
		}
		String enabled=configJson.getString(INSTALL_ENABLED);
		return "true".equals(enabled);
	}



	/**
	 *
	 * @param inputFilePath  D:\\outFile.doc 注意文件在不同系统内的区别,请使用File.separator作为分隔符
	 * @param outputFilePath D:\\intFile.pdf
	 * @return jsonObject   {success:false,reason:"openOffice系统参数缺失"}
	 */
	public static JSONObject coverFromOffice2Pdf(String inputFilePath, String outputFilePath)  {
		JSONObject configJson =  getOpenOfficeConfig();
		JSONObject jsonObject=new JSONObject();
		//openOffice参数
		String opIp="";
		//openOffice参数
		Integer opPort=null;
		try {
			opIp = configJson.getString(SERVICE_IP);//服务安装的的ip
			opPort= configJson.getIntValue(SERVICE_PORT);//服务安装的的端口
		} catch (Exception e) {
			log.error("---OpenOfficeUtil.coverFromOffice2Pdf is error--"+e.getMessage());
			jsonObject.put("success", false);
			jsonObject.put("fileNotFind", false);
			jsonObject.put("reason", "openOffice系统参数缺失");
			return jsonObject;
		}
		File inputFile = new File(inputFilePath);
		File outputFile = new File(outputFilePath);
		if (!inputFile.exists()) {
			jsonObject.put("success", false);
			jsonObject.put("fileNotFind", true);
			jsonObject.put("reason", "找不到源文件");
			log.info("---OpenOfficeUtil.coverFromOffice2Pdf is error--找不到源文件");
            return jsonObject;// 找不到源文件, 则返回
        }
		try{
			if(!isConnection()){
				log.info("---OpenOfficeUtil.coverFromOffice2Pdf isConnection begin--");
				newOpenOfficeConnection(opIp,opPort);
				log.info("---OpenOfficeUtil.coverFromOffice2Pdf isConnection end--");
			}
			log.info("---OpenOfficeUtil.coverFromOffice2Pdf converter.convert begin--");


			converter.convert(inputFile, outputFile);
			log.info("---OpenOfficeUtil.coverFromOffice2Pdf converter.convert end--");
		}catch (Exception e) {
	 		jsonObject.put("success", false);
			jsonObject.put("fileNotFind", false);
			jsonObject.put("reason", "连接错误");
			log.error("---OpenOfficeUtil.coverFromOffice2Pdf is error connection 连接错误:--"+e.getMessage());
			return jsonObject;
	 	}
		jsonObject.put("success", true);
		jsonObject.put("fileNotFind", false);
		jsonObject.put("reason", "成功执行,目标地址:"+outputFilePath);
        return jsonObject;
	}

	/**
	 * 获取连接状态
	 * @return
	 */
	public static JsonResult<String> getConnectStatus(Boolean isStart){
		JsonResult<String> rtn=new JsonResult<>(true, "office服务已启动!");
		String installPath="";
		String opId="";//openOffice参数
		Integer opPort=null;//openOffice参数
		try {
			JSONObject configJson = getOpenOfficeConfig();
			installPath = configJson.getString(INSTALL_PATH);
			opId = configJson.getString(SERVICE_IP);
			opPort=configJson.getIntValue(SERVICE_PORT);
		} catch (Exception e) {
			rtn.setSuccess(false);
			rtn.setMessage("openoffice配置不全!");
			return rtn;
		}
		try {
			log.info("---OpenOfficeUtil.coverFromOffice2Pdf getConnectStatus begin--001");
			if(!isConnection()){
				log.info("---OpenOfficeUtil.coverFromOffice2Pdf isConnection begin");
				newOpenOfficeConnection(opId,opPort);
				log.info("---OpenOfficeUtil.coverFromOffice2Pdf isConnection end--001");
			}
		} catch (Exception e) {
			if(isStart){
				startService(installPath, opId,Integer.toString(opPort));
			}
			rtn.setSuccess(false);
			rtn.setMessage("office服务未启动!");
		}
		return rtn;
	}

	public static JSONObject getOpenOfficeConfig() {
		try{
			String config= OtherConfigUtils.getFileContent(OFFICE_CONFIG);
			JSONObject configJson=JSONObject.parseObject(config);
			return configJson;
		}
		catch (Exception ex){
			return null;
		}

	}

	/**
	 * 启动服务
	 * @return
	 */
	public static JsonResult<String> startService(JSONObject configJson) throws IOException {
		if(configJson==null){
			return new JsonResult<String>(false,"没有配置openOfficeConfig");

		}
		String installPath=configJson.getString(INSTALL_PATH);
		String ip=configJson.getString(SERVICE_IP);
		String port=configJson.getString(SERVICE_PORT);
		String autoStart=configJson.getString(AUTO_START);
		if(!"true".equals(autoStart)) {
			String message ="openoffice 服务没有设置自动启动!";
			log.debug(message);
			return new JsonResult<String>(false,message);
		}
		//若过启动了就结束进程
		JsonResult<String> status = getStatus(port);
		if(status.getData().equals("1")){
			stopService(port);
		}
		JsonResult<String> result=startService(installPath, ip, port);
		return result;
	}

	/**
	 * 启动流程服务。
	 * @param installPath
	 * @param ip
	 * @param port
	 * @return
	 */
	public static JsonResult<String> startService(String installPath, String ip,String port){
		JsonResult<String> result=new JsonResult<String>(true,"启动服务成功!");
		String sys=System.getProperty("os.name");
		try {
			if(StringUtils.isBlank(installPath)|| StringUtils.isBlank(ip)|| StringUtils.isBlank(port)){
				return new JsonResult<String>(false,"系统参数不齐全!");
			}
			installPath=installPath.replaceAll("\\\\","/");

			if(!installPath.endsWith("/")){
				installPath=installPath +"/";
			}

			String command="";
			if(sys.toLowerCase().startsWith("win")){
				command= installPath   + "program/soffice.exe -headless -accept=\"socket,host="+ip+",port="+port+";urp;StarOffice.ServiceManager\" -nofirststartwizard";
			}else{
				command=installPath+"program/soffice -headless -accept=\"socket,host="+ip+",port="+port+";urp;\" -nofirststartwizard &";
			}
			Runtime.getRuntime().exec(command);
			log.info("---OpenOfficeUtil.startService  started");
		} catch (Exception e) {
			log.error(ExceptionUtil.getExceptionMessage(e));
			result.setSuccess(false);
			result.setMessage("启动失败!");
			result.setData(ExceptionUtil.getExceptionMessage(e));
		}
		return result;

	}


	/**
	 * 结束服务
	 * @return
	 */
	public static JsonResult<String> stopService(String port){
		JsonResult<String> result=new JsonResult<String>(true,"停止服务成功!");
		try {
			if(System.getProperty("os.name").toLowerCase().startsWith("win")){
				Runtime.getRuntime().exec("taskkill /f /im soffice.exe");
			}
			else {
				//还要linux下的结束进程
				String cmd[]= {"/bin/sh", "-c","sudo fuser -k -n tcp "+port};
				Runtime.getRuntime().exec(cmd);
			}
		} catch (IOException e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMessage("停止服务成功!");
			result.setData(ExceptionUtil.getExceptionMessage(e));
		}
		return result;
	}

	public static JsonResult<String> getStatus(String port) throws IOException {
		String str = null;
		JsonResult<String> result=new JsonResult<String>(true,"");
		String sys = System.getProperty("os.name");
		String [] cmd=null;
		if(sys.toLowerCase().startsWith("win")){
		    cmd= new String[]{"cmd","/C","tasklist|findstr soffice.exe"};
		}else {
			cmd= new String[]{"/bin/sh", "-c","netstat -nlp|grep :"+port};
		}
		Process process= Runtime.getRuntime().exec(cmd);
		str= FileUtil.inputStream2String(process.getInputStream());
		if(!"".equals(str) && str!=null){
			result.setData("1");
		}else {
			result.setData("0");
		}
		return 	result;

	}

	public static void main(String[] args) throws IOException {
		String [] cmd={"cmd","/C","tasklist|findstr soffice.exe"};
		Process process= Runtime.getRuntime().exec(cmd);
		String str= FileUtil.inputStream2String(process.getInputStream());
		System.err.println(str);
	}

}
