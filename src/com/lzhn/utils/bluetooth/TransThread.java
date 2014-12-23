package com.lzhn.utils.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

/**
 * 蓝牙通道数据交互的线程
 * 
 * @author lzhn
 * 
 */
public abstract class TransThread extends Thread {
	protected static final String TAG = "TransThread";

	private final BluetoothSocket mSocket;
	protected final InputStream mInStream;
	protected final OutputStream mOutStream;
	/** 和此线程进行数据交互的handler */
	protected Handler handler;

	/** 线程是否正在运行 */
	private boolean isWorking = true;

	/**
	 * 完成线程运行处理任务。<br/>
	 * Note：本方法运行在单独的线程中，该线程已建立Looper
	 */
	public abstract void doWork();

	private TransThread() {
		super();
		mSocket = null;
		mInStream = null;
		mOutStream = null;
	}

	public TransThread(BluetoothSocket socket) {
		mSocket = socket;
		InputStream tmpIn = null;
		OutputStream tmpOut = null;
		try {
			tmpIn = socket.getInputStream();
			tmpOut = socket.getOutputStream();
		} catch (IOException e) {
		}
		mInStream = tmpIn;
		mOutStream = tmpOut;
	}

	public TransThread(BluetoothSocket socket, Handler handler) {
		this(socket);
		this.handler = handler;
	}

	/**
	 * 指定线程消息传输的handler
	 * 
	 * @param handler
	 */
	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	/**
	 * 判断此线程是否正在工作
	 * 
	 * @return
	 */
	public boolean isWorking() {
		return isWorking;
	}

	/**
	 * 数据交互线程结束
	 */
	public void stopWork() {
		this.isWorking = false;
	}

	/**
	 * 向消息缓冲池发送蓝牙连接中断消息<br/>
	 * {@link Constant#WHAT_DISCONNECTEDSOCKET}
	 * 
	 * @param e
	 */
	public void sendError(Exception e) {
		Log.e(TAG, "--error-" + e.getLocalizedMessage());
		handler.sendEmptyMessage(Constant.WHAT_DISCONNECTEDSOCKET);
	}

	/**
	 * 向蓝牙设备发送指令{@link java.io.OutputStream#write(byte[], int, int)}
	 * 
	 * @param bytes
	 *            指令对应的字节数组
	 * @param start
	 *            发送内容在数组的偏移位置
	 * @param len
	 *            发送字节长度
	 */

	public void write(byte[] bytes, int start, int len) {
		// Log.i(TAG, "---指令长度：" + bytes.length + " 个字节");
		try {
			mOutStream.write(bytes, start, len);
			mOutStream.flush();
		} catch (IOException e) {
			Log.e(TAG, "--" + e.getMessage());
		}
	}

	/**
	 * 停止线程、关闭蓝牙socket连接
	 */
	public void stopSelf() {
		try {
			stopWork();
			mSocket.close();
		} catch (IOException e) {
			Log.e(TAG, "--" + e.getMessage());
		}
	}

	@Override
	public void run() {
		super.run();
		while (isWorking) {
			Looper.prepare();
			doWork();
			Looper.loop();
		}
	}

}
