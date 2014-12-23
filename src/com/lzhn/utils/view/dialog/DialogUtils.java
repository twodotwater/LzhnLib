package com.lzhn.utils.view.dialog;

import java.lang.reflect.Field;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;

import com.lzhn.utils.view.dialog.MyDialog.OnButtonClickListener;

/**
 * 提示对话框
 * 
 * @author lzhn
 * 
 */
public class DialogUtils {

	private static final String TAG = "DialogUtils";

	private DialogUtils() {
		super();
	}

	/**
	 * 取消对话框
	 * 
	 * @param dialog
	 * @param dismiss
	 *            true:取消；false:不取消
	 */
	private static void dismissDialog(DialogInterface dialog, boolean dismiss) {
		try {
			Field field = dialog.getClass().getSuperclass()
					.getDeclaredField("mShowing");
			field.setAccessible(true);
			// 设置mShowing值，欺骗android系统
			field.set(dialog, dismiss);
		} catch (Exception e) {
			Log.e(TAG, e.getLocalizedMessage());
		}
	}

	/**
	 * 显示提示信息对话框
	 * 
	 * @param context
	 * @param msg
	 *            提示信息
	 * @param onBtnClickListener
	 */
	public static void showMessageDialog(Context context, String msg,
			OnButtonClickListener onBtnClickListener) {
		MyDialog dialog = MyDialog.newInstance(context, MyDialog.MODE_MESSAGE);
		dialog.setMessage(msg).setOnBtnClickListener(onBtnClickListener).show();
	}

	/**
	 * 显示文本输入框
	 * 
	 * @param context
	 * @param title
	 *            标题
	 * @param inputText
	 *            输入框初始显示文本
	 * @param onBtnClickListener
	 */
	public static void showInputDialog(Context context, String title,
			String inputText, OnButtonClickListener onBtnClickListener) {
		MyDialog dialog = MyDialog.newInstance(context, MyDialog.MODE_INPUT);
		if (title != null) {
			dialog.setTitle(title);
		}
		dialog.setInputText(inputText)
				.setOnBtnClickListener(onBtnClickListener).show();

	}

	/**
	 * 显示自定义布局view的对话框
	 * 
	 * @param context
	 * @param title
	 *            标题
	 * @param positiveText
	 *            确认按钮显示文字
	 * @param childView
	 *            添加到对话框的自定义view
	 * @param onBtnClickListener
	 */
	public static void showViewDialog(Context context, String title,
			String positiveText, View childView,
			OnButtonClickListener onBtnClickListener) {
		MyDialog dialog = MyDialog.newInstance(context, MyDialog.MODE_VIEW);
		if (title != null) {
			dialog.setTitle(title);
		}
		if (positiveText != null) {
			dialog.setPositiveText(positiveText);
		}
		dialog.setChildView(childView)
				.setOnBtnClickListener(onBtnClickListener).show();
	}
}
