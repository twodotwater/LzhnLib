package com.tdw.utils.module.usb;

import java.util.HashMap;

import com.lzhn.utils.print.LogUtils;
import com.lzhn.utils.view.ToastUtils;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.ParcelFileDescriptor;

public class UsbUtils extends BroadcastReceiver {
	private static final String TAG = "UsbUtils";

	private static final String ACTION_USB_PERMISSION_DEVICE = "com.tdw.utils.usb.USB_PERMISSION_DEVICE";
	private static final String ACTION_USB_PERMISSION_ACCESSORY = "com.tdw.utils.usb.USB_PERMISSION_ACCESSORY";
	/** Usb设备状态变化广播 */
	public static final String ACTION_USB_STATE = "android.hardware.usb.action.USB_STATE";

	/**
	 * Boolean extra indicating whether USB is connected or disconnected. <br/>
	 * Used in extras for the {@link #ACTION_USB_STATE} broadcast.
	 * 
	 */
	public static final String USB_CONNECTED = "connected";

	private static Context context;
	private static UsbManager usbManager;
	private OnUsbStateChangedListener onUsbStateChangedListener;

	public UsbUtils(Context context) {
		UsbUtils.context = context;
		getUsbManager();
	}

	public void setOnUsbStateChangedListener(OnUsbStateChangedListener onUsbStateChangedListener) {
		this.onUsbStateChangedListener = onUsbStateChangedListener;
	}

	public UsbManager getUsbManager() {
		if (usbManager == null)
			usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
		return usbManager;
	}

	/**
	 * {@link UsbDeviceConnection
	 * android.hardware.usb.UsbManager#openDevice(UsbDevice)}
	 * 
	 * @param device
	 * @return
	 */
	public UsbDeviceConnection openDevice(UsbDevice device) {
		if (device == null)
			return null;
		return usbManager.openDevice(device);
	}

	public ParcelFileDescriptor openAccessory(UsbAccessory accessory) {
		if (accessory == null)
			return null;
		return usbManager.openAccessory(accessory);
	}

	public UsbAccessory[] getAccessoryList() {
		return usbManager.getAccessoryList();
	}

	public HashMap<String, UsbDevice> getDeviceList() {
		return usbManager.getDeviceList();
	}

	/**
	 * 注册广播过滤器
	 */
	public void registerReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(UsbManager.ACTION_USB_ACCESSORY_ATTACHED);
		filter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
		filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
		filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
		filter.addAction(ACTION_USB_PERMISSION_ACCESSORY);
		filter.addAction(ACTION_USB_PERMISSION_DEVICE);
		context.registerReceiver(this, filter);
	}

	public void unregisterReceiver() {
		context.unregisterReceiver(this);
	}

	/**
	 * 请求作为从设连接主机
	 * 
	 * @param accessory
	 * @return
	 */
	public boolean requestPermission(UsbAccessory accessory) {
		if (accessory == null) {
			return false;
		}
		if (usbManager.hasPermission(accessory)) {
			return true;
		}
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION_ACCESSORY), 0);
		usbManager.requestPermission(accessory, pi);
		return false;
	}

	/**
	 * 请求连接usb设备
	 * 
	 * @param device
	 * @return
	 */
	public boolean requestPermission(UsbDevice device) {
		if (device == null) {
			return false;
		}
		if (usbManager.hasPermission(device)) {
			return true;
		}
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION_DEVICE), 0);
		usbManager.requestPermission(device, pi);
		return false;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		ToastUtils.showToast(context, action);

		/*************************************/
		/* 手机作为host连接到其他从设 */
		/*************************************/

		// 连接从设请求权限
		if (ACTION_USB_PERMISSION_DEVICE.equals(action)) {
			synchronized (this) {
				// 获取从属设备
				UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
				if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
					if (usbDevice != null) {
						ToastUtils.showToast(context, "从设已获取权限：" + usbDevice.getDeviceName());
						LogUtils.printExcp(TAG, "从设信息：" + usbDevice.toString());

						if (onUsbStateChangedListener != null) {
							onUsbStateChangedListener.onUsbDevicePermitted(usbDevice);
						}
					}
				} else {
					ToastUtils.showToast(context, "请求访问权限被拒绝！");
				}
			}
			return;
		}

		// 从设连接
		if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
			UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
			if (usbDevice != null && onUsbStateChangedListener != null) {
				if (onUsbStateChangedListener.onUsbDeviceAttached(usbDevice)) {
					// 验证通过
					if (requestPermission(usbDevice)) {
						onUsbStateChangedListener.onUsbDevicePermitted(usbDevice);
					}
				}
			}
			return;
		}
		// 从设断开连接
		if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
			UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
			if (usbDevice != null) {
				ToastUtils.showToast(context, "从设断开连接：" + usbDevice.getDeviceName());
				if (onUsbStateChangedListener != null) {
					onUsbStateChangedListener.onUsbDeviceDetached(usbDevice);
				}
			}
			return;
		}
		if (USB_CONNECTED.equals(intent.getStringExtra(ACTION_USB_STATE))) {
			ToastUtils.showToast(context, "usb连接");
		}

		/*************************************/
		/* 手机作为从设连接到其他主机 */
		/*************************************/

		// 连接主机请求权限
		if (ACTION_USB_PERMISSION_ACCESSORY.equals(action)) {
			synchronized (this) {
				// 获取从属设备UsbAccessory
				UsbAccessory usbAccessory = intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
				if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
					if (usbAccessory != null) {
						ToastUtils.showToast(context, "主机已连接：" + usbAccessory.getDescription());
						LogUtils.printExcp(TAG, "主机信息：" + usbAccessory.toString());
					}
				} else {
					ToastUtils.showToast(context, "请求被主机拒绝！");
				}
			}
			return;
		}

		// 主机连接
		if (UsbManager.ACTION_USB_ACCESSORY_ATTACHED.equals(action)) {
			UsbAccessory usbAccessory = intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
			if (usbAccessory != null) {

				requestPermission(usbAccessory);
			}
			return;
		}
		// 主机断开连接
		if (UsbManager.ACTION_USB_ACCESSORY_DETACHED.equals(action)) {
			UsbAccessory usbAccessory = intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
			if (usbAccessory != null) {
				ToastUtils.showToast(context, "主机断开连接：" + usbAccessory.getDescription());
			}
			return;
		}
	}

	interface OnUsbStateChangedListener {
		void onUsbDevicePermitted(UsbDevice device);

		boolean onUsbDeviceAttached(UsbDevice device);

		void onUsbDeviceDetached(UsbDevice device);
	}
}
