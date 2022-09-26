package com.redxun.common.utils;

import com.alibaba.excel.util.FileUtils;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.nustaq.serialization.FSTConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.sql.Clob;
import java.text.DecimalFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;


/**
 *  文件工具类
 * @author mansan
 * @Email chshxuan@163.com
 * @Copyright (c) 2014-2016 广州红迅软件有限公司（http://www.redxun.cn）
 * 本源代码受软件著作法保护，请在授权允许范围内使用。
 */
@Slf4j
public class FileUtil {
	
	protected static Logger logger= LoggerFactory.getLogger(FileUtil.class);
	
	/**
	 * 序列化神器 
	 * @see //github.com/RuedigerMoeller/fast-serialization/wiki/Serialization
	 */
	//static FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();

    /**
     * 根据文件名获取扩展名称。
     *
     * @param fileName
     * @return
     */
    public static String getFileExt(String fileName) {
        int pos = fileName.lastIndexOf(".");
        if (pos > -1) {
            return fileName.substring(pos + 1).toLowerCase();
        }
        return "";
    }
    /**
     * 检查文件目录是否存在，不存在则创建
     * @param path 文件路径 such as d:/abc or dev/abc
     * @return 
     */
    public static void checkAndCreatePath(String path){
        File file=new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
    } 

