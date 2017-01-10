package com.hl.utils.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import com.hl.utils.LogUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;

/**
 * 网络帮助类 包含一些网络判断方法
 */
public class NetUtils {

	private static final String TAG = NetUtils.class.getSimpleName();

	// (1:2g,2:3g,3:4g,4:WIFI,5:其他)
	/** 未知 */
	public static final String NETWORKTYPE_INVALID = "5";
	/** WIFI网络 */
	public static final String NETWORKTYPE_WIFI = "4";
	/** 4G网络 */
	public static final String NETWORKTYPE_4G = "3";
	/** 3G网络 */
	public static final String NETWORKTYPE_3G = "2";
	/** 2G网络 */
	public static final String NETWORKTYPE_2G = "1";

	// (1:移动，2：连通，3：电信，4：其他)
	/**
	 * @Fields OPERATOR_CMCC : TODO 中国移动-chinamobile
	 */
	private static final String OPERATOR_CMCC = "1";
	/**
	 * @Fields OPERATOR_CUCC : TODO 中国联通-chinaunicom
	 */
	private static final String OPERATOR_CUCC = "2";
	/**
	 * @Fields OPERATOR_CTCC : TODO 中国电信-chinatelecom
	 */
	private static final String OPERATOR_CTCC = "3";
	/**
	 * @Fields OPERATOR_NONE : TODO 其他
	 */
	private static final String OPERATOR_NONE = "4";

