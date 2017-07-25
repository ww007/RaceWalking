package com.example.fpl_racewaiking;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.alibaba.fastjson.JSON;
import com.example.fpl_racewaiking.db.DbService;
import com.example.fpl_racewaiking.util.Constant;
import com.example.fpl_racewaiking.util.HttpUtil;
import com.example.fpl_racewaiking.util.TotalUtil;
import com.example.fpl_racewaiking.vo.LoginReturns;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import ww.greendao.dao.dl_referee;

public class LoginActivity extends Activity {
	@Bind(R.id.btn_server_setting)
	Button btnStateSetting;
	@Bind(R.id.et_judge)
	EditText etJudge;
	@Bind(R.id.et_password)
	EditText etPassword;
	@Bind(R.id.et_judge_position)
	EditText etPosition;
	@Bind(R.id.btn_login)
	Button btnLogin;
	@Bind(R.id.radioGroup1)
	RadioGroup rg;
	@Bind(R.id.rd_en)
	RadioButton rdEn;
	@Bind(R.id.rd_zh)
	RadioButton rdZh;
	private SharedPreferences mSharedPreferences;
	private String getIp;
	private String serviceAddress;
	private String getDownloadTime;
	private String getlanguage;

	private static TextView tvServiceState;

	private static Context context;
	private static List<LoginReturns> returns;
	public static Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				TotalUtil.showToast(context, returns.get(0).getNote());
				break;
			case 2:
				TotalUtil.showToast(context, context.getResources().getString(R.string.faild_connection));
				break;
			case 3:
				tvServiceState.setText(R.string.abnormal);
				tvServiceState.setTextColor(0xFFFF0000);
				break;
			case 4:
				tvServiceState.setText(R.string.normal);
				tvServiceState.setTextColor(0xFF00EE00);
				break;
			case 5:
				TotalUtil.showToast(context, context.getResources().getString(R.string.wrong_password));
				break;
			case 6:
				TotalUtil.showToast(context, context.getResources().getString(R.string.no_this_account));
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ButterKnife.bind(LoginActivity.this);
		context = this;

		etPassword.setInputType(Constant.PASSWORD_MIWEN);
		mSharedPreferences = this.getSharedPreferences("fpl_RaceWalking", Activity.MODE_PRIVATE);
		getIp = mSharedPreferences.getString("serviceAddress", null);
		getDownloadTime = mSharedPreferences.getString("downloadTime", null);
		getlanguage = mSharedPreferences.getString("language", null);

		tvServiceState = (TextView) findViewById(R.id.tv_server_status);

