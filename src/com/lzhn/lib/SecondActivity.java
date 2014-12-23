package com.lzhn.lib;

import android.view.View;
import android.widget.TextView;

import com.lzhn.utils.os.BaseActivity_v4;

public class SecondActivity extends BaseActivity_v4 {

	private TextView tv_display;

	@Override
	public void setContentViewLayout() {
		setContentView(R.layout.activity_second);
	}

	@Override
	public void initComponent() {
		tv_display = initViewById(R.id.tv_display);
	}

	@Override
	public void setListeners() {

	}

	@Override
	public void onClickComponent(View v) {

	}

	@Override
	public void doSomeWork() {
		super.doSomeWork();

	}
}
