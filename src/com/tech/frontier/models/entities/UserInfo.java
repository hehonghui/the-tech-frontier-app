
package com.tech.frontier.models.entities;


public class UserInfo {
    public String location;
    public String name;
    public String profileImgUrl;
    public String token;

    @Override
    public String toString() {
        return "UserInfo [location=" + location + ", name=" + name
                + ", profile_image_url=" + profileImgUrl + "]";
    }

}
