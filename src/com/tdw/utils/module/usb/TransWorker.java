package com.tdw.utils.module.usb;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lzhn.utils.print.LogUtils;
import com.lzhn.utils.view.ToastUtils;
import com.tdw.utils.module.worker.ITransWorker;

import android.content.Context;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbRequest;
import android.os.Handler;

public abstract class TransWorker implements ITransWorker {
	private static final String TAG = "TransWorker";
	public static final int TIMEOUT = 1000;

	protected Context context;
	protected UsbDeviceConnection connection;
	protected UsbDevice device;
	protected List<UsbEndpoint> endpoints;
	protected UsbInterface usbInterface;
	protected UsbEndpoint endpointIn;
	protected UsbEndpoint endpointOut;

	protected Handler handler;

	protected boolean isWorking = false;
	protected ExecutorService executor = Executors.newCachedThreadPool();
	protected Runnable readRunnable = new Runnable() {
		@Override
		public void run() {
			doReadWork();
		}
	};
	protected Runnable writeRunnable = new Runnable() {
		@Override
		public void run() {
			doWriteWork();
		}
	};

	protected void doWriteWork() {
	}

	protected void doReadWork() {
	}

	public TransWorker(Context context, UsbDevice device, Handler handler) {
		this.context = context;
		this.handler = handler;
		connection = new UsbUtils(context).openDevice(device);
		if (connection == null) {
			ToastUtils.showToast(context, "连接还未打开！");
			return;
		} else {
			ToastUtils.showToast(context, "连接打开:" + device.getDeviceName());
		}
		usbInterface = device.getInterface(0);
		boolean claim = connection.claimInterface(usbInterface, true);
		// if (usbInterface.getEndpointCount() >= 2) {
		// UsbEndpoint endpoint = usbInterface.getEndpoint(0);
		// if (UsbConstants.USB_DIR_IN == endpoint.getDirection()) {
		// endpointIn = endpoint;
		// endpointOut = usbInterface.getEndpoint(1);
		// } else if (UsbConstants.USB_DIR_OUT == endpoint.getDirection()) {
		// endpointIn = usbInterface.getEndpoint(1);
		// endpointOut = endpoint;
		// }
		// }
		endpoints = new ArrayList<UsbEndpoint>();
		for (int i = 0; i < usbInterface.getEndpointCount(); i++) {
			UsbEndpoint endpoint = usbInterface.getEndpoint(i);
			endpoints.add(endpoint);
			if (UsbConstants.USB_DIR_IN == endpoint.getDirection()) {
				endpointIn = endpoint;
			} else if (UsbConstants.USB_DIR_OUT == endpoint.getDirection()) {
				endpointOut = endpoint;
			}
			if (endpointIn != null && endpointOut != null) {
				break;
			}
		}
		if (endpointIn == null || endpointOut == null) {
			ToastUtils.showToast(context, "端口未全打开！");
		}
	}

	public void startThreads() {
		isWorking = true;
		executor.execute(writeRunnable);
		executor.execute(readRunnable);
		executor.shutdown();
	}

	public boolean isWorking() {
		return isWorking;
	}

	public void stopWork() {
		isWorking = false;
		executor.shutdownNow();
	}

	public void closeConnection() {
		if (connection == null)
			return;
		connection.releaseInterface(usbInterface);
		connection.close();
	}

	/**
	 * 发送数据
	 * 
	 * @param buffer
	 * @return length of data transferred (or zero) for success, or negative
	 *         value for failure
	 */
	public int write(byte[] buffer) {
		try {
			return synTrans(endpointOut, buffer, buffer.length);
		} catch (Exception e) {

		}
		return -1;
	}

	/**
	 * 读取数据
	 * 
	 * @param buffer
	 * @return length of data transferred (or zero) for success, or negative
	 *         value for failure
	 */
	public int read(byte[] buffer) {
		return synTrans(endpointIn, buffer, buffer.length);
	}

	/**
	 * Endpoints are the channels for sending and receiving data over USB.
	 * 
	 * @param endpoint
	 * @param buffer
	 * @param length
	 * @return length of data transferred (or zero) for success, or negative
	 *         value for failure
	 */
	public int synTrans(UsbEndpoint endpoint, byte[] buffer, int length) {
		if (connection != null && endpoint != null && buffer != null) {
			try {
				connection.claimInterface(usbInterface, false);
				return connection.bulkTransfer(endpoint, buffer, length, TIMEOUT);
			} catch (Exception e) {
				LogUtils.printExcp(TAG, e.getLocalizedMessage());
			}
		}
		return -1;
	}

	public int controlTrans(int requestType, int request, int value, int index, byte[] buffer, int length) {
		return connection.controlTransfer(requestType, request, value, index, buffer, length, TIMEOUT);
	}

	public boolean asynRead(ByteBuffer buffer, int length, Object clientData) {
		return asynTrans(endpointIn, buffer, length, clientData);
	}

	public boolean asynWrite(ByteBuffer buffer, int length, Object clientData) {
		return asynTrans(endpointOut, buffer, length, clientData);
	}

	public boolean asynTrans(final UsbEndpoint endpoint, final ByteBuffer buffer, final int length,
			final Object clientData) {
		connection.claimInterface(usbInterface, false);
		UsbRequest request = new UsbRequest();
		boolean isOpened = request.initialize(connection, endpoint);
		if (isOpened) {
			request.setClientData(clientData);
			boolean res = request.queue(buffer, length);
			if (res) {
				LogUtils.printExcp(TAG,
						endpoint.getDirection() == UsbConstants.USB_DIR_OUT ? "write success" : "read success");
				UsbRequest resRequest = connection.requestWait();
				if (resRequest != null && resRequest == request) {
					return true;
				}
			}
		}
		return false;
	}

	public interface OnInitTransWorkerListener {
		TransWorker onInitTransWorker(UsbDevice usbDevice);
	}
}
