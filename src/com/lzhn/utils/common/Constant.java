package com.lzhn.utils.common;

public class Constant {
	/** 资源类型 */
	public static final String RES_ID = "id";
	public static final String RES_LAYOUT = "layout";
	public static final String RES_DRAWABLE = "drawable";

	/** 应用是否安装或覆盖安装的类型 */
	public static final int INSTALLED_NOT = 0; // 未安装
	public static final int INSTALLED_NEW = 1; // 已安装且为新版本
	public static final int INATALLED_OLD = 2; // 已安装但为旧版本
}
