package com.lzhn.utils.profile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;

/**
 * 音量改变广播
 * 
 * @author lzhn
 * 
 */
public class VolumeReceiver extends BroadcastReceiver {

	public static final String VOLUME_CHANGED_ACTION = "android.media.VOLUME_CHANGED_ACTION";

	public static final String EXTRA_VOLUME_STREAM_TYPE = "android.media.EXTRA_VOLUME_STREAM_TYPE";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO 音量改变
		String action = intent.getAction();
		if (action.equals(VOLUME_CHANGED_ACTION)) {
			int type = intent.getIntExtra(EXTRA_VOLUME_STREAM_TYPE,
					AudioManager.STREAM_VOICE_CALL);
			// ToastUtils.showToast(context, "音量改变！" + type);
			if (type == AudioManager.STREAM_RING) {
				// new Thread(new Runnable() {
				// @Override
				// public void run() {
				// Instrumentation inst = new Instrumentation();
				// inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
				// }
				// }).start();

				// try {
				// Runtime.getRuntime().exec(
				// "input keyevent " + KeyEvent.KEYCODE_BACK);
				// } catch (IOException e) {
				// e.printStackTrace();
				// }
			}
		}
	}

	public static IntentFilter getVolumeChangedIntentFilter() {
		return new IntentFilter(VOLUME_CHANGED_ACTION);
	}
}
