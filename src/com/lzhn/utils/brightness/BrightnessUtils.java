package com.lzhn.utils.brightness;

import android.app.Activity;
import android.content.ContentResolver;
import android.provider.Settings;
import android.provider.Settings.System;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * 调节亮度-工具类
 * 
 * @author lzhn
 * 
 */
public class BrightnessUtils {
	/**
	 * 判断是否开启了自动亮度调节
	 * 
	 * @param act
	 * @return
	 */
	public static boolean isAutoBrightness(Activity act) {
		boolean automicBrightness = false;
		ContentResolver aContentResolver = act.getContentResolver();
		try {
			automicBrightness = Settings.System.getInt(aContentResolver,
					Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
		} catch (Exception e) {
			Toast.makeText(act, "无法获取亮度", Toast.LENGTH_SHORT).show();
		}
		return automicBrightness;
	}

	/**
	 * 改变亮度
	 * 
	 * @param act
	 * @param value
	 *            [0,255]
	 *            {@link android.view.WindowManager.LayoutParams#screenBrightness}
	 */
	public static void SetLightness(Activity act, int value) {
		try {
			System.putInt(act.getContentResolver(), System.SCREEN_BRIGHTNESS,
					value);
			WindowManager.LayoutParams lp = act.getWindow().getAttributes();
			if (value < 0)
				value = 0;
			else if (value > 255)
				value = 255;
			lp.screenBrightness = value / 255f;
			act.getWindow().setAttributes(lp);
		} catch (Exception e) {
			Toast.makeText(act, "无法改变亮度", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 获取亮度
	 * 
	 * @param act
	 * @return
	 */
	public static int GetLightness(Activity act) {
		return System.getInt(act.getContentResolver(),
				System.SCREEN_BRIGHTNESS, -1);
	}

	/**
	 * 停止自动亮度调节
	 * 
	 * @param activity
	 */
	public static void stopAutoBrightness(Activity activity) {
		Settings.System.putInt(activity.getContentResolver(),
				Settings.System.SCREEN_BRIGHTNESS_MODE,
				Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
	}

	/**
	 * 开启亮度自动调节
	 * 
	 * @param activity
	 */
	public static void startAutoBrightness(Activity activity) {
		Settings.System.putInt(activity.getContentResolver(),
				Settings.System.SCREEN_BRIGHTNESS_MODE,
				Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
	}
}
