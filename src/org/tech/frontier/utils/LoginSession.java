
package org.tech.frontier.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import org.tech.frontier.entities.UserInfo;

public class LoginSession {
    static LoginSession sLoginSession;
    SharedPreferences mPreferences;

    private LoginSession(Context context) {
        mPreferences = context.getSharedPreferences(Constants.PREFS_NAME, 0);
    }

    public static void init(Context context) {
        if (sLoginSession == null) {
            sLoginSession = new LoginSession(context);
        }
    }

    public static LoginSession getLoginSession() {
        return sLoginSession;
    }

    /**
     * @param userInfo
     */
    public void saveUserInfo(UserInfo userInfo) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString("location", userInfo.location);
        editor.putString("name", userInfo.name);
        editor.putString("token", userInfo.token);
        editor.putString("profile_image_url", userInfo.profileImgUrl);
        editor.putString("uid", userInfo.uid);
        editor.commit();
    }

    public void clear() {
        mPreferences.edit().clear().commit();
    }

    public UserInfo getUserInfo() {
        UserInfo info = new UserInfo();
        info.name = mPreferences.getString("name", "");
        info.location = mPreferences.getString("location", "");
        info.token = mPreferences.getString("token", "");
        info.profileImgUrl = mPreferences.getString("profile_image_url", "");
        info.uid = mPreferences.getString("uid", "");
        return info;
    }

    public boolean isLogined() {
        return !TextUtils.isEmpty(mPreferences.getString("token", ""));
    }

}
