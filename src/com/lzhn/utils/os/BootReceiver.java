package com.lzhn.utils.os;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 开机启动广播接收器
 * 
 * @author lzhn
 * 
 */
public class BootReceiver extends BroadcastReceiver {
	/** 开机启动action字符串 */
	private static final String ACTION_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";
	/** 启动应用action字符串 */
	private static final String ACTION_START_LAUNCHER_ACTIVITY = "android.intent.action.startLauncherActivity";
	private static Class<?> cls;

	/**
	 * 开机启动的Activity的类型
	 * 
	 * @param cls
	 */
	public static void setCls(Class<?> cls) {
		BootReceiver.cls = cls;
	}

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getAction().equals(ACTION_BOOT_COMPLETED)) {
			Intent intent2 = null;
			if (cls != null) {
				intent2 = new Intent(context, cls);
			} else {
				intent2 = new Intent();
				intent2.setAction(ACTION_START_LAUNCHER_ACTIVITY);
				intent2.addCategory("android.intent.category.LAUNCHER");
				intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			}
			context.startActivity(intent2);
		}
	}

}
