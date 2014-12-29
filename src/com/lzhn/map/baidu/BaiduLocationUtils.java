package com.lzhn.map.baidu;

import android.content.Context;

import com.baidu.location.BDGeofence;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.GeofenceClient.OnGeofenceTriggerListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.lzhn.utils.profile.VibrateUtils;

/**
 * 百度定位工具类
 * 
 * @author lzhn
 * 
 */
public class BaiduLocationUtils {
	private static final String TAG = "BaiduLocationUtils";

	/** 国测局经纬度坐标系 */
	public static final String COORD_TYPE_GCJ = BDGeofence.COORD_TYPE_GCJ;
	/** 百度墨卡托坐标系 */
	public static final String COORD_TYPE_BD09 = BDGeofence.COORD_TYPE_BD09;
	/** 百度经纬度坐标系--百度默认 */
	public static final String COORD_TYPE_BD09LL = BDGeofence.COORD_TYPE_BD09LL;

	private static final String PROD_NAME = "";

	private static Context context;

	public BaiduLocationUtils(Context context) {
		BaiduLocationUtils.context = context;
	}

	/**
	 * 初始化定位locationClient
	 * 
	 * @param bdLocationListener
	 *            位置监听事件
	 * @param bdNotifyListener
	 *            位置提醒监听事件
	 * @return
	 */
	public LocationClient initLocationClient(LocationClientOption locOption,
			BDLocationListener bdLocationListener,
			BDNotifyListener bdNotifyListener) {
		LocationClient locationClient = new LocationClient(context);
		if (locOption != null)
			locationClient.setLocOption(locOption);
		if (bdLocationListener != null)
			locationClient.registerLocationListener(bdLocationListener);
		if (bdNotifyListener != null)
			locationClient.registerNotify(bdNotifyListener);
		return locationClient;
	}

	public void initGeofenceClient(
			OnGeofenceTriggerListener onGeofenceTriggerListener) {
		GeofenceClient geofenceClient = new GeofenceClient(context);
		geofenceClient
				.registerGeofenceTriggerListener(onGeofenceTriggerListener);
	}

	/**
	 * 生成一个围栏
	 * 
	 * @param geofenceId
	 *            围栏名称
	 * @param longitude
	 *            经度
	 * @param latitude
	 *            纬度
	 * @param radiusType
	 *            半径类型
	 * @param druationMillis
	 *            围栏有效时间
	 * @param coorType
	 *            坐标系
	 * @return
	 */
	public BDGeofence initGeofence(String geofenceId, double longitude,
			double latitude, int radiusType, long druationMillis,
			String coorType) {
		BDGeofence geofence = new BDGeofence.Builder()
				.setGeofenceId(geofenceId)
				.setCircularRegion(longitude, latitude, radiusType)
				.setExpirationDruation(druationMillis).setCoordType(coorType)
				.build();
		return geofence;
	}

	/**
	 * 设置定位SDK的定位方式
	 * 
	 * @param mode
	 *            定位模式：Hight_Accuracy高精度、Battery_Saving低功耗、Device_Sensors仅GPS
	 * @param coorType
	 *            坐标系
	 * @param scanSpan
	 *            发起定位请求的间隔时间
	 * @param isNeedAddress
	 *            返回的定位结果是否包含地址信息
	 * @param prodName
	 *            产品线名称
	 * @param notify
	 *            是否位置提醒
	 * @param openGps
	 *            是否打开GPS
	 * @param isNeedDirect
	 *            是否返回手机方向
	 * @param ignoreException
	 *            是否进行异常捕捉。true:不捕捉异常；false:捕捉异常。默认为false
	 * @param ignoreKillProcess
	 *            是否退出定位进程。true:不退出进程； false:退出进程。默认为false。
	 * @return
	 */
	public static LocationClientOption initLocOption(LocationMode mode,
			String coorType, int scanSpan, boolean isNeedAddress,
			String prodName, boolean notify, boolean openGps,
			boolean isNeedDirect, boolean ignoreException,
			boolean ignoreKillProcess) {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(mode);// 设置定位模式
		option.setCoorType(coorType);// 返回的定位结果
		option.setScanSpan(scanSpan);// 设置发起定位请求的间隔时间
		option.setIsNeedAddress(isNeedAddress);// 返回的定位结果是否包含地址信息
		option.setProdName(prodName);// 设置产品线名称
		option.setLocationNotify(notify);
		option.setOpenGps(openGps);
		option.setNeedDeviceDirect(isNeedDirect);// 返回的定位结果包含手机机头的方向
		option.SetIgnoreCacheException(ignoreException);
		option.setIgnoreKillProcess(ignoreKillProcess);
		return option;
	}

