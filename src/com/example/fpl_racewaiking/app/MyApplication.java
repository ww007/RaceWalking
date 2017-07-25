package com.example.fpl_racewaiking.app;

import android.app.Application;
import android.content.Context;
import ww.greendao.dao.DaoMaster;
import ww.greendao.dao.DaoSession;
import ww.greendao.dao.DaoMaster.OpenHelper;

public class MyApplication extends Application {
	private static final String DB_NAME = "DB_APP";
	private static MyApplication mInstance;
	private static DaoSession daoSession;
	private static DaoMaster daoMaster;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
	}

	/**
	 * return DaoMaster
	 * 
	 * @param context
	 */
	public static DaoMaster getDaoMaster(Context context) {
		if (daoMaster == null) {
			OpenHelper helper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
			daoMaster = new DaoMaster(helper.getWritableDatabase());
		}
		return daoMaster;
	}

	/**
	 * return DaoSession
	 * 
	 * @param context
	 */
	public static DaoSession getDaoSession(Context context) {
		if (daoSession == null) {
			if (daoMaster == null) {
				daoMaster = getDaoMaster(context);
			}
			daoSession = daoMaster.newSession();
		}
		return daoSession;
	}

}
