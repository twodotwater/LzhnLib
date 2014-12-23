package com.lzhn.utils.view;

import android.content.Context;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.lzhn.utils.math.DensityUtil;

public class SlidingLayout extends SlidingPaneLayout {

	private static final float MENU_WIDTH = 220;
	private Context context;

	public SlidingLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public SlidingLayout(Context context) {
		super(context);
		this.context = context;
	}

	public SlidingLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		try {
			super.onInterceptTouchEvent(ev);
		} catch (Exception e) {
			// java.lang.IllegalArgumentException
		}
		if (ev.getX() <= 100) {
			return true;
		} else {
			if (ev.getX() > DensityUtil.dip2px(context, MENU_WIDTH)) {
				closePane();
			}
			return false;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		try {
			super.onTouchEvent(ev);
		} catch (Exception e) {
			// java.lang.IllegalArgumentException
		}
		return true;
	}
}
