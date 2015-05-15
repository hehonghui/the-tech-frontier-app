package org.tech.frontier.module;

/**
 * Created by kang on 15/5/14-下午8:26.
 */
public class NavItemInfo {
    String name;
    int iconId;

    public NavItemInfo(String name, int iconId) {
        this.name = name;
        this.iconId = iconId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }
}
