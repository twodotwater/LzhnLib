package com.lzhn.utils.bluetooth;

import java.util.UUID;

public class Constant {
	/** 打开蓝牙时使用的请求码 */
	public static final int REQUEST_BLUETOOTH_ENABLE = 1000;
	/** 蓝牙可见时间 */
	public static final int DURATION = 120;
	/** 蓝牙设备的通用识别码 */
	public static final UUID MY_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");
	/** 唯一的字符串，表示作为服务器的蓝牙设备 */
	public static final String SERVER_BLUETOOTH_NAME = "com.lzhn.utils.bluetooth";

	/** 建立蓝牙连接时，发送的message */
	public static final int WHAT_CONNECTEDSOCKET = 0xB0;
	/** 蓝牙连接失败时，发送的message */
	public static final int WHAT_DISCONNECTEDSOCKET = 0xBE;
	/** 刷新显示时间时，发送的message */
	public static final int WHAT_TIME = 0xff;

	/** 接收数据时，发送的message */
	public static final int WHAT_RECEIVEDATA = 0xB1;

}
