package com.heaven7.databinding.core.xml.elements;

import com.heaven7.databinding.core.xml.NameableElement;
import com.heaven7.databinding.core.xml.XmlKeys;
import com.heaven7.xml.XmlWriter;

import java.io.IOException;

/**
 * Created by heaven7 on 2015/8/10.
 */
public class VariableElement extends NameableElement {

    public VariableElement(String mElementName) {
        super(mElementName);
    }

    public void setClassname(String classname){
         addAttribute(XmlKeys.CLASS_NAME,classname);
    }
    public String getClassname(){
        return getAttribute(XmlKeys.CLASS_NAME);
    }

    public void setType(String type){
        addAttribute(XmlKeys.TYPE,type);
    }
    public String getType(){
        return getAttribute(XmlKeys.TYPE);
    }

    @Override
    public void write(XmlWriter writer) throws IOException {
         writer.element(getElementName());
         writeAttrs(writer);
         writer.pop();
    }

}
