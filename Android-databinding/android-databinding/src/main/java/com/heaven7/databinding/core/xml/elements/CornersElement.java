package com.heaven7.databinding.core.xml.elements;

import com.heaven7.databinding.core.xml.AbsElement;
import com.heaven7.databinding.core.xml.IElementParser;
import com.heaven7.databinding.core.xml.XmlElementNames;
import com.heaven7.xml.XmlReader;
import com.heaven7.xml.XmlWriter;

import java.io.IOException;

/**
 * the corner of round image config
 * Created by heaven7 on 2015/12/10.
 */
public class CornersElement extends AbsElement implements IElementParser{

    private String mTopLeftText;
    private String mTopRightText;
    private String mBottomRightText;
    private String mBottomLeftText;

    public CornersElement(String mElementName) {
        super(mElementName);
    }

    public String getTopLeftText() {
        return mTopLeftText;
    }

    public void setTopLeftText(String mTopLeftText) {
        this.mTopLeftText = mTopLeftText;
    }

    public String getTopRightText() {
        return mTopRightText;
    }

    public void setTopRightText(String mTopRightText) {
        this.mTopRightText = mTopRightText;
    }

    public String getBottomRightText() {
        return mBottomRightText;
    }

    public void setBottomRightText(String mbottomRightText) {
        this.mBottomRightText = mbottomRightText;
    }

    public String getBottomLeftText() {
        return mBottomLeftText;
    }

    public void setBottomLeftText(String mBottomLeftText) {
        this.mBottomLeftText = mBottomLeftText;
    }

    @Override
    public void write(XmlWriter writer) throws IOException {
        writer.element(getElementName());
        writeAttrs(writer);
        if(mTopLeftText != null) {
            writer.element(XmlElementNames.TOP_LEFT, mTopLeftText);
        }
        if(mTopRightText != null) {
            writer.element(XmlElementNames.TOP_RIGHT, mTopRightText);
        }
        if(mBottomLeftText != null) {
            writer.element(XmlElementNames.BOTTOM_LEFT, mBottomLeftText);
        }
        if(mBottomRightText != null) {
            writer.element(XmlElementNames.BOTTOM_RIGHT, mBottomRightText);
        }
        writer.pop();
    }

    @Override
    public boolean parse(XmlReader.Element root) {
        String text;
        XmlReader.Element e = root.getChildByName(XmlElementNames.TOP_LEFT);
        if(e != null){
            text = e.getText();
            setTopLeftText(text);
        }

        //top right
        e = root.getChildByName(XmlElementNames.TOP_RIGHT);
        if(e != null){
            text = e.getText();
            setTopRightText(text);
        }
        //bottom left
        e = root.getChildByName(XmlElementNames.BOTTOM_LEFT);
        if(e != null){
            text = e.getText();
            setBottomLeftText(text);
        }
        //bottom right
        e = root.getChildByName(XmlElementNames.BOTTOM_RIGHT);
        if(e != null){
            text = e.getText();
            setBottomRightText(text);
        }
        return true;
    }
}
