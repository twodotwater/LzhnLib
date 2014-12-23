package com.lzhn.utils.os;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

/**
 * 自定义fragment基类。--实现点击接口
 * 
 * @author lzhn
 * 
 */
public abstract class BaseFragment extends Fragment implements OnClickListener {

	private static final String TAG = "BaseFragment";
	/** 上下文对象fragmentActivity */
	protected Context context;
	/** fragment的布局View */
	private View currentView;

	/**
	 * 导入布局文件、初始化{@link #currentView}。请调用：inflater.inflate(resourceId,
	 * container, b);
	 * 
	 * @param inflater
	 * @param container
	 * @param b
	 * @return
	 */
	public abstract View inflateLayout(LayoutInflater inflater,
			ViewGroup container, boolean b);

	public View getCurrentView() {
		return currentView;
	}

	/**
	 * 设定布局参数信息
	 * 
	 * @param layoutParams
	 */
	public void setCurrentViewPararms(LayoutParams layoutParams) {
		currentView.setLayoutParams(layoutParams);
	}

	/**
	 * 获取内容布局参数信息
	 * 
	 * @return
	 */
	public LayoutParams getCurrentViewParams() {
		return (LayoutParams) currentView.getLayoutParams();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
		onGetArguments(getArguments());
	}

	/**
	 * 获取传递的参数值，在此可进行数据初始化工作。<br/>
	 * </br> Note：还未导入布局、组件尚未初始化
	 * 
	 * @param arguments
	 */
	public void onGetArguments(Bundle arguments) {
		return;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		currentView = inflateLayout(inflater, container, false);
		initComponent(currentView);
		setListeners();
		doSomeWork();
		return currentView;
	}

	/**
	 * 为组件添加监听器
	 */
	public void setListeners() {
		return;
	}

	/**
	 * 预留方法：在onCreateView()方法中完成的其他工作</br></br>Note：请确保
	 * {@link #initComponent(View)} 方法中组件初始化已完成
	 */
	public void doSomeWork() {

	}

	/**
	 * 初始化组件
	 * 
	 * @param view
	 *            包含组件的view
	 */
	public void initComponent(View view) {
		return;
	}

	@Override
	public void onClick(View v) {
		onClickComponent(v);
	}

	/**
	 * 组件点击处理方法
	 * 
	 * @param v
	 */
	public void onClickComponent(View v) {
		return;
	}

	/**
	 * 初始化组件
	 * 
	 * @param viewGroup
	 * @param resId
	 *            组件id
	 * @return
	 */
	public <T> T initViewById(View viewGroup, int resId) {
		return (T) currentView.findViewById(resId);
	}
}
