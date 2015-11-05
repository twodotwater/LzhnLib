package com.tdw.utils.module.usb.uart;

import android.content.Context;
import android.hardware.usb.UsbDeviceConnection;
import android.util.Log;

public class UartCdcAcm extends UartController {
	private static final String TAG = UartCdcAcm.class.getSimpleName();
	private static final int DEFAULT_BAUDRATE = 9600;

	protected UartConfig mUartConfig;

	public UartCdcAcm(Context context, UsbDeviceConnection mConnection) {
		super(context, mConnection);
		mUartConfig = new UartConfig(DEFAULT_BAUDRATE);
	}

	@Override
	public boolean open() {
		if (!init()) {
			return false;
		}
		if (!setBaudrate(DEFAULT_BAUDRATE)) {
			return false;
		}
		isOpened = true;
		return true;
	}

	@Override
	public boolean close() {
		isOpened = false;
		mConnection.close();
		return isOpened;
	}

	@Override
	public int read(byte[] buf, int size) {
		return size;
	}

	@Override
	public int write(byte[] buf, int size) {
		return size;
	}

	@Override
	public boolean setUartConfig() {
		setBaudrate(mUartConfig.baudrate);
		setDataBits(mUartConfig.dataBits);
		setParity(mUartConfig.parity);
		setStopBits(mUartConfig.stopBits);
		setDtrRts(mUartConfig.dtrOn, mUartConfig.rtsOn);
		return true;
	}

	/**
	 * Sets Uart configurations
	 * 
	 * @param config
	 *            configurations
	 * @return true : successful, false : fail
	 */
	public boolean setUartConfig(UartConfig config) {
		boolean res = true;
		boolean ret = true;
		if (mUartConfig.baudrate != config.baudrate) {
			res = setBaudrate(config.baudrate);
			ret = ret && res;
		}

		if (mUartConfig.dataBits != config.dataBits) {
			res = setDataBits(config.dataBits);
			ret = ret && res;
		}

		if (mUartConfig.parity != config.parity) {
			res = setParity(config.parity);
			ret = ret && res;
		}

		if (mUartConfig.stopBits != config.stopBits) {
			res = setStopBits(config.stopBits);
			ret = ret && res;
		}

		if (mUartConfig.dtrOn != config.dtrOn || mUartConfig.rtsOn != config.rtsOn) {
			res = setDtrRts(config.dtrOn, config.rtsOn);
			ret = ret && res;
		}

		return ret;
	}

	/**
	 * Initializes CDC communication
	 * 
	 * @return true : successful, false : fail
	 */
	private boolean init() {
		if (mConnection == null)
			return false;
		int ret = mConnection.controlTransfer(0x21, 0x22, 0x00, 0, null, 0, 0); // init
		// CDC
		if (ret < 0) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isOpened() {
		return isOpened;
	}

	/**
	 * Sets baudrate
	 * 
	 * @param baudrate
	 *            baudrate e.g. 9600
	 * @return true : successful, false : fail
	 */
	public boolean setBaudrate(int baudrate) {
		byte[] baudByte = new byte[4];
		baudByte[0] = (byte) (baudrate & 0x000000FF);
		baudByte[1] = (byte) ((baudrate & 0x0000FF00) >> 8);
		baudByte[2] = (byte) ((baudrate & 0x00FF0000) >> 16);
		baudByte[3] = (byte) ((baudrate & 0xFF000000) >> 24);
		int ret = mConnection.controlTransfer(0x21, 0x20, 0, 0,
				new byte[] { baudByte[0], baudByte[1], baudByte[2], baudByte[3], 0x00, 0x00, 0x08 }, 7, 100);
		if (ret < 0) {
			Log.d(TAG, "Fail to setBaudrate");
			return false;
		}
		mUartConfig.baudrate = baudrate;
		return true;
	}

	/**
	 * Sets Data bits
	 * 
	 * @param dataBits
	 *            data bits e.g. UartConfig.DATA_BITS8
	 * @return true : successful, false : fail
	 */
	public boolean setDataBits(int dataBits) {
		Log.d(TAG, "Fail to setDataBits");
		mUartConfig.dataBits = dataBits;
		return true;
	}

	/**
	 * Sets Parity bit
	 * 
	 * @param parity
	 *            parity bits e.g. UartConfig.PARITY_NONE
	 * @return true : successful, false : fail
	 */
	public boolean setParity(int parity) {
		Log.d(TAG, "Fail to setParity");
		mUartConfig.parity = parity;
		return true;
	}

	/**
	 * Sets Stop bits
	 * 
	 * @param stopBits
	 *            stop bits e.g. UartConfig.STOP_BITS1
	 * @return true : successful, false : fail
	 */
	public boolean setStopBits(int stopBits) {
		Log.d(TAG, "Fail to setStopBits");
		mUartConfig.stopBits = stopBits;
		return true;
	}

	@Override
	public boolean setDtrRts(boolean dtrOn, boolean rtsOn) {
		int ctrlValue = 0x0000;
		if (dtrOn) {
			ctrlValue |= 0x0001;
		}
		if (rtsOn) {
			ctrlValue |= 0x0002;
		}
		int ret = mConnection.controlTransfer(0x21, 0x22, ctrlValue, 0, null, 0, 100);
		if (ret < 0) {
			Log.d(TAG, "Fail to setDtrRts");
			return false;
		}
		mUartConfig.dtrOn = dtrOn;
		mUartConfig.rtsOn = rtsOn;
		return true;
	}

	@Override
	public UartConfig getUartConfig() {
		return mUartConfig;
	}

	@Override
	public int getBaudrate() {
		return mUartConfig.baudrate;
	}

	@Override
	public int getDataBits() {
		return mUartConfig.dataBits;
	}

	@Override
	public int getParity() {
		return mUartConfig.parity;
	}

	@Override
	public int getStopBits() {
		return mUartConfig.stopBits;
	}

	@Override
	public boolean getDtr() {
		return mUartConfig.dtrOn;
	}

	@Override
	public boolean getRts() {
		return mUartConfig.rtsOn;
	}

}
