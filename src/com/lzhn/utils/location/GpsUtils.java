package com.lzhn.utils.location;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;

import com.lzhn.utils.os.SettingsUtils;
import com.lzhn.utils.view.ToastUtils;

/**
 * Gps操作类<br/>
 * <b>启用权限：</b><br/>
 * android.permission.INTERNET<br/>
 * android.permission.ACCESS_COARSE_LOCATION<br/>
 * android.permission.ACCESS_FIND_LOCATION<br/>
 * android.permission.ACCESS_FINE_LOCATION<br/>
 * 
 * @author lzhn
 * 
 */
public class GpsUtils {

	private static final String TAG = "GpsUtils";

	private static final long MIN_TIME = 5 * 60 * 1000;

	private static final float MIN_DISTANCE = 10;

	private static Context context;
	private static LocationManager locationManager;
	private LocationListener listener;
	private String provider;
	private Location location;
	private OnLocationStateChangedListener onLocationStateChangedListener;

	public GpsUtils(Context context) {
		this.context = context;
		getLocationManager(context);
		initLocationListener();
	}

	/**
	 * 添加定位状态改变监听器
	 * 
	 * @param onLocationStateChangedListener
	 */
	public void setOnLocationStateChangedListener(
			OnLocationStateChangedListener onLocationStateChangedListener) {
		this.onLocationStateChangedListener = onLocationStateChangedListener;
	}

	/**
	 * 初始化定位监听器
	 */
	private void initLocationListener() {
		listener = new LocationListener() {

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				if (onLocationStateChangedListener != null) {
					onLocationStateChangedListener.onStatusChanged(provider,
							status, extras);
				}
				Log.i(TAG, "status:" + status);
				switch (status) {
				// GPS状态为可见时
				case LocationProvider.AVAILABLE:
					Log.i(TAG, "当前GPS状态为可见状态");
					break;
				// GPS状态为服务区外时
				case LocationProvider.OUT_OF_SERVICE:
					Log.i(TAG, "当前GPS状态为服务区外状态");
					break;
				// GPS状态为暂停服务时
				case LocationProvider.TEMPORARILY_UNAVAILABLE:
					Log.i(TAG, "当前GPS状态为暂停服务状态");
					break;
				}
			}

			@Override
			public void onProviderEnabled(String provider) {
				Log.i(TAG, "gps开启！");
				ToastUtils.showToast(context, "gps开启！");
				if (onLocationStateChangedListener != null) {
					onLocationStateChangedListener.onProviderEnabled(provider);
				}
			}

			@Override
			public void onProviderDisabled(String provider) {
				Log.i(TAG, "gps关闭！");
				ToastUtils.showToast(context, "gps关闭！");
				if (onLocationStateChangedListener != null) {
					onLocationStateChangedListener.onProviderDisabled(provider);
				}
			}

			@Override
			public void onLocationChanged(Location location) {
				Log.i(TAG, location.toString());
				if (onLocationStateChangedListener != null) {
					onLocationStateChangedListener.onLocationChanged(location);
				}
			}
		};
	}

	/**
	 * 初始化GPS定位provider
	 */
	public void initProvider() {
		// 使用Gps Provider，无网络情况下
		provider = LocationManager.GPS_PROVIDER;
		location = locationManager.getLastKnownLocation(provider);
	}

	/**
	 * 获取定位标准Criteria<br/>
	 * <b>使用方法：</b>locationManager.getBestProvider(criteria, true)<br>
	 * 获得当前的位置提供者（返回值一般是：network）
	 * 
	 * 
	 * @return
	 */
	public Criteria getCriteria() {
		Criteria criteria = new Criteria();
		// 获得最好的定位效果
		criteria.setAccuracy(Criteria.ACCURACY_FINE); // 设置为最大精度
		criteria.setAltitudeRequired(false); // 不获取海拔信息
		criteria.setBearingRequired(false); // 不获取方位信息
		criteria.setCostAllowed(false); // 是否允许付费
		criteria.setPowerRequirement(Criteria.POWER_LOW); // 使用省电模式

		// // 获得当前的位置提供者。返回值一般是：network
		// provider = locationManager.getBestProvider(criteria, true);

		return criteria;
	}

	/**
	 * 获取位置管理器LocationManager
	 * 
	 * @param context
	 * @return
	 */
	public static LocationManager getLocationManager(Context context) {
		if (locationManager == null) {
			locationManager = (LocationManager) context
					.getSystemService(Context.LOCATION_SERVICE);
		}
		return locationManager;
	}

	/**
	 * Gps定位是否开启
	 * 
	 * @return
	 */
	public boolean isGpsEnabled() {
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	/**
	 * 网络定位是否开启
	 * 
	 * @return
	 */
	public boolean isNetworkEnabled() {
		return locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	}

	/**
	 * 调用系统Gps设置
	 */
	public void openGps() {
		SettingsUtils.getInstance(context).setLocation();
	}

	public void updateLocation() {
		location = locationManager.getLastKnownLocation(provider);
	}

	/**
	 * 请求更新地理位置
	 */
	public void requestLocationUpdates() {
		locationManager.requestLocationUpdates(provider, MIN_TIME,
				MIN_DISTANCE, listener);
	}

	/**
	 * 停止地理位置更新任务
	 */
	public void removeUpdates() {
		locationManager.removeUpdates(listener);
	}

	/**
	 * 获取新的位置信息后的一般处理方法
	 * 
	 * @param location
	 * @return
	 */
	private String onGetLocation(Location location) {
		if (location == null) {
			return "未获得地址信息!";
		}
		// removeUpdates();
		double latitude = location.getLatitude();
		double longitude = location.getLongitude();
		Geocoder gc = new Geocoder(context);
		List<Address> addresses = null;
		try {
			// 根据经纬度获得地址信息
			addresses = gc.getFromLocation(latitude, longitude, 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (addresses != null && addresses.size() > 0) {
			// 获取address类的成员信息
			Address address = addresses.get(0);
			return address.toString();
		}
		return "纬度：" + latitude + "\t经度：" + longitude;
	}

	/**
	 * 定位状态改变监听器
	 * 
	 * @author lzhn
	 * 
	 */
	interface OnLocationStateChangedListener {
		/**
		 * 位置改变
		 * 
		 * @param location
		 *            新的位置信息
		 */
		void onLocationChanged(Location location);

		/**
		 * provider状态改变
		 * 
		 * @param provider
		 * @param status
		 * @param extras
		 */
		void onStatusChanged(String provider, int status, Bundle extras);

		/**
		 * provider定位启用
		 * 
		 * @param provider
		 */
		void onProviderEnabled(String provider);

		/**
		 * provider定位关闭
		 * 
		 * @param provider
		 */
		void onProviderDisabled(String provider);
	}
}
