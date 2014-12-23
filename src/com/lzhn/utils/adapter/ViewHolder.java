package com.lzhn.utils.adapter;

import android.util.SparseArray;
import android.view.View;

/**
 * listView组件优化
 * 
 * @author lzhn
 * 
 */
public class ViewHolder {

	@SuppressWarnings("unchecked")
	/**
	 * 通过组件id获取组件对象
	 * @param view 获取的组件包含在此view中
	 * @param id 组件id
	 * @return 组件对象
	 */
	public static <T extends View> T get(View view, int id) {
		SparseArray<View> viewArray = (SparseArray<View>) view.getTag();
		if (viewArray == null) {
			// 还未创建对象
			viewArray = new SparseArray<View>();
			view.setTag(viewArray);
		}
		// 先从viewholder中查找组件对象
		View childView = viewArray.get(id);
		if (childView == null) {
			// 在布局中查找组件
			childView = view.findViewById(id);
			// 将组件保存在viewholder中
			viewArray.put(id, childView);
		}
		return (T) childView;
	}
}