package com.lzhn.utils.share;

import android.content.Intent;

public class ShareUtils {

	public static Intent getShareTextIntent(String text) {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		text = text == null ? "默认要发送的文本" : text;
		shareIntent.putExtra(Intent.EXTRA_TEXT, text);
		return shareIntent;
	}
}
