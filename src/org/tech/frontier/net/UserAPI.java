
package org.tech.frontier.net;

import org.tech.frontier.entities.UserInfo;
import org.tech.frontier.listeners.DataListener;

/**
 * 从新浪获取用户信息的接口
 * 
 * @author mrsimple
 */
public interface UserAPI {
    /**
     * 获取用户信息
     * 
     * @param uid 用户id
     * @param token 用户token
     * @param listener
     */
    public void fetchUserInfo(String uid, String token, DataListener<UserInfo> listener);
}
