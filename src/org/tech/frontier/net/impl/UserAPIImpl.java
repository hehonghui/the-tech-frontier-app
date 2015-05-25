
package org.tech.frontier.net.impl;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;
import org.tech.frontier.entities.UserInfo;
import org.tech.frontier.listeners.DataListener;
import org.tech.frontier.net.UserAPI;
import org.tech.frontier.net.handlers.UserInfoHander;
import org.tech.frontier.utils.Constants;

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
