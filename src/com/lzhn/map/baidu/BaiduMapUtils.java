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
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.IllegalNaviArgumentException;
import com.baidu.mapapi.navi.NaviPara;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.busline.BusLineSearch;
import com.baidu.mapapi.search.busline.BusLineSearchOption;
import com.baidu.mapapi.search.busline.OnGetBusLineSearchResultListener;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiBoundSearchOption;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRoutePlanOption.TransitPolicy;
import com.baidu.mapapi.search.share.LocationShareURLOption;
import com.baidu.mapapi.search.share.OnGetShareUrlResultListener;
import com.baidu.mapapi.search.share.PoiDetailShareURLOption;
import com.baidu.mapapi.search.share.ShareUrlSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;

/**
 * 百度地图操作工具类<br/>
 * <b>功能简介：</b> <li>地图操作：旋转、缩放、更新状态……<li>地图UI控制<li>poi检索<li>路径规划、公交线路检索<li>
 * 地理解码、短串分享
 * 
 * @author lzhn
 * 
 */
public class BaiduMapUtils {
	/** 移动设备上未安装百度地图或百度地图版本较旧 */
	public static final int NAVIGATION_BAIDU_MAP_APP_NOT_SUPPORT = 1;
	/** 非法导航参数 */
	public static final int NAVIGATION_ILLEGAL_ARGUMENT = 2;

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

	/** 百度地图检索功能方法 */

	/**
	 * 获取PoiSearch对象，指定检索结果回调接口
	 * 
	 * @param onGetPoiSearchResultListener
	 * @return
	 */
	public static PoiSearch getNewPoiSearch(
			OnGetPoiSearchResultListener onGetPoiSearchResultListener) {
		PoiSearch poiSearch = PoiSearch.newInstance();
		if (onGetPoiSearchResultListener != null)
			poiSearch
					.setOnGetPoiSearchResultListener(onGetPoiSearchResultListener);
		return poiSearch;
	}

	/**
	 * 城市内检索
	 * 
	 * @param poiSearch
	 * @param city
	 *            城市名
	 * @param keyword
	 *            关键字
	 * @param pageNum
	 *            分页编号
	 * @param pageCapacity
	 *            每页容量。若小于1，则使用默认容量10
	 * @return
	 */
	public static boolean searchInCity(PoiSearch poiSearch, String city,
			String keyword, int pageNum, int pageCapacity) {
		PoiCitySearchOption option = new PoiCitySearchOption().city(city)
				.keyword(keyword).pageNum(pageNum)
				.pageCapacity(pageCapacity < 1 ? 10 : pageCapacity);
		return poiSearch.searchInCity(option);
	}

	/**
	 * 周边范围内检索
	 * 
	 * @param poiSearch
	 * @param latitude
	 *            纬度
	 * @param longitude
	 *            经度
	 * @param keyword
	 *            关键字
	 * @param radius
	 *            检索半径，单位：米。若小于1，则使用默认半径500米
	 * @param pageNum
	 *            分页编号
	 * @param pageCapacity
	 *            每页容量。若小于1，则使用默认容量10
	 * @return
	 */
	public static boolean searchNearby(PoiSearch poiSearch, double latitude,
			double longitude, String keyword, int radius, int pageNum,
			int pageCapacity) {
		LatLng latlng = new LatLng(latitude, longitude);
		PoiNearbySearchOption option = new PoiNearbySearchOption()
				.location(latlng).keyword(keyword)
				.radius(radius < 1 ? 500 : radius).pageNum(pageNum)
				.pageCapacity(pageCapacity < 1 ? 10 : pageCapacity);
		return poiSearch.searchNearby(option);
	}

	/**
	 * 范围内检索
	 * 
	 * @param poiSearch
	 * @param bound
	 *            地理范围对象
	 * @param keyword
	 *            关键字
	 * @param pageNum
	 *            分页编号
	 * @param pageCapacity
	 *            每页容量。若小于1，则使用默认容量10
	 * @return
	 */
	public static boolean searchInBound(PoiSearch poiSearch,
			LatLngBounds bound, String keyword, int pageNum, int pageCapacity) {
		PoiBoundSearchOption option = new PoiBoundSearchOption().bound(bound)
				.keyword(keyword).pageNum(pageNum)
				.pageCapacity(pageCapacity < 1 ? 10 : pageCapacity);
		return poiSearch.searchInBound(option);
	}

