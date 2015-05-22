package com.tech.frontier.utils;

import com.tech.frontier.models.entities.UserInfo;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferUtil {
	static SharePreferUtil sharePreferUtil;
	static SharedPreferences preferences;

	private SharePreferUtil(Context context) {
		preferences = context.getSharedPreferences(Constants.PREFS_NAME, 0);

	}

	public static void init(Context context) {
		if (sharePreferUtil == null) {
			sharePreferUtil = new SharePreferUtil(context);
		}
	}

	/**
	 * 
	 * @param userInfo
	 */
	public static void addUserInfo(UserInfo userInfo) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("location", userInfo.location);
		editor.putString("name", userInfo.name);
		editor.putString("profile_image_url", userInfo.profile_image_url);
		editor.commit();
	}

	/**
	 * 
	 * @return
	 */
	public static UserInfo getUserInfo() {
		UserInfo info = new UserInfo();
		info.name = preferences.getString("name", "");
		info.location = preferences.getString("location", "");
		info.profile_image_url = preferences.getString("profile_image_url", "");
		return info;
	}

}
