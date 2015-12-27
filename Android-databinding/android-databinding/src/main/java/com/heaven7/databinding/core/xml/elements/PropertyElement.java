package com.heaven7.databinding.core.xml.elements;

import com.heaven7.databinding.core.xml.NameableElement;
import com.heaven7.databinding.core.xml.XmlKeys;
import com.heaven7.xml.XmlWriter;

import java.io.IOException;

/**
 * Created by heaven7 on 2015/8/10.
 */
public class PropertyElement extends NameableElement {

    private String text="";

    public PropertyElement(String mElementName) {
        super(mElementName);
    }

    public void setReferVariable(String refer_variable){
        addAttribute(XmlKeys.REFER_VARIABLE, refer_variable);
    }
    public String getReferVariable(){
       return  getAttribute(XmlKeys.REFER_VARIABLE);
    }
    public void setId(String id){
        addAttribute(XmlKeys.ID,id);
    }
    public String getId(){
        return  getAttribute(XmlKeys.ID);
    }
    public void setReferImport(String refer_import){
        addAttribute(XmlKeys.REFER_IMPORT,refer_import);
    }
    public String getReferImport(){
        return  getAttribute(XmlKeys.REFER_IMPORT);
    }

    public void setText(String text){
       this.text = text;
    }
    public String getText(){
        return text;
    }

    @Override
    public void write(XmlWriter writer) throws IOException {
        writer.element(getElementName());
        writeAttrs(writer);
        writer.text(text);
        writer.pop();
    }

    @Override
    public void reset() {
        super.reset();
        text="";
    }
}
