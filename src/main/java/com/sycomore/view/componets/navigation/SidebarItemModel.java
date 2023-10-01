package com.sycomore.view.componets.navigation;

public class SidebarItemModel {
    private String caption;
    private String icon;

    public SidebarItemModel() {
    }

    public SidebarItemModel(String caption, String icon) {
        this.caption = caption;
        this.icon = icon;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
