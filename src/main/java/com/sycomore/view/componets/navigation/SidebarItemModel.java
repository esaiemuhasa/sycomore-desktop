package com.sycomore.view.componets.navigation;

public class SidebarItemModel {
    private final String caption;
    private final String icon;

    private final String name;

    public SidebarItemModel(String caption, String icon, String name) {
        this.caption = caption;
        this.icon = icon;
        this.name = name;
    }

    public String getCaption() {
        return caption;
    }

    public String getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }
}