	/**
	 * 详情检索
	 * 
	 * @param poiSearch
	 * @param uid
	 *            欲检索的poi的uid。poi点附近若有街景，可使用uid检索全景组件的全景数据
	 * @return
	 */
	public static boolean searchPoiDetail(PoiSearch poiSearch, String uid) {
		PoiDetailSearchOption option = new PoiDetailSearchOption().poiUid(uid);
		return poiSearch.searchPoiDetail(option);
	}

	/**
	 * 城市公交信息(包含地铁信息)查询。该接口用于查询整条公交线路信息
	 * 
	 * @param onGetBusLineSearchResultListener
	 *            公交详情检索结果监听者
	 * @return
	 */
	public static BusLineSearch getNewBusLineSearch(
			OnGetBusLineSearchResultListener onGetBusLineSearchResultListener) {
		BusLineSearch search = BusLineSearch.newInstance();
		if (onGetBusLineSearchResultListener != null)
			search.setOnGetBusLineSearchResultListener(onGetBusLineSearchResultListener);
		return search;
	}

	/**
	 * 查询公交线路的详情信息<br/>
	 * <li>第一步，发起POI检索，获取相应线路的UID；<li>第二步，在POI检索结果中判断该POI类型是否为公交信息；<li>
	 * 第三步，定义并设置公交信息结果监听者（与POI类似），并发起公交详情检索；
	 * 
	 * @param search
	 * @param city
	 * @param uid
	 *            可以由poi检索时传入公交路线关键字得到<br/>
	 *            if (poi.type == PoiInfo.POITYPE.BUS_LINE ||poi.type ==
	 *            PoiInfo.POITYPE.SUBWAY_LINE) <br/>
	 *            busLineId = poi.uid; //说明该条POI为公交信息，获取该条POI的UID<br/>
	 * @return
	 */
	public static boolean searchBusLine(BusLineSearch search, String city,
			String uid) {
		BusLineSearchOption option = new BusLineSearchOption().city(city).uid(
				uid);
		return search.searchBusLine(option);
	}

	/**
	 * 路径规划搜索接口
	 * 
	 * @param onGetRoutePlanResultListener
	 *            路线规划结果回调
	 * @return
	 */
	public static RoutePlanSearch getNewRoutePlanSearch(
			OnGetRoutePlanResultListener onGetRoutePlanResultListener) {
		RoutePlanSearch search = RoutePlanSearch.newInstance();
		if (onGetRoutePlanResultListener != null)
			search.setOnGetRoutePlanResultListener(onGetRoutePlanResultListener);
		return search;
	}

	/**
	 * 发起换乘路线规划
	 * 
	 * @param search
	 * @param city
	 * @param startPlaceName
	 *            起始地点
	 * @param endPlaceName
	 *            目的地点
	 * @param policy
	 *            换乘策略<br/>
	 *            <li>EBUS_NO_SUBWAY 公交检索策略常量：不含地铁<li>EBUS_TIME_FIRST
	 *            公交检索策略常量：时间优先 <li>EBUS_TRANSFER_FIRST 公交检索策略常量：最少换乘 <li>
	 *            EBUS_WALK_FIRST 公交检索策略常量：最少步行距离
	 * @return
	 */
	public static boolean transitSearch(RoutePlanSearch search, String city,
			String startPlaceName, String endPlaceName, TransitPolicy policy) {
		PlanNode startNode = PlanNode.withCityNameAndPlaceName(city,
				startPlaceName);
		PlanNode endNode = PlanNode
				.withCityNameAndPlaceName(city, endPlaceName);
		TransitRoutePlanOption option = new TransitRoutePlanOption().city(city)
				.from(startNode).to(endNode).policy(policy);
		return search.transitSearch(option);
	}

