
package com.tech.frontier.net;

import com.tech.frontier.entities.UserInfo;
import com.tech.frontier.listeners.DataListener;

/**
 * 从新浪获取用户信息的接口
 * 
 * @author mrsimple
 */
public interface UserAPI {
    public void fetchUserInfo(String uid, String token, DataListener<UserInfo> listener);
}
