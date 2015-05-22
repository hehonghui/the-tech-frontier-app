package com.tech.frontier.net;

import com.tech.frontier.listeners.DataListener;
import com.tech.frontier.models.entities.UserInfo;


public interface UserAPI {
	  public void fetchUserInfo(String uid,String token,DataListener<UserInfo> listener);
}
