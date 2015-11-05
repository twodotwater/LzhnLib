package com.tdw.utils.module.usb.uart;

import android.content.Context;
import android.hardware.usb.UsbDeviceConnection;

public abstract class UartController {
	protected Context context;
	protected UsbDeviceConnection mConnection;
	protected boolean isOpened;

	public UartController(Context context) {
		this.context = context;
	}

	public UartController(Context context, UsbDeviceConnection mConnection) {
		this.context = context;
		this.mConnection = mConnection;
		isOpened = true;
	}

	/**
	 * Opens a device
	 * 
	 * @return true : successful, false : fail
	 */
	abstract public boolean open();

	/**
	 * Closes a device
	 * 
	 * @return true : successful, false : fail
	 */
	abstract public boolean close();

	/**
	 * Reads byte array
	 * 
	 * @param buf
	 *            byte array
	 * @param size
	 *            read size
	 * @return actual read size
	 */
	abstract public int read(byte[] buf, int size);

	/**
	 * Writes byte array
	 * 
	 * @param buf
	 *            byte array
	 * @param size
	 *            write size
	 * @return actual written size
	 */
	abstract public int write(byte[] buf, int size);

	/**
	 * Checks device is opened
	 * 
	 * @return true : opened, false : not opened
	 */
	abstract public boolean isOpened();

	/**
	 * Sets Uart configurations
	 * 
	 * @param config
	 *            configurations
	 * @return true : successful, false : fail
	 */
	abstract public boolean setUartConfig(UartConfig config);

	abstract public boolean setUartConfig();

	/**
	 * Sets baudrate
	 * 
	 * @param baudrate
	 *            baudrate e.g. 9600
	 * @return true : successful, false : fail
	 */
	abstract public boolean setBaudrate(int baudrate);

	/**
	 * Sets Data bits
	 * 
	 * @param dataBits
	 *            data bits e.g. UartConfig.DATA_BITS8
	 * @return true : successful, false : fail
	 */
	abstract public boolean setDataBits(int dataBits);

	/**
	 * Sets Parity bit
	 * 
	 * @param parity
	 *            parity bits e.g. UartConfig.PARITY_NONE
	 * @return true : successful, false : fail
	 */
	abstract public boolean setParity(int parity);

	/**
	 * Sets Stop bits
	 * 
	 * @param stopBits
	 *            stop bits e.g. UartConfig.STOP_BITS1
	 * @return true : successful, false : fail
	 */
	abstract public boolean setStopBits(int stopBits);

	/**
	 * Sets flow control DTR/RTS
	 * 
	 * @param dtrOn
	 *            true then DTR on
	 * @param rtsOn
	 *            true then RTS on
	 * @return true : successful, false : fail
	 */
	abstract public boolean setDtrRts(boolean dtrOn, boolean rtsOn);

	/**
	 * Gets Uart configurations
	 * 
	 * @return UART configurations
	 */
	abstract public UartConfig getUartConfig();

	/**
	 * Gets baud-rate
	 * 
	 * @return baud-rate configuration e.g. 9600
	 */
	abstract public int getBaudrate();

	/**
	 * Gets Data bits
	 * 
	 * @return data bits e.g. UartConfig.DATA_BITS8
	 */
	abstract public int getDataBits();

	/**
	 * Gets Parity bit
	 * 
	 * @return parity bits e.g. UartConfig.PARITY_NONE
	 */
	abstract public int getParity();

	/**
	 * Gets Stop bits
	 * 
	 * @return stop bits e.g. UartConfig.STOP_BITS1
	 */
	abstract public int getStopBits();

	/**
	 * Gets flow control DTR
	 * 
	 * @return true then DTR on
	 */
	abstract public boolean getDtr();

	/**
	 * Gets flow control RTS
	 * 
	 * @reutrn true then RTS on
	 */
	abstract public boolean getRts();

}
