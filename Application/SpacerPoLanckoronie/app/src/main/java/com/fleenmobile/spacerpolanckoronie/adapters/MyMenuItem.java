package com.fleenmobile.spacerpolanckoronie.adapters;

/**
 * @author FleenMobile at 2015-08-21
 */
public class MyMenuItem {

    private String name;
    private int icon;

    public MyMenuItem(String name, int icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
