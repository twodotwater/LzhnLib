package com.lzhn.utils.share;

import android.content.Context;
import android.widget.Toast;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX.Req;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXUtils {
	private static final String TAG = "WXUtils";

	// public static final String APP_ID = "wx743b1e0852ac74a9";
	public static final String APP_ID = "wxd930ea5d5a258f4f";
	public static final String APP_SECRET = "f8bd52666ee831ca370e1251a4747e4f";
	private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;

	private static IWXAPI iwxapi;
	private static Context context;

	private WXUtils() {
		super();
	}

	public static void setContext(Context context) {
		WXUtils.context = context;
	}

	/**
	 * 程序初始必须调用，注册应用到微信
	 * 
	 * @param context
	 * @return
	 */
	public static boolean registerToWX(Context context) {
		setContext(context);
		iwxapi = WXAPIFactory.createWXAPI(context, APP_ID, true);
		boolean isRegistered = iwxapi.registerApp(APP_ID);
		if (!isRegistered) {
			Toast.makeText(context, "注册失败!", 0).show();
		} else {
			Toast.makeText(context, "注册完成!", 0).show();
		}
		return isRegistered;
	}

	public static void sendTextToWX(String text) {

		if (!isWXAppInstalled()) {
			return;
		}
		WXTextObject textObject = new WXTextObject();
		textObject.text = text;

		WXMediaMessage mediaMessage = new WXMediaMessage();
		mediaMessage.mediaObject = textObject;
		mediaMessage.description = text;
		// 发送文本类型的消息时，title字段不起作用
		// mediaMessage.title = "Will be ignored";

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.message = mediaMessage;
		req.scene = Req.WXSceneSession;
		req.transaction = System.currentTimeMillis() + "";

		boolean isSent = iwxapi.sendReq(req);
		checkResult(isSent, null, null);
	}

	public static boolean launchWX() {
		boolean isLaunched = iwxapi.openWXApp();
		if (!isLaunched)
			Toast.makeText(context, "启动微信失败!", 0).show();
		return isLaunched;
	}

	public static boolean isSupportedTimeline() {
		int wxSdkVersion = iwxapi.getWXAppSupportAPI();
		if (wxSdkVersion >= TIMELINE_SUPPORTED_VERSION) {
			Toast.makeText(context, "支持发送到朋友圈！", 0).show();
		} else {
			Toast.makeText(context, "不支持发送到朋友圈！", 0).show();
		}
		return wxSdkVersion >= TIMELINE_SUPPORTED_VERSION;
	}

	public static boolean isWXAppInstalled() {
		boolean isInstalled = iwxapi.isWXAppInstalled();
		if (!isInstalled) {
			Toast.makeText(context, "您未安装微信!", 0).show();
		}
		return isInstalled;
	}

	public static void checkResult(boolean res, String okString,
			String errorString) {
		if (res) {
			Toast.makeText(context, okString == null ? "发送完成!" : okString, 0)
					.show();
		} else {
			Toast.makeText(context,
					errorString == null ? "发送失败!" : errorString, 0).show();
		}
	}
}
