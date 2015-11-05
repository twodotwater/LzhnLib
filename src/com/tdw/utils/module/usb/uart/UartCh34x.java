package com.tdw.utils.module.usb.uart;

import static com.physicaloid.lib.usb.driver.uart.UartConfig.PARITY_NONE;

import android.content.Context;
import android.hardware.usb.UsbDeviceConnection;
import android.util.Log;

/**
 * Created by zzha on 2015/10/20. <br/>
 * NOTE: only used for CH34x.
 */
public class UartCh34x extends UartCdcAcm {
	private static final String TAG = UartCh34x.class.getSimpleName();
	public static final int DEFAULT_BAUDRATE = 9600;

	private static final int REQUEST_TYPE = 64;
	private static final int REQUEST = 161;

	public UartCh34x(Context context, UsbDeviceConnection mConnection) {
		super(context, mConnection);
	}

	@Override
	public boolean setBaudrate(int baudrate) {
		int ret = setCh34xConfig(baudrate, UartConfig.DATA_BITS8, UartConfig.STOP_BITS1, PARITY_NONE);
		if (ret < 0) {
			Log.d(TAG, "Fail to setBaudrate");
			return false;
		}
		mUartConfig.baudrate = baudrate;
		return true;
	}

	public int setCh34xConfig(int baudRate, int dataBit, int stopBit, int parity) {
		int value = 0;
		int index = 0;
		char valueHigh = '\0';
		char valueLow = '\0';
		char indexHigh = '\0';
		char indexLow = '\0';
		switch (parity) {
		case 0:
			valueHigh = '\0';
			break;
		case 1:
			valueHigh = (char) (valueHigh | 0x8);
			break;
		case 2:
			valueHigh = (char) (valueHigh | 0x18);
			break;
		case 3:
			valueHigh = (char) (valueHigh | 0x28);
			break;
		case 4:
			valueHigh = (char) (valueHigh | 0x38);
			break;
		default:
			valueHigh = '\0';
		}

		if (stopBit == 2) {
			valueHigh = (char) (valueHigh | 0x4);
		}

		switch (dataBit) {
		case 5:
			valueHigh = (char) (valueHigh | 0x0);
			break;
		case 6:
			valueHigh = (char) (valueHigh | 0x1);
			break;
		case 7:
			valueHigh = (char) (valueHigh | 0x2);
			break;
		case 8:
			valueHigh = (char) (valueHigh | 0x3);
			break;
		default:
			valueHigh = (char) (valueHigh | 0x3);
		}

		valueHigh = (char) (valueHigh | 0xC0);
		valueLow = 156;

		value |= valueLow;
		value |= valueHigh << '\b';

		switch (baudRate) {
		case 50:
			indexLow = '\0';
			indexHigh = '\22';
			break;
		case 75:
			indexLow = '\0';
			indexHigh = 'd';
			break;
		case 110:
			indexLow = '\0';
			indexHigh = 150;
			break;
		case 135:
			indexLow = '\0';
			indexHigh = 169;
			break;
		case 150:
			indexLow = '\0';
			indexHigh = 178;
			break;
		case 300:
			indexLow = '\0';
			indexHigh = 217;
			break;
		case 600:
			indexLow = '\1';
			indexHigh = 'd';
			break;
		case 1200:
			indexLow = '\1';
			indexHigh = 178;
			break;
		case 1800:
			indexLow = '\1';
			indexHigh = 204;
			break;
		case 2400:
			indexLow = '\1';
			indexHigh = 217;
			break;
		case 4800:
			indexLow = '\2';
			indexHigh = 'd';
			break;
		case 9600:
			indexLow = '\2';
			indexHigh = 178;
			break;
		case 19200:
			indexLow = '\2';
			indexHigh = 217;
			break;
		case 38400:
			indexLow = '\3';
			indexHigh = 'd';
			break;
		case 57600:
			indexLow = '\3';
			indexHigh = 152;
			break;
		case 115200:
			indexLow = '\3';
			indexHigh = 204;
			break;
		case 230400:
			indexLow = '\3';
			indexHigh = 230;
			break;
		case 460800:
			indexLow = '\3';
			indexHigh = 243;
			break;
		case 500000:
			indexLow = '\3';
			indexHigh = 244;
			break;
		case 921600:
			indexLow = '\7';
			indexHigh = 243;
			break;
		case 1000000:
			indexLow = '\3';
			indexHigh = 250;
			break;
		case 2000000:
			indexLow = '\3';
			indexHigh = 253;
			break;
		case 3000000:
			indexLow = '\3';
			indexHigh = 254;
			break;
		default:
			indexLow = '\2';
			indexHigh = 178;
		}

		index = index | 0x88 | indexLow;
		index |= indexHigh << '\b';

		return mConnection.controlTransfer(REQUEST_TYPE, REQUEST, value, index, null, 0, 500);
	}

}
