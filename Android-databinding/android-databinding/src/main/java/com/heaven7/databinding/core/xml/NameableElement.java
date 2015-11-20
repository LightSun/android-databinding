package com.heaven7.databinding.core.xml;

/**
 * Created by heaven7 on 2015/8/10.
 */
public abstract class NameableElement extends AbsElement {

    public NameableElement(String mElementName) {
        super(mElementName);
    }

    public void setName(String value){
        addAttribute(XmlKeys.NAME,value);
    }

    public String getName(){
        return getAttributeMap().get(XmlKeys.NAME);
    }

}
