package com.lzhn.utils.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.lzhn.utils.view.dialog.MyDialog.OnButtonClickListener;

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

	public MyDialog getMyDialog() {
		return myDialog;
	}

	public MyDialogFragment_v4 setOnBtnClickListener(
			OnButtonClickListener onBtnClickListener) {
		myDialog.setOnBtnClickListener(onBtnClickListener);
		return this;
	}

	public MyDialogFragment_v4 setTitle(String title) {
		myDialog.setTitle(title);
		return this;
	}

	public MyDialogFragment_v4 setChildView(View childView) {
		myDialog.setChildView(childView);
		return this;
	}

	public View getChildView() {
		return myDialog.getChildView();
	}

	public MyDialogFragment_v4 setInputText(String inputText) {
		myDialog.setInputText(inputText);
		return this;
	}

	public String getInputText() {
		return myDialog.getInputText();
	}

	public MyDialogFragment_v4 hidePositiveButton() {
		myDialog.hidePositiveButton();
		return this;
	}

	public MyDialogFragment_v4 setMessage(String msg) {
		myDialog.setMessage(msg);
		return this;
	}

	public MyDialogFragment_v4 setPositiveText(String positiveText) {
		myDialog.setPositiveText(positiveText);
		return this;
	}

	public MyDialogFragment_v4 setNegativeText(String negativeText) {
		myDialog.setNegativeText(negativeText);
		return this;
	}

	public MyDialogFragment_v4 show() {
		myDialogFragment.show(getFragmentManager(), TAG);
		return this;
	}
}