	/**
	 * 获取建议检索实例
	 * 
	 * @param onGetSuggestionResultListener
	 *            建议查询结果回调接口
	 * @return
	 */
	public static SuggestionSearch getNewSuggestionSearch(
			OnGetSuggestionResultListener onGetSuggestionResultListener) {
		SuggestionSearch search = SuggestionSearch.newInstance();
		if (onGetSuggestionResultListener != null)
			search.setOnGetSuggestionResultListener(onGetSuggestionResultListener);
		return search;
	}

	/**
	 * 建议请求入口
	 * 
	 * @param search
	 * @param city
	 *            设置建议请求城市
	 * @param keyword
	 *            指定建议关键字
	 * @param latlng
	 *            指定位置
	 * @return
	 */
	public static boolean requestSuggestion(SuggestionSearch search,
			String city, String keyword, LatLng latlng) {
		SuggestionSearchOption option = new SuggestionSearchOption().city(city)
				.keyword(keyword);
		if (latlng != null)
			option.location(latlng);
		return search.requestSuggestion(option);
	}

	/**
	 * 地理编码查询接口
	 * 
	 * @param onGetGeoCoderResultListener
	 *            设置查询结果监听者
	 * @return
	 */
	public static GeoCoder getNewGeoCoder(
			OnGetGeoCoderResultListener onGetGeoCoderResultListener) {
		GeoCoder geocoder = GeoCoder.newInstance();
		if (onGetGeoCoderResultListener != null)
			geocoder.setOnGetGeoCodeResultListener(onGetGeoCoderResultListener);
		return geocoder;
	}

	/**
	 * 发起地理编码(地址信息->经纬度)请求
	 * 
	 * @param geoCoder
	 * @param city
	 *            城市名
	 * @param address
	 *            地址
	 * @return
	 */
	public static boolean geocode(GeoCoder geoCoder, String city, String address) {
		GeoCodeOption option = new GeoCodeOption().city(city).address(address);
		return geoCoder.geocode(option);
	}

	/**
	 * 发起反地理编码请求(经纬度->地址信息)
	 * 
	 * @param geoCoder
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	public static boolean reverseGeoCode(GeoCoder geoCoder, double latitude,
			double longitude) {
		LatLng latlng = new LatLng(latitude, longitude);
		ReverseGeoCodeOption option = new ReverseGeoCodeOption()
				.location(latlng);
		return geoCoder.reverseGeoCode(option);
	}

	/**
	 * 共享URL查询接口<br/>
	 * 
	 * 共享URL是指代表特定信息(poi详情/位置)的一条经过压缩的URL，该URL可通过任何途径传播。
	 * 当终端用户点击该URL时，该URL所代表的特定信息会通过百度地图(客户端/web)重新呈现
	 * 
	 * @param onGetShareUrlResultListener
	 *            位置分享、poi详情分享检索结果监听者
	 * @return
	 */
	public static ShareUrlSearch getNewShareUrlSearch(
			OnGetShareUrlResultListener onGetShareUrlResultListener) {
		ShareUrlSearch search = ShareUrlSearch.newInstance();
		if (onGetShareUrlResultListener != null)
			search.setOnGetShareUrlResultListener(onGetShareUrlResultListener);
		return search;
	}

	/**
	 * 请求位置信息分享URL
	 * 
	 * @param search
	 * @param latlng
	 *            共享点位置
	 * @param name
	 *            共享点名称
	 * @param snippet
	 *            附加信息。通过短URL调起客户端时作为附加信息显示在名称下面
	 * @return
	 */
	public static boolean requestLocationShareUrl(ShareUrlSearch search,
			LatLng latlng, String name, String snippet) {
		LocationShareURLOption option = new LocationShareURLOption()
				.location(latlng).name(name).snippet(snippet);
		return search.requestLocationShareUrl(option);
	}

	/**
	 * 请求poi详情分享URL
	 * 
	 * @param search
	 * @param uid
	 *            欲分享的poi的uid
	 * @return
	 */
	public static boolean requestPoiDetailShareUrl(ShareUrlSearch search,
			String uid) {
		PoiDetailShareURLOption option = new PoiDetailShareURLOption()
				.poiUid(uid);
		return search.requestPoiDetailShareUrl(option);
	}

