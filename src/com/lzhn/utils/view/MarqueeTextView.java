package com.lzhn.utils.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.ViewDebug.ExportedProperty;
import android.widget.TextView;

/**
 * 跑马灯效果的TextView
 * 
 * @author lzhn
 * 
 */
public class MarqueeTextView extends TextView {

	public MarqueeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public MarqueeTextView(Context context) {
		super(context);
		init();
	}

	public void init() {
		setFocusable(true);
		setFocusableInTouchMode(true);
		setSingleLine(true);
		setHorizontallyScrolling(true);
		setMarqueeRepeatLimit(-1);
		setEllipsize(android.text.TextUtils.TruncateAt.MARQUEE);
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction,
			Rect previouslyFocusedRect) {
		if (focused)
			super.onFocusChanged(focused, direction, previouslyFocusedRect);
	}

	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		if (hasWindowFocus)
			super.onWindowFocusChanged(hasWindowFocus);
	}

	@Override
	@ExportedProperty(category = "focus")
	public boolean isFocused() {
		return true;
	}
}