	/**
	 * 检查网络状态 在请求网络前 建议先检查一下 网络状态
	 * 
	 * @param context
	 * @return true 有网络； false 没有网络;
	 */
	public static boolean checkNetState(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		}
		NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
		if (networkInfo == null) {
			return false;
		}
		return networkInfo.isConnected();
	}

	/**
	 * 判断wifi 是否可用
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static boolean isWifiDataEnable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager == null) {
			return false;
		}
		NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (networkInfo == null) {
			return false;
		}
		return networkInfo.isConnected();
	}

	private static boolean isFastMobileNetwork(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		switch (telephonyManager.getNetworkType()) {
		case TelephonyManager.NETWORK_TYPE_1xRTT:
			return false; // ~ 50-100 kbps
		case TelephonyManager.NETWORK_TYPE_CDMA:
			return false; // ~ 14-64 kbps
		case TelephonyManager.NETWORK_TYPE_EDGE:
			return false; // ~ 50-100 kbps
		case TelephonyManager.NETWORK_TYPE_EVDO_0: // 3G
			return false; // ~ 400-1000 kbps
		case TelephonyManager.NETWORK_TYPE_EVDO_A: // 3G
			return false; // ~ 600-1400 kbps
		case TelephonyManager.NETWORK_TYPE_GPRS:
			return false; // ~ 100 kbps
		case TelephonyManager.NETWORK_TYPE_HSDPA: // 3G
			return false; // ~ 2-14 Mbps
		case TelephonyManager.NETWORK_TYPE_HSPA: // 3G
			return false; // ~ 700-1700 kbps
		case TelephonyManager.NETWORK_TYPE_HSUPA:
			return false; // ~ 1-23 Mbps
		case TelephonyManager.NETWORK_TYPE_UMTS: // 3G
			return false; // ~ 400-7000 kbps
		case TelephonyManager.NETWORK_TYPE_EHRPD:
			return false; // ~ 1-2 Mbps
		case TelephonyManager.NETWORK_TYPE_EVDO_B:
			return false; // ~ 5 Mbps
		case TelephonyManager.NETWORK_TYPE_HSPAP:
			return false; // ~ 10-20 Mbps
		case TelephonyManager.NETWORK_TYPE_IDEN:
			return false; // ~25 kbps
		case TelephonyManager.NETWORK_TYPE_LTE:
			return true; // ~ 10+ Mbps
		case TelephonyManager.NETWORK_TYPE_UNKNOWN:
			return false;
		default:
			return false;
		}
	}

	/**
	 * 判断网络速度 ，wifi 4g is true , else false
	 */
	public static boolean isFastNet(Context context) {

		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isConnected()) {
			String type = networkInfo.getTypeName();

			if (type.equalsIgnoreCase("WIFI")) {
				return true;
			} else if (type.equalsIgnoreCase("MOBILE")) {

				if (isFastMobileNetwork(context)) {
					return true;
				} else
					return false;
			}
		}
		return false;
	}

	/**
	 *  获取网络类型 (1:2g,2:3g,3:4g,4:WIFI,5:其他)
	 */
	public static String getNetWorkType(Context context) {
		String type = null;
		ConnectivityManager connectionManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectionManager == null) {
			return null;
		}
		NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return null;
		}
		String typeName = networkInfo.getTypeName();
		if ("WIFI".equals(typeName)) {// wifi
			type = NETWORKTYPE_WIFI;
		} else if ("MOBILE".equals(typeName)) {// 手机
			int subType = networkInfo.getSubtype();
			switch (subType) {
			case TelephonyManager.NETWORK_TYPE_CDMA:// 电信2G
				type = NETWORKTYPE_2G;
				break;
			case TelephonyManager.NETWORK_TYPE_EDGE:// 移动2G
				type = NETWORKTYPE_2G;
				break;
			case TelephonyManager.NETWORK_TYPE_GPRS:// 联通2G
				type = NETWORKTYPE_2G;
				break;
			case TelephonyManager.NETWORK_TYPE_EVDO_0:// 版本0.（电信3g）
				type = NETWORKTYPE_3G;
				break;
			case TelephonyManager.NETWORK_TYPE_EVDO_A:// 版本A （电信3g）
				type = NETWORKTYPE_3G;
				break;
			case TelephonyManager.NETWORK_TYPE_EVDO_B:// 版本B（电信3g）
				type = NETWORKTYPE_3G;
				break;
			case TelephonyManager.NETWORK_TYPE_HSDPA:// （联通3g）
				type = NETWORKTYPE_3G;
				break;
			case TelephonyManager.NETWORK_TYPE_UMTS:// UMTS（联通3g）
				type = NETWORKTYPE_3G;
				break;
			case TelephonyManager.NETWORK_TYPE_LTE:// LTE(3g到4g的一个过渡，称为准4g)
				type = NETWORKTYPE_4G;
				break;
			case TelephonyManager.NETWORK_TYPE_UNKNOWN:// 未知
				type = NETWORKTYPE_INVALID;
				break;
			default:
				type = NETWORKTYPE_INVALID;
				break;
			}
		}
		return type;
	}

	/**
	 *  获取运营商(1:移动，2：连通，3：电信，4：其他)
	 */
	public static String getSimOperatorName(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String operator = OPERATOR_NONE;
		if (tm.getSimState() == TelephonyManager.SIM_STATE_READY) {
			String name = tm.getSimOperatorName();// 运营商名字
			LogUtils.e(TAG, "" + name);
			String IMSI = tm.getSubscriberId();
			if (IMSI == null || IMSI.equals("")) {
				return operator;
			}
			if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {// 中国移动
				operator = OPERATOR_CMCC;
			} else if (IMSI.startsWith("46001")) {// 中国联通
				operator = OPERATOR_CUCC;
			} else if (IMSI.startsWith("46003")) {// 中国电信
				operator = OPERATOR_CTCC;
			}
		}
		return operator;
	}

	/**
	 * 测试rul 是否可连接
	 */
	public static boolean testUrl(String url_s) {
		try {
			URL url = new URL(url_s);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			if (conn.getResponseCode() == 200) {
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			return false;
		}
	}

	public static String getNetIpAddress(Context mContext) {
		String ip = null;
		WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);

		if (!wifiManager.isWifiEnabled()) {
			try {
				for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
					NetworkInterface intf = en.nextElement();
					for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
						InetAddress inetAddress = enumIpAddr.nextElement();
						if (!inetAddress.isLoopbackAddress()) {
							ip = inetAddress.getHostAddress().toString();
						}
					}
				}
			} catch (SocketException ex) {
			}
		} else {
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			int ipAddress = wifiInfo.getIpAddress();
			ip = intToIp(ipAddress);

		}
		return ip;
	}

	private static String intToIp(int i) {
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
	}
}
