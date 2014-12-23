package com.lzhn.utils.profile;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

import com.lzhn.lib.R;
import com.lzhn.utils.print.LogUtils;

/**
 * 多媒体播放工具类
 * 
 * @author lzhn
 * 
 */
public class MediaUtils {

	private static final String TAG = "MediaUtils";
	/** 是否重复播放 */
	private static boolean isRepeat = false;
	/** 上一次停止播放时间 */
	private static long preTime;
	private static MediaPlayer mediaPlayer;

	public static long getPreTime() {
		return preTime;
	}

	public static void setPreTime(long preTime) {
		MediaUtils.preTime = preTime;
	}

	public static void setPreTime() {
		preTime = System.currentTimeMillis();
	}

	public static AudioManager getAudioManager(Context context) {
		return (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
	}

	private static void initMedia(Context context, int resource) {

		AudioManager audioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		// 调至普通模式（铃声+震动）
		audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		int volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		// LogUtils.printExcp(TAG, volume + "  " + max);// max = 15
		if (volume < max / 5) {
			volume = max / 5;
			audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume,
					AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
		}

		if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) {
			mediaPlayer = MediaPlayer.create(context, resource);
			// 另一种设置播放器资源的方式
			// AssetFileDescriptor descriptor =
			// context.getResources().openRawResourceFd(resource);
			// mediaPlayer.setDataSource(descriptor.getFileDescriptor());
			if (mediaPlayer == null) {
				return;
			}
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			// mediaPlayer.setVolume(0.6f, 0.6f);
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					if (isRepeat) {
						mp.seekTo(0);
						mp.start();
					} else {
						mp.release();
					}
				}
			});

			try {
				// mediaPlayer.prepare();
				mediaPlayer.start();
			} catch (Exception e) {
				mediaPlayer = null;
				LogUtils.printExcp(TAG, e.getLocalizedMessage());
			}
		}
	}

	/**
	 * 播放ok提示音
	 * 
	 * @param context
	 */
	public static void playOKRing(Context context) {
		initMedia(context, R.raw.ok);
	}

	/**
	 * 播放一次报警提示音{@link #playWarningRing(Context, boolean)}
	 * 
	 * @param context
	 */
	public static void playWarningRing(Context context) {
		playWarningRing(context, false);
	}

	/**
	 * 播放报警提示音
	 * 
	 * @param context
	 * @param repeat
	 *            是否重复播放。true：重复播放；false：仅播放一次
	 */
	public static void playWarningRing(Context context, boolean repeat) {
		isRepeat = repeat;
		if (isMediaPlayerPlaying()) {
			return;
		}
		initMedia(context, R.raw.warning);
	}

	/**
	 * 停止声音播放
	 */
	public static void stopRing() {
		isRepeat = false;
		setPreTime();
		if (isMediaPlayerPlaying()) {
			mediaPlayer.stop();
		}
	}

	/**
	 * 判断播放器是否正在播放声音
	 * 
	 * @return
	 */
	public static boolean isMediaPlayerPlaying() {
		try {
			if (mediaPlayer != null && mediaPlayer.isPlaying()) {
				return true;
			}
		} catch (Exception e) {
			LogUtils.printExcp(TAG, e.getLocalizedMessage());
		}
		return false;
	}

}
