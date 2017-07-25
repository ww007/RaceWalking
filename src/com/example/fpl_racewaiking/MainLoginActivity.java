package com.example.fpl_racewaiking;

import java.io.IOException;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.alibaba.fastjson.JSON;
import com.example.fpl_racewaiking.db.DbService;
import com.example.fpl_racewaiking.util.Constant;
import com.example.fpl_racewaiking.util.TotalUtil;
import com.example.fpl_racewaiking.vo.LoginInfo;
import com.example.fpl_racewaiking.vo.LoginReturns;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ww.greendao.dao.dl_referee;

public class MainLoginActivity extends Activity {

	@Bind(R.id.et_dialog_judge)
	EditText etJudge;
	@Bind(R.id.et_dialog_password)
	EditText etPassword;
	@Bind(R.id.et_dialog_stand)
	EditText etStandNo;
	@Bind(R.id.btn_dialog_login)
	Button btnLogin;
	@Bind(R.id.ib_dialog_return)
	ImageButton ibRetnrn;

	private Context context;
	private SharedPreferences mSharedPreferences;
	private String judgeName;
	private String password;
	private String standNo;
	private String getIp;
	private String serviceAddress;
	private List<LoginReturns> returns;

	private Handler mhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				TotalUtil.showToast(context, "服务器连接异常");
				break;
			case 2:
				TotalUtil.showToast(context, returns.get(0).getNote());
				break;
			case 5:
				TotalUtil.showToast(context, "密码错误");
				break;
			case 6:
				TotalUtil.showToast(context, "账号不存在");
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_judge_login);
		ButterKnife.bind(MainLoginActivity.this);
		context = this;

		mSharedPreferences = this.getSharedPreferences("fpl_RaceWalking", Activity.MODE_PRIVATE);
		judgeName = mSharedPreferences.getString("judgeName", "");
		password = mSharedPreferences.getString("password", "");
		standNo = mSharedPreferences.getString("standNo", "");
		getIp = mSharedPreferences.getString("serviceAddress", null);

		etJudge.setText(judgeName);
		etJudge.setSelection(judgeName.length());
		etPassword.setText(password);
		etPassword.setInputType(Constant.PASSWORD_MIWEN);
		etStandNo.setText(standNo);

		if (getIp == null || getIp.equals("")) {
			serviceAddress = Constant.SERVICE_ADDRESS;
		} else {
			serviceAddress = getIp;
		}

	}

	@OnClick({ R.id.btn_dialog_login, R.id.ib_dialog_return })
	public void setOnClickListener(View view) {
		switch (view.getId()) {
		case R.id.btn_dialog_login:
			login();
			break;
		case R.id.ib_dialog_return:
			finish();
			break;

		default:
			break;
		}
	}

	public void login() {
		final String Judge = etJudge.getText().toString().trim();
		final String pass = etPassword.getText().toString().trim();
		final String position = etStandNo.getText().toString().trim();
		final String mac = TotalUtil.getMacAddress(context);
		if (Judge.isEmpty() || password.isEmpty()) {
			TotalUtil.showToast(context, "登录项不能为空");
		} else {
			new Thread(new Runnable() {

				@Override
				public void run() {
					SharedPreferences mSharedPreferences = context.getSharedPreferences("fpl_RaceWalking",
							Activity.MODE_PRIVATE);
					// 命名空间；
					String nameSpace = "http://tempuri.org/";
					// 连接服务器的网址；
					String endPoint = "http://" + serviceAddress + ":8020/WKWebService.asmx";
					Log.i("endPoint", endPoint);
					// 指定WebService的命名空间和调用的方法名 
					SoapObject soapObject = new SoapObject(nameSpace, Constant.LOGIN_METHOD_NAME);
					soapObject.addProperty("strUserName", Judge);
					soapObject.addProperty("strPassWord", pass);
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

					try {
						transportSE.call(nameSpace + Constant.LOGIN_METHOD_NAME, envelope);
					} catch (IOException e) {
						e.printStackTrace();
						outLineLogin(Judge, pass);
						return;
					} catch (XmlPullParserException e) {
						e.printStackTrace();
						outLineLogin(Judge, pass);
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
							SharedPreferences.Editor editor = mSharedPreferences.edit();
							editor.putString("judgeName", Judge);
							editor.putString("password", pass);
							editor.putString("standNo", returns.get(0).getStandNo());
							editor.putString("gmid", returns.get(0).getGMID());
							editor.putStringSet("selectFoulItems", null);
							editor.putStringSet("selectItems", null);
							editor.commit();
							startActivity(new Intent(context, MainActivity.class));
							MainActivity.activity.finish();
							finish();
						} else {
							mhandler.sendEmptyMessage(2);
						}
					}
				}
			}).start();
		}
	}

	private void outLineLogin(final String Judge, final String password) {
		List<dl_referee> allReferees = DbService.getInstance(context).loadAllReferee();
		if (allReferees == null || allReferees.isEmpty()) {
			mhandler.sendEmptyMessage(2);
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
				MainActivity.activity.finish();
				finish();
			} else {
				mhandler.sendEmptyMessage(5);
			}
		} else {
			mhandler.sendEmptyMessage(6);
		}
	}
}
