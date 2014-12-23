package com.lzhn.utils.view;

import android.content.Context;
import android.widget.Toast;

/**
 * toast提示
 * 
 * @author zzha
 * 
 */
public class ToastUtils {
	/**
	 * 短时间显示
	 * 
	 * @param context
	 * @param text
	 */
	public static void showToast(Context context, CharSequence text) {
		Toast.makeText(context, text, 0).show();
	}

	/**
	 * 长时间显示
	 * 
	 * @param context
	 * @param text
	 */
	public static void showLongToast(Context context, CharSequence text) {
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}
}
