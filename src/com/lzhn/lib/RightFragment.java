package com.lzhn.lib;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lzhn.utils.os.BaseFragment_v4;

public class RightFragment extends BaseFragment_v4 {

	@Override
	public View inflateLayout(LayoutInflater inflater, ViewGroup container,
			boolean b) {
		return inflater.inflate(R.layout.frgment_right, container, b);
	}

}
