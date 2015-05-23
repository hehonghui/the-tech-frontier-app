
package com.tech.frontier.net.impl;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tech.frontier.entities.UserInfo;
import com.tech.frontier.listeners.DataListener;
import com.tech.frontier.net.UserAPI;
import com.tech.frontier.net.handlers.UserInfoHander;
import com.tech.frontier.utils.Constants;

import org.json.JSONObject;

/**
 * 从新浪获取用户信息的实现类
 * 
 * @author mrsimple
 */
public class UserAPIImpl extends AbsNetwork<UserInfo, JSONObject> implements UserAPI {

    public UserAPIImpl() {
        mRespHandler = new UserInfoHander();
    }

    @Override
    public void fetchUserInfo(String uid, String token, final DataListener<UserInfo> listener) {
        JsonObjectRequest request = new JsonObjectRequest(Constants.SINA_UID_TOKEN + uid
                + "&access_token=" + token, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (listener != null) {
                            listener.onComplete(mRespHandler.parse(response));
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error: ", error.getMessage());
                    }
                });

        performRequest(request);
    }

}
