package org.tech.frontier.net.handlers;

import org.json.JSONObject;
import org.tech.frontier.entities.UserInfo;

public class UserInfoHander implements RespHandler<UserInfo,JSONObject>{

	@Override
	public UserInfo parse(JSONObject data) {
		
		UserInfo info = new UserInfo();
		info.location = data.optString("location");
		info.name = data.optString("name");
		info.profileImgUrl = data.optString("profile_image_url");
		return info;
	}

	

}
