package com.lzhn.utils.os;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

/**
 * 调用系统设置项
 * 
 * @author lzhn
 * 
 */
public class SettingsUtils {

	public static final String ACTION_VIEW = "android.intent.action.VIEW";

	private static Context context;
	private static SettingsUtils settingsUtils;

	private SettingsUtils() {

	}

	public static SettingsUtils getInstance(Context context) {
		if (settingsUtils == null) {
			settingsUtils = new SettingsUtils();
			SettingsUtils.context = context;
		}
		return settingsUtils;
	}

	/**
	 * 调用系统设置：{@link android.provider.Settings#ACTION_SETTINGS}
	 * 
	 * @param context
	 * @param settings
	 */
	public void startIntent(String settings) {
		Intent intent = new Intent();
		intent.setAction(settings);
		context.startActivity(intent);
	}

	public void setDate() {
		startIntent(Settings.ACTION_DATE_SETTINGS);
	}

	public void setWifi() {
		startIntent(Settings.ACTION_WIFI_SETTINGS);
	}

	public void setNetwork() {
		startIntent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
	}

	public void setLocation() {
		startIntent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	}

	public void setBluetooth() {
		startIntent(Settings.ACTION_BLUETOOTH_SETTINGS);
	}

	public void setSound() {
		startIntent(Settings.ACTION_SOUND_SETTINGS);
	}

	public void setDisplay() {
		startIntent(Settings.ACTION_DISPLAY_SETTINGS);
	}
}
