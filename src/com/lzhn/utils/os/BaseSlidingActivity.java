package com.lzhn.utils.os;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.lzhn.lib.R;
import com.lzhn.slidingmenu.lib.SlidingMenu;
import com.lzhn.slidingmenu.lib.app.SlidingFragmentActivity;

public abstract class BaseSlidingActivity extends SlidingFragmentActivity
		implements OnClickListener {
	private static final String TAG = "BaseSlidingActivity";

	private CatchExcepApplication_v4 application;
	private boolean isFullScreen;

	protected SlidingMenu slidingMenu;

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
		setContentViewLayout();
		collectCurrentActivity();
		initSomeWork();
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
	 * 初始化slidingMenu：设定滑动模式（左右）、触摸范围、菜单宽度……
	 * 
	 * @param menuWidthResId
	 *            指定菜单宽度。设为0，则使用默认宽度
	 * @param mode
	 *            侧边栏滑动模式：SlideingMenu.LEFT_RIGHT
	 * @param touchMode
	 *            侧边栏触摸显示模式：SlidingMenu.TOUCHMODE_MARGIN
	 */
	public void initSlidingMenu(int menuWidthResId, int mode, int touchMode) {
		slidingMenu = getSlidingMenu();
		slidingMenu.setMode(mode);
		// 设置触摸屏幕的模式
		slidingMenu.setTouchModeAbove(touchMode);
		// 设置侧滑菜单的阴影效果
		// slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
		// slidingMenu.setShadowDrawable(R.drawable.shadow);
		// slidingMenu.setSecondaryShadowDrawable(R.drawable.shadow);
		// 设置滑动菜单视图的宽度
		// slidingMenu.setBehindOffsetRes(menuOffsetResId);
		menuWidthResId = menuWidthResId == 0 ? R.dimen.menu_width
				: menuWidthResId;
		slidingMenu.setBehindWidthRes(menuWidthResId);
		// 设置渐入渐出效果的值
		slidingMenu.setFadeDegree(0.6f);
		// 设置SlidingMenu与下方视图的移动的速度比
		// slidingMenu.setBehindScrollScale(1.0f);

		// 左侧滑栏布局文件，必须在onCreate()方法中调用
		setBehindContentView(R.layout.slidingmenu_left_frame);
	}

	/**
	 * 添加左侧栏fragment
	 * 
	 * @param leftMenuFragment
	 */
	public void initLeftMenu(Fragment leftMenuFragment) {
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.id_left_menu_frame, leftMenuFragment).commit();

	}

	/**
	 * 添加右侧栏fragment。<br/>
	 * Note：在 {@link BaseSlidingActivity#initSlidingMenu(int, int, int)}
	 * 方法之后才可以调用
	 * 
	 * @param rightMenuFragment
	 */
	public void initRightMenu(Fragment rightMenuFragment) {
		// 设置右边侧滑菜单
		slidingMenu.setSecondaryMenu(R.layout.slidingmenu_right_frame);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.id_right_menu_frame, rightMenuFragment).commit();
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