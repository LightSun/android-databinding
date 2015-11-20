package com.heaven7.databinding.core.xml.elements;

import android.text.TextUtils;

import com.heaven7.databinding.core.xml.AbsElement;
import com.heaven7.databinding.core.xml.IElementParser;
import com.heaven7.databinding.core.xml.XmlElementNames;
import com.heaven7.databinding.core.xml.XmlKeys;
import com.heaven7.xml.Array;
import com.heaven7.xml.XmlReader;
import com.heaven7.xml.XmlWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heaven7 on 2015/8/10.
 */
public class DataBindingElement extends AbsElement implements IElementParser {

    private DataElement mDataElement;
    private List<BindElement> mBindEles;
    private List<BindElement> mVariableBindElements;
    private IElementParseListener mElementListener;

    public DataBindingElement(String mElementName) {
        super(mElementName);
    }

    public DataElement getDataElement() {
        return mDataElement;
    }
    public void setDataElement(DataElement dataElement) {
        this.mDataElement = dataElement;
    }

    public List<BindElement> getBindElements() {
        return mBindEles;
    }
    public void addBindElement(BindElement e){
        if(mBindEles ==null)
            mBindEles = new ArrayList<BindElement>();
        mBindEles.add(e);
    }
    public void setBindElements(List<BindElement> mBindEles) {
        this.mBindEles = mBindEles;
    }
    public void addVariableBindElement(BindElement e){
        if(mVariableBindElements ==null)
            mVariableBindElements = new ArrayList<BindElement>();
        mVariableBindElements.add(e);
    }
    public List<BindElement> getVariableBindElements(){
        return mVariableBindElements;
    }

    public IElementParseListener getElementParseListener() {
        return mElementListener;
    }
    public void setElementParseListener(IElementParseListener l) {
        this.mElementListener = l;
    }

    @Override
    public void write(XmlWriter writer) throws IOException {
        writer.element(XmlElementNames.DATA_BINDING);
        writeAttrs(writer);
        if( mDataElement !=null )
            mDataElement.write(writer);
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
        mDataElement.reset();
        mBindEles.clear();
        mElementListener = null;
    }

    @Override
    public boolean parse(XmlReader.Element root) {
        XmlReader.Element dataEle = root.getChildByName(XmlElementNames.DATA);
        DataElement dataElement = new DataElement(XmlElementNames.DATA);
       //parse var and import
        parseVariableAndImport(dataEle, dataElement);
        setDataElement(dataElement);
        //parse all bind
        parseBindElements(root);

        if(mElementListener != null){
            mElementListener.onParseDataElement(dataElement);
            mElementListener.onParseBindElements(getBindElements());
            mElementListener.onParseVariableBindElements(getVariableBindElements());
        }
        return true;
    }

