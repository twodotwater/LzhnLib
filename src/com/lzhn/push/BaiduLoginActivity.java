package com.lzhn.push;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;

import com.lzhn.push.BaiduPushUtils.OnGetAccessTokenListener;

/**
 * 登录百度账号初始化的Activity
 */
public class BaiduLoginActivity extends Activity {
	private static final String TAG = BaiduLoginActivity.class.getSimpleName();

	private WebView mWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mWebView = new WebView(BaiduLoginActivity.this);
		setContentView(mWebView);
		BaiduPushUtils.initWebView(this, mWebView,
				new OnGetAccessTokenListener() {

					@Override
					public void onGetAccessTocken(String accessToken) {
						goBackToOtherActivity(accessToken);
					}
				});
	}

	/**
	 * 获取了accessToken，回转其他界面Activity
	 * 
	 * @param context
	 * @param accessToken
	 *            传递给其他Activity的认证码
	 */
	private void goBackToOtherActivity(String accessToken) {
		// 回转其他界面Activity的类名
		String className = getIntent().getStringExtra(
				BaiduPushUtils.EXTRA_OTHER_ACTIVITY_CLASS_NAME);
		Intent intent = new Intent();
		intent.setClassName(this, className);
		intent.putExtra(BaiduPushUtils.EXTRA_ACCESS_TOKEN, accessToken);
		startActivity(intent);
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (goBack()) {
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private boolean goBack() {
		if (mWebView != null && mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		}
		return false;
	}
}
