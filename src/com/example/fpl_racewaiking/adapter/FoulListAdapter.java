package com.example.fpl_racewaiking.adapter;

import java.util.HashMap;
import java.util.List;

import com.example.fpl_racewaiking.R;
import com.example.fpl_racewaiking.vo.ViewHolder2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import ww.greendao.dao.pf_foul;

public class FoulListAdapter extends BaseAdapter {
	// 填充数据的list
	private List<pf_foul> list;

	// 用来控制CheckBox的选中状况
	private static HashMap<Integer, Boolean> isSelected;

	private Context context;

	// 导入布局
	private LayoutInflater inflater = null;

	// 构造器
	public FoulListAdapter(List<pf_foul> foulLists, Context context) {
		this.list = foulLists;
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
		ViewHolder2 holder = null;
		if (convertView == null) {
			holder = new ViewHolder2();
			convertView = inflater.inflate(R.layout.foul_list, null);
			holder.cb = (CheckBox) convertView.findViewById(R.id.cb_FoulList);
			holder.tvAthlete = (TextView) convertView.findViewById(R.id.tv_FoulList_athlete);
			holder.tvFoulContent = (TextView) convertView.findViewById(R.id.tv_FoulList_content);
			holder.tvFoulTime = (TextView) convertView.findViewById(R.id.tv_FoulList_time);
			holder.tvUpload = (TextView) convertView.findViewById(R.id.tv_FoulList_upload);

			convertView.setTag(holder);
		} else {
			// 取出holder
			holder = (ViewHolder2) convertView.getTag();
		}
		// 设置list中TextView的显示
		holder.tvAthlete.setText(list.get(position).getFoul_athlete_ID());
		if (list.get(position).getFoul_type()==1) {
			holder.tvFoulContent.setText("屈膝");
		} else {
			holder.tvFoulContent.setText("腾空");
		}
		holder.tvFoulTime.setText(list.get(position).getFoul_time());
		if (list.get(position).getFoul_upload().equals("√")) {
			holder.tvUpload.setTextColor(0xFF00EE00);
		} else {
			holder.tvUpload.setTextColor(0xFFFF0000);
		}
		holder.tvUpload.setText(list.get(position).getFoul_upload());
		if (list.get(position).getFoul_card() == 1) {
			holder.tvAthlete.setBackgroundResource(R.color.color_red);
			holder.tvFoulContent.setBackgroundResource(R.color.color_red);
			holder.tvFoulTime.setBackgroundResource(R.color.color_red);
		} else {
			holder.tvAthlete.setBackgroundResource(R.color.color_yellow);
			holder.tvFoulContent.setBackgroundResource(R.color.color_yellow);
			holder.tvFoulTime.setBackgroundResource(R.color.color_yellow);
		}
		// 根据isSelected来设置checkbox的选中状况
		holder.cb.setChecked(getIsSelected().get(position));
		return convertView;
	}

	public static HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}

	public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
		FoulListAdapter.isSelected = isSelected;
	}

}