    private void parseBindElements(XmlReader.Element root) {
        BindElement be;
        PropertyElement pe ;
        /**
         *  <bind id="bt">
         <property name="text" referVariable="user" valueType="string">@{user.username}</property>
         <property name="textColor" referVariable="user" >user.male ? {@color/red} : {@color/green}</property>
         </bind>  means oneView = true

         <bind variable="user">
         <property id ="bt1" name="text"  valueType="string">@{user.username}</property>
         <property id ="bt2" name="text" >user.nickname</property>
         </bind>
         means oneView = false
         */
        boolean oneView ;

        Array<XmlReader.Element> array = root.getChildrenByName(XmlElementNames.BIND);
        Array<XmlReader.Element> propArray ;

        for( int i=0,size = array.size ; i<size ;i++){
            be = new BindElement(XmlElementNames.BIND);

            XmlReader.Element bindEle =  array.get(i);
            String id = bindEle.getAttribute(XmlKeys.ID, null);
            String variable = bindEle.getAttribute(XmlKeys.VARIABLE,null);

            if(TextUtils.isEmpty(id)){
                if(TextUtils.isEmpty(variable)){
                    throw new RuntimeException("in BindElement attr id and variable can't be empty at the same time!");
                }
                oneView = false;
                be.setVariable(variable.trim());
            }else{
                oneView = true;
                be.setId(id.trim());
            }
            propArray = bindEle.getChildrenByName(XmlElementNames.PROPERTY);
            for( int j=0,size2 = propArray.size ; j<size2 ;j++){
                XmlReader.Element propEle =  propArray.get(j);
                pe = new PropertyElement(XmlElementNames.PROPERTY);

                String name = propEle.getAttribute(XmlKeys.NAME, null);
                checkEmpty(name,XmlKeys.NAME);
                pe.setName(name.trim());

                //check referVariable and id
                String referVariable = propEle.getAttribute(XmlKeys.REFER_VARIABLE, null);
                String propId = propEle.getAttribute(XmlKeys.ID, null);
                if(oneView) {
                    checkEmpty(referVariable, XmlKeys.REFER_VARIABLE);
                    pe.setReferVariable(referVariable.trim());
                }else{
                    checkEmpty(propId, XmlKeys.ID);
                    pe.setId(propId);
                }

                String referImport = propEle.getAttribute(XmlKeys.REFER_IMPORT, null);
                if( !TextUtils.isEmpty(referImport) ){
                    pe.setReferImport(referImport.trim());
                }
                String valueType = propEle.getAttribute(XmlKeys.VALUE_TYPE, null);
                if(!TextUtils.isEmpty(valueType)){
                    // checkEmpty(valueType,XmlKeys.VALUE_TYPE);
                    pe.setValueType(valueType);
                }

                String text = propEle.getText();//text can be null?
                pe.setText(TextUtils.isEmpty(text) ? "" : text.trim());

                be.addPropertyElement(pe);
            }
            if(oneView) {
                addBindElement(be);
            }else{
                addVariableBindElement(be);
            }
        }
    }

    /**
     * @param val  the value to check
     * @param tag   the tag to log
     */
    private static void checkEmpty(String val,String tag){
        if(TextUtils.isEmpty(val)){
            throw new RuntimeException(tag+" can't be empty");
        }
    }

    private void parseVariableAndImport(XmlReader.Element dataEle, DataElement dataElement) {
        VariableElement ve;
        for (XmlReader.Element e : dataEle.getChildrenByName(XmlElementNames.VARIABLE)) {
            // System.out.println("parseVariableAndImport(): "+e.toString());
             ve = new VariableElement(XmlElementNames.VARIABLE);
             String classname = e.getAttribute(XmlKeys.CLASS_NAME,null);
            checkEmpty(classname,XmlKeys.CLASS_NAME);
            ve.setClassname(classname.trim());

            String name = e.getAttribute(XmlKeys.NAME,null);
            checkEmpty(name,XmlKeys.NAME);
            ve.setName(name.trim());

            String type = e.getAttribute(XmlKeys.TYPE,null);
           // System.out.println("parseVariableAndImport(): type = "+ type);
            checkEmpty(type, XmlKeys.TYPE);
            ve.setType(type.trim());
          //  System.out.println("parseVariableAndImport(): type = " + ve.getType());//null? why? treemap bug

            dataElement.addVariableElement(ve);
        }

        ImportElement ie ;
        for (XmlReader.Element e : dataEle.getChildrenByName(XmlElementNames.IMPORT)) {
            ie = new ImportElement(XmlElementNames.IMPORT);
            String classname = e.getAttribute(XmlKeys.CLASS_NAME,null);
            checkEmpty(classname,XmlKeys.CLASS_NAME);
            ie.setClassname(classname.trim());

            String alias = e.getAttribute(XmlKeys.ALIAS,null);
           //can be null, if android.widget.View ,  alias is View
            ie.setAlias(alias == null ? classname.substring(classname.lastIndexOf(".") + 1) : alias.trim());

            dataElement.addImportElement(ie);
        }
    }

    /** element parse listener */
    public interface IElementParseListener{

        void onParseDataElement(DataElement e);

        void onParseBindElements(List<BindElement> e);

        void onParseVariableBindElements(List<BindElement> e);

    }
}
