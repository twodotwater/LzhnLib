package com.lzhn.utils.common;

import android.content.Context;

public class Resource {
	private static Resource resource;
	private static Context context;
	private static String packageName;

	private Resource(Context context) {
		Resource.context = context;
		Resource.packageName = context.getPackageName();
	}

	public static Resource getInstance(Context context) {
		if (resource == null)
			resource = new Resource(context);
		return resource;
	}

	/**
	 * 获取资源文件的唯一标示<br/>
	 * {@link android.content.res.Resources#getIdentifier(String, String, String)}
	 * 
	 * @param name
	 *            资源名称：如：@+id/textView_name
	 * @param defType
	 *            资源类型：如：id/layout/drawable等
	 * @return
	 */
	public int getIdentifier(String name, String defType) {
		return context.getResources().getIdentifier(name, defType, packageName);
	}

	/**
	 * 类型为id的资源标示
	 * 
	 * @param name
	 * @return
	 */
	public int getIdIdentifier(String name) {
		return getIdentifier(name, Constant.RES_ID);
	}

	/**
	 * 类型为layout的资源标示
	 * 
	 * @param name
	 * @return
	 */
	public int getLayoutIdentifier(String name) {
		return getIdentifier(name, Constant.RES_LAYOUT);
	}

	/**
	 * 类型为drawable的资源标示
	 * 
	 * @param name
	 * @return
	 */
	public int getDrawableIdentifier(String name) {
		return getIdentifier(name, Constant.RES_DRAWABLE);
	}
}
