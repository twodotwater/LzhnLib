package com.lzhn.push;

import android.app.Notification;
import android.content.Context;
import android.net.Uri;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;

public class BaiduPushUtils {

	/** redirect uri 值为"oob" */
	public static final String REDIRECT = "oob";

	/** 开发中心 */
	public static final String DEV_CENTER = "https://openapi.baidu.com/";

	public static final String RESPONSE_METHOD = "method";
	public static final String RESPONSE_CONTENT = "content";
	public static final String RESPONSE_ERRCODE = "errcode";
	public static final String ACTION_RESPONSE = "bccsclient.action.RESPONSE";
	public static final String ACTION_SHOW_MESSAGE = "bccsclient.action.SHOW_MESSAGE";
	protected static final String EXTRA_ACCESS_TOKEN = "access_token";
	public static final String EXTRA_MESSAGE = "message";

	/**
	 * 获取请求Access token的网址
	 * 
	 * @param apiKey
	 * @return
	 */
	public static String getBaiduAccessTockenUrl(String apiKey) {
		String url = DEV_CENTER + "oauth/2.0/authorize?"
				+ "response_type=token" + "&client_id=" + apiKey
				+ "&redirect_uri=" + REDIRECT + "&display=mobile";
		return url;
	}

	/**
	 * 从百度账号绑定认证通过后重定向的URL中获取Access token
	 * 
	 * @param url
	 * @return
	 */
	public static String getBaiduAccessTocken(String url) {
		String accessToken = null;
		if (url.startsWith(REDIRECT) || url.contains("login_success")) {

			// change # to ?
			int fragmentIndex = url.indexOf("#");
			url = "http://localhost/?" + url.substring(fragmentIndex + 1);

			// 从URL中获得Access token
			accessToken = Uri.parse(url).getQueryParameter("access_token");
		}
		return accessToken;
	}

	/**
	 * 无账号初始化，用api key绑定
	 * 
	 * @param context
	 * @param apiKey
	 */
	public static void initWithApiKey(Context context, String apiKey) {
		PushManager.startWork(context.getApplicationContext(),
				PushConstants.LOGIN_TYPE_API_KEY, apiKey);
	}

	/**
	 * 以百度账号登陆，获取access token来绑定
	 * 
	 * @param isLogin
	 * @param context
	 */
	public static void initWithBaiduAccount(boolean isLogin, Context context) {
		if (isLogin) {
			// 已登录则清除Cookie, access token, 设置登录按钮
			CookieSyncManager.createInstance(context.getApplicationContext());
			CookieManager.getInstance().removeAllCookie();
			CookieSyncManager.getInstance().sync();
			isLogin = false;
		}
		// // 跳转到百度账号登录的activity
		// Intent intent = new Intent(context, LoginActivity.class);
		// startActivity(intent);
	}

	/**
	 * 设置自定义的通知样式。使用系统默认的可以不加这段代码！<br/>
	 * <br/>
	 * 请在通知推送界面中，高级设置->通知栏样式->自定义样式，选中并且填写值：1<br/>
	 * 与下方代码中 PushManager.setNotificationBuilder(this, 1, cBuilder)中的第二个参数对应
	 * 
	 * @param context
	 * @param layoutResId
	 *            自定义通知布局id
	 * @param iconViewId
	 *            显示图标的组件id
	 * @param titleViewId
	 *            显示标题文字的组件id
	 * @param msgViewId
	 *            显示通知内容文字的组件id
	 * @param notificationIconId
	 *            通知显示在手机状态栏的图标资源id
	 */
	public static void setNotificationBuilder(Context context, int layoutResId,
			int iconViewId, int titleViewId, int msgViewId,
			int notificationIconId) {
		CustomPushNotificationBuilder cBuilder = new CustomPushNotificationBuilder(
				context.getApplicationContext(), layoutResId, iconViewId,
				titleViewId, msgViewId);
		cBuilder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
		cBuilder.setNotificationDefaults(Notification.DEFAULT_SOUND
				| Notification.DEFAULT_VIBRATE);
		cBuilder.setStatusbarIcon(context.getApplicationInfo().icon);
		cBuilder.setLayoutDrawable(notificationIconId);
		PushManager.setNotificationBuilder(context, 1, cBuilder);
	}
}
