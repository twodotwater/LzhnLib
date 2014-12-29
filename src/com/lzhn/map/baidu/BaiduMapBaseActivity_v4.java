package com.lzhn.map.baidu;

import android.content.Intent;
import android.os.Bundle;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.lzhn.utils.os.BaseActivity_v4;

/**
 * 包含百度地图组件的Activity，已初始化百度SDK<br/>
 * <br/>
 * Note：若想要使用Fragment框架。可以使用SupportMapFragment控件完成相应框架内的开发工作
 * 
 * @author lzhn
 * 
 */
public abstract class BaiduMapBaseActivity_v4 extends BaseActivity_v4 {
	/** 一个显示地图的视图（View） */
	protected MapView mapView;

	/** 百度地图对象，可调用各种操作方法及指定监听事件 */
	protected BaiduMap baiduMap;

	/**
	 * 子类调用时，必须调用super.initSomeWork()方法来初始化mapView
	 */
	@Override
	public void initSomeWork() {
		super.initSomeWork();
		// 必须在指定Activity的视图View之前、先初始化百度SDK
		SDKInitializer.initialize(getApplicationContext());
		Intent intent = getIntent();
		if (intent.hasExtra("x") && intent.hasExtra("y")) {
			// 当用intent参数时，设置中心点为指定点。x：经度；y：纬度
			Bundle b = intent.getExtras();
			LatLng p = new LatLng(b.getDouble("y"), b.getDouble("x"));
			mapView = new MapView(this,
					new BaiduMapOptions().mapStatus(new MapStatus.Builder()
							.target(p).build()));
		} else {
			mapView = new MapView(this, new BaiduMapOptions());
		}
	}

	@Override
	public void setContentViewLayout() {
		setContentView(mapView);
		// 初始化百度地图对象
		baiduMap = mapView.getMap();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// activity 暂停时同时暂停地图控件
		mapView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// activity 恢复时同时恢复地图控件
		mapView.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// activity 销毁时同时销毁地图控件
		mapView.onDestroy();
	}
}
