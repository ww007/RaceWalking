package com.example.fpl_racewaiking.util;

import com.example.fpl_racewaiking.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainLoginDialog extends Dialog {

	private EditText etJudge;
	private EditText etPassword;
	private EditText etStandNo;
	private Button btnLogin;
	private ImageButton ibRetnrn;

	public MainLoginDialog(Context context) {
		super(context, R.style.LoginSettingDialog);
		setLoginSettingDialog();
	}

	private void setLoginSettingDialog() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_judge_login, null);
		etJudge = (EditText) view.findViewById(R.id.et_dialog_judge);
		etPassword = (EditText) view.findViewById(R.id.et_dialog_password);
		etStandNo = (EditText) view.findViewById(R.id.et_dialog_stand);
		btnLogin = (Button) view.findViewById(R.id.btn_dialog_login);
		ibRetnrn = (ImageButton) view.findViewById(R.id.ib_dialog_return);

		super.setContentView(view);
	}

	public EditText getEtJudge() {
		return etJudge;
	}

	public EditText getEtPassword() {
		return etPassword;
	}

	public EditText getEtStandNo() {
		return etStandNo;
	}

	@Override
	public void setContentView(int layoutResID) {
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
	}

	@Override
	public void setContentView(View view) {
	}

	public void setLoginListener(View.OnClickListener listener) {
		btnLogin.setOnClickListener(listener);
	}

	public void setOnReturnListener(View.OnClickListener listener) {
		ibRetnrn.setOnClickListener(listener);
	}

}
