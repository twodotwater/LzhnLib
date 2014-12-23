package com.lzhn.utils.battery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

/**
 * 手机电量管理类（广播接收器）
 * 
 * @author lzhn
 * 
 */
public class BatteryUtils extends BroadcastReceiver {
	private OnReceiveBatteryListener onReceiveBatteryListener;

	public void setOnReceiveBatteryListener(
			OnReceiveBatteryListener onReceiveBatteryListener) {
		this.onReceiveBatteryListener = onReceiveBatteryListener;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action != null && action.equals(Intent.ACTION_BATTERY_CHANGED)) {
			int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
			int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
			int battery = (level * 100) / scale;
			if (onReceiveBatteryListener != null) {
				onReceiveBatteryListener.onReceiveBattery(battery);
			}
		}
	}

	public static IntentFilter getBatteryChangedIntentFilter() {
		return new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
	}

	/**
	 * 接收电量值的监听器
	 * 
	 * @author lzhn
	 * 
	 */
	public interface OnReceiveBatteryListener {
		/**
		 * 接收到电量百分比
		 * 
		 * @param battery
		 *            电量百分比
		 */
		void onReceiveBattery(int battery);
	}
}
