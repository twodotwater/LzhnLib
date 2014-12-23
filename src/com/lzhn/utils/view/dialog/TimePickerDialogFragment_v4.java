package com.lzhn.utils.view.dialog;

import java.util.Calendar;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import com.lzhn.utils.view.ToastUtils;

public class TimePickerDialogFragment_v4 extends DialogFragment implements
		OnTimeSetListener {
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Calendar cal = Calendar.getInstance();
		int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		boolean is24HourView = DateFormat.is24HourFormat(getActivity());
		// TODO 创建对话框
		return new TimePickerDialog(getActivity(), this, hourOfDay, minute,
				is24HourView);
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		// TODO 获取选择时间
		ToastUtils.showToast(getActivity(), hourOfDay + ":" + minute);
	}
}
