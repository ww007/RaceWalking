package com.example.fpl_racewaiking;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.example.fpl_racewaiking.adapter.FoulListAdapter;
import com.example.fpl_racewaiking.db.DbService;
import com.example.fpl_racewaiking.util.Constant;
import com.example.fpl_racewaiking.util.HttpUtil;
import com.example.fpl_racewaiking.util.TotalUtil;
import com.example.fpl_racewaiking.vo.ViewHolder2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnItemClick;
import ww.greendao.dao.pf_foul;

public class FoulListActivity extends Activity {

	@Bind(R.id.lv_foul)
	ListView lvFoul;
	@Bind(R.id.btn_resend_fail)
	Button btnResendFail;
	@Bind(R.id.btn_resend_select)
	Button btnResendSelect;
	@Bind(R.id.btn_print_select)
	Button btnPrintSelect;
	@Bind(R.id.tv_foul_item)
	TextView tvItem;
	@Bind(R.id.ll_foul_title)
	LinearLayout llTitle;
	@Bind(R.id.tv_foul_number)
	TextView tvNumber;
	@Bind(R.id.tv_foul_success)
	TextView tvSuccess;
	@Bind(R.id.tv_foul_fail)
	TextView tvFail;
	@Bind(R.id.rg_foul)
	RadioGroup rgFoul;
	@Bind(R.id.rb_FoulList_red)
	RadioButton rbRed;
	@Bind(R.id.rb_FoulList_yellow)
	RadioButton rbYellow;
	@Bind(R.id.cb_foul_all)
	CheckBox cbAll;
	@Bind(R.id.ib_foul_return)
	ImageButton ibReturn;

	private static Context context;
	private SharedPreferences mSharedPreferences;
	private String judgeName;
	private List<pf_foul> foulLists;
	private FoulListAdapter adapter;
	private Set<String> selItems;
	private ArrayList<String> games;
	private Iterator<String> iterator;
	private String getIp;
	private String serviceAddress;
	private String gMid;

	public static final byte[][] byteCommands = { { 0x1b, 0x40 }, // 复位打印机
			{ 0x1b, 0x4d, 0x00 }, // 标准ASCII字体
			{ 0x1b, 0x4d, 0x01 }, // 压缩ASCII字体
			{ 0x1d, 0x21, 0x00 }, // 字体不放大
			{ 0x1d, 0x21, 0x11 }, // 宽高加倍
			{ 0x1b, 0x45, 0x00 }, // 取消加粗模式
			{ 0x1b, 0x45, 0x01 }, // 选择加粗模式
			{ 0x1b, 0x7b, 0x00 }, // 取消倒置打印
			{ 0x1b, 0x7b, 0x01 }, // 选择倒置打印
			{ 0x1d, 0x42, 0x00 }, // 取消黑白反显
			{ 0x1d, 0x42, 0x01 }, // 选择黑白反显
			{ 0x1b, 0x56, 0x00 }, // 取消顺时针旋转90°
			{ 0x1b, 0x56, 0x01 }, // 选择顺时针旋转90°
			{ 0x1b, 0x69 } // 选择顺时针旋转90°
	};

