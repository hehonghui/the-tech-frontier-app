
package org.tech.frontier.entities;


public class UserInfo {
    public String location;
    public String name;
    public String profileImgUrl;
    public String token;
    public String uid;

    @Override
    public String toString() {
        return "UserInfo [location=" + location + ", name=" + name
                + ", profile_image_url=" + profileImgUrl + "]";
    }

}
