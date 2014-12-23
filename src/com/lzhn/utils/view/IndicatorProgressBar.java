package com.lzhn.utils.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lzhn.lib.R;

/**
 * 自定义指示进度条
 * 
 * @author lzhn
 * 
 */
public class IndicatorProgressBar extends FrameLayout {
	private static final String TAG = "IndicatorProgressBar";
	private TextView tv_indicate;
	private ProgressBar progressBar;
	private TextView tv_min;
	private TextView tv_max;
	private TextView tv_center;

	public IndicatorProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context, attrs);
	}

	public IndicatorProgressBar(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		initView(context, attrs);
	}

	public IndicatorProgressBar(Context context) {
		super(context);
		initView(context);
	}

	private void initView(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.my_progressbar_indicator, null);
		tv_indicate = (TextView) view.findViewById(R.id.tv_indicate);
		progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		tv_min = (TextView) view.findViewById(R.id.tv_min);
		tv_max = (TextView) view.findViewById(R.id.tv_max);
		tv_center = (TextView) view.findViewById(R.id.tv_center);
		addView(view);
	}

	private void initView(Context context, AttributeSet attrs) {
		initView(context);
		// 获取自定义属性值
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.IndicatorProgressBar);
		int max = a.getInt(R.styleable.IndicatorProgressBar_maxProgress, 100);
		int progress = a.getInt(R.styleable.IndicatorProgressBar_progress, 0);
		int secondaryProgress = a.getInt(
				R.styleable.IndicatorProgressBar_secondaryProgress, 0);
		float textSize = a.getDimension(
				R.styleable.IndicatorProgressBar_textSize, 10);
		float height = a.getDimension(
				R.styleable.IndicatorProgressBar_progressBarHeight, 10);
		setTextSize(textSize);
		setMax(max);
		setProgress(progress);
		setSecondaryProgress(secondaryProgress);
		setProgressBarHeight(height);
		a.recycle();
	}

	/**
	 * 设置当前进度值
	 * 
	 * @param progress
	 */
	public void setProgress(final int progress) {
		if (progress > progressBar.getMax()) {
			return;
		}
		progressBar.setProgress(progress);
		tv_indicate.setText(progress + "");
		final LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) tv_indicate
				.getLayoutParams();
		progressBar.post(new Runnable() {

			@Override
			public void run() {
				float leftMargin = progressBar.getWidth() * progress
						/ progressBar.getMax() + progressBar.getLeft()
						- tv_indicate.getWidth() / 2;
				lp.leftMargin = (int) Math.ceil(leftMargin);
				tv_indicate.setLayoutParams(lp);
			}
		});
	}

	public void setMax(int max) {
		progressBar.setMax(max);
		tv_max.setText(max + "");
	}

	public void setSecondaryProgress(int secondaryProgress) {
		progressBar.setSecondaryProgress(secondaryProgress);
	}

	public void setProgressBarHeight(float height) {
		ViewGroup.LayoutParams lp = progressBar.getLayoutParams();
		lp.height = (int) height;
		progressBar.setLayoutParams(lp);
	}

	public void setTextSize(float size) {
		tv_indicate.setTextSize(size);
		tv_min.setTextSize(size);
		tv_max.setTextSize(size);
		tv_center.setTextSize(size);
	}
}
