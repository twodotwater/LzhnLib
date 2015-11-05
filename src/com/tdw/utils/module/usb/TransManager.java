package com.tdw.utils.module.usb;

import java.util.HashMap;
import java.util.Map;

import com.tdw.utils.module.usb.TransWorker.OnInitTransWorkerListener;
import com.tdw.utils.module.usb.UsbUtils.OnUsbStateChangedListener;
import com.tdw.utils.module.worker.WorkerManager;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.os.Handler;

public class TransManager extends WorkerManager implements OnUsbStateChangedListener {
	private static final String Tag = "TransManager";

	private static Context context;
	private UsbUtils usbUtils;

	private OnInitTransWorkerListener onInitTransWorkerListener;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {

			default:
				break;
			}
		};
	};

	public TransManager(final Context context) {
		this.usbUtils = new UsbUtils(context);
		TransManager.context = context;
		usbUtils.setOnUsbStateChangedListener(this);
		init();
	}

	public void init() {
		HashMap<String, UsbDevice> maps = usbUtils.getDeviceList();
		if (maps != null && maps.size() > 0) {
			for (Map.Entry<String, UsbDevice> entry : maps.entrySet()) {
				String name = entry.getKey();
				UsbDevice device = entry.getValue();
				onUsbDeviceAttached(device);
			}
		}
	}

	public TransWorker initTransWorker(UsbDevice usbDevice) {
		return initTransWorker(usbDevice.getVendorId());
	}

	public TransWorker initTransWorker(int vid) {
		return null;
	}

	@Override
	public void onUsbDeviceDetached(UsbDevice device) {
		removeTransWorker(device.getDeviceName());
	}

	@Override
	public void onUsbDevicePermitted(UsbDevice device) {
		if (onInitTransWorkerListener != null) {
			TransWorker transWorker = onInitTransWorkerListener.onInitTransWorker(device);
			if (transWorker != null) {
				addTransWorker(device.getDeviceName(), transWorker);
			}
		}
	}

	@Override
	public boolean onUsbDeviceAttached(UsbDevice device) {
		return checkDeviceValidity(device);
	}

	private boolean checkDeviceValidity(UsbDevice device) {
		return true;
	}

	public void registerReceiver() {
		this.usbUtils.registerReceiver();
	}

	public void unregisterReceiver() {
		this.usbUtils.unregisterReceiver();
	}

}
