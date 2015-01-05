package com.lzhn.utils.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;

import com.lzhn.utils.bluetooth.BTUtils.OnBtChangeListener;
import com.lzhn.utils.bluetooth.BTUtils.OnBtDiscoveredListener;
import com.lzhn.utils.bluetooth.BTUtils.OnBtStateChangeListener;

/**
 * 蓝牙管理类
 * 
 * @author lzhn
 * 
 */
public class BTManager {

	/** {@link BTUtils} */
	private static BTUtils btUtils;
	/** 用作判断蓝牙名称是否符合的字符串 */
	private final String btName;
	/** {@link ClientThread} */
	private ClientThread clientThread;

	private OnManageConnectionListener onManageConnectionListener;
	private OnBtChangeListener onBtChangeListener;
	private OnBtStateChangeListener onBtStateChangeListener;
	private OnBtDiscoveredListener onBtDiscoveredListener;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Constant.WHAT_CONNECTEDSOCKET:
				if (null != onManageConnectionListener)
					onManageConnectionListener
							.onConnected((BluetoothSocket) msg.obj);
				break;
			case Constant.WHAT_DISCONNECTEDSOCKET:
				if (null != onManageConnectionListener)
					onManageConnectionListener
							.onDisconnected((BluetoothSocket) msg.obj);
				break;

			default:
				break;
			}
		};
	};

	private BTManager(Context context, String btName) {
		btUtils = BTUtils.getInstance(context);
		this.btName = btName;
	}

	/**
	 * 生成新的蓝牙管理类对象
	 * 
	 * @param context
	 * @param btName
	 *            用作判断蓝牙名称是否符合的字符串
	 * @return
	 */
	public static BTManager getNewInstance(Context context, String btName) {
		return new BTManager(context, btName);
	}

	public void setOnManageConnectionListener(
			OnManageConnectionListener onManageConnectionListener) {
		this.onManageConnectionListener = onManageConnectionListener;
	}

	public void setOnBtChangeListener(OnBtChangeListener onBtChangeListener) {
		this.onBtChangeListener = onBtChangeListener;
		btUtils.setBtChangeListener(this.onBtChangeListener);
	}

	public void setOnBtStateChangeListener(
			OnBtStateChangeListener onBtStateChangeListener) {
		this.onBtStateChangeListener = onBtStateChangeListener;
		btUtils.setBtStateChangeListener(this.onBtStateChangeListener);
	}

	public void setOnBtDiscoveredListener(
			OnBtDiscoveredListener onBtDiscoveredListener) {
		this.onBtDiscoveredListener = onBtDiscoveredListener;
		btUtils.setBtDiscoveredListener(this.onBtDiscoveredListener);
	}

	/**
	 * 判断蓝牙设备是否正确（蓝牙名称是否符合预定），使用{@link #btName}
	 * 
	 * @param device
	 */
	public boolean checkBtDevice(BluetoothDevice device) {
		if (device == null || device.getName() == null
				|| !device.getName().contains(btName)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 关闭蓝牙
	 */
	public void closeBluetooth() {
		BTUtils.closeBluetooth();
	}

	/**
	 * 启动蓝牙
	 */
	public void startBluetooth() {
		if (!BTUtils.isBtEnabled()) {
			BTUtils.startBluetooth();
		} else {
			bondOrDiscoverBtDevice();
		}
	}

	/**
	 * 蓝牙开启后、和之前配对的蓝牙设备进行连接、否则搜索设备
	 */
	public void bondOrDiscoverBtDevice() {
		if (BTUtils.isBtEnabled()) {
			BluetoothDevice device = BTUtils.getBluetoothDevice(
					BTUtils.getPairedDevices(), btName);
			if (onManageConnectionListener != null
					&& onManageConnectionListener.onCheckBtDevice(device)) {
				bondOrConnectBtDevice(device);
			} else {
				btUtils.discoverBluetooth();
			}
		}
	}

	/**
	 * 搜索到设备后调用、和蓝牙设备进行配对或建立连接通道
	 */
	public void bondOrConnectBtDevice(BluetoothDevice device) {
		if (onManageConnectionListener == null
				|| !onManageConnectionListener.onCheckBtDevice(device)) {
			return;
		}
		if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
			btUtils.createBond(BluetoothDevice.class, device);
		} else {
			connectBtDevice(device);
		}
	}

	/**
	 * 配对完成、建立连接通道
	 * 
	 * @param device
	 */
	public void connectBtDevice(BluetoothDevice device) {
		clientThread = ClientThread.getNewInstance(device, handler);
		clientThread.start();
	}

	public boolean isBtConnected() {
		return clientThread != null && clientThread.isConnected();
	}

	/** 回调接口，客户端-处理蓝牙连接 */
	public interface OnManageConnectionListener {
		/**
		 * 处理蓝牙建立连接后的操作
		 * 
		 * @param socket
		 */
		void onConnected(BluetoothSocket socket);

		void onDisconnected(BluetoothSocket socket);

		/**
		 * 判断蓝牙设备是否匹配
		 * 
		 * @param device
		 * @return true：设备匹配，可以连接；false：不匹配，放弃与此设备连接
		 */
		boolean onCheckBtDevice(BluetoothDevice device);
	}

}
