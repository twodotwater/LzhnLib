package com.lzhn.utils.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

/**
 * 自定义BaseExpandableListAdapter适配器，适用于ExpandableListView
 * 
 * @author lzhn
 * 
 * @param <TG>
 * @param <TC>
 */
public class ExpandableBaseAdpater<TG, TC> extends BaseExpandableListAdapter {
	private Context context;
	/** group数据源 */
	private List<TG> groupItems;
	/** child数据源 */
	private List<List<TC>> childItems;
	/** group布局文件id */
	private int groupViewResourceId;
	/** child布局文件id */
	private int childViewResourceId;
	/** 显示数据监听器：在getGroupView/getChildView方法中调用 */
	private OnGetViewListener<TG, TC> onGetViewListener;

	private ExpandableBaseAdpater(Context context) {
		super();
		this.context = context;
	}

	/**
	 * 构造函数，默认数据源：不为null，不包含数据
	 * 
	 * @param context
	 * @param groupViewResourceId
	 * @param childViewResourceId
	 */
	public ExpandableBaseAdpater(Context context, int groupViewResourceId,
			int childViewResourceId) {
		this(context, null, null, groupViewResourceId, childViewResourceId);
	}

	/**
	 * 构造函数
	 * 
	 * @param context
	 * @param groupItems
	 * @param childItems
	 * @param groupViewResourceId
	 * @param childViewResourceId
	 */
	public ExpandableBaseAdpater(Context context, List<TG> groupItems,
			List<List<TC>> childItems, int groupViewResourceId,
			int childViewResourceId) {
		super();
		this.context = context;
		this.groupViewResourceId = groupViewResourceId;
		this.childViewResourceId = childViewResourceId;
		setGroupItems(groupItems);
		setChildItems(childItems);
	}

	public List<TG> getGroupItems() {
		return groupItems;
	}

	public void setGroupItems(List<TG> groupItems) {
		if (groupItems == null)
			groupItems = new ArrayList<TG>();
		this.groupItems = groupItems;
	}

	public List<List<TC>> getChildItems() {
		return childItems;
	}

	public void setChildItems(List<List<TC>> childItems) {
		if (childItems == null)
			childItems = new ArrayList<List<TC>>();
		this.childItems = childItems;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public void setGroupViewResourceId(int groupViewResourceId) {
		this.groupViewResourceId = groupViewResourceId;
	}

	public void setChildViewResourceId(int childViewResourceId) {
		this.childViewResourceId = childViewResourceId;
	}

	/**
	 * 添加显示数据监听器
	 * 
	 * @param onGetViewListener
	 */
	public void setOnGetViewListener(OnGetViewListener<TG, TC> onGetViewListener) {
		this.onGetViewListener = onGetViewListener;
	}

	/**
	 * 更新数据源、刷新显示
	 * 
	 * @param gItems
	 * @param cItems
	 */
	public void refresh(List<TG> gItems, List<List<TC>> cItems) {
		setGroupItems(gItems);
		setChildItems(cItems);
		notifyDataSetChanged();
	}

	/**
	 * 刷新显示
	 */
	public void refresh() {
		notifyDataSetChanged();
	}

	@Override
	public int getGroupCount() {
		return groupItems.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return childItems.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groupItems.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return childItems.get(groupPosition).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		convertView = initConvertView(convertView, groupViewResourceId, parent);
		if (onGetViewListener != null) {
			onGetViewListener.onGetGroupView(convertView, groupItems,
					groupPosition, isExpanded);
		}
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		convertView = initConvertView(convertView, childViewResourceId, parent);
		if (onGetViewListener != null) {
			onGetViewListener.onGetChildView(convertView, childItems,
					groupPosition, childPosition, isLastChild);
		}
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	/**
	 * 导入布局View
	 * 
	 * @param convertView
	 * @param resourceId
	 * @param parent
	 * @return
	 */
	private View initConvertView(View convertView, int resourceId,
			ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(resourceId,
					parent, false);
		}
		return convertView;
	}

	/**
	 * 获取数据后显示接口
	 * 
	 * @author lzhn
	 * 
	 * @param <TG>
	 * @param <TC>
	 */
	public interface OnGetViewListener<TG, TC> {
		/**
		 * 显示group数据
		 * 
		 * @param convertView
		 * @param groupItems
		 * @param groupPosition
		 * @param isExpanded
		 */
		public void onGetGroupView(View convertView, List<TG> groupItems,
				int groupPosition, boolean isExpanded);

		/**
		 * 显示child数据
		 * 
		 * @param convertView
		 * @param childItems
		 * @param groupPosition
		 * @param childPosition
		 * @param isLastChild
		 */
		public void onGetChildView(View convertView, List<List<TC>> childItems,
				int groupPosition, int childPosition, boolean isLastChild);
	}
}
