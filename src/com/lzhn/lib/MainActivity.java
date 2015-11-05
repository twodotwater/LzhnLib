package com.lzhn.lib;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption.LocationMode;
import com.lzhn.map.baidu.BaiduLocationUtils;
import com.lzhn.utils.common.Resource;
import com.lzhn.utils.location.GpsUtils;
import com.lzhn.utils.os.BaseActivity_v4;
import com.tdw.utils.module.usb.TransManager;

import android.view.Menu;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends BaseActivity_v4 {
	private int id_tv_info;
	private static int id_btn_date;
	private static int id_btn_time;
	private static TextView tv_info;
	private static EditText et_send;
	private Button btn_send;

	private static TransManager transManager;

	private GpsUtils gpsUtils;
	private BaiduLocationUtils baiduLocationUtils;
	private LocationClient locationClient;

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
		// locationClient.start();
	}

	@Override
	protected void onStop() {
		super.onStop();
		locationClient.stop();
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

		gpsUtils = new GpsUtils(this);
		baiduLocationUtils = new BaiduLocationUtils(this);
	}

	@Override
	public void initComponent() {
		id_tv_info = Resource.getInstance(this).getIdIdentifier("tv_info");
		id_btn_date = Resource.getInstance(this).getIdIdentifier("btn_date");
		id_btn_time = Resource.getInstance(this).getIdIdentifier("btn_time");
		tv_info = initViewById(id_tv_info);
		et_send = (EditText) findViewById(R.id.et_send);
		btn_send = (Button) findViewById(R.id.btn_send);
	}

	@Override
	public void setListeners() {
		locationClient = baiduLocationUtils.initLocationClient(
				BaiduLocationUtils.initLocOption(LocationMode.Hight_Accuracy, 3000, true), new BDLocationListener() {

					@Override
					public void onReceiveLocation(BDLocation location) {
						writeInfo("纬度：" + location.getLatitude() + "经度：" + location.getLongitude());
						writeInfo(location.getAddrStr());
						writeInfo(location.getProvince() + location.getCity() + location.getDistrict());
					}
				}, null);

		tv_info.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				tv_info.setText("");
				return false;
			}
		});

		btn_send.setOnClickListener(this);

	}

	@Override
	public void onClickComponent(View v) {
		int viewId = v.getId();
		switch (viewId) {
		case R.id.btn_send:
			transManager.writeMsg(et_send.getText().toString().trim());
			break;

		default:
			break;
		}
		if (viewId == id_btn_date) {
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

			// BaiduPushUtils.initWithApiKey(this, BaiduPushUtils.API_KEY);

			locationClient.start();
		} else if (viewId == id_btn_time) {
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
			// AppUtils.launchOtherApp(this, "com.zzha.contamination");

			locationClient.stop();
		}
	}

	@Override
	public void doSomeWork() {
		super.doSomeWork();
		transManager = new TransManager(this);
		gpsUtils.initProvider();
	}

}
