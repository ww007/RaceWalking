package com.example.fpl_racewaiking.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class TotalUtil {
	private static Toast toast;

	public static Date stringToDate(String dateString) {
		ParsePosition position = new ParsePosition(0);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
		Date dateValue = simpleDateFormat.parse(dateString, position);
		return dateValue;
	}

	/**
	 * 一次toast
	 * 
	 * @param context
	 * @param content
	 */
	public static void showToast(Context context, String content) {
		if (toast == null) {
			toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
		} else {
			toast.setText(content);
		}
		toast.show();
	}

	public static String getCurrentTimeString() {
		Calendar calendar = Calendar.getInstance();
		// 当前年
		String year = calendar.get(calendar.YEAR) + "";
		String month;
		// 当前月
		if (calendar.get(Calendar.MONTH) + 1 < 10) {
			month = "0" + ((calendar.get(Calendar.MONTH)) + 1 + "");
		} else {
			month = (calendar.get(Calendar.MONTH)) + 1 + "";
		}
		String day_of_month;
		// 当前日
		if (calendar.get(Calendar.DAY_OF_MONTH) < 10) {
			day_of_month = "0" + (calendar.get(Calendar.DAY_OF_MONTH) + "");
		} else {
			day_of_month = calendar.get(Calendar.DAY_OF_MONTH) + "";
		}
		String hour;
		// 当前时：HOUR_OF_DAY-24小时制；HOUR-12小时制
		if (calendar.get(Calendar.HOUR_OF_DAY) < 10) {
			hour = "0" + calendar.get(Calendar.HOUR_OF_DAY) + "";
		} else {
			hour = calendar.get(Calendar.HOUR_OF_DAY) + "";
		}
		String minute;
		// 当前分
		if (calendar.get(Calendar.MINUTE) < 10) {
			minute = "0" + calendar.get(Calendar.MINUTE) + "";
		} else {
			minute = calendar.get(Calendar.MINUTE) + "";
		}
		String second;
		// 当前秒
		if (calendar.get(Calendar.SECOND) < 10) {
			second = "0" + calendar.get(Calendar.SECOND) + "";
		} else {
			second = calendar.get(Calendar.SECOND) + "";
		}
		String ms;
		// 当前毫秒
		if (calendar.get(Calendar.MILLISECOND) < 10) {
			ms = "00" + calendar.get(Calendar.MILLISECOND) + "";
		} else if (calendar.get(Calendar.MILLISECOND) < 100) {
			ms = "0" + calendar.get(Calendar.MILLISECOND) + "";
		} else {
			ms = calendar.get(Calendar.MILLISECOND) + "";
		}
		return year + month + day_of_month + hour + minute + second + ms;
	}

	/**
	 * 获取手机mac地址<br/>
	 * 错误返回12个0
	 */
	public static String getMacAddress(Context context) {
		// 获取mac地址：
		String macAddress = "000000000000";
		try {
			WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
			if (null != info) {
				if (!TextUtils.isEmpty(info.getMacAddress()))
					macAddress = info.getMacAddress().replace(":", "");
				else
					return macAddress;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return macAddress;
		}
		return macAddress;
	}

	/**
	 * 获取系统时间
	 * 
	 * @return
	 */
	public static String getTime() {
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int min = calendar.get(Calendar.MINUTE);
		if (min < 10) {
			return hour + ":0" + min;
		} else {
			return hour + ":" + min;
		}
	}

	/**
	 * 获取指定网站的日期时间
	 * 
	 * @param webUrl
	 * @return
	 */
	public static String getWebsiteDatetime() {
		try {
			URL url = new URL("http://www.baidu.com");// 取得资源对象
			URLConnection uc = url.openConnection();// 生成连接对象
			uc.connect();// 发出连接
			long ld = uc.getDate();// 读取网站日期时间
			Date date = new Date(ld);// 转换为标准时间对象
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);// 输出北京时间
			Log.i("sdf.format(date)", sdf.format(date));
			return sdf.format(date);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
