package com.example.fpl_racewaiking.util;

import com.example.fpl_racewaiking.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginSettingDialog extends Dialog {

	private EditText etCode;
	private TextView tvCode;
	private Button btnConfirm, btnCancel;
	private LoginSettingDialog dialog;

	public LoginSettingDialog(Context context) {
		super(context, R.style.LoginSettingDialog);
		setLoginSettingDialog();
	}

	private void setLoginSettingDialog() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_setting, null);
		etCode = (EditText) view.findViewById(R.id.et_dialog_code);
		tvCode = (TextView) view.findViewById(R.id.tv_dialog_code);
		btnCancel = (Button) view.findViewById(R.id.btn_dialog_cancel);
		btnConfirm = (Button) view.findViewById(R.id.btn_dialog_confirm);

		super.setContentView(view);
	}

	public EditText getEtCode() {
		return etCode;
	}

	public TextView getTvCode() {
		return tvCode;
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

	public void setOnConfirmListener(View.OnClickListener listener) {
		btnConfirm.setOnClickListener(listener);
	}

	public void setOnCancelListener(View.OnClickListener listener) {
		btnCancel.setOnClickListener(listener);
	}

}
