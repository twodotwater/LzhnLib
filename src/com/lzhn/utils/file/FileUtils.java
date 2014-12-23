package com.lzhn.utils.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.widget.Toast;

import com.lzhn.utils.print.LogUtils;

/**
 * 文件操作工具类
 * 
 * @author lzhn
 * 
 */
public class FileUtils {
	private static final String TAG = "FileUtils";

	/** 文件选择请求码 */
	public static final int FILE_SELECT = 0xff;

	/** 消息标记：文件传输开始 */
	private static final int WHAT_PROGRESS_SHOW = 0xa0;

	/** 消息标记：文件传输中，一般携带文件传输进度信息 */
	private static final int WHAT_PROGRESS = 0xa1;

	/** 消息标记：文件传输错误 */
	private static final int WHAT_IOEXCEPTION = 0xe0;

	/** usb存储路径 */
	private static final String DIRECTORY_USB = "/storage/usb";
	/** 外部存储路径 */
	private static final String DIRECTORY_EXTERNAL = "/storage/sdcard0";
	/** 外部存储路径，插入SD卡 */
	private static final String DIRECTORY_EXTERNAL_1 = "/storage/sdcard1";

	/**
	 * 获取手机存储根目录文件
	 * 
	 * @return
	 */
	public static File getExternalStorageDirecory() {
		return Environment.getExternalStorageDirectory();
	}

	/**
	 * 获取手机存储根目录路径
	 * 
	 * @return
	 */
	public static String getExternalStoragePath() {
		return getExternalStorageDirecory().getAbsolutePath();
	}

	/**
	 * 获取扩展存储路径，TF卡、U盘
	 */
	public static List<String> getAllExternalStoragePaths() {
		List<String> pathList = new ArrayList<String>();
		Runtime runtime = Runtime.getRuntime();
		Process proc = null;
		InputStream is = null;
		BufferedReader br = null;
		String line = null;
		try {
			proc = runtime.exec("mount");
			is = proc.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				if (!line.contains("storage"))
					continue;

				String columns[] = line.split(" ");
				if (columns != null && columns.length > 1) {
					pathList.add(columns[1]);
					LogUtils.printExcp(TAG, columns[1]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			proc.destroy();
		}
		return pathList;
	}

	/**
	 * 获取一个文件目录
	 * 
	 * @param directory
	 *            文件目录字符串
	 * @return 目录路径字符串，字符串尾部为：文件目录分隔符（如：“/”）
	 */
	public static String getDirectory(String directory) {
		File file = new File(directory);
		if (!file.exists())
			file.mkdirs();
		return file.getAbsolutePath() + File.separator;
	}

	/**
	 * 获取指定目录下、后缀为suffix的文件列表
	 * 
	 * @param directory
	 *            文件目录
	 * @param suffix
	 *            文件后缀（即文件格式）如（“.txt .jpg”）。为null则显示所有文件
	 * @param isOnlyFile
	 *            是否仅显示文件，不显示文件夹。true：仅显示文件；false：显示文件夹
	 * @return
	 */
	public static ArrayList<File> getListFiles(String directory,
			final String suffix, final boolean isOnlyFile) {
		File fileDirectory = new File(directory);
		FileFilter filter = new FileFilter() {// 过滤器
			@Override
			public boolean accept(File pathname) {
				if (pathname.isFile()) {
					if (suffix != null && !pathname.getName().endsWith(suffix)) {
						return false;
					}
					return true;
				}
				if (!isOnlyFile) {
					return true;
				}
				return false;
			}
		};
		File[] files = fileDirectory.listFiles(filter);// 列出目录下文件
		ArrayList<File> fileList = new ArrayList<File>();
		for (int i = 0; i < files.length; i++) {
			fileList.add(files[i]);
		}
		return fileList;
	}

	/**
	 * 判断外部存储是否可写
	 * 
	 * @return
	 */
	public static boolean isExternalStorageWritable() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	/**
	 * 获取usb存储路径
	 * 
	 * @return
	 */
	public static String getUsbStoragePath() {
		if (getUsbStorageDirectory() == null)
			return null;
		return getUsbStorageDirectory().getAbsolutePath();
	}

	/**
	 * 获取usb存储目录文件
	 * 
	 * @return
	 */
	public static File getUsbStorageDirectory() {
		File file = null;
		for (String path : getAllExternalStoragePaths()) {
			if (path.equals(DIRECTORY_USB)) {
				file = new File(path);
				break;
			}
		}
		if (isExists(file))
			return file;
		return null;
	}

	/**
	 * 判断文件路径是否存在
	 * 
	 * @param path
	 * @return
	 */
	public static boolean isExists(String path) {
		return isExists(new File(path));
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isExists(File file) {
		try {
			if (!file.exists()) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 调用文件选择器来选择文件
	 * 
	 * @param Context
	 *            发送请求的Activity
	 **/
	public static void showFileChooser(Context context) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		try {
			((Activity) context).startActivityForResult(
					Intent.createChooser(intent, "请选择一个的文件"), FILE_SELECT);
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(context, "请安装文件管理器！", 0).show();
		}
	}

}