	/**
	 * 调起百度地图客户端导航页面
	 * 
	 * @param context
	 * @param startName
	 *            导航起点名称
	 * @param startPoint
	 *            导航起点， 百度经纬度坐标
	 * @param endName
	 *            导航终点名称
	 * @param endPoint
	 *            导航终点， 百度经纬度坐标
	 * @return <li>0：调用成功 <li>
	 *         {@link BaiduMapUtils#NAVIGATION_BAIDU_MAP_APP_NOT_SUPPORT} <li>
	 *         {@link BaiduMapUtils#NAVIGATION_ILLEGAL_ARGUMENT}
	 */
	public static int openBaiduMapNavi(Context context, String startName,
			LatLng startPoint, String endName, LatLng endPoint) {
		NaviPara para = new NaviPara();
		para.startName = startName == null ? "起点" : startName;
		para.startPoint = startPoint;
		para.endName = endName == null ? "终点" : endName;
		para.endPoint = endPoint;
		try {
			BaiduMapNavigation.openBaiduMapNavi(para, context);
		} catch (BaiduMapAppNotSupportNaviException notSupportNaviException) {
			return NAVIGATION_BAIDU_MAP_APP_NOT_SUPPORT;
		} catch (IllegalNaviArgumentException illegalNaviArgumentException) {
			return NAVIGATION_ILLEGAL_ARGUMENT;
		}
		return 0;
	}

	/**
	 * 调起百度地图网页导航页面
	 * 
	 * @param context
	 * @param startName
	 *            导航起点名称
	 * @param startPoint
	 *            导航起点， 百度经纬度坐标
	 * @param endName
	 *            导航终点名称
	 * @param endPoint
	 *            导航终点， 百度经纬度坐标
	 * @return <li>0：调用成功 <li>
	 *         {@link BaiduMapUtils#NAVIGATION_ILLEGAL_ARGUMENT}
	 */
	public static int openWebBaiduMapNavi(Context context, String startName,
			LatLng startPoint, String endName, LatLng endPoint) {
		NaviPara para = new NaviPara();
		para.startName = startName == null ? "起点" : startName;
		para.startPoint = startPoint;
		para.endName = endName == null ? "终点" : endName;
		para.endPoint = endPoint;
		try {
			BaiduMapNavigation.openWebBaiduMapNavi(para, context);
		} catch (IllegalNaviArgumentException illegalNaviArgumentException) {
			return NAVIGATION_ILLEGAL_ARGUMENT;
		}
		return 0;
	}

	/**
	 * 自定义 PoiOverlay 类
	 * 
	 * @author lzhn
	 * 
	 */
	static class BaiduPoiOverlay extends PoiOverlay {
		private OnPoiClickListener onPoiClickListener;

		public BaiduPoiOverlay(BaiduMap map) {
			super(map);
		}

		/**
		 * 创建PoiOverlay实例，选择是否设置地图 Marker覆盖物点击事件监听者
		 * 
		 * @param map
		 * @param setMarkerClickListener
		 *            true：指定自身为事件监听者；false：不指定事件监听者
		 */
		public BaiduPoiOverlay(BaiduMap map, boolean setMarkerClickListener) {
			super(map);
			if (setMarkerClickListener)
				map.setOnMarkerClickListener(this);
		}

		public void setOnPoiClickListener(OnPoiClickListener onPoiClickListener) {
			this.onPoiClickListener = onPoiClickListener;
		}

		@Override
		public boolean onPoiClick(int index) {
			if (onPoiClickListener != null) {
				super.onPoiClick(index);
				return onPoiClickListener.onPoiClick(index);
			}
			return super.onPoiClick(index);

		}

		/**
		 * poi结果点击回调接口
		 * 
		 * @author lzhn
		 * 
		 */
		interface OnPoiClickListener {
			/**
			 * poi结果点击事件
			 * 
			 * @param index
			 *            被点击的poi在 PoiResult.getAllPoi() 中的索引
			 * @return
			 */
			public boolean onPoiClick(int index);
		}

	}
}
