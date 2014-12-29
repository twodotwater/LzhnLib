package com.lzhn.map.baidu;

import android.content.Context;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatus.Builder;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;

public class BaiduMapUtils {
	/**
	 * 获取一个新的MapStatus对象
	 * 
	 * @return
	 */
	public static MapStatus getMapStatus() {
		return new MapStatus.Builder().build();
	}

	/**
	 * 获取一个MapStatus对象，以之前的地图状态为基础
	 * 
	 * @param mapStatus
	 * @return
	 */
	public static MapStatus getMapStatus(MapStatus mapStatus) {
		return new MapStatus.Builder(mapStatus).build();
	}

	/**
	 * 获取新的地图状态构造器
	 * 
	 * @return
	 */
	public static Builder getMapStatusBuilder() {
		return new MapStatus.Builder();
	}

	/**
	 * 获取一个MapStatus地图状态构造器，以之前的地图状态为基础
	 * 
	 * @param mapStatus
	 * @return
	 */
	public static Builder getMapStatusBuilder(MapStatus mapStatus) {
		return new MapStatus.Builder();
	}

	/**
	 * 缩放地图
	 * 
	 * @param map
	 *            目标地图对象
	 * @param zoomLevel
	 *            缩放级别
	 */
	public static void ZoomMap(BaiduMap map, float zoomLevel) {
		MapStatusUpdate status = MapStatusUpdateFactory.zoomTo(zoomLevel);
		map.animateMapStatus(status);
	}

	/**
	 * 设置地图旋转角度，逆时针旋转
	 * 
	 * @param map
	 * @param rotateAngle
	 *            旋转角度
	 */
	public static void rotateMap(BaiduMap map, float rotateAngle) {
		updateMapStatus(map,
				getMapStatusBuilder(map.getMapStatus()).rotate(rotateAngle)
						.build());
	}

	/**
	 * 设置地图俯仰角
	 * 
	 * @param map
	 * @param overlook
	 *            俯仰角
	 */
	public static void overlookMap(BaiduMap map, float overlook) {
		updateMapStatus(map,
				getMapStatusBuilder(map.getMapStatus()).overlook(overlook)
						.build());
	}

	/**
	 * 动画更新地图状态
	 * 
	 * @param map
	 * @param mapStatus
	 */
	public static void updateMapStatus(BaiduMap map, MapStatus mapStatus) {
		MapStatusUpdate statusUpdate = MapStatusUpdateFactory
				.newMapStatus(mapStatus);
		map.animateMapStatus(statusUpdate);
	}

	/**
	 * 百度地图 UI控制器。控制地图移动、缩放、俯视、指南针显示等选项控制
	 * 
	 * @param map
	 * @return
	 */
	public static UiSettings getUiSettings(BaiduMap map) {
		return map.getUiSettings();
	}

	/**
	 * 在地图上添加marker
	 * 
	 * @param map
	 * @param latitude
	 *            纬度
	 * @param longitude
	 *            经度
	 * @param iconId
	 *            图标资源id
	 * @param title
	 *            标题
	 * @return
	 */
	public static Marker addMarker(BaiduMap map, double latitude,
			double longitude, int iconId, String title) {
		LatLng latlng = new LatLng(latitude, longitude);
		MarkerOptions options = new MarkerOptions().position(latlng);
		if (iconId != 0) {
			options.icon(BitmapDescriptorFactory.fromResource(iconId));
		}
		if (title != null) {
			options.title(title);
		}
		return (Marker) map.addOverlay(options);
	}

	/**
	 * 在地图上显示信息
	 * 
	 * @param context
	 * @param map
	 * @param infoText
	 *            信息文字
	 * @param bgResId
	 *            文字背景图片资源id。0-无背景
	 * @param latitude
	 *            纬度
	 * @param longitude
	 *            经度
	 * @param yOffset
	 *            Y轴偏移
	 * @param listener
	 *            信息框点击事件监听器
	 */
	public static void showInfoWindow(Context context, BaiduMap map,
			CharSequence infoText, int bgResId, double latitude,
			double longitude, int yOffset, OnInfoWindowClickListener listener) {
		TextView view = new TextView(context);
		if (infoText != null)
			view.setText(infoText);
		if (bgResId != 0)
			view.setBackgroundResource(bgResId);
		LatLng latlng = new LatLng(latitude, longitude);
		InfoWindow window = new InfoWindow(
				BitmapDescriptorFactory.fromView(view), latlng, yOffset,
				listener);
		map.showInfoWindow(window);
	}

}
