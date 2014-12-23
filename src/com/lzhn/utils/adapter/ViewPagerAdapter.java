package com.lzhn.utils.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 自定义viewpager适配器
 * 
 * @author lzhn
 * 
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
	private ArrayList<Fragment> fragments;

	/**
	 * 构造函数，指定数据源
	 * 
	 * @param fm
	 * @param fragments
	 */
	public ViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
		super(fm);
		setFragments(fragments);
	}

	public ViewPagerAdapter(FragmentManager fm) {
		this(fm, null);
	}

	/**
	 * 指定数据源
	 * 
	 * @param fragments
	 */
	public void setFragments(ArrayList<Fragment> fragments) {
		if (fragments == null)
			fragments = new ArrayList<Fragment>();
		this.fragments = fragments;
	}

	@Override
	public android.support.v4.app.Fragment getItem(int position) {
		if (fragments != null && fragments.size() > 0) {
			return fragments.get(position % fragments.size());
		}
		return null;
	}

	@Override
	public int getCount() {
		if (fragments != null && fragments.size() > 0) {
			return fragments.size();
		}
		return 0;
	}

}
