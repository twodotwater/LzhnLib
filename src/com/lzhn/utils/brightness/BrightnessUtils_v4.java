package com.lzhn.utils.brightness;

import android.content.ContentResolver;
import android.provider.Settings;
import android.provider.Settings.System;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;
import android.widget.Toast;

import com.lzhn.utils.print.LogUtils;

/**
 * 调节亮度-工具类
 * 
 * @author lzhn
 * 
 */
public class BrightnessUtils_v4 {
	/**
	 * 判断是否开启了自动亮度调节
	 * 
	 * @param act
	 * @return
	 */
	public static boolean isAutoBrightness(FragmentActivity act) {
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
	 */
	public static void SetLightness(FragmentActivity act, int value) {
		try {
			System.putInt(act.getContentResolver(), System.SCREEN_BRIGHTNESS,
					value);
			WindowManager.LayoutParams lp = act.getWindow().getAttributes();
			lp.screenBrightness = (value <= 0 ? 1 : value) / 255f;
			act.getWindow().setAttributes(lp);
		} catch (Exception e) {
			LogUtils.printExcp("Light", e.getLocalizedMessage());
			Toast.makeText(act, "无法改变亮度", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 获取亮度
	 * 
	 * @param act
	 * @return
	 */
	public static int GetLightness(FragmentActivity act) {
		return System.getInt(act.getContentResolver(),
				System.SCREEN_BRIGHTNESS, -1);
	}

	/**
	 * 停止自动亮度调节
	 * 
	 * @param activity
	 */
	public static void stopAutoBrightness(FragmentActivity activity) {
		Settings.System.putInt(activity.getContentResolver(),
				Settings.System.SCREEN_BRIGHTNESS_MODE,
				Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
	}

	/**
	 * 开启亮度自动调节
	 * 
	 * @param activity
	 */
	public static void startAutoBrightness(FragmentActivity activity) {
		Settings.System.putInt(activity.getContentResolver(),
				Settings.System.SCREEN_BRIGHTNESS_MODE,
				Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
	}
}
