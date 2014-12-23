package com.lzhn.utils.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 自定义BaseAdapter适配器
 * 
 * @author lzhn
 * 
 * @param <T>
 */
public class CustomBaseAdapter<T> extends BaseAdapter {
	/** 上下文对象 */
	private Context context;
	/** item中的数据 */
	private List<T> items;
	/** listview中item的布局文件id */
	private int resourceId;
	/** 显示数据监听器：数据显示在item布局中时调用 */
	private OnGetViewListener<T> onGetViewListener;

	private CustomBaseAdapter() {
		super();
	}

	/**
	 * 构造函数：默认数据源（不为null、不包含数据的ArrayList<T>）
	 * 
	 * @param context
	 * @param resourceId
	 *            item布局文件id
	 */
	public CustomBaseAdapter(Context context, int resourceId) {
		this(context, null, resourceId);
	}

	/**
	 * 构造函数
	 * 
	 * @param context
	 * @param items
	 *            数据源
	 * @param resourceId
	 *            item布局文件id
	 */
	public CustomBaseAdapter(Context context, List<T> items, int resourceId) {
		this();
		this.context = context;
		setItems(items);
		setResourceId(resourceId);
	}

	/**
	 * 获取数据源
	 * 
	 * @return
	 */
	public List<T> getItems() {
		return items;
	}

	/**
	 * 设置数据源。容错：数据源不为null
	 * 
	 * @param items
	 */
	public void setItems(List<T> items) {
		if (items == null) {
			items = new ArrayList<T>();
		}
		this.items = items;
	}

	public int getResourceId() {
		return resourceId;
	}

	/**
	 * 指定item布局文件id
	 * 
	 * @param resourceId
	 */
	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}

	/**
	 * 添加显示数据监听器
	 * 
	 * @param onGetViewListener
	 */
	public void setOnGetViewListener(OnGetViewListener<T> onGetViewListener) {
		this.onGetViewListener = onGetViewListener;
	}

	/**
	 * 刷新数据列表
	 * 
	 * @param items
	 *            新的数据源
	 */
	public void refresh(List<T> items) {
		setItems(items);
		refresh();
	}

	/**
	 * 刷新数据列表
	 */
	public void refresh() {
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			// 导入item布局
			convertView = LayoutInflater.from(context).inflate(resourceId,
					parent, false);
		}
		if (onGetViewListener != null) {
			onGetViewListener.onGetView(items, position, convertView);
		}
		return convertView;
	}

	/**
	 * 回调接口，完成baseAdater的getView方法中的操作
	 * 
	 * @param <T>
	 */
	public interface OnGetViewListener<T> {
		/**
		 * 在baseAdater的getView方法中调用。 Note：ViewHolder优化
		 * 
		 * @param items
		 *            listview中item中的数据的集合
		 * @param position
		 *            item的位置
		 * @param convertView
		 *            item的视图view
		 */
		public void onGetView(List<T> items, int position, View convertView);
	}
}
