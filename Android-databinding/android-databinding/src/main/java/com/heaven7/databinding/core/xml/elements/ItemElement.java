package com.heaven7.databinding.core.xml.elements;

import com.heaven7.databinding.core.xml.AbsElement;
import com.heaven7.databinding.core.xml.XmlElementNames;
import com.heaven7.databinding.core.xml.XmlKeys;
import com.heaven7.xml.XmlWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * indicate is item element used by ListView or RecyclerView
 * eg: <pre>
 *     <item layout="item_xxx" tag = "1" referVariable="user,itemHandler">
 <property name="onItemClick" >itemHandler.onItemClick(user)</property>
 <property name="onClick" >itemHandler.onItemClick(user)</property>
 <bind id="bt">
 <property name="text" >user.username</property>
 <property name="textColor" >user.male ? {@color/red} : {@color/random}</property>
 <property name="onClick" referVariable="orderHandler">orderHandler.onClickName(user)</property>
 </bind>
 </item>
 * </pre>
 * Created by heaven7 on 2015/11/23.
 */
public class ItemElement extends AbsElement {

    private List<PropertyElement> mPropEles;
    private List<BindElement> mBindEles;

    public ItemElement(String mElementName) {
        super(mElementName);
    }

    public void setLayoutName(String layout){
        addAttribute(XmlKeys.LAYOUT, layout);
    }
    public String getLayoutName(){
        return getAttribute(XmlKeys.LAYOUT);
    }

    public void setTag(String tag){
        addAttribute(XmlKeys.TAG,tag);
    }
    public String getTag(){
        return getAttribute(XmlKeys.TAG);
    }

    public void setReferVariable(String refer_variable){
        addAttribute(XmlKeys.REFER_VARIABLE, refer_variable);
    }
    public String getReferVariable(){
        return  getAttribute(XmlKeys.REFER_VARIABLE);
    }

    public void addPropertyElement(PropertyElement pe){
        if(mPropEles == null)
            mPropEles = new ArrayList<>();
        mPropEles.add(pe);
    }
    public void setPropertyElements(List<PropertyElement> pes){
        this.mPropEles = pes;
    }
    public List<PropertyElement> getPropertyElements(){
        return mPropEles;
    }

    public List<BindElement> getBindElements() {
        return mBindEles;
    }

    public void addBindElement(BindElement be) {
        if(mBindEles == null)
            mBindEles = new ArrayList<>();
        mBindEles.add(be);
    }
    public void setBindElements(List<BindElement> bes){
        this.mBindEles = bes;
    }

    @Override
    public void write(XmlWriter writer) throws IOException {
        writer.element(XmlElementNames.ITEM);
        writeAttrs(writer);
        if(mPropEles!=null){
            List<PropertyElement> mPropEles = this.mPropEles;
            int len = mPropEles.size();
            for (int i = len - 1; i >=0 ; i--) {
                mPropEles.get(i).write(writer);
            }
        }
        if(mBindEles != null ) {
            List<BindElement> bindElements = this.mBindEles;
            int len = bindElements.size();
            for (int i = len - 1; i >=0 ; i--) {
                bindElements.get(i).write(writer);
            }
        }
        writer.pop();
    }
    @Override
    public void reset() {
        super.reset();
        if(mPropEles!=null)
             mPropEles.clear();
        if(mBindEles!=null)
            mBindEles.clear();
    }




}
