package com.example.fpl_racewaiking.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.alibaba.fastjson.JSON;
import com.example.fpl_racewaiking.FoulListActivity;
import com.example.fpl_racewaiking.LoginActivity;
import com.example.fpl_racewaiking.ServiceSettingActivity;
import com.example.fpl_racewaiking.db.DbService;
import com.example.fpl_racewaiking.vo.GameInfo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import ww.greendao.dao.dl_referee;
import ww.greendao.dao.pf_athlete;
import ww.greendao.dao.pf_foul;
import ww.greendao.dao.pf_group;

public class HttpUtil {

	/**
	 * 下载比赛信息
	 * 
	 * @param context
	 * @param number
	 * @param serviceAddress
	 */
	public static void getGameInfo(final Context context, final int number, final String serviceAddress) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// 命名空间；
				String nameSpace = "http://tempuri.org/";
				// 连接服务器的网址；
				String endPoint = "http://" + serviceAddress + ":8020/WKWebService.asmx";
				// 指定WebService的命名空间和调用的方法名 
				SoapObject soapObject = new SoapObject(nameSpace, Constant.DOWNLOAD_METHOD_NAME);
				soapObject.addProperty("nGMID", number);

				//  生成调用WebService方法的SOAP请求信息,并指定SOAP的版本 ; 
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);

				envelope.bodyOut = soapObject;
				//  设置是否调用的是dotNet开发的WebService;
				envelope.dotNet = true;
				envelope.setOutputSoapObject(soapObject);

				HttpTransportSE transportSE = new HttpTransportSE(endPoint, 5000);
				try {
					transportSE.call(nameSpace + Constant.DOWNLOAD_METHOD_NAME, envelope);
				} catch (IOException e) {
					e.printStackTrace();
					Log.e("111111111", "111111111111");
					return;
				} catch (XmlPullParserException e) {
					e.printStackTrace();
					Log.e("222222222", "222222222222");
					return;
				}
				// 获取返回的数据
				SoapObject object = (SoapObject) envelope.bodyIn;
				// 获取返回的结果
				String result = object.getProperty(0).toString();
				if (result.isEmpty()) {
				} else {
					Log.i("下载信息result=", result);
					SharedPreferences mSharedPreferences = context.getSharedPreferences("fpl_RaceWalking",
							Activity.MODE_PRIVATE);
					SharedPreferences.Editor editor = mSharedPreferences.edit();
					editor.putStringSet("selectFoulItems", null);
					editor.putStringSet("selectItems", null);
					editor.commit();
					editor.putString("downloadTime", TotalUtil.getWebsiteDatetime());
					editor.commit();

					String newResult = "[" + result.replace("$", ",").substring(0, result.length() - 1) + "]";
					List<GameInfo> gameInfo = JSON.parseArray(newResult, GameInfo.class);
					Log.i("gameInfo", gameInfo.toString());
					for (GameInfo info : gameInfo) {
						if (info.getType().equals("1")) {
							String[] groups = info.getInfo().split(",");
							Log.i("groups", "=" + groups.length);
							List<pf_group> pf_groups = new ArrayList<>();
							for (int i = 0; i < Integer.parseInt(groups[0].trim()); i++) {
								pf_group pf_group = new pf_group();
								pf_group.setGroup_CHN_content(groups[2 + 3 * i]);
								pf_group.setGroup_description("");
								pf_group.setGroup_ENG_content(groups[3 + 3 * i]);
								pf_group.setGroup_ID((long) Integer.parseInt(groups[1 + 3 * i].trim()));

								pf_groups.add(pf_group);
							}
							Log.i("pf_groups", pf_groups.get(0).getGroup_ID() + "");
							DbService.getInstance(context).saveGroupList(pf_groups);
						} else if (info.getType().equals("3")) {
							String[] athletes = info.getInfo().split(",");
							List<pf_athlete> pf_athletes = new ArrayList<>();
							for (int i = 0; i < Integer.parseInt(athletes[0].trim()); i++) {
								pf_athlete pf_athlete = new pf_athlete();
								pf_athlete.setAthlete_group(athletes[3 + 3 * i]);
								pf_athlete.setAthlete_ID(athletes[1 + 3 * i]);
								pf_athlete.setAthlete_name(null);
								pf_athlete.setAthlete_order(Integer.parseInt(athletes[2 + 3 * i]));
								pf_athlete.setID(null);

								pf_athletes.add(pf_athlete);
							}
							DbService.getInstance(context).saveAthleteList(pf_athletes);
						} else if (info.getType().equals("2")) {
							String[] referees = info.getInfo().split(",");
							List<dl_referee> dl_referees = new ArrayList<>();
							for (int i = 0; i < Integer.parseInt(referees[0].trim()); i++) {
								dl_referee dl_referee = new dl_referee();
								dl_referee.setReferee_CHN_name(referees[3 + 6 * i]);
								dl_referee.setReferee_ENG_name(referees[4 + 6 * i]);
								dl_referee.setReferee_password(referees[2 + 6 * i]);
								dl_referee.setReferee_position(referees[5 + 6 * i]);
								dl_referee.setReferee_state(referees[6 + 6 * i]);
								dl_referee.setReferee_gmid(info.getId());

								dl_referees.add(dl_referee);
							}
							DbService.getInstance(context).saveRefereeList(dl_referees);
						}
					}
				}

			}
		}).start();
	}

	/**
	 * 获取服务器时间
	 * 
	 * @param flag
	 * @param ip
	 */
	public static void getServiceTime(final String flag, final String ip) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// 命名空间；
				String nameSpace = "http://tempuri.org/";
				// 连接服务器的网址；
				String endPoint = "http://" + ip + ":8020/WKWebService.asmx";
				Log.i("endPoint", endPoint);
				// 指定WebService的命名空间和调用的方法名 
				SoapObject soapObject = new SoapObject(nameSpace, Constant.GET_SERVICE_TIME);
				//  生成调用WebService方法的SOAP请求信息,并指定SOAP的版本 ; 
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);

				envelope.bodyOut = soapObject;
				//  设置是否调用的是dotNet开发的WebService;
				envelope.dotNet = true;
				envelope.setOutputSoapObject(soapObject);

				HttpTransportSE transportSE = new HttpTransportSE(endPoint, 8000);
				// MyAndroidHttpTransport transportSE = new
				// MyAndroidHttpTransport(endPoint, Constant.OUTOF_TIME);
				try {
					transportSE.debug = true;
					transportSE.call(nameSpace + Constant.GET_SERVICE_TIME, envelope);
				} catch (IOException e) {
					e.printStackTrace();
					Log.e("111111111", "111111111111");
					if (flag.equals("登录")) {
						LoginActivity.mHandler.sendEmptyMessage(3);
					} else {
						ServiceSettingActivity.mHandler.sendEmptyMessage(1);
					}
					return;
				} catch (XmlPullParserException e) {
					e.printStackTrace();
					Log.e("222222222", "222222222");
					return;
				}
				// 获取返回的数据
				SoapObject object = (SoapObject) envelope.bodyIn;
				// 获取返回的结果
				String serviceTime = object.getProperty(0).toString();
				Log.i("serviceTime", serviceTime);
				if (flag.equals("登录")) {
					LoginActivity.mHandler.sendEmptyMessage(4);
				} else {
					ServiceSettingActivity.mHandler.sendEmptyMessage(2);
				}
			}
		}).start();

	}

	/**
	 * 上传判罚数据
	 * 
	 * @param context
	 * 
	 * @param gMid
	 * @param ip
	 * @param foulLists
	 */
	public static void sendFoulInfo(final Context context, final String gMid, final String ip,
			final List<pf_foul> foulLists) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// 命名空间；
				String nameSpace = "http://tempuri.org/";
				// 连接服务器的网址；
				String endPoint = "http://" + ip + ":8020/WKWebService.asmx";
				Log.i("endPoint", endPoint);
				// 指定WebService的命名空间和调用的方法名 
				SoapObject soapObject = new SoapObject(nameSpace, Constant.UPDATE_FOULINFO_NAME);
				String packageID = gMid + TotalUtil.getCurrentTimeString();
				soapObject.addProperty("PackageID", packageID);
				String strSendData = "";
				List<pf_foul> pf_fouls = new ArrayList<>();
				for (pf_foul foul : foulLists) {
					String string = gMid + "," + foul.getGroup_ID() + "," + foul.getFoul_referee_name() + ","
							+ foul.getFoul_athlete_ID() + "," + foul.getFoul_card() + "," + foul.getFoul_type() + ","
							+ foul.getFoul_time() + "$";
					strSendData += string;
					pf_foul foul2 = new pf_foul(foul.getFoul_ID(), foul.getGroup_ID(), foul.getReferee_ID(),
							foul.getFoul_athlete_ID(), foul.getFoul_time(), foul.getFoul_card(), foul.getFoul_type(),
							"√", foul.getFoul_referee_name(), foul.getFoul_MAC(), foul.getFoul_description());
					pf_fouls.add(foul2);
				}
				Log.i("strSendData=", strSendData.substring(0, strSendData.length() - 1));
				soapObject.addProperty("strSendData", strSendData.substring(0, strSendData.length() - 1));
				//  生成调用WebService方法的SOAP请求信息,并指定SOAP的版本 ; 
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);

				envelope.bodyOut = soapObject;
				//  设置是否调用的是dotNet开发的WebService;
				envelope.dotNet = true;
				envelope.setOutputSoapObject(soapObject);

				HttpTransportSE transportSE = new HttpTransportSE(endPoint, 8000);
				try {
					transportSE.debug = true;
					transportSE.call(nameSpace + Constant.UPDATE_FOULINFO_NAME, envelope);
					// 获取返回的数据
					SoapObject object = (SoapObject) envelope.bodyIn;
					// 获取返回的结果
					String returnResult = object.getProperty(0).toString();
					Log.i("returnResult", returnResult);
					if (returnResult.equals(packageID)) {
						FoulListActivity.mHandler.sendEmptyMessage(1);
						DbService.getInstance(context).saveFoulList(pf_fouls);
					} else {
						FoulListActivity.mHandler.sendEmptyMessage(2);
					}
				} catch (IOException e) {
					e.printStackTrace();
					FoulListActivity.mHandler.sendEmptyMessage(3);
					return;
				} catch (XmlPullParserException e) {
					e.printStackTrace();
					FoulListActivity.mHandler.sendEmptyMessage(3);
					return;
				}
			}
		}).start();
	}

	// public static void login(final Context context, final String
	// serviceAddress, final LoginInfo loginInfo) {
	// new Thread(new Runnable() {
	//
	// @Override
	// public void run() {
	// SharedPreferences mSharedPreferences =
	// context.getSharedPreferences("fpl_RaceWalking",
	// Activity.MODE_PRIVATE);
	// // 命名空间；
	// String nameSpace = "http://tempuri.org/";
	// // 连接服务器的网址；
	// String endPoint = "http://" + serviceAddress + ":8020/WKWebService.asmx";
	// Log.i("endPoint", endPoint);
	// // 指定WebService的命名空间和调用的方法名 
	// SoapObject soapObject = new SoapObject(nameSpace,
	// Constant.LOGIN_METHOD_NAME);
	// soapObject.addProperty("strUserName", loginInfo.getStrUserName());
	// soapObject.addProperty("strPassWord", loginInfo.getStrPassWord());
	// soapObject.addProperty("strStandNo", loginInfo.getStrStandNo());
	// soapObject.addProperty("strNumber", "13922880954");
	// soapObject.addProperty("strMacInfo", "A2ACACABA2AC");
	//
	// //  生成调用WebService方法的SOAP请求信息,并指定SOAP的版本 ; 
	// SoapSerializationEnvelope envelope = new
	// SoapSerializationEnvelope(SoapEnvelope.VER10);
	//
	// envelope.bodyOut = soapObject;
	// //  设置是否调用的是dotNet开发的WebService;
	// envelope.dotNet = true;
	// envelope.setOutputSoapObject(soapObject);
	//
	// HttpTransportSE transportSE = new HttpTransportSE(endPoint, 5000);
	//
	// try {
	// transportSE.call(nameSpace + Constant.LOGIN_METHOD_NAME, envelope);
	// } catch (IOException e) {
	// e.printStackTrace();
	// MainActivity.mhandler.sendEmptyMessage(1);
	// return;
	// } catch (XmlPullParserException e) {
	// e.printStackTrace();
	// MainActivity.mhandler.sendEmptyMessage(1);
	// return;
	// }
	// // 获取返回的数据
	// SoapObject object = (SoapObject) envelope.bodyIn;
	// // 获取返回的结果
	// String result = object.getProperty(0).toString();
	// if (result.isEmpty()) {
	// TotalUtil.showToast(context, "获取数据失败");
	// } else {
	// Log.i("返回的结果result=", result);
	// List<LoginReturns> returns = JSON.parseArray("[" + result + "]",
	// LoginReturns.class);
	// Log.i("loginReturns", returns.toString());
	// if (returns.get(0).getNote().equals("登录成功")) {
	// SharedPreferences.Editor editor = mSharedPreferences.edit();
	// editor.putString("judgeName", loginInfo.getStrUserName());
	// editor.putString("password", loginInfo.getStrPassWord());
	// editor.putString("standNo", loginInfo.getStrStandNo());
	// editor.commit();
	// } else {
	// MainActivity.sendHandlerReturn(returns.get(0).getNote());
	// }
	// }
	//
	// }
	// }).start();
	// }

}
