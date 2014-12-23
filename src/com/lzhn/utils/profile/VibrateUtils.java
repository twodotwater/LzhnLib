package com.lzhn.utils.profile;

import android.content.Context;
import android.os.Vibrator;

/**
 * 手机震动管理
 * 
 * @author lzhn
 * 
 */
public class VibrateUtils {

	private static Vibrator vibrator;

	public static Vibrator getVibrator(Context context) {
		return vibrator == null ? vibrator = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE) : vibrator;
	}

	/**
	 * 震动
	 * 
	 * @param context
	 * @param milliseconds
	 *            时间长度（毫秒）
	 */
	public static void vibrate(Context context, long milliseconds) {
		getVibrator(context);
		vibrator.vibrate(milliseconds);
	}

	/**
	 * 震动：等待一定时间后震动一段时间<br/>
	 * {@link android.os.Vibrator#vibrate(long[], int)}
	 * 
	 * @param context
	 * @param pattern
	 *            例：long[]{1000,2000} = 等待1秒后震动2秒
	 * @param repeat
	 */
	public static void vibrate(Context context, long[] pattern, int repeat) {
		getVibrator(context);
		vibrator.vibrate(pattern, repeat);
	}
}
