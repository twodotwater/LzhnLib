package com.lzhn.utils.os;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

/**
 * 抽象的Activity基类，声明常用方法，实现点击接口
 * 
 * @author lzhn
 * 
 */
public abstract class BaseActivity_v4 extends FragmentActivity implements
		OnClickListener {
	private static final String TAG = "BaseActivity_v4";
	private CatchExcepApplication_v4 application;
	private boolean isFullScreen;

	/**
	 * 指定页面的布局id。调用：
	 * {@link android.support.v4.app.FragmentActivity#setContentView}
	 */
	public abstract void setContentViewLayout();

	/**
	 * 初始化组件
	 */
	public abstract void initComponent();

	/**
	 * 为组件添加监听器
	 */
	public abstract void setListeners();

	/**
	 * 组件点击事件
	 * 
	 * @param v
	 */
	public abstract void onClickComponent(View v);

	/**
	 * 整理收集当前Activity
	 */
	private void collectCurrentActivity() {
		application = (CatchExcepApplication_v4) getApplication();
		application.addActivity(this);
	}

	/**
	 * 
	 * @param cls
	 *            重启后首次加载的activity ~ RTTI
	 */
	public void initUnCatchExceptionHandler(Class cls) {
		application.init(cls);
	}

	/**
	 * 获取application
	 * 
	 * @return
	 */
	public CatchExcepApplication_v4 getCatchExcepApplication() {
		return application;
	}

	/**
	 * 初始设置是否全屏：默认不全屏
	 */
	public void initIsFullScreen() {
		setIsFullScreen(false);
	}

	public void setIsFullScreen(boolean isFullScreen) {
		this.isFullScreen = isFullScreen;
	}

	public boolean getIsFullScreen() {
		return isFullScreen;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "--> onCreate");
		super.onCreate(savedInstanceState);
		initIsFullScreen();
		if (isFullScreen) {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
		}
		initSomeWork();
		setContentViewLayout();
		collectCurrentActivity();
		initComponent();
		setListeners();
		doSomeWork();
	}

	/**
	 * 预留方法：在onCreate()方法中完成一些可选工作--组件初始化之前
	 */
	public void initSomeWork() {
		return;
	}

	/**
	 * 预留方法：在onCreate()方法中完成一些可选工作--组件初始化之后
	 */
	public void doSomeWork() {
		return;
	}

	@Override
	public void onClick(View v) {
		onClickComponent(v);
	}

	/**
	 * 初始化组件
	 * 
	 * @param view
	 * @param resId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T initViewById(int resId) {
		return (T) findViewById(resId);
	}

	/**
	 * 设置屏幕方向{@link ActivityInfo#screenOrientation
	 * ActivityInfo.screenOrientation}
	 * 
	 * @see {@link ActivityInfo#screenOrientation
	 *      ActivityInfo.screenOrientation}
	 */
	public void forceScreenOrientation(int orientation) {
		if (getRequestedOrientation() != orientation) {
			setRequestedOrientation(orientation);
		}
	}

	@Override
	protected void onResume() {
		Log.i(TAG, "--> onResume");
		super.onResume();
	}

	@Override
	protected void onPause() {
		Log.i(TAG, "--> onPause");
		super.onPause();
	}

	@Override
	protected void onRestart() {
		Log.i(TAG, "--> onRestart");
		super.onRestart();
	}

	@Override
	protected void onDestroy() {
		Log.i(TAG, "--> onDestroy");
		super.onDestroy();
	}
}
