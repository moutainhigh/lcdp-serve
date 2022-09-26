package com.redxun.ureport.provider;

import com.bstek.ureport.provider.report.ReportFile;
import com.bstek.ureport.provider.report.ReportProvider;
import com.redxun.common.tool.BeanUtil;
import com.redxun.ureport.core.entity.UreportFile;
import com.redxun.ureport.core.service.UreportFileServiceImpl;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Mysql 报表存储
 * @author qiaolin
 * @version 2018年5月9日
 *
 */

@Setter
@Component
@ConfigurationProperties(prefix = "ureport.db.provider")
public class DbSqlProvider implements ReportProvider {
	private static final String NAME = "db-provider";
	
	private String prefix = "db:";
	
	private boolean disabled;

	@Resource
	private UreportFileServiceImpl ureportFileService;
	
	@Override
	public InputStream loadReport(String file) {
		UreportFile ureportFile = ureportFileService.getByFileName(getCorrectName(file));
		if(BeanUtil.isEmpty(ureportFile)){
			return null;
		}
		byte[] content = ureportFile.getContent();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(content);
		return inputStream;
	}

	@Override
	public void deleteReport(String file) {
		UreportFile ureportFile = ureportFileService.getByFileName(getCorrectName(file));
		if(BeanUtil.isNotEmpty(ureportFile)){
			ureportFileService.delete(ureportFile.getId());
		}
	}

	@Override
	public List<ReportFile> getReportFiles() {
		List<ReportFile> list = new ArrayList<>();

		return list	;
	}

	@Override
	public void saveReport(String file, String content) {
		file = getCorrectName(file);
		UreportFile ureportFileEntity = ureportFileService.getByFileName(getCorrectName(file));
		Date currentDate = new Date();
		if(ureportFileEntity == null){
			ureportFileEntity = new UreportFile();
			ureportFileEntity.setName(file);
			ureportFileEntity.setContent(content.getBytes());
			ureportFileEntity.setCreateTime(currentDate);
			ureportFileEntity.setUpdateTime(currentDate);
			ureportFileService.insert(ureportFileEntity);
		}else{
			ureportFileEntity.setContent(content.getBytes());
   			ureportFileEntity.setUpdateTime(currentDate);
			ureportFileService.update(ureportFileEntity);
		}
	}

	@Override
	public void saveReportByCategoryId(String file,String categoryId,String content) {
		file = getCorrectName(file);
		UreportFile ureportFileEntity = ureportFileService.getByFileName(getCorrectName(file));
		Date currentDate = new Date();
		if(ureportFileEntity == null){
			ureportFileEntity = new UreportFile();
			ureportFileEntity.setName(file);
			ureportFileEntity.setContent(content.getBytes());
			ureportFileEntity.setCategoryId(categoryId);
			ureportFileEntity.setCreateTime(currentDate);
			ureportFileEntity.setUpdateTime(currentDate);
			String appId=ureportFileService.getAppIdByTreeId(categoryId);
			ureportFileEntity.setAppId(appId);
			ureportFileService.insert(ureportFileEntity);
		}else{
			ureportFileEntity.setContent(content.getBytes());
			ureportFileEntity.setUpdateTime(currentDate);
			ureportFileService.update(ureportFileEntity);
		}
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public boolean disabled() {
		return disabled;
	}

	@Override
	public String getPrefix() {
		return prefix;
	}

	/**
	 * 获取没有前缀的文件名
	 * @param name
	 * @return
	 */
	private String getCorrectName(String name){
		if(name.startsWith(prefix)){
			name = name.substring(prefix.length(), name.length());
		}
		return name; 
	}
}