    /**
     * 把文件压缩至ZIP的输出流中去
     *
     * @param inputFile
     * @param outputStream
     */
    public static void zipFile(File inputFile, ZipOutputStream outputStream) {
    	if (!inputFile.exists()) {
    		return;
		}
        try {
            FileInputStream is = new FileInputStream(inputFile);
            BufferedInputStream bins = new BufferedInputStream(is, 512);
            //org.apache.tools.zip.ZipEntry
            ZipEntry entry = new ZipEntry(inputFile.getName());
            outputStream.putNextEntry(entry);
            // 向压缩文件中输出数据   
            int nNumber;
            byte[] buffer = new byte[512];
            while ((nNumber = bins.read(buffer)) != -1) {
                outputStream.write(buffer, 0, nNumber);
            }
            // 关闭创建的流对象   
            bins.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 压缩文件
     * @param is 输入文件流
     * @param fileName 输出文件名
     * @param outputStream 压缩的输出流
     */
    public static void zipFile(InputStream is,String fileName,ZipOutputStream outputStream){
    	try {
                BufferedInputStream bins = new BufferedInputStream(is, 512);
          
                ZipEntry entry = new ZipEntry(fileName);
                outputStream.putNextEntry(entry);
                // 向压缩文件中输出数据   
                int nNumber;
                byte[] buffer = new byte[512];
                while ((nNumber = bins.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, nNumber);
                }
                // 关闭创建的流对象   
                bins.close();
                is.close();
                outputStream.closeEntry();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 解压文件。
     * @param zipFilePath			压缩文件名称
     * @param unzipFilePath			解压路径
     * @param includeZipFileName	是否包含目录
     * @throws Exception
     */
    public static void unzip(String zipFilePath, String unzipFilePath,
            boolean includeZipFileName) throws Exception {
        if (StringUtils.isEmpty(zipFilePath) || StringUtils.isEmpty(unzipFilePath)) {
            throw new Exception("PARAMETER_IS_NULL");
        }
        File zipFile = new File(zipFilePath);
        // 如果解压后的文件保存路径包含压缩文件的文件名，则追加该文件名到解压路径
        if (includeZipFileName) {
            String fileName = zipFile.getName();
            if (StringUtils. isNotEmpty(fileName)) {
                fileName = fileName.substring(0, fileName.lastIndexOf("."));
            }
            unzipFilePath = unzipFilePath + File.separator + fileName;
        }
        // 创建解压缩文件保存的路径
        File unzipFileDir = new File(unzipFilePath);
        if (!unzipFileDir.exists() || !unzipFileDir.isDirectory()) {
            unzipFileDir.mkdirs();
        }

        // 开始解压
        ZipEntry entry = null;
        String entryFilePath = null, entryDirPath = null;
        File entryFile = null, entryDir = null;
        int index = 0, count = 0, bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        ZipFile zip = new ZipFile(zipFile);
        Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zip.entries();
        // 循环对压缩包里的每一个文件进行解压
        while (entries.hasMoreElements()) {
            entry = entries.nextElement();
            if (entry.isDirectory()) {
                continue;
            }
            // 构建压缩包中一个文件解压后保存的文件全路径
            entryFilePath = unzipFilePath + File.separator + entry.getName();
            // 构建解压后保存的文件夹路径
            index = entryFilePath.lastIndexOf(File.separator)>entryFilePath.lastIndexOf("/")?entryFilePath.lastIndexOf(File.separator):entryFilePath.lastIndexOf("/");
            if (index != -1) {
                entryDirPath = entryFilePath.substring(0, index);
            } else {
                entryDirPath = "";
            }
            entryDir = new File(entryDirPath);
            // 如果文件夹路径不存在，则创建文件夹
            if (!entryDir.exists() || !entryDir.isDirectory()) {
                entryDir.mkdirs();
            }

            // 创建解压文件
            entryFile = new File(entryFilePath);
            if (entryFile.exists()) {
        
                entryFile.delete();
            }
            try {
                // 写入文件
                bos = new BufferedOutputStream(new FileOutputStream(entryFile));
                bis = new BufferedInputStream(zip.getInputStream(entry));
                while ((count = bis.read(buffer, 0, bufferSize)) != -1) {
                    bos.write(buffer, 0, count);
                }
                bos.flush();
                bos.close();
                bis.close();
            } catch (Exception e) {
                e.printStackTrace();
             
            }
        }
        zip.close();
    }
    
    /**
	 * 深度克隆对象。
	 * 
	 * <pre>
	 * 	将一个对象序列化一个内存数据组。
	 *  在从数组中反序列化这个类。
	 * </pre>
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static Object cloneObject(Object obj) throws Exception {
		String tmp=serializeToString(obj);
		return deserializeToObject(tmp);
	}
	
	
	
	/**
	 * 对象转字节。
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static byte[] objToBytes(Object obj) throws Exception {
		byte[] bytes = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			oos.flush();
			bytes = bos.toByteArray ();
			oos.close();
			bos.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return bytes;
	}
	
	/**
	 * 字节转对象。
	 * @param bytes
	 * @return
	 * @throws Exception
	 */
	public static Object bytesToObject(byte[] bytes ) throws Exception {
		Object obj = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream (bytes);
			ObjectInputStream ois = new ObjectInputStream (bis);
			obj = ois.readObject();
			ois.close();
			bis.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		return obj;
	}

	
	
	
	
	
	/**
	 * 写入文件
	 * 
	 * @param fileName
	 *            文件名称
	 * @param content
	 *            文件内容
	 */
	public static void writeFile(String fileName, String content) {
		writeFile(fileName, content, "utf-8");
	}

	/**
	 * 指定字符集，写入文件。
	 * 
	 * @param fileName
	 * @param content
	 * @param charset
	 */
	public static void writeFile(String fileName, String content, String charset) {
		Writer out=null;
		try {
			createFolder(fileName, true);
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), charset));
			out.write(content);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 写入文件
	 * 
	 * @param fileName
	 * @param is
	 * @throws IOException
	 */
	public static void writeFile(String fileName, InputStream is) throws IOException {
		FileOutputStream fos = new FileOutputStream(fileName);
		byte[] bs = new byte[512];
		int n = 0;
		while ((n = is.read(bs)) != -1) {
			fos.write(bs, 0, n);
		}
		is.close();
		fos.close();
	}

	/**
	 * 读取文件
	 * 
	 * @param fileName
	 *            文件名称
	 * @return
	 */
	public static String readFile(String fileName) {
		try {
			File file = new File(fileName);
			String charset = getCharset(file);
			StringBuffer sb = new StringBuffer();
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), charset));
			String str;
			while ((str = in.readLine()) != null) {
				sb.append(str + "\r\n");
			}
			in.close();
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 读取文件
	 *
	 * @param url
	 *            文件路径
	 * @return
	 */
	public static String readFile(URL url) {
		try {
			File f = new File(url.toURI());
			FileInputStream fis = new FileInputStream(f);
			return readFile(fis);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String readFile(InputStream is) {
		StringBuffer sb = new StringBuffer();
		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(is);
			int len = 0;
			byte[] temp = new byte[1024];
			while ((len = bis.read(temp)) != -1) {
				sb.append(new String(temp, 0, len));
			}
		} catch (Exception e) {
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * 获取文件的字符集
	 * 
	 * @param file
	 * @return
	 */
	public static String getCharset(File file) {
		String charset = "GBK";
		byte[] first3Bytes = new byte[3];
		try {
			boolean checked = false;
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			bis.mark(0);
			int read = bis.read(first3Bytes, 0, 3);
			if (read == -1) {
				return charset;
			}
			if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
				charset = "UTF-16LE";
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xFE && first3Bytes[1] == (byte) 0xFF) {
				charset = "UTF-16BE";
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xEF && first3Bytes[1] == (byte) 0xBB && first3Bytes[2] == (byte) 0xBF) {
				charset = "UTF-8";
				checked = true;
			}
			bis.reset();

			if (!checked) {
				// int loc = 0;
				while ((read = bis.read()) != -1) {
					// loc++;
					if (read >= 0xF0){
						break;
					}

					// 单独出现BF以下的，也算是GBK
					if (0x80 <= read && read <= 0xBF) {
						break;
					}
					if (0xC0 <= read && read <= 0xDF) {
						read = bis.read();
						if (0x80 <= read && read <= 0xBF){
							continue;
						}
						else{
							break;
						}
						// 也有可能出错，但是几率较小
					} else if (0xE0 <= read && read <= 0xEF) {
						read = bis.read();
						if (0x80 <= read && read <= 0xBF) {
							read = bis.read();
							if (0x80 <= read && read <= 0xBF) {
								charset = "UTF-8";
								break;
							} else {
								break;
							}
						} else {
							break;
						}
					}
				}

			}
			bis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return charset;
	}

	/**
	 * 通过类路径读取文件
	 * 
	 * @param filePath
	 *            文件类路径
	 * @return
	 */
	public static String readByClassPath(String filePath) {
		return readByClassPath(filePath, null);
	}

	public static String readByClassPath(String filePath, ClassLoader classLoader) {
		InputStream is = null;
		try {
			URL url = null;
			if (classLoader != null) {
				url = classLoader.getResource("/" + filePath);
			} else {
				url = FileUtil.class.getResource("/" + filePath);
			}

			is = url.openStream();

			InputStreamReader isr = new InputStreamReader(is);

			BufferedReader br = new BufferedReader(isr);

			String s = null;
			String fileText = "";
			while ((s = br.readLine()) != null) {
				fileText = fileText + s + "\r\n";
			}

			return fileText;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param dir
	 * @return
	 */
	public static boolean isExistFile(String dir) {
		boolean isExist = false;
		File fileDir = new File(dir);
		if (fileDir.isDirectory()) {
			File[] files = fileDir.listFiles();
			if (files != null && files.length != 0) {
				isExist = true;
			}
		}
		return isExist;
	}

	

	/**
	 * 读取流到字节数组
	 * 
	 * @param is
	 * @return
	 */
	public static byte[] readByte(InputStream is) {
		try {
			byte[] r = new byte[is.available()];
			is.read(r);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 读取文件到字节数组
	 * 
	 * @param fileName
	 * @return
	 */
	public static byte[] readByte(String fileName) {
		try {
			FileInputStream fis = new FileInputStream(fileName);
			byte[] r = new byte[fis.available()];
			fis.read(r);
			fis.close();
			return r;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 写字节数组到文件
	 * 
	 * @param fileName
	 * @param b
	 * @return
	 */
	public static boolean writeByte(String fileName, byte[] b) {
		createFolder(fileName, true);
		try {
			BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(fileName));
			fos.write(b);
			fos.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 删除目录
	 * 
	 * @param dir
	 * @return
	 */
	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

	/**
	 * 序列化对象到文件
	 * 
	 * @param obj
	 * @param fileName
	 */
	public static void serializeToFile(Object obj, String fileName) {
		try {
			ObjectOutput out = new ObjectOutputStream(new FileOutputStream(fileName));
			out.writeObject(obj);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从文件反序列化出对象
	 * 
	 * @param fileName
	 * @return
	 */
	public static Object deserializeFromFile(String fileName) {
		try {
			File file = new File(fileName);
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
			Object obj = in.readObject();
			in.close();
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * stream 转为字符串
	 * 
	 * @param input
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static String inputStream2String(InputStream input, String charset) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(input, charset));
		StringBuffer buffer = new StringBuffer();
		String line = "";
		while ((line = in.readLine()) != null) {
			buffer.append(line + "\n");
		}
		return buffer.toString();

	}

	/**
	 * 将stream按照utf-8编码转换为字符串。
	 * 
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public static String inputStream2String(InputStream input) throws IOException {
		return inputStream2String(input, "utf-8");

	}

	/**
	 * 根据目录取得文件列表
	 * 
	 * @param path
	 * @return
	 */
	public static File[] getFiles(String path) {
		File file = new File(path);
		return file.listFiles();
	}

	/**
	 * 根据文件路径创建文件夹,如果路径不存在则创建.
	 * 
	 * @param path
	 */
	public static void createFolderFile(String path) {
		createFolder(path, true);
	}

	/**
	 * 创建文件夹
	 * 
	 * @param path
	 * @param isFile
	 */
	public static void createFolder(String path, boolean isFile) {
		if (isFile) {
			if(path.lastIndexOf(File.separator)==-1){
				path = path.substring(0, path.lastIndexOf("/"));
			}
			else{
				path = path.substring(0, path.lastIndexOf(File.separator));
			}
			
		}
		File file = new File(path);
		if (!file.exists())
			file.mkdirs();
	}

	/**
	 * 复制文件来新路径上
	 * 
	 * @param path
	 *            原路径
	 * @param newName
	 *            新路径
	 */
	public static void renameFolder(String path, String newName) {
		File file = new File(path);
		if (file.exists())
			file.renameTo(new File(newName));
	}

	/**
	 * 仅取得文件目录下的子目录。
	 * 
	 * @param dir
	 *            目录
	 * @return 子目录列表
	 */
	public static ArrayList<File> getDiretoryOnly(File dir) {
		ArrayList<File> dirs = new ArrayList<File>();
		if (dir != null && dir.exists() && dir.isDirectory()) {
			File[] files = dir.listFiles(new FileFilter() {

				@Override
				public boolean accept(File file) {
					return file.isDirectory();
				}
			});
			for (int i = 0; i < files.length; i++) {
				dirs.add(files[i]);
			}
		}
		return dirs;
	}

	/**
	 * 列出子文件列表
	 * 
	 * @param dir
	 *            指定目录
	 * @return 子文件列表
	 */
	public ArrayList<File> getFileOnly(File dir) {
		ArrayList<File> dirs = new ArrayList<File>();
		File[] files = dir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.isFile();
			}
		});
		for (int i = 0; i < files.length; i++) {
			dirs.add(files[i]);
		}
		return dirs;
	}

	/**
	 * 删除文件
	 * 
	 * @param path
	 * @return
	 */
	public static boolean deleteFile(String path) {
		File file = new File(path);
		return file.delete();
	}

	/**
	 * 文件拷贝
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public static boolean copyFile(String from, String to) {
		File fromFile = new File(from);
		File toFile = new File(to);
		FileInputStream fis = null;
		FileOutputStream fos = null;

		try {
			fis = new FileInputStream(fromFile);
			fos = new FileOutputStream(toFile);
			int bytesRead;
			byte[] buf = new byte[4 * 1024]; // 4K buffer
			while ((bytesRead = fis.read(buf)) != -1) {
				fos.write(buf, 0, bytesRead);
			}

			fos.flush();
			fos.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}
	
	/**
	 * 使用Java NIO复制文件,速度能加快
	 * @param source
	 * @param dest
	 * @throws IOException
	 */
	public static void copyFileUsingFileChannels(File source, File dest) throws IOException {    
        FileChannel inputChannel = null;    
        FileChannel outputChannel = null;    
    try {
        inputChannel = new FileInputStream(source).getChannel();
        outputChannel = new FileOutputStream(dest).getChannel();
        outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
    } finally {
        inputChannel.close();
        outputChannel.close();
    }
}

	/**
	 * 备份文件。 如果有备份文件，先删除。
	 * 
	 * @param filePath
	 */
	public static void backupFile(String filePath) {
		String backupName = filePath + ".bak";
		File file = new File(backupName);
		if (file.exists()) {
			file.delete();
		}

		copyFile(filePath, backupName);

	}

	/**
	 * 取得文件扩展名
	 * 
	 * @return
	 */
	public static String getFileExt(File file) {
		if (file.isFile()) {
			return getFileExt(file.getName());
		}
		return "";
	}



	/**
	 * copy目录
	 * 
	 * @param fromDir
	 *            源目录
	 * @param toDir
	 *            目标目录
	 * @throws IOException
	 */
	public static void copyDir(String fromDir, String toDir) throws IOException {
		(new File(toDir)).mkdirs();
		File[] file = (new File(fromDir)).listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				String fromFile = file[i].getAbsolutePath();
				String toFile = toDir + "/" + file[i].getName();

				copyFile(fromFile, toFile);
			}
			if (file[i].isDirectory()) {
				copyDir(fromDir + "/" + file[i].getName(), toDir + "/" + file[i].getName());
			}
		}
	}


	/**
	 * 取得文件大小
	 * 
	 * @return 返回文件大小
	 * @throws IOException
	 */
	public static String getFileSize(File file) throws IOException {
		if (file.isFile()) {
			FileInputStream fis = new FileInputStream(file);
			int size = fis.available();
			fis.close();
			return getSize(size);
		}
		return "";
	}

	/**
	 * 根据字节大小获取带单位的大小。
	 * 
	 * @param size
	 * @return
	 */
	public static String getSize(double size) {
		DecimalFormat df = new DecimalFormat("0.00");
		if (size > 1024 * 1024) {
			double ss = size / (1024 * 1024);
			return df.format(ss) + " M";
		} else if (size > 1024) {
			double ss = size / 1024;
			return df.format(ss) + " KB";
		} else {
			return size + " bytes";
		}
	}

	/**
	 * 取得文件的相对父目录
	 * 
	 * @param baseDir
	 *            基准目录
	 * @param currentFile
	 *            当前文件
	 * @return 相对基准目录路径
	 */
	public static String getParentDir(String baseDir, String currentFile) {
		File f = new File(currentFile);
		String parentPath = f.getParent();
		String path = parentPath.replace(baseDir, "");
		return path.replace(File.separator, "/");
	}

	/**
	 * 根据键在属性文件中获取数据。
	 * 
	 * @param fileName
	 *            属性文件名称。
	 * @param key
	 *            属性的键值。
	 * @return
	 */
	public static String readFromProperties(String fileName, String key) {
		String value = "";
		InputStream stream = null;
		try {
			stream = new BufferedInputStream(new FileInputStream(fileName));
			Properties prop = new Properties();
			prop.load(stream);
			value = prop.getProperty(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return value;
	}

	/**
	 * 保存属性文件。
	 * 
	 * @param fileName
	 *            文件名
	 * @param key
	 *            键名
	 * @param value
	 *            键值
	 * @return 保存是否成功。
	 */
	public static boolean saveProperties(String fileName, String key, String value) {
		StringBuffer sb = new StringBuffer();
		boolean isFound = false;
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8));
			String str;
			while ((str = in.readLine()) != null) {
				if (str.startsWith(key)) {
					sb.append(key + "=" + value + "\r\n");
					isFound = true;
				} else {
					sb.append(str + "\r\n");
				}
			}
			// 添加新的键值。
			if (!isFound) {
				sb.append(key + "=" + value + "\r\n");
			}
			FileUtil.writeFile(fileName, sb.toString(), "utf-8");
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 删除属性key。
	 * 
	 * @param fileName
	 * @param key
	 * @return
	 */
	public static boolean delProperties(String fileName, String key) {
		StringBuffer sb = new StringBuffer();

		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8));
			String str;
			while ((str = in.readLine()) != null) {
				if (!str.startsWith(key)) {
					sb.append(str + "\r\n");
				}
			}
			FileUtil.writeFile(fileName, sb.toString(), "utf-8");
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	

	

	

	/**
	 * 获取classes路径。
	 * 
	 * @return 
	 *         返回如下的路径E:/redxun/target/classes
	 *         。
	 * @throws URISyntaxException
	 */
	public static String getClassesPath()  {
		String path = FileUtil.class.getClassLoader().getResource("").getPath();
		if ("\\".equals(File.separator)) {
			path = StringUtils.trimPrefix(path, "/");
		}
		path = path.replace("\\", "/");
		path = StringUtils.trimSuffix(path, "/");
		return path;
	}


	

	

	/**
	 * 获取应用程序根路径。
	 * 
	 * @return 返回如下路径 E:/work/bpm/src/main/webapp
	 * @throws Exception
	 */
	public static String getWebRootPath() {
		String path = getClassesPath();
		path = path.substring(0, path.lastIndexOf("WEB-INF"));
		path = StringUtils.trimSuffix(path, "/");
		return path;
	}

	/**
	 * 将输入流写入到输出流中。
	 * @param is
	 * @param out
	 * @throws IOException
	 */
	public static void writeInput(InputStream is, OutputStream out) throws IOException{
		try {
			
			byte[] bs = new byte[1024];
			int n = 0;
			while ((n = is.read(bs)) != -1) {
				out.write(bs, 0, n);
			}
			out.flush();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (is != null) {
				is.close();
			}
			if (out != null) {
				out.close();
			}
		}
	}
	
	/**
	 * 将colb字段转成字符串。
	 * @param clob
	 * @return
	 */
	public static String clobToString(Clob clob)  {   
        String reString = "";
        try{
        	 Reader is = clob.getCharacterStream();// 得到流
             BufferedReader br = new BufferedReader(is);   
             String s = br.readLine();   
             StringBuffer sb = new StringBuffer();   
             while (s != null) {// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING   
                 sb.append(s);   
                 s = br.readLine();   
             }   
             reString = sb.toString();   
             return reString;   
        }
        catch(Exception ex){
        	return reString;
        }
    }
	
	
	/**
	 * 下载文件。
	 * @param file
	 * @param response
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public static void downLoad(File file, HttpServletResponse response) throws UnsupportedEncodingException, IOException{
		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream buff = null;
		OutputStream out = null;
		try {
			buff = new BufferedInputStream(fis);
			out = response.getOutputStream();
			IOUtils.copy(buff, out);
		} finally {
			out.flush();
			fis.close();
			buff.close();
			out.close();
		}
	}
	
	public static void downLoad(InputStream is, HttpServletResponse response) throws IOException{
		inputToOut(is,response.getOutputStream());
	}
	
	public static void inputToOut(InputStream is,OutputStream out) throws IOException{
		BufferedInputStream buff = null;
		try {
			buff = new BufferedInputStream(is);
			IOUtils.copy(buff, out);
		} finally {
			out.flush();
			is.close();
			buff.close();
			out.close();
		}
	}
	/**
	 * 获取图片的宽度
	 * @param path
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static int getImageWidth(String path) throws FileNotFoundException, IOException{
		File picture = new File(path);
		if(picture.exists()){
			BufferedImage sourceImg =ImageIO.read(new FileInputStream(picture));
			return sourceImg.getWidth();
		}else{
			return 0;
		}
	}
	/**
	 * 获取图片的高度
	 * @param path
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static int getImageHeight(String path) throws FileNotFoundException, IOException{
		File picture = new File(path);
		if(picture.exists()){
			BufferedImage sourceImg =ImageIO.read(new FileInputStream(picture));
			return sourceImg.getHeight();
		}else{
			return 0;
		}
		
	}
	
	/**
	 * 获取文件大小。
	 * @param file
	 * @return
	 */
	public static long getSize(File file){
		FileChannel fc = null; // 计算文件大小
		try {
			if (file.exists() && file.isFile()) {
				FileInputStream fis = new FileInputStream(file);
				fc = fis.getChannel();
				long size = fc.size();
				fis.close();
				return size;
			} else {
				logger.debug("file not exist!");
			}
		}catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != fc) {
				try {
					fc.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return 0;
	}
	
	/**
	 * 由inputsteam 转成bytes。
	 * @param inStream
	 * @return
	 * @throws IOException
	 */
	public static final byte[] input2byte(InputStream inStream)   throws IOException {  
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();  
		byte[] buff = new byte[1024];  
		int rc = 0;  
		while ((rc = inStream.read(buff, 0, 1024)) > 0) {  
				swapStream.write(buff, 0, rc);  
		}  
		byte[] in2b = swapStream.toByteArray();  
		return in2b;  
	}  
	
	
	public static String serializeToString(Object obj) throws Exception{
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();  
        ObjectOutputStream objOut = new ObjectOutputStream(byteOut);  
        objOut.writeObject(obj);  
        String str = byteOut.toString("ISO-8859-1");//此处只能是ISO-8859-1,但是不会影响中文使用
        return str;
    }
    //反序列化
    public static Object deserializeToObject(String str) throws Exception{
         ByteArrayInputStream byteIn = new ByteArrayInputStream(str.getBytes(StandardCharsets.ISO_8859_1));
         ObjectInputStream objIn = new ObjectInputStream(byteIn);  
         Object obj =objIn.readObject();  
         return obj;  
    }


	/**
	 * 下载文件。
	 * @param res
	 * @param content		下载内容
	 * @param downFileName	下载文件名称
	 * @param zipFileName	压缩文件名称
	 * @throws IOException
	 * @throws ArchiveException
	 */
    public  static void  downloadZip(HttpServletResponse res, String content, String downFileName, String zipFileName) throws IOException, ArchiveException {
		res.setContentType("application/zip");

		downFileName = URLEncoder.encode(downFileName, "UTF-8");

		res.addHeader("Content-Disposition", "attachment; filename=\"" + downFileName + ".zip\"");
		res.addHeader("blob", "true");

		ArchiveOutputStream zipOutputStream = new ArchiveStreamFactory()
				.createArchiveOutputStream(ArchiveStreamFactory.ZIP,
						res.getOutputStream());

		zipOutputStream.putArchiveEntry(new ZipArchiveEntry(zipFileName));
		InputStream is = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
		IOUtils.copy(is, zipOutputStream);
		zipOutputStream.closeArchiveEntry();
		zipOutputStream.close(); //关闭流
	}

	/**
	 * 下载ZIP包。
	 * @param response
	 * @param downFileName
	 * @param contentMap
	 * @throws IOException
	 * @throws ArchiveException
	 */
	public  static void  downloadZip(HttpServletResponse response, String downFileName, Map<String,String> contentMap) throws IOException, ArchiveException {
		response.setContentType("application/zip");

		downFileName = URLEncoder.encode(downFileName, "UTF-8");
		response.addHeader("Content-Disposition", "attachment; filename=\"" + downFileName + ".zip\"");
		response.addHeader("blob", "true");

		ArchiveOutputStream zipOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP,
						response.getOutputStream());

		for(Map.Entry<String, String> entry : contentMap.entrySet()){
			String fileName = entry.getKey();
			String content = entry.getValue();
			zipOutputStream.putArchiveEntry(new ZipArchiveEntry(fileName));
			InputStream is = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
			IOUtils.copy(is, zipOutputStream);
			zipOutputStream.closeArchiveEntry();
		}
		zipOutputStream.close();

	}

	/**
	 * 将文件压缩并下载
	 * @param response
	 * @param fileList
	 * @param path
	 * @throws IOException
	 * @throws ArchiveException
	 */
	public  static void  compressedDownload(HttpServletResponse response, List<File> fileList,String path,String zipName) throws IOException, ArchiveException {
		if(fileList.size()>0){
			response.setContentType("application/zip");
			FileOutputStream fos2 = new FileOutputStream(new File(path+zipName));
			ZipUtil.toZip(fileList, fos2);
			// 创建file对象
			File file = new File(path+zipName);
			String ext=new MimetypesFileTypeMap().getContentType(file);
			response.setContentType(ext);
			zipName = URLEncoder.encode(zipName,"UTF-8");
			response.setHeader("Content-Disposition",  "attachment;filename=" +zipName);
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream buff = null;
			OutputStream out = null;
			try {
				buff = new BufferedInputStream(fis);
				out = response.getOutputStream();
				IOUtils.copy(buff, out);
				deleteFile(path+zipName);
			} finally {
				out.flush();
				fis.close();
				buff.close();
				out.close();
			}
		}
	}

	/**
	 * 读取classpath中的文件，包括jar的文件。
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static  String readJarFile(String path)   {
		try{
			ClassPathResource classPathResource = new ClassPathResource(path);
			InputStream inputStream= classPathResource.getInputStream();
			String str= inputStream2String(inputStream);
			return str;
		}
		catch (Exception ex){
			log.error(ExceptionUtil.getExceptionMessage(ex));
			return "";
		}
	}

	public static MultipartFile getMultipartFile(File file) {
		DiskFileItem item = new DiskFileItem("file"
				, MediaType.MULTIPART_FORM_DATA_VALUE
				, true
				, file.getName()
				, (int)file.length()
				, file.getParentFile());
		try {
			OutputStream os = item.getOutputStream();
			os.write(FileUtils.readFileToByteArray(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new CommonsMultipartFile(item);
	}

}
