package com.example.fpl_racewaiking.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.fpl_racewaiking.R;
import com.example.fpl_racewaiking.vo.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {
	// 填充数据的list
	private ArrayList<String> list;

	// 用来控制CheckBox的选中状况
	private static HashMap<Integer, Boolean> isSelected;

	private Context context;

	// 导入布局
	private LayoutInflater inflater = null;

	// 构造器
	public MyAdapter(ArrayList<String> list, Context context) {
		this.list = list;
		this.context = context;
		inflater = LayoutInflater.from(context);
		isSelected = new HashMap<>();

		// 初始化数据
		initData();
	}

	// 初始化isSelected的数据
	private void initData() {
		for (int i = 0; i < list.size(); i++) {
			getIsSelected().put(i, false);
		}
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_checkbox, null);
			holder.cb = (CheckBox) convertView.findViewById(R.id.cb_item);
			holder.tvItem = (TextView) convertView.findViewById(R.id.tv_item);

			convertView.setTag(holder);
		} else {
			// 取出holder
			holder = (ViewHolder) convertView.getTag();
		}
		// 设置list中TextView的显示
		holder.tvItem.setText(list.get(position));
		// 根据isSelected来设置checkbox的选中状况
		holder.cb.setChecked(getIsSelected().get(position));
		return convertView;
	}

	public static HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}

	public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
		MyAdapter.isSelected = isSelected;
	}

}
