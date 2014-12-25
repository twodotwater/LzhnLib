package com.lzhn.utils.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class MyDialogFragment_v4 extends DialogFragment {

	private static final String TAG = "MyDialogFragment_v4";

	private static MyDialogFragment_v4 myDialogFragment;
	private static MyDialog myDialog;

	public static MyDialogFragment_v4 newInstance(Context context, int mode) {
		myDialog = MyDialog.newInstance(context, mode);
		myDialogFragment = new MyDialogFragment_v4();
		return myDialogFragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return myDialog;
	}

	/**
	 * 返回实际对话框对象{@link MyDialog}
	 * 
	 * @return
	 */
	public MyDialog getMyDialog() {
		return myDialog;
	}

	public MyDialogFragment_v4 show() {
		myDialogFragment.show(getFragmentManager(), TAG);
		return this;
	}
}
