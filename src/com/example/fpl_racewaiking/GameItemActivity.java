package com.example.fpl_racewaiking;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.example.fpl_racewaiking.adapter.MyAdapter;
import com.example.fpl_racewaiking.db.DbService;
import com.example.fpl_racewaiking.vo.ViewHolder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import ww.greendao.dao.pf_foul;
import ww.greendao.dao.pf_group;

public class GameItemActivity extends Activity {

	@Bind(R.id.lv_game_item)
	ListView lvGame;
	private Context context;
	private MyAdapter adapter;
	private SharedPreferences mSharedPreferences;
	private ArrayList<String> items;
	private Set<String> selItems;
	private String judgeName;
	private List<pf_group> groups;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_item);
		context = this;
		ButterKnife.bind(GameItemActivity.this);
		groups = new ArrayList<>();

		Intent intent = getIntent();
		judgeName = intent.getStringExtra("judgeName");

		mSharedPreferences = this.getSharedPreferences("fpl_RaceWalking", Activity.MODE_PRIVATE);

		if (judgeName != null) {
			List<pf_foul> fouls = DbService.getInstance(context).queryFoulByjudgeName(judgeName);
			for (pf_foul pf_foul : fouls) {
				pf_group foulGroup = DbService.getInstance(context).queryGroupByGroupID(pf_foul.getGroup_ID());
				groups.add(foulGroup);
			}
			items = new ArrayList<>();
			for (pf_group pf_group : groups) {
				items.add(pf_group.getGroup_CHN_content());
			}
			selItems = mSharedPreferences.getStringSet("selectFoulItems", null);
			for (int i = 0; i < items.size(); i++) {
				for (int j = items.size() - 1; j > i; j--) // 内循环是 外循环一次比较的次数
				{
					if (items.get(i) == items.get(j)) {
						items.remove(j);
					}
				}
			}
			adapter = new MyAdapter(items, context);
			lvGame.setAdapter(adapter);
			if (selItems != null && !selItems.isEmpty()) {
				for (int i = 0; i < items.size(); i++) {
					Iterator<String> iterator = selItems.iterator();
					while (iterator.hasNext()) {
						if (iterator.next().equals(items.get(i))) {
							MyAdapter.getIsSelected().put(i, true);
						}
					}
				}
			}
		} else {
			groups = DbService.getInstance(context).loadAllGroup();
			items = new ArrayList<>();
			for (pf_group pf_group : groups) {
				items.add(pf_group.getGroup_CHN_content());
			}
			selItems = mSharedPreferences.getStringSet("selectItems", null);
			adapter = new MyAdapter(items, context);
			lvGame.setAdapter(adapter);
			Log.i("2222222-----", "2222222222");
			if (selItems != null && !selItems.isEmpty()) {
				for (int i = 0; i < items.size(); i++) {
					Iterator<String> iterator = selItems.iterator();
					while (iterator.hasNext()) {
						if (iterator.next().equals(items.get(i))) {
							MyAdapter.getIsSelected().put(i, true);
						}
					}
				}
			}
		}

	}

	@OnClick(R.id.ib_quit)
	public void setonClickListener() {
		finish();
	}

	@OnItemClick(R.id.lv_game_item)
	public void setOnItemClickListener(int position, View view) {
		// 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
		ViewHolder holder = (ViewHolder) view.getTag();
		// 改变CheckBox的状态
		holder.cb.toggle();
		// 将CheckBox的选中状况记录下来
		MyAdapter.getIsSelected().put(position, holder.cb.isChecked());

		// String gameItem = lvGame.getItemAtPosition(position).toString();
		// if (gameItem != null) {
		// Intent intent = new Intent(context, MainActivity.class);
		// intent.putExtra("gameItem", gameItem);
		// startActivity(intent);
		// finish();
		// }
	}

	@Override
	protected void onPause() {
		super.onPause();
		Set<String> selectItems = new HashSet<>();
		for (int i = 0; i < items.size(); i++) {
			if (MyAdapter.getIsSelected().get(i)) {
				selectItems.add(items.get(i));
			}
		}
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		if (judgeName == null) {
			editor.putStringSet("selectItems", selectItems);
		} else {
			editor.putStringSet("selectFoulItems", selectItems);
		}
		editor.commit();
		Log.i("onPause选择项目已保存", selectItems.toString());
	}

}
