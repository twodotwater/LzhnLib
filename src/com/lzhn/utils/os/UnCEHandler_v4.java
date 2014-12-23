package com.lzhn.utils.os;

import java.lang.Thread.UncaughtExceptionHandler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

/**
 * 处理程序运行时异常：重启程序
 */
public class UnCEHandler_v4 implements UncaughtExceptionHandler {

	public static final String TAG = "UnCEHandler";
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	private CatchExcepApplication_v4 application;
	private Class<?> cls;

	public UnCEHandler_v4(CatchExcepApplication_v4 application) {
		// 获取系统默认的UncaughtException处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		this.application = application;
	}

	/**
	 * 
	 * @param application
	 * @param cls
	 *            重启的activity类型class
	 */
	public UnCEHandler_v4(CatchExcepApplication_v4 application, Class<?> cls) {
		this(application);
		this.cls = cls;
	}

	public void setCls(Class<?> cls) {
		this.cls = cls;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				Log.e(TAG, "--error : ", e);
			}
			Intent intent = new Intent(application.getApplicationContext(), cls);
			PendingIntent restartIntent = PendingIntent.getActivity(
					application.getApplicationContext(), 0, intent,
					Intent.FLAG_ACTIVITY_NEW_TASK);
			// 退出程序
			AlarmManager mgr = (AlarmManager) application
					.getSystemService(Context.ALARM_SERVICE);
			mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
					restartIntent); // 1秒钟后重启应用
			application.finishActivity();
		}
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 * 
	 * @param ex
	 * @return true:如果处理了该异常信息; 否则返回false.
	 */
	private boolean handleException(final Throwable ex) {
		if (ex == null) {
			return false;
		}
		// 使用Toast来显示异常信息
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				// Log.e(TAG, "--" + ex.getMessage());
				Toast.makeText(application.getApplicationContext(), "程序重新启动!",
						Toast.LENGTH_SHORT).show();
				Looper.loop();
			}
		}.start();
		return true;
	}
}
