package com.heaven7.databinding.core.xml.elements;

import android.text.TextUtils;

import com.heaven7.databinding.core.xml.IElementParser;
import com.heaven7.databinding.core.xml.XmlElementNames;
import com.heaven7.databinding.core.xml.XmlKeys;
import com.heaven7.databinding.util.DataBindUtil;
import com.heaven7.xml.XmlReader;
import com.heaven7.xml.XmlWriter;

import java.io.IOException;

/**
 * this is the config element of ImageView
 * Created by heaven7 on 2015/12/5.
 */
public class ImagePropertyElement extends PropertyElement implements IElementParser{

    public static final int TYPE_ROUND   = "round".hashCode();
    public static final int TYPE_CIRCLE  = "circle".hashCode();
    public static final int TYPE_OVAL    = "oval".hashCode();

    public static final byte CORNER_LEFT   = 0x1;
    public static final byte CORNER_RIGHT  = 0x2;
    public static final byte CORNER_TOP    = 0x4;
    public static final byte CORNER_BOTTOM = 0x8;

    private String mRoundSizeText;
    private String mBorderSizeText;
    private String mBorderColorText;

    private String mUrl;
    private String mErrorResIdText;
    private String mDefaultResId;

    private CornersElement mCornersElement;


    public ImagePropertyElement(String mElementName) {
        super(mElementName);
    }

    public void setType(String type){
        addAttribute(XmlKeys.TYPE, type);
    }
    public String getType(){
        return getAttribute(XmlKeys.TYPE);
    }

    public String getRoundSizeText() {
        return mRoundSizeText;
    }

    public void setRoundSizeText(String mRoundSizeText) {
        this.mRoundSizeText = mRoundSizeText;
    }

    public String getBorderWidthText() {
        return mBorderSizeText;
    }

    public void setBorderWidthText(String mBorderSizeText) {
        this.mBorderSizeText = mBorderSizeText;
    }

    public String getBorderColorText() {
        return mBorderColorText;
    }

    public void setBorderColorText(String mBorderColorText) {
        this.mBorderColorText = mBorderColorText;
    }

    public String getUrlText() {
        return mUrl;
    }
    public void setUrlText(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getErrorResIdText() {
        return mErrorResIdText;
    }
    public void setErrorResIdText(String mErrorResIdText) {
        this.mErrorResIdText = mErrorResIdText;
    }
    public String getDefaultText() {
        return mDefaultResId;
    }
    public void setDefaultText(String mDefaultResIdText) {
        this.mDefaultResId = mDefaultResIdText;
    }

    public CornersElement getCornersElement() {
        return mCornersElement;
    }

    public void setCornersElement(CornersElement mCornersElement) {
        this.mCornersElement = mCornersElement;
    }

    @Override
    public void write(XmlWriter writer) throws IOException {
        writer.element(getElementName());
        writeAttrs(writer);
        if(getUrlText()!=null) {
            writer.element(XmlElementNames.URL, getUrlText());
        }
        if(getRoundSizeText()!=null) {
            writer.element(XmlElementNames.ROUND_SIZE, getRoundSizeText());
        }
        if(getBorderColorText()!=null) {
            writer.element(XmlElementNames.BORDER_COLOR, getBorderColorText());
        }
        if(getBorderWidthText()!=null) {
            writer.element(XmlElementNames.BORDER_WIDTH, getBorderWidthText());
        }
        if(getDefaultText()!=null) {
            writer.element(XmlElementNames.DEFAULT, getDefaultText());
        }
        if(getErrorResIdText()!=null) {
            writer.element(XmlElementNames.ERROR_RES_ID, getErrorResIdText());
        }
        if(mCornersElement!=null){
            mCornersElement.write(writer);
        }
        writer.pop();
    }

    @Override
    public boolean parse(XmlReader.Element root) {
        String val = root.getAttribute(XmlKeys.TYPE,null);
        //type
        DataBindUtil.checkEmpty(val, XmlKeys.TYPE);
        int hash  = val.hashCode();
        if(hash != TYPE_ROUND &&  hash != TYPE_OVAL && hash != TYPE_CIRCLE){
            throw new RuntimeException("The imageParam's attr type of imageView can only be round/circle/oval," +
                    " but got " + val);
        }
        setType(val);

        //id
        val = root.getAttribute(XmlKeys.ID,null);
        if(!TextUtils.isEmpty(val)){
            setId(val);
        }

        //refer variable
        val = root.getAttribute(XmlKeys.REFER_VARIABLE,null);
        if(!TextUtils.isEmpty(val)){
           setReferVariable(val);
        }

        //round size
        XmlReader.Element e = root.getChildByName(XmlElementNames.ROUND_SIZE);
        if(e!=null){
            val = e.getText();
            DataBindUtil.checkEmpty(val,XmlElementNames.ROUND_SIZE);
            setRoundSizeText(val);
        }
       //border color
       e = root.getChildByName(XmlElementNames.BORDER_COLOR);
        if(e!=null){
            val = e.getText();
            DataBindUtil.checkEmpty(val,XmlElementNames.BORDER_COLOR);
            setBorderColorText(val);
        }
        //border size
        e = root.getChildByName(XmlElementNames.BORDER_WIDTH);
        if(e!=null){
            val = e.getText();
            DataBindUtil.checkEmpty(val,XmlElementNames.BORDER_WIDTH);
            setBorderWidthText(val);
        }
        //url
        e = root.getChildByName(XmlElementNames.URL);
        if(e!=null){
            val = e.getText();
            DataBindUtil.checkEmpty(val,XmlElementNames.URL);
            setUrlText(val);
        }
        //default
        e = root.getChildByName(XmlElementNames.DEFAULT);
        if(e!=null){
            val = e.getText();
            DataBindUtil.checkEmpty(val,XmlElementNames.DEFAULT);
            setDefaultText(val);
        }
        //error
        e = root.getChildByName(XmlElementNames.ERROR_RES_ID);
        if(e!=null){
            val = e.getText();
            DataBindUtil.checkEmpty(val,XmlElementNames.ERROR_RES_ID);
            setErrorResIdText(val);
        }

        //corners
        e = root.getChildByName(XmlElementNames.CORNERS);
        if(e != null) {
            CornersElement ce = new CornersElement(XmlElementNames.CORNERS);
            ce.parse(e);
            setCornersElement(ce);
        }
        return true;
    }

    @Override
    public void reset() {
        super.reset();
    }
}
