package com.heaven7.databinding.core.xml.elements;

import android.text.TextUtils;

import com.heaven7.databinding.core.xml.AbsElement;
import com.heaven7.databinding.core.xml.IElementParser;
import com.heaven7.databinding.core.xml.XmlElementNames;
import com.heaven7.databinding.core.xml.XmlKeys;
import com.heaven7.databinding.util.DataBindUtil;
import com.heaven7.xml.Array;
import com.heaven7.xml.XmlReader;
import com.heaven7.xml.XmlWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.heaven7.databinding.util.DataBindUtil.mergeReferVariable;

/**
 * used for  <bindAdapter id="lv">...</bindAdapter>
 * Created by heaven7 on 2015/11/24.
 */
public class BindAdapterElement extends AbsElement implements IElementParser{

    private List<ItemElement> mItemEles;
    /** the total refer */
    private String  mTotalRefer;

    public BindAdapterElement(String mElementName) {
        super(mElementName);
    }

    public void addItemElement(ItemElement ie){
        if(mItemEles ==null)
            mItemEles = new ArrayList<>();
        mItemEles.add(ie);
    }

    public List<ItemElement> getItemElements(){
        return mItemEles;
    }

    public String[] getTotalRefers(){
        return DataBindUtil.convertRefer(mTotalRefer);
    }
    public String getSelectMode(){
        return getAttribute(XmlKeys.SELECT_MODE);
    }
    public void setSelectMode(String selectMode){
         addAttribute(XmlKeys.SELECT_MODE, selectMode);
    }

    public void setId(String id){
        addAttribute(XmlKeys.ID, id);
    }
    public String getId(){
        return getAttribute(XmlKeys.ID);
    }

    /** main refer */
    public void setReferVariable(String refer){
        addAttribute(XmlKeys.REFER_VARIABLE, refer);
    }
    public String getReferVariable(){
        return getAttribute(XmlKeys.REFER_VARIABLE);
    }

    @Override
    public void write(XmlWriter writer) throws IOException {
        writer.element(XmlElementNames.BIND_ADAPTER);
        writeAttrs(writer);
        if(mItemEles!=null){
            List<ItemElement> mItemEles = this.mItemEles;
            int len = mItemEles.size();
            for (int i = len - 1; i >=0 ; i--) {
                mItemEles.get(i).write(writer);
            }
        }
        writer.pop();
    }

    @Override
    public void reset() {
        super.reset();
        mTotalRefer = null;
        if(mItemEles!=null){
            List<ItemElement> mItemEles = this.mItemEles;
            int len = mItemEles.size();
            for (int i = len - 1; i >=0 ; i--) {
                mItemEles.get(i).reset();
            }
            mItemEles.clear();
        }
    }

    @Override
    public boolean parse(XmlReader.Element root) {
        final String id = root.getAttribute(XmlKeys.ID, null);
        if(id == null){
            throw new RuntimeException("you must declare attr id in <bindAdapter> element !");
        }
        setId(id);

        final String referVar = root.getAttribute(XmlKeys.REFER_VARIABLE, null);
        if(TextUtils.isEmpty(referVar)){
            throw new RuntimeException("in <bindAdapter> element , referVariable can't be null.");
        }
        setReferVariable(referVar);

        String selectMode = root.getAttribute(XmlKeys.SELECT_MODE, null);
        if(TextUtils.isEmpty(selectMode)){
            selectMode = "1";
        }
        setSelectMode(selectMode);

        final Array<XmlReader.Element> items = root.getChildrenByName(XmlElementNames.ITEM);
        XmlReader.Element e;
        if(items!=null && items.size >0){
            ItemElement ie;
            String attr;
            for(int i=0,size = items.size ;i<size ;i++){
                ie = new ItemElement(XmlElementNames.ITEM);
                e = items.get(i);
                //layout
                attr = e.getAttribute(XmlKeys.LAYOUT,null);
                if(attr == null){
                    throw new RuntimeException("you must declare attr layout in <item> element !");
                }
                ie.setLayoutName(attr);
             //tag
                attr = e.getAttribute(XmlKeys.TAG,null);
                if(attr == null ){
                    if(size > 1 ){
                        throw new RuntimeException("while your list view has multi items,tag must be declared in <item> element.");
                    }
                }else {
                    try {
                        Integer.parseInt(attr);
                    } catch (NumberFormatException ne) {
                        throw new RuntimeException("in <item> element ,attribute tag must be integer");
                    }
                    ie.setTag(attr);
                }

                //refer variable
                attr = e.getAttribute(XmlKeys.REFER_VARIABLE,null);
                if(!TextUtils.isEmpty(attr)){
                    //throw new RuntimeException("in <item> element ,attribute referVariable can't be empty");
                    ie.setReferVariable(attr);
                }
                attr = DataBindUtil.mergeReferVariable(referVar,attr);

                ie.setPropertyElements(parsePropertyElements(e, attr,null,false));
                parseBindElements(e,ie,attr);

                addItemElement(ie);
            }
        }
        return true;
    }

