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

    private List<IElementParseListener> mListeners;

    private DataElement mDataElement;
    private List<BindElement> mBindEles;
    private List<BindElement> mVariableBindElements;

    private List<BindAdapterElement> mBindAdapterEles;

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


    public void addBindAdapterElement(BindAdapterElement bae){
        if(mBindAdapterEles ==null)
            mBindAdapterEles = new ArrayList<>();
        mBindAdapterEles.add(bae);
    }
    public List<BindAdapterElement> getBindAdapterElements(){
        return mBindAdapterEles;
    }

    public void addElementParseListener(IElementParseListener l) {
        if(mListeners == null)
            mListeners = new ArrayList<>(3);
        mListeners.add(l);
    }

    public void clearElementParseListeners(){
        if(mListeners != null){
            mListeners.clear();
        }
    }

    private void handleCallback(){
        if(mListeners!=null){
           for(IElementParseListener l : mListeners){
               l.onParseDataElement(getDataElement());
               l.onParseBindElements(getBindElements());
               l.onParseVariableBindElements(getVariableBindElements());
               l.onParseBindAdapterElements(getBindAdapterElements());
           }
        }
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
        if(mVariableBindElements != null ) {
            List<BindElement> bindElements = this.mVariableBindElements;
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
        if(mBindEles!=null)
            mBindEles.clear();
        if(mVariableBindElements != null)
             mVariableBindElements.clear();
        if( mBindAdapterEles !=null){
            mBindAdapterEles.clear();
        }
        if(mListeners != null)
            mListeners.clear();
    }

    @Override
    public boolean parse(XmlReader.Element root) {
        XmlReader.Element dataEle = root.getChildByName(XmlElementNames.DATA);
        DataElement dataElement = new DataElement(XmlElementNames.DATA);
       //parse var and import
        parseVariableAndImport(dataEle, dataElement);
        setDataElement(dataElement);
        //parse all custom bind
        parseBindElements(root);

        Array<XmlReader.Element> adapterEles = root.getChildrenByName(XmlElementNames.BIND_ADAPTER);
        if(adapterEles != null && adapterEles.size > 0){
             BindAdapterElement bae;
             XmlReader.Element e;
             for(int i=0,size = adapterEles.size ; i < size ; i++){
                 e = adapterEles.get(i);

                 bae = new BindAdapterElement(XmlElementNames.BIND_ADAPTER);
                 bae.parse(e);

                 addBindAdapterElement(bae);
             }
        }

        handleCallback();
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
            String refVariable = bindEle.getAttribute(XmlKeys.REFER_VARIABLE,null);

            if(TextUtils.isEmpty(id)){
                if(TextUtils.isEmpty(refVariable)){
                    throw new RuntimeException("in BindElement attr id and refVariable can't be empty at the same time!");
                }
                oneView = false;
                be.setReferVariable(refVariable.trim());
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
               //refer import
                String referImport = propEle.getAttribute(XmlKeys.REFER_IMPORT, null);
                if( !TextUtils.isEmpty(referImport) ){
                    pe.setReferImport(referImport.trim());
                }
                //value type
                String valueType = propEle.getAttribute(XmlKeys.VALUE_TYPE, null);
                if(!TextUtils.isEmpty(valueType)){
                    // checkEmpty(valueType,XmlKeys.VALUE_TYPE);
                    pe.setValueType(valueType);
                }

                String text = propEle.getText();//text can be null?
                if(TextUtils.isEmpty(text)){
                    throw new RuntimeException("text content expression can't be null in <property> element." );
                }
                pe.setText(text);

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

        void onParseBindAdapterElements(List<BindAdapterElement> bindAdapterElements);
    }
}