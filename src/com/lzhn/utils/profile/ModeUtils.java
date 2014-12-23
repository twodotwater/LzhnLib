package com.lzhn.utils.profile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;

/**
 * 手机情景模式（静音、震动）管理类（广播接收器）
 * 
 * @author lzhn
 * 
 */
public class ModeUtils extends BroadcastReceiver {

	private OnRingerModeChangedListener onRingerModeChangedListener;

	public void setOnRingerModeChangedListener(
			OnRingerModeChangedListener onRingerModeChangedListener) {
		this.onRingerModeChangedListener = onRingerModeChangedListener;
	}

	public static AudioManager getAudioManager(Context context) {
		AudioManager audioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		return audioManager;
	}

	public static int getRingerMode(Context context) {

		return getAudioManager(context).getRingerMode();
	}

	/**
	 * 是否响铃
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isRingEnable(Context context) {
		return getRingerMode(context) == AudioManager.RINGER_MODE_NORMAL;
	}

	/**
	 * 是否震动
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isVibrateEnable(Context context) {
		return getRingerMode(context) != AudioManager.RINGER_MODE_SILENT;
	}

	/**
	 * 调至普通模式（铃声+震动）
	 * 
	 * @param context
	 */
	public static void setNormalMode(Context context) {
		getAudioManager(context).setRingerMode(AudioManager.RINGER_MODE_NORMAL);
	}

	/**
	 * 调至静音模式
	 * 
	 * @param context
	 */
	public static void setSilentMode(Context context) {
		getAudioManager(context).setRingerMode(AudioManager.RINGER_MODE_SILENT);
	}

	/**
	 * 调至震动模式
	 * 
	 * @param context
	 */
	public static void setVibrateMode(Context context) {
		getAudioManager(context)
				.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
	}

	public static IntentFilter getRingerModeChangedIntentFilter() {
		return new IntentFilter(AudioManager.RINGER_MODE_CHANGED_ACTION);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(AudioManager.RINGER_MODE_CHANGED_ACTION)) {
			switch (getRingerMode(context)) {
			case AudioManager.RINGER_MODE_NORMAL:
				onRingerModeChangedListener.onRingerModeNormal();
				break;
			case AudioManager.RINGER_MODE_VIBRATE:
				onRingerModeChangedListener.onRingerModeVibrate();
				break;
			case AudioManager.RINGER_MODE_SILENT:
				onRingerModeChangedListener.onRingerModeSilent();
				break;

			default:
				break;
			}
		}
	}

	/**
	 * 情景模式切换监听器
	 * 
	 * @author lzhn
	 * 
	 */
	public interface OnRingerModeChangedListener {
		/**
		 * 响铃、震动
		 */
		public void onRingerModeNormal();

		public void onRingerModeVibrate();

		/**
		 * 无响铃、无震动
		 */
		public void onRingerModeSilent();
	}
}