    /**
     * parse bind element and add it to the target ItemElement
     * @param referVariable  the referVariable which is an attr of ItemElement
     */
    private void parseBindElements(XmlReader.Element e, ItemElement ie, String referVariable) {
        final Array<XmlReader.Element> binds = e.getChildrenByName(XmlElementNames.BIND);
        if(binds == null || binds.size ==0)
            return;

        BindElement be;
        XmlReader.Element bindEle;
        List<PropertyElement> props;

        for(int i =0 ,size = binds.size  ; i<size ; i++){
            be = new BindElement(XmlElementNames.BIND);
            bindEle = binds.get(i);

            String id = bindEle.getAttribute(XmlKeys.ID, null);
            if(!TextUtils.isEmpty(id)) {
                be.setId(id);
            }
            String refer = bindEle.getAttribute(XmlKeys.REFER_VARIABLE, null);
            if(!TextUtils.isEmpty(refer)){
                be.setReferVariable(refer);
            }
            refer = DataBindUtil.mergeReferVariable(referVariable , refer);
           //imageProperty
            props = parsePropertyElements(bindEle, refer, id, true);
            if(props != null && props.size() > 0) {
                be.setPropertyElements(props);
            }
            parseImageProperty(be,bindEle, refer, id);
            ie.addBindElement(be);
        }
    }

    /** parse imageProperty element */
    private void parseImageProperty(BindElement be, XmlReader.Element e, String refer,
                                    String id_bind) {
        Array<XmlReader.Element> array = e.getChildrenByName(XmlElementNames.IMAGE_PROPERTY);
        if(array ==null || array.size ==0)
            return;
        XmlReader.Element ipEle;
        ImagePropertyElement ipe;
        for( int j=0,size2 = array.size ; j<size2 ;j++){
            ipEle =  array.get(j);
            ipe = new ImagePropertyElement(XmlElementNames.IMAGE_PROPERTY);
            ipe.parse(ipEle);
            if(ipe.getId() == null){
                ipe.setId(id_bind);
            }
            ipe.setReferVariable(mergeReferVariable(ipe.getReferVariable(), refer));
            if(ipe.getId() == null && ipe.getReferVariable() == null)
                throw new RuntimeException("view id and referVariable can't be empty at the same time");

            be.addPropertyElement(ipe);
            mTotalRefer = mergeReferVariable(ipe.getReferVariable(),mTotalRefer);
        }
    }

    /**
     * @param referVariable  the referVariable which is an attr of ItemElement or BindElement
     * @param id_bind  the id from bind element
     * @param checkId  true to check the id in property element
     */
    private List<PropertyElement> parsePropertyElements(XmlReader.Element e, String referVariable,
                                                               String id_bind,boolean checkId) {
        final Array<XmlReader.Element> props = e.getChildrenByName(XmlElementNames.PROPERTY);
        if(props == null || props.size ==0) {
            mTotalRefer = DataBindUtil.mergeReferVariable(referVariable,mTotalRefer);
            return null;
        }
        checkId = checkId && id_bind == null;
        final List<PropertyElement> pes = new ArrayList<>();

        final boolean checkRefer = TextUtils.isEmpty(referVariable);

        PropertyElement pe;
        XmlReader.Element propEle;

        for( int i=0,size = props.size ; i<size ;i++){
            propEle = props.get(i);

            pe = new PropertyElement(XmlElementNames.PROPERTY);

            String name = propEle.getAttribute(XmlKeys.NAME, null);
            if(TextUtils.isEmpty(name)){
                throw new RuntimeException("attr name can't be empty in <property> element.");
            }
            pe.setName(name.trim());

            //id
            String id = propEle.getAttribute(XmlKeys.ID, null);
            if(TextUtils.isEmpty(id)){
                if(checkId){
                    throw new RuntimeException("attr id can't be empty in <bind> and <property>"
                            + "at the same time");
                }
                if(id_bind != null)
                     pe.setId(id_bind);
            }else{
                pe.setId(id);
            }

       //referVariable
            String refVar = propEle.getAttribute(XmlKeys.REFER_VARIABLE, null);
            if(TextUtils.isEmpty(refVar)){
                if(checkRefer){
                    throw new RuntimeException("attr referVariable can't be empty in <item> and <property>"
                             + "at the same time");
                }
            }
            refVar = DataBindUtil.mergeReferVariable(referVariable,refVar);
            mTotalRefer = DataBindUtil.mergeReferVariable(refVar,mTotalRefer);
            //property element use the result of merged
            pe.setReferVariable(refVar);

            //text
            String text = propEle.getText();
            if(TextUtils.isEmpty(text)){
                throw new RuntimeException("text content expression can't be empty in <property> element." );
            }
            pe.setText(text);

            pes.add(pe);
        }
        return pes;
    }

}
