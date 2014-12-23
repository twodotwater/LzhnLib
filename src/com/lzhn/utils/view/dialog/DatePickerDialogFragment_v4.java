package com.lzhn.utils.view.dialog;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.lzhn.utils.view.ToastUtils;

public class DatePickerDialogFragment_v4 extends DialogFragment implements
		OnDateSetListener {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO 生成对话框
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int monthOfYear = cal.get(Calendar.MONTH);
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		return new DatePickerDialog(getActivity(), this, year, monthOfYear,
				dayOfMonth);
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		// TODO 日期选定
		ToastUtils.showToast(getActivity(), year + "/" + (monthOfYear + 1)
				+ "/" + dayOfMonth);
	}

}
