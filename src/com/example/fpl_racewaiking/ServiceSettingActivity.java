package com.example.fpl_racewaiking;

import java.util.Random;

import com.example.fpl_racewaiking.util.Constant;
import com.example.fpl_racewaiking.util.HttpUtil;
import com.example.fpl_racewaiking.util.LoginSettingDialog;
import com.example.fpl_racewaiking.util.TotalUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ServiceSettingActivity extends Activity {

	@Bind(R.id.et_service_address)
	EditText etAddress;
	@Bind(R.id.btn_first_address)
	Button btnFirstAddress;
	@Bind(R.id.btn_test_connection)
	Button btnConnect;
	@Bind(R.id.btn_clear_data)
	Button btnClear;
	@Bind(R.id.ib_setting_return)
	ImageButton ibReturn;
	private Context context;
	private LoginSettingDialog dialog;
	private String getIp;
	private static SharedPreferences mSharedPreferences;
	private static TextView tvState;
	private static String string;

	public static Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				tvState.setText("失败");
				tvState.setTextColor(0xFFFF0000);
				break;
			case 2:
				tvState.setText("成功");
				tvState.setTextColor(0xFF00EE00);
				SharedPreferences.Editor editor = mSharedPreferences.edit();
				editor.putString("serviceAddress", string);
				editor.commit();
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service_setting);
		context = this;

		ButterKnife.bind(ServiceSettingActivity.this);

		mSharedPreferences = this.getSharedPreferences("fpl_RaceWalking", Activity.MODE_PRIVATE);
		getIp = mSharedPreferences.getString("serviceAddress", null);

		Intent intent = getIntent();
		String state = intent.getStringExtra("serviceState");

		tvState = (TextView) findViewById(R.id.tv_service_state);
		if (state.equals("正常")) {
			tvState.setText("成功");
			tvState.setTextColor(0xFF00EE00);
		} else if (state.equals("异常")) {
			tvState.setText("失败");
			tvState.setTextColor(0xFFFF0000);
		}

		if (getIp == null || getIp.equals("")) {
			etAddress.setText(Constant.SERVICE_ADDRESS);
			etAddress.setSelection(Constant.SERVICE_ADDRESS.length());
		} else {
			etAddress.setText(getIp);
			etAddress.setSelection(getIp.length());
		}

	}

	@OnClick({ R.id.btn_first_address, R.id.btn_test_connection, R.id.ib_setting_return, R.id.btn_clear_data })
	public void onClickListener(View view) {
		switch (view.getId()) {
		case R.id.btn_first_address:
			etAddress.setText(Constant.SERVICE_ADDRESS);
			etAddress.setSelection(Constant.SERVICE_ADDRESS.length());
			tvState.setText("");
			break;
		case R.id.btn_test_connection:
			tvState.setText("");
			string = etAddress.getText().toString().trim();
			HttpUtil.getServiceTime("服务器设置", string);
			break;
		case R.id.ib_setting_return:
			finish();
			break;
		case R.id.btn_clear_data:
			dialogShow();
			break;
		default:
			break;
		}

	}

	private void dialogShow() {
		final String code = createCode();
		dialog = new LoginSettingDialog(context);
		dialog.getTvCode().setText(code);
		dialog.setOnConfirmListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String getCode = dialog.getEtCode().getText().toString().trim();
				String currentCode = dialog.getTvCode().getText().toString();
				Log.i("getCode", getCode);
				Log.i("currentCode", currentCode);
				if (currentCode.equals(getCode)) {
					SharedPreferences.Editor editor1 = mSharedPreferences.edit();
					editor1.putString("serviceAddress", "");
					editor1.commit();
					dialog.dismiss();
					TotalUtil.showToast(context, "本地数据已清空");
				} else {
					dialog.getEtCode().setText("");
					dialog.getTvCode().setText(createCode());
				}

			}
		});
		dialog.setOnCancelListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});
		dialog.show();
	}

	private String createCode() {
		Random random = new Random();
		String code1 = random.nextInt(10) + "";
		String code2 = random.nextInt(10) + "";
		String code3 = random.nextInt(10) + "";
		String code4 = random.nextInt(10) + "";
		String code = code1 + code4 + code2 + code3;
		return code;
	}

}
