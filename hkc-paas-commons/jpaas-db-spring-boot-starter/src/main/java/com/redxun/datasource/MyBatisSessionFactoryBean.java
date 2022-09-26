package com.redxun.datasource;

import com.redxun.common.utils.Dom4jUtil;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.core.NestedIOException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

/**
 * 
 * @author mansan
 *@Email chshxuan@163.com
 * @Copyright (c) 2014-2016 广州红迅软件有限公司（http://www.redxun.cn）
 * 本源代码受软件著作法保护，请在授权允许范围内使用。
 */
public class MyBatisSessionFactoryBean extends SqlSessionFactoryBean {
	
	protected static Logger logger=LogManager.getLogger(MyBatisSessionFactoryBean.class);

	private InputStream[] mappingAllLocation;

	@Override
	public SqlSessionFactory buildSqlSessionFactory() throws Exception {
//		ContextLoader
		SqlSessionFactory sqlSessionFactory = super.buildSqlSessionFactory();
		Configuration configuration = sqlSessionFactory.getConfiguration();
		if (mappingAllLocation != null && mappingAllLocation.length > 0) {

			String[] mappingLocations = getMappingLocations(mappingAllLocation[0]);
			PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
			//遍历解析
			for(int i=0;i<mappingLocations.length;i++){
				if(mappingLocations[i]==null){
					continue;
				}
				Resource[] mappingAllLocations = resolver.getResources(mappingLocations[i]);
				loadMapping(configuration,mappingAllLocations);
			}				
			SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
			sqlSessionFactory = sqlSessionFactoryBuilder.build(configuration);
			
		}
		return sqlSessionFactory;
	}

	public void setMappingAllLocation(InputStream[] mappingAllLocation) {
		this.mappingAllLocation = mappingAllLocation;
	}

	/**
	 * 加载Mapping
	 * @param configuration
	 * @param mappingAllLocations
	 * @throws NestedIOException
	 */
	private void loadMapping(Configuration configuration,
			Resource[] mappingAllLocations) throws NestedIOException {
		if (mappingAllLocations!=null && mappingAllLocations.length>0) {
			for (Resource mapperLocation : mappingAllLocations) {
				if (mapperLocation == null) {
					continue;
				}

				try {
					XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(
							mapperLocation.getInputStream(), configuration,
							mapperLocation.toString(),
							configuration.getSqlFragments());
					xmlMapperBuilder.parse();
				} catch (Exception e) {
					throw new NestedIOException(
							"Failed to parse mapping resource: '"
									+ mapperLocation + "'", e);
				} finally {
					ErrorContext.instance().reset();
				}

				if (logger.isDebugEnabled()) {
					logger.debug("Parsed mapper file: '" + mapperLocation
							+ "'");
				}
			}
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("Property 'mapperLocations' was not specified or no matching resources found");
			}
		}
	}
	/**
	 * 获得mybatis的mapping的路径
	 * @param mappingAllXmlFile
	 * @return
	 * @throws FileNotFoundException

	 */
	public static String[] getMappingLocations(InputStream mappingAllXmlFile)  {
		Document doc= Dom4jUtil.load(mappingAllXmlFile);
		Element rootEl=doc.getRootElement();
		List<Element> locationNodes=(List<Element>)rootEl.selectNodes("./mappingLocation");
		if(locationNodes.size()<=0) {
			logger.error("No found mappingLocation elements, please check 'mybatis-mappings.xml' file");
			return null;
		}
		String[] locations=new String[locationNodes.size()];
		int i=0;
		for(Element el:locationNodes){
			locations[i++]=el.attributeValue("value");
		}
		return locations;
	}
}