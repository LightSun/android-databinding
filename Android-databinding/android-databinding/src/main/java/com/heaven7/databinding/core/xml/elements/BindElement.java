package com.heaven7.databinding.core.xml.elements;

import com.heaven7.databinding.core.xml.AbsElement;
import com.heaven7.databinding.core.xml.XmlKeys;
import com.heaven7.xml.XmlWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heaven7 on 2015/8/10.
 */
public class BindElement extends AbsElement {
    //id = resname
    private List<PropertyElement> mProps;

    public BindElement(String mElementName) {
        super(mElementName);
    }

    public void setId( String id ){
        addAttribute(XmlKeys.ID, id);
    }
    public String getId(){
        return getAttribute(XmlKeys.ID);
    }
    public void setReferVariable( String variable){
        addAttribute(XmlKeys.REFER_VARIABLE, variable);
    }
    public String getReferVariable(){
        return getAttribute(XmlKeys.REFER_VARIABLE);
    }

    public void addPropertyElement(PropertyElement pe){
        if(mProps ==null)
            mProps = new ArrayList<PropertyElement>();
        mProps.add(pe);
    }
    public void setPropertyElements(List<PropertyElement> list){
        this.mProps = list;
    }
    public List<PropertyElement> getPropertyElements(){
        return mProps;
    }

    @Override
    public void write(XmlWriter writer) throws IOException {
        writer.element(getElementName());
        writeAttrs(writer);

        if(mProps != null && mProps.size()>0 ) {
            List<PropertyElement> list = this.mProps;
            int len = list.size();
            for (int i = len - 1; i >=0 ; i--) {
                list.get(i).write(writer);
            }
        }
        writer.pop();
    }

    @Override
    public void reset() {
        super.reset();
        mProps.clear();
    }
}
