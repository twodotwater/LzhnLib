package com.lzhn.utils.bluetooth;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.lzhn.utils.view.ToastUtils;

/**
 * 蓝牙操作的工具类
 * 
 * @author lzhn
 * 
 */

public class BTUtils extends BroadcastReceiver {
	private static final String TAG = "BTUtils";

	/** 蓝牙适配器 */
	private static BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
			.getDefaultAdapter();
	/** 蓝牙状态 */
	private OnBtChangeListener btChangeListener;
	/** 蓝牙搜索状态 */
	private OnBtDiscoveredListener btDiscoveredListener;
	/** 蓝牙绑定状态 */
	private OnBtStateChangeListener btStateChangeListener;
	private Context context;
	private static BTUtils btUtils;

	private BTUtils(Context context) {
		this.context = context;
	}

	public static BTUtils getInstance(Context context) {
		if (btUtils == null) {
			btUtils = new BTUtils(context);
		}
		return btUtils;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	/**
	 * 蓝牙开启状态改变监听器
	 * 
	 * @param btChangeListener
	 */
	public void setBtChangeListener(OnBtChangeListener btChangeListener) {
		this.btChangeListener = btChangeListener;
	}

	/**
	 * 搜索蓝牙监听器
	 * 
	 * @param btDiscoveredListener
	 */
	public void setBtDiscoveredListener(
			OnBtDiscoveredListener btDiscoveredListener) {
		this.btDiscoveredListener = btDiscoveredListener;
	}

	/**
	 * 蓝牙配对状态改变监听器
	 * 
	 * @param btStateChangeListener
	 */
	public void setBtStateChangeListener(
			OnBtStateChangeListener btStateChangeListener) {
		this.btStateChangeListener = btStateChangeListener;
	}

	/**
	 * 检测蓝牙是否开启
	 * 
	 * @return
	 */
	public static boolean isBtEnabled() {
		// 没有蓝牙设备
		if (mBluetoothAdapter == null) {
			return false;
		}
		return mBluetoothAdapter.getState() == BluetoothAdapter.STATE_ON;
	}

	/**
	 * 获取已配对过的蓝牙
	 * 
	 * @return 蓝牙设备集合
	 */
	public static ArrayList<BluetoothDevice> getPairedDevices() {
		Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
		if (devices != null && devices.size() > 0) {
			ArrayList<BluetoothDevice> bluetoothDevices = new ArrayList<BluetoothDevice>();
			bluetoothDevices.addAll(devices);
			return bluetoothDevices;
		}
		return null;
	}

	/**
	 * 由蓝牙地址获取一个BluetoothDevice对象<br/>
	 * {@link android.bluetooth.BluetoothAdapter#getRemoteDevice(String)}
	 * 
	 * @param address
	 * @return
	 */
	public static BluetoothDevice getRemoteDevice(String address) {
		return mBluetoothAdapter.getRemoteDevice(address);
	}

	/**
	 * 通过蓝牙设备名称获取BluetoothDevice对象
	 * 
	 * @param devices
	 * @param deviceName
	 *            蓝牙名称
	 * @return 如果list中包含名称为deviceName的蓝牙设备，则返回该对象；否则返回null
	 */
	public static BluetoothDevice getBluetoothDevice(
			List<BluetoothDevice> devices, String deviceName) {
		if (devices == null) {
			return null;
		}
		for (BluetoothDevice device : devices) {
			if (device.getName().contains(deviceName)) {
				return device;
			}
		}
		return null;
	}

	/**
	 * 启动蓝牙 {@link android.bluetooth.BluetoothAdapter#ACTION_REQUEST_ENABLE}
	 * 
	 * @return 打开蓝牙的intent
	 */
	public Intent getBtStartIntent() {
		Intent enableBtIntent = new Intent(
				BluetoothAdapter.ACTION_REQUEST_ENABLE);
		// // 允许蓝牙设备可见
		// enableBtIntent.setAction(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		// // 设置蓝牙设备可见时间
		// enableBtIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,
		// DURATION);
		// ((FragmentActivity) context).startActivityForResult(enableBtIntent,
		// REQUEST_BLUETOOTH_ENABLE);
		return enableBtIntent;
	}

	/**
	 * 启动蓝牙
	 */
	public static void startBluetooth() {
		Log.e(TAG, "-->" + "start bluetooth...");
		if (!isBtEnabled())
			mBluetoothAdapter.enable();
	}

	/**
	 * 搜索蓝牙
	 */
	public void discoverBluetooth() {
		Log.e(TAG, "-->" + "discover bluetooth...");
		if (mBluetoothAdapter.isDiscovering()
				|| mBluetoothAdapter.startDiscovery()) {
			ToastUtils.showToast(context, "正在搜索蓝牙设备...");
		}
	}

	/**
	 * 关闭蓝牙
	 */
	public static void closeBluetooth() {
		Log.e(TAG, "-->" + "close bluetooth...");
		if (isBtEnabled())
			mBluetoothAdapter.disable();
	}

	/**
	 * 接收打开蓝牙的操作结果
	 * 
	 * @param requestCode
	 *            请求码
	 * @param resultCode
	 *            返回值
	 */
	public void onActivityResult(int requestCode, int resultCode) {
		// Log.i(TAG, "--" + requestCode + "-" + resultCode);
		if (requestCode == Constant.REQUEST_BLUETOOTH_ENABLE) {
			if (resultCode == Constant.DURATION
					|| resultCode == FragmentActivity.RESULT_OK) {
				btChangeListener.onBtON();
			} else {
				btChangeListener.onBtOFF();
			}
		}
	}

	/**
	 * 获取蓝牙设备状态改变的意图过滤器
	 * 
	 * @return
	 */
	public static IntentFilter getBtChangeIntentFilter() {
		// 蓝牙状态改变
		return new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
	}

	/**
	 * 获取蓝牙搜索结果的意图过滤器
	 * 
	 * @return
	 */
	public static IntentFilter getBtDiscoverIntentFilter() {

		return new IntentFilter(BluetoothDevice.ACTION_FOUND);
	}

	/**
	 * 蓝牙绑定状态的意图过滤器
	 * 
	 * @return
	 */
	public static IntentFilter getBtBondStateChangedIntentFilter() {
		return new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// 接收蓝牙开启状态改变广播
		switch (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0)) {
		case BluetoothAdapter.STATE_OFF:
			ToastUtils.showToast(context, "蓝牙设备关闭");
			btChangeListener.onBtOFF();
			break;
		case BluetoothAdapter.STATE_ON:
			ToastUtils.showToast(context, "蓝牙设备启动");
			btChangeListener.onBtON();
			break;
		default:
			break;
		}

		// 接收搜索发现蓝牙设备的广播
		if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction())) {
			// 接收Intent中传递的BluetoothDevice信息
			BluetoothDevice device = intent
					.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			if (device != null) {
				ToastUtils.showToast(context, "搜索到蓝牙设备:" + device.getName());
				btDiscoveredListener.onBtDiscovered(device);
			}
		}

		// 接收蓝牙绑定状态改变的广播
		switch (intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, 0)) {
		case BluetoothDevice.BOND_BONDED:
			// 接收Intent中传递的BluetoothDevice信息
			BluetoothDevice device = intent
					.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			btStateChangeListener.onBtBonded(device);
			break;
		case BluetoothDevice.BOND_BONDING:
			// 接收Intent中传递的BluetoothDevice信息
			BluetoothDevice device2 = intent
					.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			btStateChangeListener.onBtBonding(device2);

			break;
		case BluetoothDevice.BOND_NONE:
			// 接收Intent中传递的BluetoothDevice信息
			BluetoothDevice device3 = intent
					.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			btStateChangeListener.onBtBondNone(device3);
			break;

		default:
			break;
		}

	}

	/**
	 * 蓝牙设备配对
	 * 
	 * @param btClass
	 *            BluetoothDevice的运行时类
	 * @param device
	 *            配对的蓝牙设备
	 * @return
	 */
	public boolean createBond(Class<BluetoothDevice> btClass,
			BluetoothDevice device) {
		if (!isBtEnabled()) {
			ToastUtils.showToast(context, "蓝牙设备未启动！");
			return false;
		}
		Log.e(TAG, "-->" + "bond device...");
		Method createBondMethod;
		if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
			try {
				createBondMethod = btClass.getMethod("createBond");
				Boolean returnValue = (Boolean) createBondMethod.invoke(device);
				return returnValue;
			} catch (NoSuchMethodException e) {
				Log.e(TAG, "--bond1-" + e.getMessage());
			} catch (IllegalAccessException e) {
				Log.e(TAG, "--bond2-" + e.getMessage());
			} catch (IllegalArgumentException e) {
				Log.e(TAG, "--bond3-" + e.getMessage());
			} catch (InvocationTargetException e) {
				Log.e(TAG, "--bond4-" + e.getTargetException());
			}
			return false;
		}
		return true;
	}

	/**
	 * 解除配对
	 * 
	 * @param btClass
	 * @param device
	 * @return
	 */
	public boolean removeBond(Class<BluetoothDevice> btClass,
			BluetoothDevice device) {
		if (!isBtEnabled()) {
			ToastUtils.showToast(context, "蓝牙设备未启动！");
			return false;
		}
		Log.e(TAG, "-->" + "remove bond...");
		Method removeBondMethod;
		try {
			removeBondMethod = btClass.getMethod("removeBond");
			return (Boolean) removeBondMethod.invoke(device);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 设置蓝牙配对的PIN码
	 * 
	 * @param btClass
	 *            BluetoothDevice的运行时类
	 * @param device
	 *            配对的蓝牙设备
	 * @param strPin
	 *            配对Pin码
	 * @return
	 */
	public boolean setPin(Class<BluetoothDevice> btClass,
			BluetoothDevice device, String strPin) throws Exception {
		try {
			Method autoBondMethod = btClass.getMethod("setPin",
					new Class[] { byte[].class });
			Boolean result = (Boolean) autoBondMethod.invoke(device,
					new Object[] { strPin.getBytes() });
			return result;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 回调接口，处理蓝牙设备打开和关闭事件
	 */
	public interface OnBtChangeListener {
		/**
		 * 蓝牙打开时调用
		 */
		public void onBtON();

		/**
		 * 蓝牙关闭时调用
		 */
		public void onBtOFF();
	}

	/**
	 * 蓝牙搜索到其他设备时调用接口
	 */
	public interface OnBtDiscoveredListener {
		/**
		 * 搜索到蓝牙设备时，调用此方法
		 * 
		 * @param device
		 *            搜索到的蓝牙设备
		 */
		public void onBtDiscovered(BluetoothDevice device);
	}

	/**
	 * 蓝牙绑定状态改变时的回调接口
	 * 
	 * @author lzhn
	 * 
	 */
	public interface OnBtStateChangeListener {
		/**
		 * 配对完成
		 * 
		 * @param device
		 */
		public void onBtBonded(BluetoothDevice device);

		/**
		 * 正在配对
		 * 
		 * @param device
		 */
		public void onBtBonding(BluetoothDevice device);

		/**
		 * 取消配对
		 * 
		 * @param device
		 */
		public void onBtBondNone(BluetoothDevice device);
	}
}
