package com.lzhn.push;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.lzhn.utils.print.LogUtils;

/**
 * 百度推送辅助工具类
 * 
 * @author lzhn
 * 
 */
public class BaiduPushUtils {
	protected static final String TAG = "BaiduPushUtils";

	public static final String API_KEY = "8ctyZLQbniuamGmwrsObVi5b";

	/** redirect uri 值为"oob" */
	public static final String REDIRECT = "oob";

	/** 开发中心 */
	public static final String DEV_CENTER = "https://openapi.baidu.com/";

	public static final String RESPONSE_METHOD = "method";
	public static final String RESPONSE_CONTENT = "content";
	public static final String RESPONSE_ERRCODE = "errcode";
	public static final String ACTION_RESPONSE = "bccsclient.action.RESPONSE";
	public static final String ACTION_SHOW_MESSAGE = "bccsclient.action.SHOW_MESSAGE";
	public static final String EXTRA_ACCESS_TOKEN = "access_token";
	public static final String EXTRA_MESSAGE = "message";

	public static final String ACTION_LOGIN = "com.lzhn.push.action.LOGIN";
	public static final String EXTRA_OTHER_ACTIVITY_CLASS_NAME = "com.lzhn.push.intent.EXTRA_OTHER_ACTIVITY_CLASS_NAME";

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
		LogUtils.printExcp(TAG, "无账号绑定");
		PushManager.startWork(context.getApplicationContext(),
				PushConstants.LOGIN_TYPE_API_KEY, apiKey);
	}

	/**
	 * 百度账号登陆，使用access token来绑定
	 * 
	 * @param context
	 * @param accessToken
	 *            百度账号登陆认证码。通过百度账号请求获取，请登陆百度账号：
	 *            {@link BaiduPushUtils#startBaiduLoginActivity(boolean, Context, Class)}
	 */
	public static void initWithBaiduAccount(Context context, String accessToken) {
		LogUtils.printExcp(TAG, "百度账号绑定");
		PushManager.startWork(context.getApplicationContext(),
				PushConstants.LOGIN_TYPE_ACCESS_TOKEN, accessToken);
	}

	/**
	 * 启动百度登陆Activity，获取access token来绑定百度账号
	 * 
	 * @param isLogin
	 *            <li>true：已登录则清除Cookie, access token后登陆；<li>
	 *            false：直接百度账号登陆
	 * @param context
	 * @param className
	 *            获取access token后，从BaiduLoginActivity返回的Activity的类名
	 * @return 是否已使用百度账号登陆
	 */
	public static boolean startBaiduLoginActivity(boolean isLogin,
			Context context, String className) {
		LogUtils.printExcp(TAG, "启动百度登陆");
		if (isLogin) {
			// 已登录则清除Cookie, access token, 设置登录按钮
			CookieSyncManager.createInstance(context.getApplicationContext());
			CookieManager.getInstance().removeAllCookie();
			CookieSyncManager.getInstance().sync();
			isLogin = false;
		}
		// 跳转到百度账号登录的activity
		Intent intent = new Intent(context, BaiduLoginActivity.class);
		intent.putExtra(EXTRA_OTHER_ACTIVITY_CLASS_NAME, className);
		context.startActivity(intent);
		return isLogin;
	}

	/**
	 * 设置Webview的WebviewClient
	 * 
	 * @param context
	 * @param webview
	 *            webview
	 * @param cls
	 *            获取access token后转回的Activity
	 */
	public static void initWebView(final Context context, WebView webview,
			final OnGetAccessTokenListener onGetAccessTokenListener) {
		WebSettings settings = webview.getSettings();
		settings.setJavaScriptEnabled(true);
		// 设置Webview的WebviewClient
		webview.setWebViewClient(new WebViewClient() {

			@Override
			public void onReceivedSslError(WebView view,
					SslErrorHandler handler, SslError error) {
				handler.proceed();
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return super.shouldOverrideUrlLoading(view, url);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				String accessToken = getBaiduAccessTocken(url);
				if (accessToken != null && onGetAccessTokenListener != null) {
					onGetAccessTokenListener.onGetAccessTocken(accessToken);
				}
			}

		});
		webview.loadUrl(getBaiduAccessTockenUrl(API_KEY));
	}

	/**
	 * 打开富媒体列表界面，该界面由百度推送SDK提供
	 * 
	 * @param context
	 */
	public static void startRichMediaListActivity(Context context) {
		Intent sendIntent = new Intent();
		sendIntent.setClassName(context,
				"com.baidu.android.pushservice.richmedia.MediaListActivity");
		sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(sendIntent);
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

	/**
	 * 网络请求获取百度accesstoken成功监听器
	 * 
	 * @author zzha
	 * 
	 */
	interface OnGetAccessTokenListener {
		/**
		 * 网络请求获取百度accesstoken成功
		 * 
		 * @param accessToken
		 */
		void onGetAccessTocken(String accessToken);
	}
}
