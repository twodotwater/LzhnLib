package com.lzhn.utils.bluetooth;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

/**
 * 作为客户端进行蓝牙连接
 */
public class ClientThread extends Thread {
	private static final String TAG = "ClientThread";

	private final BluetoothSocket mBluetoothSocket;
	private final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
			.getDefaultAdapter();
	/** 和此连接线程进行通信 */
	private Handler mHandler;
	/** 蓝牙设备是否连接 */
	private boolean isConnected = false;

	private ClientThread(BluetoothDevice device, Handler handler) {
		BluetoothSocket tmp = null;
		try {
			// 创建符合RFCOMM协议的BluetoothSocket
			tmp = device
					.createInsecureRfcommSocketToServiceRecord(Constant.MY_UUID);
			// tmp = reflectCreateSocket(device);
			Log.e(TAG,
					"--create-" + device.getName() + " ~ "
							+ device.getAddress());
			// Log.i(TAG, "--create-" + Constants.MY_UUID);
		} catch (IOException e) {
			Log.e(TAG, "--create-" + e.getLocalizedMessage());
		}
		this.mBluetoothSocket = tmp;
		this.mHandler = handler;
	}

	/**
	 * 生成新的蓝牙通道连接线程对象
	 * 
	 * @param device
	 * @param handler
	 *            和此连接线程进行通信的handler
	 * @return
	 */
	public static ClientThread getNewInstance(BluetoothDevice device,
			Handler handler) {
		return new ClientThread(device, handler);
	}

	public void setmHandler(Handler mHandler) {
		this.mHandler = mHandler;
	}

	public boolean isConnected() {
		return isConnected;
	}

	private BluetoothSocket reflectCreateSocket(BluetoothDevice device) {
		Method m;
		try {
			m = device.getClass().getMethod(
					"createInsecureRfcommSocketToServiceRecord",
					new Class[] { UUID.class });
			return (BluetoothSocket) m.invoke(device, Constant.MY_UUID);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	public synchronized void run() {

		connect();
	}

	private void connect() {
		// 关闭搜索
		mBluetoothAdapter.cancelDiscovery();
		try {
			// 建立连接，成功之前处于阻塞状态
			mBluetoothSocket.connect();

			// 成功建立连接
			if (mBluetoothSocket.isConnected()) {
				isConnected = mBluetoothSocket.isConnected();
				Log.e(TAG, "--connect-" + isConnected);
				mHandler.obtainMessage(Constant.WHAT_CONNECTEDSOCKET,
						mBluetoothSocket).sendToTarget();
				return;
			}

		} catch (IOException e) {
			mHandler.obtainMessage(Constant.WHAT_DISCONNECTEDSOCKET)
					.sendToTarget();

			Log.e(TAG, "--connect-" + e.getLocalizedMessage());
			// e.printStackTrace();
			closeConnection();
		}
	}

	/** 关闭连接 */
	public void closeConnection() {
		isConnected = false;
		try {
			mBluetoothSocket.close();
		} catch (IOException e) {
			Log.e(TAG, "--" + e.getLocalizedMessage());
		}
	}
}