	/**
	 * 设置定位SDK的定位方式
	 * 
	 * @param mode
	 *            定位模式：Hight_Accuracy高精度、Battery_Saving低功耗、Device_Sensors仅GPS
	 * @param scanSpan
	 *            发起定位请求的间隔时间
	 * @param isNeedAddress
	 *            返回的定位结果是否包含地址信息
	 * @return
	 */
	public static LocationClientOption initLocOption(LocationMode mode,
			int scanSpan, boolean isNeedAddress) {
		return initLocOption(mode, COORD_TYPE_BD09LL, scanSpan, isNeedAddress,
				PROD_NAME, false, false, false, false, false);
	}

	/**
	 * 初始化BDNotifyListener对象
	 * 
	 * @param latitude
	 *            纬度
	 * @param longtitude
	 *            经度
	 * @param distance
	 *            范围
	 * @param coorType
	 *            坐标系
	 * @return
	 */
	public BDNotifyListener initNotifyListener(double latitude,
			double longtitude, float distance, String coorType) {
		BDNotifyListener notifyListener = new BDNotifyListener() {
			@Override
			public void onNotify(BDLocation mlocation, float distance) {
				VibrateUtils.vibrate(context, 1000);
			}
		};
		notifyListener.SetNotifyLocation(latitude, longtitude, distance,
				coorType == null ? COORD_TYPE_BD09LL : coorType);
		return notifyListener;
	}

	/**
	 * 设置百度地图是否开启定位功能
	 * 
	 * @param map
	 * @param isEnable
	 */
	public static void setBaiduMapLocationEnabled(BaiduMap map, boolean isEnable) {
		map.setMyLocationEnabled(isEnable);
	}

	/**
	 * 配置百度地图定位图层显示方式
	 * 
	 * @param map
	 * @param locationMode
	 *            <li>COMPASS 罗盘态，显示定位方向圈，保持定位图标在地图中心<li>FOLLOWING
	 *            跟随态，保持定位图标在地图中心 <li>NORMAL 普通态： 更新定位数据时不对地图做任何操作
	 * @param enableDirection
	 *            是否允许显示方向信息
	 * @param markerDrawableId
	 *            自定义marker图标资源id
	 */
	public static void setBaiduMapLocationConfigeration(BaiduMap map,
			MyLocationConfiguration.LocationMode locationMode,
			boolean enableDirection, int markerDrawableId) {

		BitmapDescriptor customMarker = null;
		if (markerDrawableId != 0) {
			customMarker = BitmapDescriptorFactory
					.fromResource(markerDrawableId);
		}
		map.setMyLocationConfigeration(new MyLocationConfiguration(
				locationMode, enableDirection, customMarker));
	}

	/**
	 * 将com.baidu.location.BDLocation对象转换为com.baidu.mapapi.map.
	 * MyLocationData定位数据<br/>
	 * 原因：定位SDK和地图SDK
	 * 
	 * @param location
	 * @return
	 */
	public static MyLocationData transBDLocation(BDLocation location) {
		return new MyLocationData.Builder().accuracy(location.getRadius())
				.direction(location.getDirection())
				.latitude(location.getLatitude())
				.longitude(location.getLongitude())
				.satellitesNum(location.getSatelliteNumber())
				.speed(location.getSpeed()).build();
	}
}
