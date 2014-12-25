package com.lzhn.lib;

import android.content.Context;

import com.lzhn.push.BaiduPushMessageReceiver;
import com.lzhn.utils.view.ToastUtils;

public class BaiduPushReceiver extends BaiduPushMessageReceiver {
	@Override
	public void onBind(Context context, int errorCode, String appid,
			String userId, String channelId, String requestId) {
		super.onBind(context, errorCode, appid, userId, channelId, requestId);
		if (errorCode == 0) {
			ToastUtils.showToast(context, "绑定成功！");
			return;
		}
		ToastUtils.showToast(context, "绑定失败！");
	}
}
