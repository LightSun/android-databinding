package com.heaven7.databinding.demo.bean;

import com.heaven7.databinding.core.ITag;

import org.heaven7.core.adapter.ISelectable;

/**
 * as the item of bind adapter . ImageInfo must implement ISelectable interface.
 * if multi item. must implement ITag interface
 * Created by heaven7 on 2015/11/30.
 */
public class ImageInfo implements ISelectable,ITag{

    private String url;

    private String desc;

    private String title;

    private boolean selected;
    private int tag ;

    public ImageInfo(String url, String desc) {
        this.url = url;
        this.desc = desc;
    }

    public ImageInfo(String url, String desc,String title) {
        this.url = url;
        this.desc = desc;
        this.title = title;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public int getTag() {
        return tag;
    }

    @Override
    public void setTag(int tag) {
        this.tag = tag;
    }
}
