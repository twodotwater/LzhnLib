package com.tdw.utils.module.usb.uart;

public enum UsbVid {
	ARDUINO(0x2341), // 9025
	FTDI(0x0403), // 1027
	MBED_LPC1768(0x0d28), // 3368
	MBED_LPC11U24(0x0d28), // 3368
	MBED_FRDM_KL25Z_OPENSDA_PORT(0x1357), // 4951
	MBED_FRDM_KL25Z_KL25Z_PORT(0x15a2), // 5538
	// custom
	CP210X(0x10C4), // 4292
	CH34X(0x1a86), // 6790
	DIGIBASE(0x0a2d);// 2605

	private int vid;

	private UsbVid(int vid) {
		this.vid = vid;
	}

	public int getVid() {
		return vid;
	}
}
