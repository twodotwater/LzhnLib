package com.lzhn.utils.view;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Spinner;

/**
 * 组件View辅助工具类
 * 
 * @author lzhn
 * 
 */
public class ViewUtils {

	private static final String TAG = "ViewUtils";

	/**
	 * 指定Spinner选择item位置
	 * 
	 * @param spinner
	 * @param str
	 *            某item文本
	 * @return 选择item的位置
	 */
	public static int setSpinnerSelectedItem(Spinner spinner, String str) {
		int count = spinner.getCount();
		int position = 0;
		for (int i = 0; i < count; i++) {
			if (spinner.getItemAtPosition(i).toString().equals(str)) {
				position = i;
				spinner.setSelection(position);
				return position;
			}
		}
		return spinner.getSelectedItemPosition();
	}

	/**
	 * 指定View是否显示
	 * 
	 * @param v
	 * @param visibility
	 *            true：显示；false：隐藏
	 */
	public static void setViewVisibility(View v, boolean visibility) {
		v.setVisibility(visibility ? View.VISIBLE : View.GONE);
	}

	/**
	 * 获取手机屏幕显示尺寸信息{@link android.util.DisplayMetrics}
	 * 
	 * @param context
	 * @return
	 */
	public static DisplayMetrics getWindowDisplayMetrics(Context context) {
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(metrics);
		return metrics;
	}
}
