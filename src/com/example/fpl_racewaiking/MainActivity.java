package com.example.fpl_racewaiking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.example.fpl_racewaiking.adapter.MyAdapter;
import com.example.fpl_racewaiking.db.DbService;
import com.example.fpl_racewaiking.util.Constant;
import com.example.fpl_racewaiking.util.TotalUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import ww.greendao.dao.dl_referee;
import ww.greendao.dao.pf_athlete;
import ww.greendao.dao.pf_foul;
import ww.greendao.dao.pf_group;

public class MainActivity extends Activity {

	@Bind(R.id.tv_gameInfo)
	TextView tvGameInfo;
	@Bind(R.id.tv_main_judge)
	TextView tvJudgeInfo;
	@Bind(R.id.tv_main_flag)
	TextView tvAthleteFlag;
	@Bind(R.id.numberGroup)
	RadioGroup rg;
	@Bind(R.id.et_athlete)
	EditText etAthlete;
	@Bind(R.id.et_main_time)
	EditText etFoulTime;
	@Bind(R.id.btn_red_knees)
	Button btnRedKnees;
	@Bind(R.id.btn_red_soar)
	Button btnRedSoar;
	@Bind(R.id.btn_yellow_knees)
	Button btnYellowKnees;
	@Bind(R.id.btn_yellow_soar)
	Button btnYellowSoar;
	@Bind(R.id.btn_foul_list)
	Button btnFoulList;
	@Bind(R.id.rb_athleteID)
	RadioButton rbAthleteID;
	@Bind(R.id.rb_athleteOrder)
	RadioButton rbAthleteOrder;

	private String radioName;
	private static Context context;
	private SharedPreferences mSharedPreferences;
	private String judgeName;
	private String standNo;
	private String getIp;
	private String password;
	private String serviceAddress;
	private String gameItem;
	private TextView tvFoulAthlete;
	private TextView tvFoulStyle;
	private TextView tvFoulContent;
	private LinearLayout llFoul;
	private Set<String> selItems;
	private Iterator<String> iterator;
	private List<String> games;
	private String gMid;
	public static Activity activity;

	public static Handler mhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				TotalUtil.showToast(context, "服务器连接异常");
				break;
			case 2:
				TotalUtil.showToast(context, "上传失败");
				break;
			case 3:
				TotalUtil.showToast(context, "上传成功");
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;
		activity = this;
		ButterKnife.bind(MainActivity.this);

		// Intent intent = getIntent();
		// gameItem = intent.getStringExtra("gameItem");
		//
		// if (gameItem != null && !gameItem.equals("")) {
		// tvGameInfo.setText(gameItem);
		// }

		etFoulTime.setText(TotalUtil.getTime());

		mSharedPreferences = this.getSharedPreferences("fpl_RaceWalking", Activity.MODE_PRIVATE);
		judgeName = mSharedPreferences.getString("judgeName", "");
		password = mSharedPreferences.getString("password", "");
		standNo = mSharedPreferences.getString("standNo", "");
		gMid = mSharedPreferences.getString("gmid", "");
		getIp = mSharedPreferences.getString("serviceAddress", null);

		if (getIp == null || getIp.equals("")) {
			serviceAddress = Constant.SERVICE_ADDRESS;
		} else {
			serviceAddress = getIp;
		}

		if (judgeName != null && !judgeName.equals("")) {
			tvJudgeInfo.setText(context.getResources().getString(R.string.Referee) + judgeName + " ; "
					+ context.getResources().getString(R.string.Referee_number) + standNo);
		}

		radioName = context.getResources().getString(R.string.Number);

		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				int radioId = group.getCheckedRadioButtonId();
				RadioButton rb = (RadioButton) MainActivity.this.findViewById(radioId);
				radioName = rb.getText().toString().trim();
				etAthlete.setText("");
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i("onResume", "--->onResume");
		selItems = mSharedPreferences.getStringSet("selectItems", null);
		games = new ArrayList<>();

