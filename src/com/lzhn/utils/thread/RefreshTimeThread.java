package com.lzhn.utils.thread;

import android.os.Handler;

import com.lzhn.utils.print.FormatUtils;

/**
 * 刷新时间线程--时钟功能
 * 
 * @author lzhn
 * 
 */
public class RefreshTimeThread extends Thread {

	public static final int WHAT_TIME = 0xf5;
	private Handler mHandler;
	private boolean isRunning = true;
	private String pattern = FormatUtils.PATTERN_3;

	/**
	 * 
	 * @param handler
	 * @param pattern
	 *            设定时间显示字符串格式 {@link com.lzhn.utils.print.FormatUtils#PATTERN_3}
	 */
	public RefreshTimeThread(Handler handler, String pattern) {
		this.mHandler = handler;
		setPattern(pattern);
	}

	/**
	 * 构造函数。默认返回时间格式： {@link com.lzhn.utils.print.FormatUtils#PATTERN_3}
	 * 
	 * @param handler
	 */
	public RefreshTimeThread(Handler handler) {
		this(handler, null);
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		if (pattern == null || pattern.isEmpty()) {
			return;
		}
		this.pattern = pattern;
	}

	@Override
	public void run() {
		super.run();
		while (isRunning) {
			mHandler.obtainMessage(WHAT_TIME,
					FormatUtils.formatDate(pattern, System.currentTimeMillis()))
					.sendToTarget();
			ThreadUtils.sleep(1000);
		}

	}

	public void stopSelf() {
		isRunning = false;
	}
}
