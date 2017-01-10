package com.hl.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Base64;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class FileUtils {

	/**
	 * 文件转字节数组 base64 加密
	 * 
	 * @param path
	 * @return
	 */
	public static byte[] getBufferByBase64(String path) {
		byte[] cover = getBuffer(path);
		return Base64.encodeToString(cover, Base64.DEFAULT).getBytes();
	}

	public static boolean exists(String path) {
		File f = new File(path);
		return f.exists();
	}

	/**
	 * 判断路径是否存在，如果不存在则创建
	 * 
	 * @param path
	 *            路径
	 */
	public static void mkdirs(String path) {
		File f = new File(path);
		if (!f.exists()) {
			f.mkdirs();
		}
	}

	/**
	 * 
	 * 文件转字符串 编码(做Base64加密)
	 * 
	 * @param path
	 * @return
	 */
	public static String FileToString(String path) {
		try {
			File file = new File(path);
			if (!file.exists()) {
				return "";
			}
			FileInputStream fis = new FileInputStream(file);
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			String result = Base64.encodeToString(buffer, Base64.DEFAULT);
			return result;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * @Title: FileToString2
	 * @Description: 文件转字符串编码(不做Base64加密)
	 * @param path
	 * @return
	 *
	 */
	public static String FileToString2(String path) {
		byte[] fileByte = getBuffer(path);
		if (fileByte != null) {
			return new String(fileByte);
		}
		return "";
	}

	/**
	 * @Title: FileToString
	 * @Description: 文件转字符串编码(不做Base64加密),设置编码
	 * @param path
	 * @return
	 * @author eye_fa
	 * 
	 */
	public static String FileToString(String path, String charsetName) {
		byte[] fileByte = getBuffer(path);
		if (fileByte != null) {
			try {
				return new String(fileByte, charsetName);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	/**
	 * 文件转字节数组
	 * 
	 * @param path
	 * @return
	 */
	public static byte[] getBuffer(String path) {
		File file = null;
		FileInputStream fis = null;
		byte[] cover = null;
		try {
			file = new File(path);
			if (file.exists()) {
				int length = (int) file.length();
				fis = new FileInputStream(file);
				cover = new byte[length];
				fis.read(cover, 0, length);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeCloseable(fis);
		}
		return cover;
	}

	/**
	 * 字符串 保存到文件
	 * 
	 * @param s
	 * @param path
	 */
	public static void StringToFile(String s, String path) {
		ByteToFile(s.getBytes(), path);
	}

	/**
	 * byte 保存到文件
	 * @param path
	 */
	public static void ByteToFile(byte[] b, String path) {
		if (StringUtils.isEmpty(path)) {
			return;
		}
		FileOutputStream fos = null;
		try {
			File f = new File(path);
			mkdirs(f.getParentFile().getPath());
			if (f.exists()) {
				f.delete();
			}
			fos = new FileOutputStream(f);
			fos.write(b);
			fos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeCloseable(fos);
		}
	}

	/**
	 * @Title: write2File
	 * @Description: 字符串写到文件中
	 * @param str
	 * @param path
	 *
	 */
	public static void write2File(String str, String path) {
		File txt = new File(path);
		try {
			if (!txt.exists()) {
				txt.createNewFile();
			}
			byte bytes[] = new byte[1024];
			bytes = str.getBytes(); // 新加的
			int b = str.length(); // 改
			FileOutputStream fos = new FileOutputStream(txt);
			fos.write(bytes, 0, b);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Title: writeStrToFile
	 * @Description: String写入文件
	 * @param str
	 * @param path
	 * @return void
	 */
	public static void writeStrToFile(String str, String path) {
		writeStrToFile2(str, path, "UTF-8");
	}

	/**
	 * @Title: writeStrToFile
	 * @Description: String写入文件
	 * @param str
	 * @param path
	 * @return void
	 */
	public static void writeStrToFile2(String str, String path, String charsetEncoder) {
		try {
			File f = new File(path);
			mkdirs(f.getParentFile().getPath());
			FileOutputStream fos = new FileOutputStream(f);
			Writer os = new OutputStreamWriter(fos, charsetEncoder);
			os.write(str);
			os.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeFile(Context context, String fileName, InputStream input) {
		try {
			FileOutputStream output = context.openFileOutput(fileName, Context.MODE_PRIVATE);

			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = input.read(b)) != -1) {
				output.write(b, 0, len);
			}
			output.flush();
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 复制文件
	 */
	public static boolean copyFile(String oldPath, String newPath) {

		boolean result = false;
		File oldFile = new File(oldPath);
		File newFile = new File(newPath);
		if (!oldFile.exists() || !oldFile.isFile() || !oldFile.canRead()) {
			return result;
		}

		File parentFile = newFile.getParentFile();
		if (parentFile != null && !parentFile.exists()) {
			newFile.getParentFile().mkdirs();
		}
		if (newFile.exists()) {
			newFile.delete();
		}
		FileInputStream fosfrom = null;
		FileOutputStream fosto = null;
		try {
			fosfrom = new FileInputStream(oldFile);
			fosto = new FileOutputStream(newFile);
			byte[] buffer = new byte[1024 * 4];
			int length;
			while ((length = fosfrom.read(buffer)) != -1) {
				fosto.write(buffer, 0, length);
			}
			fosto.flush();
			result = true;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeCloseable(fosto);
			closeCloseable(fosfrom);
		}
		return result;

	}

	/**
	 * 关闭stream or reader
	 * 
	 * @param closeObj
	 */
	public static void closeCloseable(Closeable closeObj) {
		try {
			if (null != closeObj) {
				closeObj.close();
				closeObj = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 复制整个文件夹内容
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf/ff
	 * @return boolean
	 */
	public static boolean copyFolder(String oldPath, String newPath) {
		boolean isok = true;
		try {
			(new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}

				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath + "/" + (temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory()) {// 如果是子文件夹
					copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
		} catch (Exception e) {
			isok = false;
		}
		return isok;
	}

	/**
	 * 删除目录及内部所有东西
	 * 
	 * @param file
	 *            路径
	 */
	public static void delDir(File file) {
		if (file.isDirectory()) {
			File[] fs = file.listFiles();
			for (File f : fs) {
				if (f.isFile()) {
					f.delete();
				} else if (f.isDirectory()) {
					delDir(f);
					f.delete();
				}
			}
			file.delete();
		} else if (file.isFile()) {
			file.delete();
		}
	}

	/**
	 * 删除目录及内部所有东西
	 * 
	 * @param path
	 *            路径
	 */
	public static void delDir(String path) {
		delDir(new File(path));
	}

	/**
	 * @Title: delFileInDir
	 * @Description: 删除指定目录下以指定字符开头的文件
	 * @param @param fileName
	 * @param @param filepath
	 * @return boolean
	 *
	 */
	public static boolean delFileInDir(String startFileName, String filePath) {
		File file = new File(filePath);
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File f : files) {
				if (f.getName().startsWith(startFileName) && f.isFile()) {
					f.delete();
				}
			}
			return true;
		} else {
			return false;
		}
	}



	/**
	 * @Title: isExistsFile
	 * @Description: 根据指定文件名开头和文件路径判断该文件夹下是否存在这个文件
	 * @param path
	 * @param startFileName
	 * @return
	 *
	 */
	public static boolean isExistsFile(String path, String startFileName) {
		File file = new File(path);
		if (file.exists() && file.isDirectory()) {
			File[] files = file.listFiles();
			for (File f : files) {
				if (f.isFile() && f.length() > 0) {
					if (f.getName().startsWith(startFileName)) {
						return true;
					}
				}
			}
		} else {
			return false;
		}
		return false;
	}

	/**
	 * 判断文件是否存在如果不存在则 拷贝
	 * 
	 * @param c
	 */
	public static void CheckExistsAndCopy(Context c, File f) {
		if (!f.exists()) {
			copyFileFromAssets(c, f);
		}
	}

	/**
	 * 拷贝assets下的文件到指定目录
	 */
	public static void copyFileFromAssets(Context c, File f) {

		try {
			AssetManager am = c.getAssets();
			InputStream is = am.open(f.getName());
			FileOutputStream fos = new FileOutputStream(f.getParent() + File.separator + f.getName());

			byte[] buf = new byte[1024];
			int len = 0;
			len = is.read(buf);
			while (len != -1) {
				fos.write(buf, 0, len);
				len = is.read(buf);
			}
			is.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
