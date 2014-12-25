package com.lzhn.lib;

import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.lzhn.push.BaiduPushUtils;
import com.lzhn.slidingmenu.lib.SlidingMenu;
import com.lzhn.utils.location.GpsUtils;
import com.lzhn.utils.os.AppUtils;
import com.lzhn.utils.os.BaseSlidingActivity;
import com.lzhn.utils.usb.TransManager;
import com.lzhn.utils.view.ToastUtils;

public class MainActivity extends BaseSlidingActivity {
	private static TextView tv_info;

	private static TransManager transManager;

	private GpsUtils gpsUtils;

	public static void writeInfo(String info) {
		info = tv_info.getText().toString() + "\n" + info;
		tv_info.setText(info);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		transManager.registerReceiver();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		transManager.unregisterReceiver();
	}

	@Override
	public void setContentViewLayout() {
		setContentView(R.layout.activity_main);
	}

	@Override
	public void initSomeWork() {
		super.initSomeWork();
		transManager = new TransManager(this);
		gpsUtils = new GpsUtils(this);
	}

	@Override
	public void initComponent() {
		tv_info = initViewById(R.id.tv_info);
	}

	@Override
	public void setListeners() {

	}

	@Override
	public void onClickComponent(View v) {
		switch (v.getId()) {
		case R.id.btn_date:
			// 日期选择对话框
			// DatePickerDialogFragment_v4 dialog = new
			// DatePickerDialogFragment_v4();
			// dialog.show(getSupportFragmentManager(), "datePickerDialog");
			// SettingsUtils.getInstance(this).setSound();
			// 发送数据到USB
			// byte[] buffer = "hello world".getBytes();
			// int len = transManager.send(buffer);
			// writeInfo(buffer.length + "\tsend:" + len);

			// gpsUtils.openGps();

			// slidingMenu.showMenu(true);

			BaiduPushUtils.initWithApiKey(this, BaiduPushUtils.API_KEY);
			break;
		case R.id.btn_time:
			// 时间选择对话框、
			// TimePickerDialogFragment_v4 tDialog = new
			// TimePickerDialogFragment_v4();
			// tDialog.show(getSupportFragmentManager(), "");
			// 接收USB数据
			// byte[] buffer2 = new byte[16];
			// int len2 = transManager.read(buffer2);
			// writeInfo(buffer2.length + "\tread:" + len2);

			// slidingMenu.showSecondaryMenu(true);

			// BaiduPushUtils.startRichMediaListActivity(this);
			// BaiduPushUtils.startBaiduLoginActivity(true, this, getClass()
			// .getName());
			AppUtils.launchOtherApp(this, "com.zzha.contamination");
			break;

		default:
			break;
		}
		ToastUtils.showToast(this, "onClick !");
	}

	@Override
	public void doSomeWork() {
		super.doSomeWork();
		transManager.init();
		gpsUtils.initProvider();
		Fragment rightMenuFragment = new RightFragment();
		initSlidingMenu(0, SlidingMenu.LEFT_RIGHT, SlidingMenu.TOUCHMODE_MARGIN);
		initRightMenu(rightMenuFragment);
	}
}
