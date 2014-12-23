package com.lzhn.utils.usb;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbRequest;

import com.lzhn.lib.MainActivity;
import com.lzhn.utils.file.FileUtils;
import com.lzhn.utils.print.LogUtils;
import com.lzhn.utils.usb.UsbUtils.OnUsbStateChangedListener;
import com.lzhn.utils.view.ToastUtils;

public class TransManager {
	private static final String Tag = "TransManager";

	private static final int TIMEOUT = 0;

	private static Context context;
	private UsbUtils usbUtils;

	private UsbDevice device;
	private List<UsbEndpoint> endpoints;
	private UsbInterface usbInterface;
	private UsbEndpoint endpointIn;
	private UsbEndpoint endpointOut;
	private UsbDeviceConnection connection;

	public TransManager(Context context) {
		this.usbUtils = new UsbUtils(context);
		TransManager.context = context;
		usbUtils.setOnUsbStateChangedListener(new OnUsbStateChangedListener() {

			@Override
			public void onUsbDeviceDetached(UsbDevice device) {
			}

			@Override
			public void onUsbDeviceAttached(UsbDevice device) {
				// TODO 设备连接
				init(device);
			}
		});
	}

	public void init() {
		HashMap<String, UsbDevice> maps = usbUtils.getDeviceList();
		if (maps.size() > 0) {
			for (Map.Entry<String, UsbDevice> entry : maps.entrySet()) {
				String name = entry.getKey();
				UsbDevice device = entry.getValue();
				MainActivity.writeInfo(name + "设备已连接");
				init(device);
			}
		} else {
			initUsbStorage();
		}
	}

	private void initUsbStorage() {
		MainActivity
				.writeInfo("存储:\n" + FileUtils.getAllExternalStoragePaths());
		if (FileUtils.getUsbStoragePath() == null) {
			return;
		}
		MainActivity.writeInfo("usb路径：" + FileUtils.getUsbStoragePath());
		List<File> fileList = FileUtils.getListFiles(
				FileUtils.getUsbStoragePath(), null, false);
		for (File file : fileList) {
			LogUtils.printExcp(Tag, file.getName());
		}
	}

	public void init(UsbDevice device) {
		this.device = device;
		connection = usbUtils.openDevice(device);
		connection = usbUtils.openDevice(device);
		connection = usbUtils.openDevice(device);
		if (connection == null) {
			ToastUtils.showToast(context, "连接还未打开！");
			return;
		}
		usbInterface = device.getInterface(0);
		connection.claimInterface(usbInterface, true);
		endpoints = new ArrayList<UsbEndpoint>();
		for (int i = 0; i < usbInterface.getEndpointCount(); i++) {
			UsbEndpoint endpoint = usbInterface.getEndpoint(i);
			endpoints.add(endpoint);
			if (UsbConstants.USB_DIR_IN == endpoint.getDirection()) {
				endpointIn = endpoint;
			}
			if (UsbConstants.USB_DIR_OUT == endpoint.getDirection()) {
				endpointOut = endpoint;
			}
		}
		initUsbStorage();
	}

	/**
	 * 发送数据
	 * 
	 * @param buffer
	 * @return
	 */
	public int send(byte[] buffer) {
		return trans(endpointOut, buffer, buffer.length);
	}

	/**
	 * 读取数据
	 * 
	 * @param buffer
	 * @return
	 */
	public int read(byte[] buffer) {
		return trans(endpointIn, buffer, buffer.length);
	}

	public int trans(UsbEndpoint endpoint, byte[] buffer, int length) {
		if (connection == null || endpoint == null || buffer == null)
			return -1;
		return connection.bulkTransfer(endpoint, buffer, length, TIMEOUT);
	}

	public void trans(UsbEndpoint endpoint, Object obj) {
		if (connection == null || endpoint == null)
			return;
		UsbRequest request = connection.requestWait();
		// request.initialize(connection, endpoint);
		if (request.getEndpoint().equals(endpoint)) {
			obj = request.getClientData();
		}
	}

	public void registerReceiver() {
		this.usbUtils.registerReceiver();
	}

	public void unregisterReceiver() {
		this.usbUtils.unregisterReceiver();
	}

	public void closeConnection() {
		if (connection == null)
			return;
		connection.releaseInterface(usbInterface);
		connection.close();
	}
}
