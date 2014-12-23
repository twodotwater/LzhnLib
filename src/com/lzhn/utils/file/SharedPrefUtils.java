package com.lzhn.utils.file;

import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * SharedPreferences的一个工具类
 * 
 * @author lzhn
 * 
 */
public class SharedPrefUtils {
	private static final String TAG = "SharedPrefUtils";

	/** 默认SharedPreferences文件名 */
	public static final String PREF_FILE = "sharedPrefs";

	/**
	 * 保存数据
	 * 
	 * @param context
	 * @param key
	 *            键
	 * @param object
	 *            值
	 */
	@SuppressWarnings("unchecked")
	public static void setValueByKey(Context context, String key, Object object) {
		String type = object.getClass().getSimpleName();
		SharedPreferences sp = context.getSharedPreferences(PREF_FILE,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();

		if ("String".equals(type)) {
			editor.putString(key, (String) object);
		} else if ("Integer".equals(type)) {
			editor.putInt(key, (Integer) object);
		} else if ("Boolean".equals(type)) {
			editor.putBoolean(key, (Boolean) object);
		} else if ("Float".equals(type)) {
			editor.putFloat(key, (Float) object);
		} else if ("Long".equals(type)) {
			editor.putLong(key, (Long) object);
		}

		if (object instanceof Set<?>) {
			editor.putStringSet(key, (Set<String>) object);
		}

		editor.commit();
	}

	/**
	 * 得到保存的数据
	 * 
	 * @param context
	 * @param key
	 * @param defaultObject
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Object getValueByKey(Context context, String key,
			Object defaultObject) {
		try {
			String type = defaultObject.getClass().getSimpleName();
			SharedPreferences sp = context.getSharedPreferences(PREF_FILE,
					Context.MODE_PRIVATE);
			if ("String".equals(type)) {
				return sp.getString(key, (String) defaultObject);
			} else if ("Integer".equals(type)) {
				return sp.getInt(key, (Integer) defaultObject);
			} else if ("Boolean".equals(type)) {
				return sp.getBoolean(key, (Boolean) defaultObject);
			} else if ("Float".equals(type)) {
				return sp.getFloat(key, (Float) defaultObject);
			} else if ("Long".equals(type)) {
				return sp.getLong(key, (Long) defaultObject);
			}

			if (defaultObject instanceof Set<?>) {
				return sp.getStringSet(key, (Set<String>) defaultObject);
			}
		} catch (Exception e) {
			Log.e(TAG, e.getLocalizedMessage());
		}
		return null;
	}
}
