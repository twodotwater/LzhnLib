package com.lzhn.utils.bluetooth;

import java.io.IOException;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

/**
 * 作为蓝牙服务器，建立连接
 */
public class ServerThread extends Thread {
	private static final String TAG = "AcceptThread";

	private final BluetoothServerSocket mServerSocket;

	private static boolean isStoped = false;

	public ServerThread() {
		BluetoothServerSocket tmp = null;
		try {
			Log.e(TAG, "--create-" + Constant.MY_UUID);
			// 创建符合RFCOMM协议的侦听BluetoothServerSocket
			tmp = BluetoothAdapter.getDefaultAdapter()
					.listenUsingRfcommWithServiceRecord(
							Constant.SERVER_BLUETOOTH_NAME, Constant.MY_UUID);
		} catch (IOException e) {
			Log.e(TAG, "--create-" + e.getLocalizedMessage());
		}
		mServerSocket = tmp;
	}

	public void run() {
		BluetoothSocket socket = null;
		// 持续侦听
		while (!isStoped) {
			try {
				// 建立连接，成功之前处于阻塞状态
				socket = mServerSocket.accept();
				Log.i(TAG, "--connect-" + socket.isConnected());
			} catch (IOException e) {
				Log.e(TAG, "--connect-" + e.getLocalizedMessage());
				break;
			}
		}
	}

	/** 取消侦听的socket，断开连接 */
	private void closeServerSocket() {
		try {
			mServerSocket.close();
		} catch (IOException e) {
		}
	}

	/** 取消侦听的socket，断开连接，终止线程 */
	public void stopSelf() {
		isStoped = true;
		closeServerSocket();
	}

}