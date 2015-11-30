package com.heaven7.databinding.demo.bean;

import org.heaven7.core.adapter.ISelectable;

/**
 * Created by heaven7 on 2015/11/30.
 */
public class ImageInfo implements ISelectable{

    private String url;

    private String desc;

    private boolean selected;

    public ImageInfo(String url, String desc) {
        this.url = url;
        this.desc = desc;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    @Override
    public boolean isSelected() {
        return selected;
    }
}
