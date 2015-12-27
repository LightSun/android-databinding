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

import static com.heaven7.databinding.util.DataBindUtil.checkEmpty;
import static com.heaven7.databinding.util.DataBindUtil.writeElements;
import static com.heaven7.databinding.util.DataBindUtil.mergeReferVariable;

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
            writeElements(writer, mBindEles);
        }
        if(mVariableBindElements != null ) {
            writeElements(writer, mVariableBindElements);
        }
        if(mBindAdapterEles!=null){
            writeElements(writer, mBindAdapterEles);
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
        ImagePropertyElement ipe;
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

        final Array<XmlReader.Element> array = root.getChildrenByName(XmlElementNames.BIND);
        Array<XmlReader.Element> propArray ;
        XmlReader.Element bindEle;
        XmlReader.Element propEle;


        for( int i=0,size = array.size ; i<size ;i++){
            be = new BindElement(XmlElementNames.BIND);

            bindEle =  array.get(i);
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
                propEle =  propArray.get(j);
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

                String text = propEle.getText();//text can be null?
                if(TextUtils.isEmpty(text)){
                    throw new RuntimeException("text content expression can't be null in <property> element." );
                }
                pe.setText(text);

                be.addPropertyElement(pe);
            }

            propArray = bindEle.getChildrenByName(XmlElementNames.IMAGE_PROPERTY);
            for( int j=0,size2 = propArray.size ; j<size2 ;j++){
                propEle =  propArray.get(j);
                ipe = new ImagePropertyElement(XmlElementNames.IMAGE_PROPERTY);
                ipe.parse(propEle);
                if(ipe.getId() == null){
                    ipe.setId(id);
                }
                ipe.setReferVariable(mergeReferVariable(ipe.getReferVariable(), refVariable));
                if(ipe.getId() ==null && ipe.getReferVariable() == null)
                    throw new RuntimeException("view id and referVariable can't be empty at the same time");

                be.addPropertyElement(ipe);
            }

            if(oneView) {
                addBindElement(be);
            }else{
                addVariableBindElement(be);
            }
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
           //can be null, eg: if android.widget.View ,  alias is View
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
