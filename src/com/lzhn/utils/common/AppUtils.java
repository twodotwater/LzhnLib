package com.lzhn.utils.common;

import java.io.File;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;

public class AppUtils {

	/**
	 * 判断应用是否安装或者是否为最新版本
	 * 
	 * @param packageName
	 *            目标应用安装后的包名
	 * @param versionCode
	 *            指定的应用版本号
	 * @return 安装的类型
	 */
	public static int isAppInstalled(Context context, String packageName,
			int versionCode) {
		// 判断是否安装
		if (new File("/data/data/" + packageName).exists()) {
			// 获取系统中安装的所有应用包名集合
			List<PackageInfo> packages = context.getPackageManager()
					.getInstalledPackages(0);
			for (int i = 0; i < packages.size(); i++) {
				PackageInfo packageInfo = packages.get(i);
				// 找出指定的应用
				if (packageName.equals(packageInfo.packageName)) {
					if (packageInfo.versionCode >= versionCode) {
						return Constant.INSTALLED_NEW;
					} else {
						return Constant.INATALLED_OLD;
					}
				}
			}
		}
		return Constant.INSTALLED_NOT;
	}

	/**
	 * 获取manifest文件中的metaValue <br/>
	 * <b>例：<br/>
	 * 在百度开发者中心查询应用的API Key :</b><br/>
	 * meta-data android:name="api_key" android:value="8ctyZLQbniuamGmwrsObVi5b"
	 * 
	 * @param context
	 * @param metaKey
	 * @return
	 */
	public static String getMetaValue(Context context, String metaKey) {
		Bundle metaData = null;
		String apiKey = null;
		if (context == null || metaKey == null) {
			return null;
		}
		try {
			ApplicationInfo ai = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			if (null != ai) {
				metaData = ai.metaData;
			}
			if (null != metaData) {
				apiKey = metaData.getString(metaKey);
			}
		} catch (NameNotFoundException e) {

		}
		return apiKey;
	}

	/**
	 * 启动手机其他应用
	 * 
	 * @param context
	 * @param pkg
	 *            应用包名
	 * @param cls
	 *            应用中的组件名称：一般是MainActivity的全名
	 */
	public static void launchOtherApp(Context context, String pkg, String cls) {
		Intent intent = new Intent();
		intent.setComponent(new ComponentName(pkg, cls));
		context.startActivity(intent);
	}

	/**
	 * 启动手机其他应用
	 * 
	 * @param context
	 * @param packageName
	 *            应用包名
	 */
	public static void launchOtherApp(Context context, String packageName) {
		Intent intent = context.getPackageManager().getLaunchIntentForPackage(
				packageName);
		context.startActivity(intent);
	}

	/**
	 * 安装应用
	 * 
	 * @param context
	 * @param path
	 *            应用apk文件路径
	 */
	public static void installApp(Context context, String path) {
		Intent intent = new Intent();
		// 设置目标应用安装包路径
		intent.setDataAndType(Uri.fromFile(new File(path)),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	/**
	 * 卸载应用
	 * 
	 * @param context
	 * @param packageName
	 *            应用包名
	 */
	public static void uninstallApp(Context context, String packageName) {
		Uri uri = Uri.fromParts("package", packageName, null);
		Intent intent = new Intent(Intent.ACTION_DELETE, uri);
		context.startActivity(intent);
	}
}