	public static Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				TotalUtil.showToast(context, context.getResources().getString(R.string.Send_success));
				break;
			case 2:
				TotalUtil.showToast(context, context.getResources().getString(R.string.Fail_in_send));
				break;
			case 3:
				TotalUtil.showToast(context, context.getResources().getString(R.string.faild_connection));
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_foul_list);
		ButterKnife.bind(FoulListActivity.this);
		context = this;

		mSharedPreferences = this.getSharedPreferences("fpl_RaceWalking", Activity.MODE_PRIVATE);
		judgeName = mSharedPreferences.getString("judgeName", "");
		getIp = mSharedPreferences.getString("serviceAddress", null);
		gMid = mSharedPreferences.getString("gmid", "");

		if (getIp == null || getIp.equals("")) {
			serviceAddress = Constant.SERVICE_ADDRESS;
		} else {
			serviceAddress = getIp;
		}

		foulLists = DbService.getInstance(context).queryFoulByjudgeAndCard(judgeName, 1);
		rbRed.isChecked();

		rgFoul.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (rbRed.getId() == checkedId) {
					foulLists.clear();
					if (games != null && !games.isEmpty()) {
						for (String string : games) {
							foulLists.addAll(DbService.getInstance(context).queryFoulByjudgeAndCardAndItem(judgeName, 1,
									string));
						}
					} else {
						foulLists.addAll(DbService.getInstance(context).queryFoulByjudgeAndCard(judgeName, 1));
					}
					Log.i("红", "红");
					setAdapter();
					setTvNumber();
				} else {
					foulLists.clear();
					if (games != null && !games.isEmpty()) {
						for (String string : games) {
							foulLists.addAll(DbService.getInstance(context).queryFoulByjudgeAndCardAndItem(judgeName, 0,
									string));
						}
					} else {
						foulLists.addAll(DbService.getInstance(context).queryFoulByjudgeAndCard(judgeName, 0));
					}
					Log.i("黄", "黄");
					setAdapter();
					setTvNumber();
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		selItems = mSharedPreferences.getStringSet("selectFoulItems", null);
		games = new ArrayList<>();

		if (selItems != null && !selItems.isEmpty()) {
			iterator = selItems.iterator();
			String text = "";
			foulLists.clear();
			while (iterator.hasNext()) {
				String string = iterator.next();
				text += string + ";";
				games.add(string);
				if (rbRed.isChecked()) {
					Log.i("红=========", "红");
					foulLists.addAll(
							DbService.getInstance(context).queryFoulByjudgeAndCardAndItem(judgeName, 1, string));
				} else {
					Log.i("黄=========", "黄");
					foulLists.addAll(
							DbService.getInstance(context).queryFoulByjudgeAndCardAndItem(judgeName, 0, string));
				}
			}
			Log.i("onResume", text);
			tvItem.setText(text.subSequence(0, text.length() - 1));
		} else {
			tvItem.setText(context.getResources().getString(R.string.Event));
			if (rbRed.isChecked()) {
				Log.i("红----------", "红");
				foulLists = DbService.getInstance(context).queryFoulByjudgeAndCard(judgeName, 1);
			} else {
				Log.i("黄------", "黄");
				foulLists = DbService.getInstance(context).queryFoulByjudgeAndCard(judgeName, 0);
			}
		}
		setTvNumber();
		setAdapter();

	}

	private void setTvNumber() {
		int i = 0;
		for (pf_foul foul : foulLists) {
			if (foul.getFoul_upload().equals("√")) {
				i++;
			}
		}
		tvNumber.setText(context.getResources().getString(R.string.Fine_number) + foulLists.size());
		tvSuccess.setText(context.getResources().getString(R.string.Send_success) + "：" + i);
		tvFail.setText(context.getResources().getString(R.string.Fail_in_send) + "：" + (foulLists.size() - i));
		if (foulLists.size() == 0) {
			llTitle.setVisibility(View.INVISIBLE);
		} else {
			llTitle.setVisibility(View.VISIBLE);
		}
	}

	@OnClick({ R.id.tv_foul_item, R.id.btn_resend_select, R.id.btn_resend_fail, R.id.btn_print_select,
			R.id.ib_foul_return })
	public void onClickListener(View view) {
		switch (view.getId()) {
		case R.id.tv_foul_item:
			Intent intent = new Intent(context, GameItemActivity.class);
			intent.putExtra("judgeName", judgeName);
			startActivity(intent);
			break;
		case R.id.btn_resend_select:
			List<pf_foul> selectFouls = new ArrayList<>();
			for (int i = 0; i < foulLists.size(); i++) {
				if (FoulListAdapter.getIsSelected().get(i)) {
					selectFouls.add(foulLists.get(i));
				}
			}
			if (selectFouls != null && !selectFouls.isEmpty()) {
				HttpUtil.sendFoulInfo(context, gMid, serviceAddress, selectFouls);
			} else {
				TotalUtil.showToast(context, context.getResources().getString(R.string.plese_select_upload));
			}
			break;
		case R.id.btn_resend_fail:
			List<pf_foul> failFouls = new ArrayList<>();
			for (int i = 0; i < foulLists.size(); i++) {
				if (foulLists.get(i).getFoul_upload().equals("×")) {
					failFouls.add(foulLists.get(i));
				}
			}
			if (failFouls != null && !failFouls.isEmpty()) {
				HttpUtil.sendFoulInfo(context, gMid, serviceAddress, failFouls);
			} else {
				TotalUtil.showToast(context, context.getResources().getString(R.string.no_failed_item));
			}
			break;
		case R.id.btn_print_select:
			printDialog();
			break;
		case R.id.ib_foul_return:
			finish();
			break;

		default:
			break;
		}

	}

	private ArrayList<BluetoothDevice> mDeviceList;

	private void printDialog() {
		new AlertDialog.Builder(FoulListActivity.this).setTitle("提示").setMessage("是否打印选中的判罚内容？")
				.setPositiveButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				}).setNegativeButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 获取蓝牙适配器
						// mBlueAdapter = BluetoothAdapter.getDefaultAdapter();
						// if (mBlueAdapter == null) {
						// TotalUtil.showToast(context, "该设备不支持蓝牙功能");
						// return;
						// }
						// mDeviceList = new ArrayList<BluetoothDevice>();
						// initIntentFilter();
					}
				}).show();

	}

	protected void initIntentFilter() {
		// 设置广播信息过滤
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
		intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		// 注册广播接收器，接收并处理搜索结果
		registerReceiver(receiver, intentFilter);

	}

	@OnCheckedChanged(R.id.cb_foul_all)
	public void onCheckedListener(boolean ischecked) {
		if (ischecked) {
			for (int i = 0; i < foulLists.size(); i++) {
				FoulListAdapter.getIsSelected().put(i, true);
			}
			adapter.notifyDataSetChanged();
		} else {
			for (int i = 0; i < foulLists.size(); i++) {
				FoulListAdapter.getIsSelected().put(i, false);
			}
			adapter.notifyDataSetChanged();
		}
	}

	private void setAdapter() {
		//按对象集合中时间倒序排列显示到listview
		Collections.sort(foulLists, new Comparator<pf_foul>() {
			@Override
			public int compare(pf_foul lhs, pf_foul rhs) {
				Date date1 = TotalUtil.stringToDate(lhs.getFoul_time());  
                Date date2 = TotalUtil.stringToDate(rhs.getFoul_time());  
                // 对日期字段进行升序，如果欲降序可采用after方法  
                if (date1.before(date2)) {  
                    return 1;  
                }  
                return -1;  
			}
		});
		adapter = new FoulListAdapter(foulLists, context);
		lvFoul.setAdapter(adapter);
	}

	@OnItemClick(R.id.lv_foul)
	public void setOnItemListener(int position, View view) {
		// 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
		ViewHolder2 holder = (ViewHolder2) view.getTag();
		// 改变CheckBox的状态
		holder.cb.toggle();
		// 将CheckBox的选中状况记录下来
		FoulListAdapter.getIsSelected().put(position, holder.cb.isChecked());
	}

	/**
	 * 蓝牙广播接收器
	 */
	private BroadcastReceiver receiver = new BroadcastReceiver() {

		ProgressDialog progressDialog = null;

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
					// 该设备是之前配对设备
				} else {
					// 该设备是没有进行过配对的设备
					mDeviceList.add(device);
				}
			} else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
				progressDialog = ProgressDialog.show(context, "请稍等...", "正在搜索蓝牙设备...", true);
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				// 蓝牙设备搜索完毕
				progressDialog.dismiss();
			}
		}

	};
	private BluetoothSocket mBlueSocket;
	private OutputStream mOutputStream;
	private BluetoothAdapter mBlueAdapter;

	// @Override
	// protected void onDestroy() {
	// super.onDestroy();
	// // 注销广播
	// unregisterReceiver(receiver);
	// }

	private boolean mIsConnect = false;

	public void connect(BluetoothDevice device) {
		UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
		try {
			// 根据uuid 获取一个蓝牙socket
			mBlueSocket = device.createRfcommSocketToServiceRecord(uuid);
			// 进行连接
			mBlueSocket.connect();
			// 连接后获取输出流
			mOutputStream = mBlueSocket.getOutputStream();
			// 如果蓝牙还在搜索的话 则停止搜索 （蓝牙搜索比较耗资源）
			if (mBlueAdapter.isDiscovering()) {
				mBlueAdapter.cancelDiscovery();
			}
		} catch (Exception e) {
			TotalUtil.showToast(context, "连接失败！");
		}
		mIsConnect = true;
		TotalUtil.showToast(context, device.getName() + "连接成功！");
	}

	/**
	 * 打印内容
	 */
	public void printContent() {
		// 如果连接成功
		if (mIsConnect) {
			try {
				String title = "打印的标题";
				// 执行其他命令之前 先进行复位
				mOutputStream.write(byteCommands[0]);
				// 宽高加倍指令
				mOutputStream.write(byteCommands[4]);
				byte[] titleData = title.getBytes("GB2312");
				mOutputStream.write(titleData, 0, titleData.length);

				String content = "这是需要打印的内容 hello word";
				mOutputStream.write(byteCommands[0]);
				// 恢复到标准字体

				mOutputStream.write(byteCommands[1]);
				byte[] contentData = title.getBytes("GB2312");
				mOutputStream.write(contentData, 0, contentData.length);
				mOutputStream.flush();
			} catch (IOException e) {
				TotalUtil.showToast(context, "打印失败！");
			}
		} else {
			TotalUtil.showToast(context, "蓝牙设备未连接，或者连接失败！");
		}
	}

}
