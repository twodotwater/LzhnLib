package com.lzhn.utils.wifi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

import org.apache.http.conn.util.InetAddressUtils;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.util.Log;

/**
 * wifi管理类
 * 
 * @author lzhn
 * 
 */
public class WifiUtils {
	private static final String TAG = "WifiUtils";
	/** 配置wifi热点的名称 */
	private static final String SSID = "WIFI_HOTSPOT";
	/** 配置wifi热点的密码 */
	private static final String PRE_SHARED_KEY = "12345678";
	/** 定义一个{@link android.net.wifi.WifiManager}对象 */
	private static WifiManager mWifiManager;
	/** 定义一个{@link android.net.wifi.WifiInfo}对象 */
	private WifiInfo mWifiInfo;
	/** 扫描出的网络连接列表：{@link android.net.wifi.WifiInfo} */
	private List<ScanResult> mWifiList;
	/** 网络配置列表{@link android.net.wifi.WifiConfiguration} */
	private List<WifiConfiguration> mWifiConfigurations;
	/** 用来控制wifi接收器持续工作{@link android.net.wifi.WifiManager.WifiLock} */
	private WifiLock mWifiLock;

	public WifiUtils(Context context) {
		// 取得WifiManager对象
		getWifiManager(context);
		// 取得WifiInfo对象
		mWifiInfo = mWifiManager.getConnectionInfo();
	}

	public static WifiManager getWifiManager(Context context) {
		mWifiManager = mWifiManager == null ? (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE) : mWifiManager;
		return mWifiManager;
	}

	/**
	 * 打开wifi
	 */
	public void openWifi() {
		mWifiManager.setWifiEnabled(true);
	}

	/**
	 * 关闭wifi
	 */
	public void closeWifi() {
		mWifiManager.setWifiEnabled(false);
	}

