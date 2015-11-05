package com.tdw.utils.module.usb;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import com.lzhn.utils.thread.ThreadUtils;
import com.lzhn.utils.view.ToastUtils;
import com.tdw.utils.module.usb.uart.UartCp210x;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.os.Handler;

public class GammaTransWorker extends TransWorker {
	// 指令编号
	public static final int HV = 0;
	public static final int CB = 1;
	public static final int CS = 2;
	public static final int TM = 3;
	public static final int TT = 4;
	public static final int OF = 5;
	public static final int CD = 6;
	public static final int QC = 7;
	public static final int CE = 8;
	public static final int RE = 9;
	public static final int BT = 10;
	public static final int PT = 11;
	public static final int SY = 12;
	// 指令名
	public static final String[] CMD_NAME = { "设置高压指令", "启动测量指令", "停止测量指令", "设置活时间测量指令", "设置实时间测量指令", "设置零点偏移",
			"清除测量数据指令", "设置二次压缩与否", "查询测量状态指令", "出错重新发送数据指令", "读取探测器内部电池电压指令", "设置探测器波特率指令", "发送同步字符串" };

	// 有关探测器的指令串
	byte[] insHV = { (byte) 0x80, 0x18, (byte) 0xb8, (byte) 0xd0, 0x0d }; // 设置高压指令
	public static byte[] insCB = { (byte) 0x81, 0x0d }; // 启动测量指令
	byte[] insCS = { (byte) 0x82, 0x0d }; // 停止测量指令
	byte[] insTM = { (byte) 0x83, 0, 0, 0, 0, 0x0d }; // 设置活时间测量指令
	byte[] insTT = { (byte) 0x84, 0, 0, 0, 0, 0x0d }; // 设置实时间测量指令
	byte[] insOF = { (byte) 0x85, (byte) 0xcc, 0x20, 0, 0x0d }; // 设置零点偏移
	byte[] insCD = { (byte) 0x86, 0x0d }; // 清除测量数据指令
	byte[] insQC = { (byte) 0x87, 0, 0x0d }; // 设置二次压缩与否
	public static byte[] insCE = { (byte) 0x88, 0x0d }; // 查询测量状态指令
	byte[] insRE = { (byte) 0x89, 0x0d }; // 出错重新发送数据指令
	byte[] insBT = { (byte) 0x8A, 0x0d }; // 读取探测器内部电池电压指令
	byte[] insPT = { (byte) 0x98, (byte) 0xcc, 0x07, 0x0d }; // 设置探测器波特率指令
	public static byte[] insSY = { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA,
			(byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA,
			(byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA };

	public GammaTransWorker(Context context, UsbDevice device, final Handler handler) {
		super(context, device, handler);
		uartSettings = new UartCp210x(context, connection);
		int rate = uartSettings.getBaudrate();
		ToastUtils.showToast(context, "当前：" + rate);
		// if (rate != 19200 && setBaudrate(19200)) {
		// ToastUtils.showToast(context, "更新：" + uartSettings.getBaudrate());
		// }
	}

	private ReentrantLock lock = new ReentrantLock();
	private Condition readCondition = lock.newCondition();
	private Condition writeCondition = lock.newCondition();
	public static int workState = SY;
	// private ByteBuffer buffer = ByteBuffer.allocate(4111);
	private byte[] buffer = new byte[1];

	public void setBuffer(byte[] data) {
		this.buffer = data.clone();
		int len = write(buffer);
		if (len >= 0) {
			handler.obtainMessage(SY, len, 0, buffer).sendToTarget();
		}
	}

	@Override
	protected void doReadWork() {
		super.doReadWork();
		while (isWorking()) {
			// if (workState == SY) {
			// lock.lock();
			int size = 256;
			byte[] res = new byte[size];
			int len = read(res);
			if (len >= 0) {
				handler.obtainMessage(HV, len, size, res).sendToTarget();
			} else {
				handler.obtainMessage(CB, len, size, res).sendToTarget();
			}

			// ByteBuffer buffer = ByteBuffer.allocate(size);
			// if (asynRead(buffer, size, null)) {
			// handler.obtainMessage(HV, buffer.capacity(), 0,
			// buffer.array()).sendToTarget();
			// }
			//
			// len = controlTrans(UsbConstants.USB_DIR_IN, 0x01, 1, 1, res, 1);
			// if (len >= 0)
			// handler.obtainMessage(HV, res[0]).sendToTarget();
			// }

			// lock.unlock();
			ThreadUtils.sleep(TIMEOUT);
		}
	}

	@Override
	protected void doWriteWork() {
		super.doWriteWork();
		while (isWorking()) {
			if (workState == SY) {
				lock.lock();

				ThreadUtils.sleep(TIMEOUT);

				lock.unlock();
			}
			break;
		}
	}
}
