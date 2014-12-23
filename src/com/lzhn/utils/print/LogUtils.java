package com.lzhn.utils.print;

import android.util.Log;

/**
 * 打印log
 * 
 * @author lzhn
 * 
 */
public class LogUtils {
	/**
	 * 打印一般信息
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void printInfo(String tag, Object msg) {
		Log.i(tag, "-->" + msg);
	}

	public static void printInfo(String tag, Object... msg) {
		for (Object obj : msg)
			Log.i(tag, "-->" + obj);
	}

	/**
	 * 打印异常信息
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void printExcp(String tag, Object msg) {
		Log.e(tag, "-->" + msg);
	}

	public static void printExcp(String tag, Object... msg) {
		for (Object obj : msg)
			Log.e(tag, "-->" + obj);
	}

	public static void printExcp(Object obj) {
		System.err.println(obj);
	}

	public static void print(Object obj) {
		System.out.print(obj + " ; ");
	}

	public static void println(Object obj) {
		System.out.println(obj);
	}
}