		if (selItems != null && !selItems.isEmpty()) {
			iterator = selItems.iterator();
			String text = "";
			while (iterator.hasNext()) {
				String string = iterator.next();
				text += string + ";";
				games.add(string);
			}
			Log.i("onResume", text);
			tvGameInfo.setText(text.subSequence(0, text.length() - 1));
		} else {
			tvGameInfo.setText("");
		}
		etAthlete.setText("");
	}

	private pf_athlete athletes;

	@OnTextChanged(R.id.et_athlete)
	public void etChangedListener() {

		if (radioName.equals(context.getResources().getString(R.string.Number))) {
			if (etAthlete.getText().toString().trim().isEmpty()) {
				tvAthleteFlag.setText("");
			} else {
				if (iterator == null) {
					TotalUtil.showToast(context, context.getResources().getString(R.string.plese_select));
					return;
				}
				athletes = DbService.getInstance(context).queryAthleteByID(etAthlete.getText().toString().trim());
				if (athletes == null) {
					tvAthleteFlag.setText("×");
					tvAthleteFlag.setTextColor(0xFFFF0000);
				} else {
					pf_group game = DbService.getInstance(context)
							.queryGroupByGroupID((long) Integer.parseInt(athletes.getAthlete_group()));
					if (games.contains(game.getGroup_CHN_content())) {
						tvAthleteFlag.setText("√");
						tvAthleteFlag.setTextColor(0xFF00EE00);
					} else {
						tvAthleteFlag.setText("×");
						tvAthleteFlag.setTextColor(0xFFFF0000);
					}
				}
				// if (athletes == null) {
				// tvAthleteFlag.setText("×");
				// tvAthleteFlag.setTextColor(0xFFFF0000);
				// } else {
				// tvAthleteFlag.setText("√");
				// tvAthleteFlag.setTextColor(0xFF00EE00);
				// }
			}
		} else {
			if (etAthlete.getText().toString().trim().isEmpty()) {
				tvAthleteFlag.setText("");
			} else {
				if (iterator == null) {
					TotalUtil.showToast(context, context.getResources().getString(R.string.plese_select));
					return;
				}
				athletes = DbService.getInstance(context)
						.queryAthleteByOrder(Integer.parseInt(etAthlete.getText().toString().trim()));
				if (athletes == null) {
					tvAthleteFlag.setText("×");
					tvAthleteFlag.setTextColor(0xFFFF0000);
				} else {
					pf_group game = DbService.getInstance(context)
							.queryGroupByGroupID((long) Integer.parseInt(athletes.getAthlete_group()));
					if (games.contains(game.getGroup_CHN_content())) {
						tvAthleteFlag.setText("√");
						tvAthleteFlag.setTextColor(0xFF00EE00);
					} else {
						tvAthleteFlag.setText("×");
						tvAthleteFlag.setTextColor(0xFFFF0000);
					}
				}

				// if (athletes == null) {
				// tvAthleteFlag.setText("×");
				// tvAthleteFlag.setTextColor(0xFFFF0000);
				// } else {
				// tvAthleteFlag.setText("√");
				// tvAthleteFlag.setTextColor(0xFF00EE00);
				// }
			}
		}

	}

	@OnClick({ R.id.tv_gameInfo, R.id.tv_main_judge, R.id.btn_red_knees, R.id.btn_red_soar, R.id.btn_yellow_knees,
			R.id.btn_yellow_soar, R.id.btn_foul_list })
	public void onClickListener(View view) {
		switch (view.getId()) {
		case R.id.tv_gameInfo:
			startActivity(new Intent(MainActivity.this, GameItemActivity.class));
			break;
		case R.id.tv_main_judge:
			startActivity(new Intent(MainActivity.this, MainLoginActivity.class));
			break;
		case R.id.btn_red_knees:
			etFoulTime.setText(TotalUtil.getTime());
			if (tvAthleteFlag.getText().toString().equals("√")) {
				List<pf_foul> hasFoul=new ArrayList<>();
				if (radioName.equals(context.getResources().getString(R.string.Number))) {
					hasFoul.clear();
					hasFoul = DbService.getInstance(context).queryFoulByAthleteIdAndJudge(judgeName,
							etAthlete.getText().toString().trim());
				} else {
					hasFoul.clear();
					hasFoul = DbService.getInstance(context).queryFoulByAthleteOrderAndJudge(judgeName,
							Integer.parseInt(etAthlete.getText().toString().trim()));
				}
				if (hasFoul != null && !hasFoul.isEmpty()) {
					TotalUtil.showToast(context, context.getResources().getString(R.string.Toast_has_red));
				} else {
					showFoulDialog(Constant.RED, Constant.KNEES);
				}
			} else {
				TotalUtil.showToast(context, context.getResources().getString(R.string.Toast_no_athlete));
			}
			break;
		case R.id.btn_red_soar:
			etFoulTime.setText(TotalUtil.getTime());
			if (tvAthleteFlag.getText().toString().equals("√")) {
				List<pf_foul> hasFoul1=new ArrayList<>();
				if (radioName.equals(context.getResources().getString(R.string.Number))) {
					hasFoul1.clear();
					hasFoul1 = DbService.getInstance(context).queryFoulByAthleteIdAndJudge(judgeName,
							etAthlete.getText().toString().trim());
				} else {
					hasFoul1.clear();
					hasFoul1 = DbService.getInstance(context).queryFoulByAthleteOrderAndJudge(judgeName,
							Integer.parseInt(etAthlete.getText().toString().trim()));
				}
				if (hasFoul1 != null && !hasFoul1.isEmpty()) {
					TotalUtil.showToast(context, context.getResources().getString(R.string.Toast_has_red));
				} else {
					showFoulDialog(Constant.RED, Constant.SOAR);
				}
			} else {
				TotalUtil.showToast(context, context.getResources().getString(R.string.Toast_no_athlete));
			}
			break;
		case R.id.btn_yellow_knees:
			etFoulTime.setText(TotalUtil.getTime());
			if (tvAthleteFlag.getText().toString().equals("√")) {
				showFoulDialog(Constant.YELLOW, Constant.KNEES);
			} else {
				TotalUtil.showToast(context, context.getResources().getString(R.string.Toast_no_athlete));
			}
			break;
		case R.id.btn_yellow_soar:
			etFoulTime.setText(TotalUtil.getTime());
			if (tvAthleteFlag.getText().toString().equals("√")) {
				showFoulDialog(Constant.YELLOW, Constant.SOAR);
			} else {
				TotalUtil.showToast(context, context.getResources().getString(R.string.Toast_no_athlete));
			}
			break;
		case R.id.btn_foul_list:
			startActivity(new Intent(context, FoulListActivity.class));
			break;

		default:
			break;
		}
	}

	private void showFoulDialog(final String foulStyle, final String foulContent) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_foul, null);
		tvFoulAthlete = (TextView) view.findViewById(R.id.tv_foul_athlete);
		tvFoulStyle = (TextView) view.findViewById(R.id.tv_foul_style);
		tvFoulContent = (TextView) view.findViewById(R.id.tv_foul_content);
		llFoul = (LinearLayout) view.findViewById(R.id.ll_foul_dialog);

		if (foulStyle.equals(Constant.RED)) {
			llFoul.setBackgroundResource(R.color.color_red);
		} else if (foulStyle.equals(Constant.YELLOW)) {
			llFoul.setBackgroundResource(R.color.color_yellow);
		}

		if (radioName.equals(context.getResources().getString(R.string.Number))) {
			tvFoulAthlete.setText(etAthlete.getText().toString().trim() + "（"
					+ context.getResources().getString(R.string.Number) + "）");
		} else {
			tvFoulAthlete.setText(etAthlete.getText().toString().trim() + "（"
					+ context.getResources().getString(R.string.Serial_number) + "）");
		}

		tvFoulContent.setText(foulContent);
		tvFoulStyle.setText(foulStyle);

		builder.setView(llFoul);

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String typeFlag = "";
				String contentFlag = "";
				if (foulStyle.equals(Constant.RED)) {
					typeFlag = "1";
				} else if (foulStyle.equals(Constant.YELLOW)) {
					typeFlag = "0";
				}
				if (foulContent.equals("屈膝")) {
					contentFlag = "1";

				} else if (foulContent.equals("腾空")) {
					contentFlag = "0";
				}
				uploadFoulInfo(typeFlag, contentFlag);
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	private dl_referee judge;

	protected void uploadFoulInfo(final String typeFlag, final String contentFlag) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// 命名空间；
				String nameSpace = "http://tempuri.org/";
				// 连接服务器的网址；
				String endPoint = "http://" + serviceAddress + ":8020/WKWebService.asmx";
				Log.i("endPoint", endPoint);
				// 指定WebService的命名空间和调用的方法名 
				SoapObject soapObject = new SoapObject(nameSpace, Constant.UPDATE_FOULINFO_NAME);

				String athleteID;
				if (radioName.equals("编号")) {
					athleteID = etAthlete.getText().toString().trim();
				} else {
					athleteID = DbService.getInstance(context)
							.queryAthleteByOrder(Integer.parseInt(etAthlete.getText().toString().trim()))
							.getAthlete_ID();
				}
				String timeString = gMid + TotalUtil.getCurrentTimeString();
				Log.i("timeString=", timeString);

				String FoulInfoString = gMid + "," + athletes.getAthlete_group() + "," + judgeName + "," + athleteID
						+ "," + typeFlag + "," + contentFlag + "," + "16:27:47";
				Log.i("FoulInfoString=", FoulInfoString);
				soapObject.addProperty("PackageID", timeString);
				soapObject.addProperty("strSendData", FoulInfoString);

				//  生成调用WebService方法的SOAP请求信息,并指定SOAP的版本 ; 
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);

				envelope.bodyOut = soapObject;
				//  设置是否调用的是dotNet开发的WebService;
				envelope.dotNet = true;
				envelope.setOutputSoapObject(soapObject);

				HttpTransportSE transportSE = new HttpTransportSE(endPoint, 5000);
				try {
					transportSE.debug = true;
					transportSE.call(nameSpace + Constant.UPDATE_FOULINFO_NAME, envelope);
					// 获取返回的数据
					SoapObject object = (SoapObject) envelope.bodyIn;

					Log.i("envelope.getResponse()", envelope.getResponse().toString());
					Log.i("envelope.bodyIn", envelope.bodyIn.toString());
					// 获取返回的结果
					String returnResult = object.getProperty(0).toString();
					Log.i("returnResult", returnResult);
					judge = DbService.getInstance(context).queryRefereeByName(judgeName);
					if (!returnResult.equals(timeString)) {
						mhandler.sendEmptyMessage(2);
						pf_foul foul = new pf_foul(null, (long) Integer.parseInt(athletes.getAthlete_group()),
								judge.getReferee_ID(), athleteID, TotalUtil.getTime(), Integer.parseInt(typeFlag),
								Integer.parseInt(contentFlag), "×", judgeName, TotalUtil.getMacAddress(context), null);
						DbService.getInstance(context).saveFoul(foul);
					} else {
						Log.i("上传成功", returnResult + ",,," + etFoulTime.getText().toString());
						mhandler.sendEmptyMessage(3);
						pf_foul foul = new pf_foul(null, (long) Integer.parseInt(athletes.getAthlete_group()),
								judge.getReferee_ID(), athleteID, TotalUtil.getTime(), Integer.parseInt(typeFlag),
								Integer.parseInt(contentFlag), "√", judgeName, TotalUtil.getMacAddress(context), null);
						DbService.getInstance(context).saveFoul(foul);
					}
				} catch (IOException e) {
					e.printStackTrace();
					Log.e("111111111", "111111111111");
					mhandler.sendEmptyMessage(2);
					judge = DbService.getInstance(context).queryRefereeByName(judgeName);
					pf_foul foul = new pf_foul(null, (long) Integer.parseInt(athletes.getAthlete_group()),
							judge.getReferee_ID(), athleteID, TotalUtil.getTime(), Integer.parseInt(typeFlag),
							Integer.parseInt(contentFlag), "×", judgeName, TotalUtil.getMacAddress(context), null);
					DbService.getInstance(context).saveFoul(foul);
					return;
				} catch (XmlPullParserException e) {
					e.printStackTrace();
					Log.e("222222222", "222222222");
					judge = DbService.getInstance(context).queryRefereeByName(judgeName);
					mhandler.sendEmptyMessage(2);
					pf_foul foul = new pf_foul(null, (long) Integer.parseInt(athletes.getAthlete_group()),
							judge.getReferee_ID(), athleteID, TotalUtil.getTime(), Integer.parseInt(typeFlag),
							Integer.parseInt(contentFlag), "×", judgeName, TotalUtil.getMacAddress(context), null);
					DbService.getInstance(context).saveFoul(foul);
					return;
				}
			}
		}).start();

	}

}