		if (getlanguage != null && getlanguage.equals("en")) {
			rdEn.setChecked(true);
		} else {
			rdZh.setChecked(true);
		}

		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// 获取变更后的选中项的ID
				int radioButtonId = group.getCheckedRadioButtonId();
				// 根据ID获取RadioButton的实例
				RadioButton rb = (RadioButton) LoginActivity.this.findViewById(radioButtonId);
				Editor editor = mSharedPreferences.edit();
				if (rb.getText().toString().equals("English")) {
					shiftLanguage("en");
					editor.putString("language", "en");
				} else {
					shiftLanguage("zh");
					editor.putString("language", "zh");
				}
				editor.commit();
			}
		});

	}

	private void shiftLanguage(String sta) {
		if (sta.equals("en")) {
			Locale.setDefault(Locale.ENGLISH);
			Configuration config = getBaseContext().getResources().getConfiguration();
			config.locale = Locale.ENGLISH;
			getBaseContext().getResources().updateConfiguration(config,
					getBaseContext().getResources().getDisplayMetrics());
			refreshSelf();
		} else {
			Locale.setDefault(Locale.CHINESE);
			Configuration config = getBaseContext().getResources().getConfiguration();
			config.locale = Locale.CHINESE;
			getBaseContext().getResources().updateConfiguration(config,
					getBaseContext().getResources().getDisplayMetrics());
			refreshSelf();
		}

	}

	private void refreshSelf() {
		Intent intent = new Intent(this, LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(intent);
		finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (getIp == null || getIp.equals("")) {
			serviceAddress = Constant.SERVICE_ADDRESS;
		} else {
			serviceAddress = getIp;
		}
		HttpUtil.getServiceTime("登录", serviceAddress);
		Log.i("onResume--->", "onResume");
		etJudge.setText("");
		etPassword.setText("");
		etPosition.setText("");
	}

	@OnClick(R.id.btn_server_setting)
	public void serviceSetting() {
		Intent intent = new Intent(LoginActivity.this, ServiceSettingActivity.class);
		intent.putExtra("serviceState", tvServiceState.getText().toString().trim());
		startActivity(intent);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			new AlertDialog.Builder(LoginActivity.this).setTitle("提示").setMessage("确定退出程序？")
					.setPositiveButton("取消", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					}).setNegativeButton("确定", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							finish();
						}
					}).show();
			break;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@OnClick(R.id.btn_login)
	public void login() {
		final String Judge = etJudge.getText().toString().trim();
		final String password = etPassword.getText().toString().trim();
		final String position = etPosition.getText().toString().trim();
		final String mac = TotalUtil.getMacAddress(context);
		Log.i("mac", mac);

		if (Judge.isEmpty() || password.isEmpty()) {
			TotalUtil.showToast(context, context.getResources().getString(R.string.no_refree_pass));
		} else {
			new Thread(new Runnable() {

				@Override
				public void run() {
					// 命名空间；
					String nameSpace = "http://tempuri.org/";
					// 连接服务器的网址；
					String endPoint = "http://" + serviceAddress + ":8020/WKWebService.asmx";
					Log.i("endPoint", endPoint);
					// 指定WebService的命名空间和调用的方法名 
					SoapObject soapObject = new SoapObject(nameSpace, Constant.LOGIN_METHOD_NAME);
					soapObject.addProperty("strUserName", Judge);
					soapObject.addProperty("strPassWord", password);
					soapObject.addProperty("strStandNo", position);
					soapObject.addProperty("strNumber", "13922880954");
					soapObject.addProperty("strMacInfo", "A2ACACABA2AC");

					//  生成调用WebService方法的SOAP请求信息,并指定SOAP的版本 ; 
					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);

					envelope.bodyOut = soapObject;
					//  设置是否调用的是dotNet开发的WebService;
					envelope.dotNet = true;
					envelope.setOutputSoapObject(soapObject);

					HttpTransportSE transportSE = new HttpTransportSE(endPoint, 5000);

					// 重写MyAndroidHttpTransport方法自定义连接超时时间
					// MyAndroidHttpTransport transportSE = new
					// MyAndroidHttpTransport(endPoint, Constant.OUTOF_TIME);

					try {
						transportSE.call(nameSpace + Constant.LOGIN_METHOD_NAME, envelope);
					} catch (IOException e) {
						e.printStackTrace();
						outLineLogin(Judge, password);
						return;
					} catch (XmlPullParserException e) {
						e.printStackTrace();
						outLineLogin(Judge, password);
						return;
					}
					// 获取返回的数据
					SoapObject object = (SoapObject) envelope.bodyIn;
					// 获取返回的结果
					String result = object.getProperty(0).toString();
					if (result.isEmpty()) {
						TotalUtil.showToast(context, "获取数据失败");
					} else {
						Log.i("返回的结果result=", result);
						returns = JSON.parseArray("[" + result + "]", LoginReturns.class);
						Log.i("loginReturns", returns.toString());
						if (returns.get(0).getNote().equals("登录成功")) {
							Editor editor = mSharedPreferences.edit();
							editor.putString("judgeName", Judge);
							editor.putString("password", password);
							editor.putString("standNo", returns.get(0).getStandNo());
							editor.putString("gmid", returns.get(0).getGMID());
							editor.commit();
							startActivity(new Intent(context, MainActivity.class));
							if (getDownloadTime != null && !getDownloadTime.isEmpty()) {
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
								try {
									if (sdf.parse(getDownloadTime).before(sdf.parse(returns.get(0).getEndUpTime()))) {
										HttpUtil.getGameInfo(context, Integer.parseInt(returns.get(0).getGMID().trim()),
												serviceAddress);
									} else {
										Log.i("不用更新", "不用更新");
									}
								} catch (NumberFormatException e) {
									e.printStackTrace();
								} catch (ParseException e) {
									e.printStackTrace();
								}
							} else {
								HttpUtil.getGameInfo(context, Integer.parseInt(returns.get(0).getGMID().trim()),
										serviceAddress);
							}
						} else {
							mHandler.sendEmptyMessage(1);
						}
					}

				}

			}).start();
		}

	}

	private void outLineLogin(final String Judge, final String password) {
		List<dl_referee> allReferees = DbService.getInstance(context).loadAllReferee();
		if (allReferees == null || allReferees.isEmpty()) {
			mHandler.sendEmptyMessage(2);
			return;
		}
		dl_referee currentjudge = DbService.getInstance(context).queryRefereeByName(Judge);
		if (currentjudge != null) {
			if (currentjudge.getReferee_password().equals(password)) {
				SharedPreferences.Editor editor = mSharedPreferences.edit();
				editor.putString("judgeName", Judge);
				editor.putString("password", password);
				editor.putString("standNo", currentjudge.getReferee_position());
				editor.putString("gmid", currentjudge.getReferee_gmid());
				editor.commit();
				startActivity(new Intent(context, MainActivity.class));
			} else {
				mHandler.sendEmptyMessage(5);
			}
		} else {
			mHandler.sendEmptyMessage(6);
		}
	}

}
