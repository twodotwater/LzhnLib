package com.lzhn.map.baidu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.baidu.mapapi.SDKInitializer;
import com.lzhn.utils.view.ToastUtils;

/**
 * 百度SDK初始化广播接受者
 * 
 * @author lzhn
 * 
 */
public class BaiduSdkReceiver extends BroadcastReceiver {
	private OnBaiduSdkInitErrorListener onBaiduSdkInitErrorListener;

	public void setOnBaiduSdkInitErrorListener(
			OnBaiduSdkInitErrorListener onBaiduSdkInitErrorListener) {
		this.onBaiduSdkInitErrorListener = onBaiduSdkInitErrorListener;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR
				.equals(action)) {
			// key 验证出错!
			ToastUtils.showToast(context, "开发秘钥验证出错!");
			if (onBaiduSdkInitErrorListener != null) {
				onBaiduSdkInitErrorListener.OnPermissionCheckError();
			}
		} else if (SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR
				.equals(action)) {
			ToastUtils.showToast(context, "网络出错，请检查网络连接!");
			// 网络故障
			if (onBaiduSdkInitErrorListener != null) {
				onBaiduSdkInitErrorListener.OnNetworkCheckError();
			}
		}
	}

	/**
	 * 百度SDK初始化广播过滤器
	 * 
	 * @return
	 */
	public static IntentFilter getBaiduSdkInitIntentFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
		filter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
		return filter;
	}

	/**
	 * 百度SDK初始化失败监听器
	 * 
	 * @author lzhn
	 * 
	 */
	interface OnBaiduSdkInitErrorListener {
		/**
		 * 开发秘钥验证出错
		 */
		public void OnPermissionCheckError();

		/**
		 * 网络连接出错
		 */
		public void OnNetworkCheckError();
	}
}