	/**
	 * 获取wifi热点状态
	 * 
	 * @return One of <br/>
	 *         android.net.wifi.WifiManager. {@link #WIFI_AP_STATE_DISABLED},
	 *         {@link #WIFI_AP_STATE_DISABLING} , {@link #WIFI_AP_STATE_ENABLED}
	 *         , {@link #WIFI_AP_STATE_ENABLING} , {@link #WIFI_AP_STATE_FAILED}
	 */
	public int getWifiApState() {
		// 通过反射调用设置热点
		try {
			Method method = mWifiManager.getClass().getMethod("getWifiApState");
			return (Integer) method.invoke(mWifiManager);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * wifi热点是否开启
	 * 
	 * @return
	 */
	public boolean isWifiApEnabled() {

		// 通过反射调用设置热点
		try {
			Method method = mWifiManager.getClass()
					.getMethod("isWifiApEnabled");
			return (Boolean) method.invoke(mWifiManager);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * wifi热点开关
	 * 
	 * @param enabled
	 * @return
	 */
	public boolean setWifiApEnabled(boolean enabled) {
		// wifi和热点不能同时打开
		mWifiManager.setWifiEnabled(false);

		try {
			// 热点的配置类
			WifiConfiguration apConfig = new WifiConfiguration();
			if (enabled) {
				// 配置热点的名称
				apConfig.SSID = SSID;
				// 配置热点的密码
				apConfig.preSharedKey = PRE_SHARED_KEY;
				apConfig.allowedAuthAlgorithms
						.set(WifiConfiguration.AuthAlgorithm.OPEN);
				apConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
				apConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
				apConfig.allowedKeyManagement
						.set(WifiConfiguration.KeyMgmt.WPA_PSK);
				apConfig.allowedPairwiseCiphers
						.set(WifiConfiguration.PairwiseCipher.CCMP);
				apConfig.allowedPairwiseCiphers
						.set(WifiConfiguration.PairwiseCipher.TKIP);
				apConfig.allowedGroupCiphers
						.set(WifiConfiguration.GroupCipher.CCMP);
				apConfig.allowedGroupCiphers
						.set(WifiConfiguration.GroupCipher.TKIP);
			}
			// 通过反射调用设置热点
			Method method = mWifiManager.getClass().getMethod(
					"setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
			// 返回热点打开状态
			return (Boolean) method.invoke(mWifiManager, apConfig, enabled);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 得到本机ip地址
	 */
	public String getLocalHostIp() {
		try {
			Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces();
			// 遍历所用的网络接口
			while (en.hasMoreElements()) {
				NetworkInterface nif = en.nextElement();// 得到每一个网络接口绑定的所有ip
				Enumeration<InetAddress> inet = nif.getInetAddresses();
				// 遍历每一个接口绑定的所有ip
				while (inet.hasMoreElements()) {
					InetAddress ip = inet.nextElement();
					if (!ip.isLoopbackAddress()
							&& InetAddressUtils.isIPv4Address(ip
									.getHostAddress())) {
						return ip.getHostAddress();
					}
				}

			}
		} catch (SocketException e) {
			Log.e(TAG, "获取本地ip地址失败");
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 得到本机Mac地址
	 * 
	 * @return
	 */
	public String getLocalMac() {
		return mWifiManager.getConnectionInfo().getMacAddress();
	}

	/**
	 * 检查当前wifi状态
	 */
	public int getWifiState() {
		return mWifiManager.getWifiState();
	}

	/**
	 * 锁定wifiLock
	 */
	public void acquireWifiLock() {
		mWifiLock.acquire();
	}

	/**
	 * 解锁wifiLock
	 */
	public void releaseWifiLock() {
		// 判断是否锁定
		if (mWifiLock.isHeld()) {
			mWifiLock.release();
		}
	}

	/**
	 * 创建一个wifiLock
	 * 
	 * @return
	 */
	public WifiLock createWifiLock() {
		mWifiLock = mWifiManager.createWifiLock("lockWifi");
		return mWifiLock;
	}

	/**
	 * 得到配置好的网络
	 * 
	 * @return
	 */
	public List<WifiConfiguration> getConfiguration() {
		return mWifiConfigurations;
	}

	/**
	 * 指定配置好的网络进行连接
	 * 
	 * @param index
	 */
	public void connetionConfiguration(int index) {
		if (index > mWifiConfigurations.size()) {
			return;
		}
		// 连接配置好指定ID的网络
		mWifiManager.enableNetwork(mWifiConfigurations.get(index).networkId,
				true);
	}

	/**
	 * 扫描可用网络
	 */
	public void startScan() {
		mWifiManager.startScan();
		// 得到扫描结果
		mWifiList = mWifiManager.getScanResults();
		// 得到配置好的网络连接
		mWifiConfigurations = mWifiManager.getConfiguredNetworks();
	}

	/**
	 * 得到网络列表
	 * 
	 * @return
	 */
	public List<ScanResult> getWifiList() {
		return mWifiList;
	}

	/**
	 * 查看扫描结果
	 * 
	 * @return
	 */
	public StringBuffer lookUpScan() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < mWifiList.size(); i++) {
			sb.append("Index_" + new Integer(i + 1).toString() + ":");
			// 将ScanResult信息转换成一个字符串包
			// 其中把包括：BSSID、SSID、capabilities、frequency、level
			sb.append((mWifiList.get(i)).toString()).append("\n");
		}
		return sb;
	}

	public String getMacAddress() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
	}

	public String getBSSID() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
	}

	public int getIpAddress() {
		return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
	}

	/**
	 * 得到连接的ID
	 * 
	 * @return
	 */
	public int getNetWordId() {
		return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
	}

	/**
	 * 得到wifiInfo的所有信息
	 * 
	 * @return
	 */
	public String getWifiInfo() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();
	}

	/**
	 * 添加一个网络并连接
	 * 
	 * @param configuration
	 */
	public void addNetWork(WifiConfiguration configuration) {
		int wcgId = mWifiManager.addNetwork(configuration);
		mWifiManager.enableNetwork(wcgId, true);
	}

	/**
	 * 断开指定ID的网络
	 * 
	 * @param netId
	 */
	public void disConnectionWifi(int netId) {
		mWifiManager.disableNetwork(netId);
		mWifiManager.disconnect();
	}
}
