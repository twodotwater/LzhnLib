package com.lzhn.utils.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

/**
 * 自定义绘图SurfaceView
 * 
 * @author lzhn
 * 
 */
public class CustomSurfaceView extends SurfaceView implements
		SurfaceHolder.Callback {
	private static final String TAG = "CustomSurfaceView";
	/** 用来操作Surface {@link android.view.SurfaceHolder} */
	private SurfaceHolder holder;
	/** 宽度 */
	private int width;
	/** 高度 */
	private int height;

	public CustomSurfaceView(Context context) {
		super(context);
		initWork();
	}

	public CustomSurfaceView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomSurfaceView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initWork();
	}

	/**
	 * 初始化{@link #holder}，并添加回调接口（类本身实现了
	 * {@link android.view.SurfaceHolder.Callback}）
	 */
	private void initWork() {
		holder = getHolder();
		holder.addCallback(this);
	}

	public int getViewWidth() {
		return width;
	}

	public int getViewHeight() {
		return height;
	}

	public CustomSurfaceView setSize(int w, int h) {
		ViewGroup.LayoutParams lp = getLayoutParams();
		lp.width = w;
		lp.height = h;
		setLayoutParams(lp);
		return this;
	}

	/**
	 * 在UI线程调用渲染
	 */
	public void render() {
		invalidate();
	}

	/**
	 * 在非UI线程调用渲染
	 */
	public void postRender() {
		postInvalidate();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Canvas canvas = holder.lockCanvas();
		width = canvas.getWidth();
		height = canvas.getHeight();
		holder.unlockCanvasAndPost(canvas);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}
}
