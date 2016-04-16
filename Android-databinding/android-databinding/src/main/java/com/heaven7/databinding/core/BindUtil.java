package com.heaven7.databinding.core;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;

import com.heaven7.core.util.Logger;
import com.heaven7.databinding.core.expression.ExpressionParseException;
import com.heaven7.databinding.core.expression.ExpressionParser;
import com.heaven7.databinding.core.xml.elements.BindElement;
import com.heaven7.databinding.core.xml.elements.CornersElement;
import com.heaven7.databinding.core.xml.elements.ImagePropertyElement;
import com.heaven7.databinding.core.xml.elements.PropertyElement;
import com.heaven7.databinding.util.DataBindUtil;
import com.heaven7.databinding.util.ResourceUtil;
import com.heaven7.xml.Array;

import java.util.Collection;
import java.util.List;

import static com.heaven7.databinding.core.DataBindParser.PropertyBindInfo;
/**
 *
 * Created by heaven7 on 2016/1/6.
 */
/*public*/ class BindUtil {

    private static final String TAG = "BindUtil";

    public static Array<PropertyBindInfo> parseListItemBindInfos(Context context,
                                                                  List<BindElement> list) {
        if(list== null || list.size() == 0)
            return  null;
        final Array<PropertyBindInfo> infos = new Array<>(7);
        for( BindElement be : list ){
            convert2BindInfos(context, be.getPropertyElements(), infos);
        }
        return infos;
    }

    public static Array<PropertyBindInfo> parseListItemEventPropertyInfos( List<PropertyElement> list) {
        if(list== null || list.size() ==0)
            return  null;
        Array<PropertyBindInfo> infos = new Array<>(3);
        PropertyBindInfo info ;
        for(PropertyElement pe : list){
            info = new PropertyBindInfo();
            convert(info,pe);
            infos.add(info);
        }
        return infos;
    }

    public static void releasePropertyBindInfos(SparseArray<Array<PropertyBindInfo>> mBindMap) {
        if(mBindMap == null ) return;
        for(int i =0 , size = mBindMap.size() ;i<size ;i++){
            releasePropertyBindInfos(mBindMap.get(i));
        }
        mBindMap.clear();
    }
    /** release the array of PropertyBindInfo */
    public static void releasePropertyBindInfos( Array<PropertyBindInfo> infos) {
        if(infos!=null && infos.size>0){
            for(int i=0,size = infos.size ; i<size ;i++){
                ExpressionParser.recycleIfNeed(infos.get(i).realExpr);
            }
            infos.clear();
        }
    }


    public static void convert(DataBindParser.PropertyBindInfo outInfo, PropertyElement inElement) {
        String var;
        //referVariables
        var = inElement.getReferVariable();
        if(var!=null){
            outInfo.referVariables = var.trim().split(",");
        }
        outInfo.expression = inElement.getText().trim();
        outInfo.propertyName = inElement.getName();
        // outInfo.expressionValueType = inElement.getValueType();
        try {
            outInfo.realExpr = ExpressionParser.parse(outInfo.expression);
        } catch (ExpressionParseException e) {
            throw new DataBindException("can't parse the expression of " + outInfo.expression);
        }
    }

    /** convert  PropertyElement to  PropertyBindInfo,  and put it into target infos ! */
    public static void convert2BindInfos(Context ctx,List<PropertyElement> propEles,
                                         Array<DataBindParser.PropertyBindInfo> infos) {
        if(propEles.size() == 0){
            return;
        }
        DataBindParser.PropertyBindInfo info ;
        DataBindParser.ImagePropertyBindInfo ipb;

        PropertyElement pe ;
        ImagePropertyElement ipe;
        CornersElement ce;

        String expre;

        for (int i = 0, size = propEles.size(); i < size; i++) {
            pe = propEles.get(i);
            if (pe instanceof ImagePropertyElement) {
                ipe = (ImagePropertyElement) pe;
                ipb = new DataBindParser.ImagePropertyBindInfo();
                try{
                    ipb.referVariables = DataBindUtil.convertRefer(ipe.getReferVariable());
                    ipb.type = ipe.getType().hashCode();

                    expre = ipe.getUrlText();
                    if(expre!=null) {
                        ipb.url = ExpressionParser.parse(expre);
                    }
                    expre = ipe.getDefaultText();
                    if(expre!=null) {
                        ipb.defaultExpre = ExpressionParser.parse(expre);
                    }
                    expre = ipe.getErrorResIdText();
                    if(expre!=null) {
                        ipb.errorExpre = ExpressionParser.parse(expre);
                    }
                    //round , border
                    expre = ipe.getRoundSizeText();
                    if(expre!=null) {
                        ipb.roundSizeExpre = ExpressionParser.parse(expre);
                    }
                    expre = ipe.getBorderColorText();
                    if(expre!=null) {
                        ipb.borderColorExpre = ExpressionParser.parse(expre);
                    }
                    expre = ipe.getBorderWidthText();
                    if(expre!=null) {
                        ipb.borderWidthExpre = ExpressionParser.parse(expre);
                    }
                    //corners
                    ce = ipe.getCornersElement();
                    if(ce != null){
                        ipb.cornerInfo = new DataBindParser.CornerInfo();
                        expre = ce.getTopLeftText();
                        if(!TextUtils.isEmpty(expre)){
                            ipb.cornerInfo.topLeftExpre = ExpressionParser.parse(expre);
                        }
                        expre = ce.getTopRightText();
                        if(!TextUtils.isEmpty(expre)){
                            ipb.cornerInfo.topRightExpre = ExpressionParser.parse(expre);
                        }
                        expre = ce.getBottomLeftText();
                        if(!TextUtils.isEmpty(expre)){
                            ipb.cornerInfo.bottomLeftExpre = ExpressionParser.parse(expre);
                        }
                        expre = ce.getBottomRightText();
                        if(!TextUtils.isEmpty(expre)){
                            ipb.cornerInfo.bottomRightExpre = ExpressionParser.parse(expre);
                        }
                    }
                    info = ipb;
                }catch (ExpressionParseException e){
                    throw new DataBindException("while parse the <imageProperty> , the view id = " +
                            ResourceUtil.getResId(ctx, ipe.getId(), ResourceUtil.ResourceType.Id),e);
                }
            } else {
                info = new DataBindParser.PropertyBindInfo();
                convert(info, pe);
            }
            if(pe.getId()!=null){
                info.viewId = ResourceUtil.getResId(ctx, pe.getId(), ResourceUtil.ResourceType.Id);
            }
            infos.add(info);
        }
    }

    public static boolean containsAll(Array<DataBindParser.VariableInfo> container, String[] variables) {
        for(int i = 0,size = variables.length ;  i<size ;i++){
            if(!container.contains(new DataBindParser.VariableInfo(variables[i],null),false)){
                Logger.w(TAG, "the variable = " + variables[i] + " can't find the mapping data!");
                return false;
            }
        }
        return true;
    }

    public static boolean containsAll(Array<DataBindParser.VariableInfo> container, Collection<String> vars) {
        for(String var: vars){
            if(!container.contains(new DataBindParser.VariableInfo(var,null),false)){
                Logger.w(TAG, "the variable = " + var + " can't find the mapping data!");
                return false;
            }
        }
        return true;
    }

    public static void checkDatasExist(Object[] datas) {
        if(datas == null || datas.length == 0)
            throw new RuntimeException("datas can't be empty");
    }

}
