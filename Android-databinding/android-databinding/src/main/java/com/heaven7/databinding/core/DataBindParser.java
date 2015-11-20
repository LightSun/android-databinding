package com.heaven7.databinding.core;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;

import com.heaven7.databinding.core.expression.ExpressionParseException;
import com.heaven7.databinding.core.expression.ExpressionParser;
import com.heaven7.databinding.core.expression.IExpression;
import com.heaven7.databinding.core.listener.ListenerImplContext;
import com.heaven7.databinding.core.xml.elements.BindElement;
import com.heaven7.databinding.core.xml.elements.DataBindingElement;
import com.heaven7.databinding.core.xml.elements.DataElement;
import com.heaven7.databinding.core.xml.elements.ImportElement;
import com.heaven7.databinding.core.xml.elements.PropertyElement;
import com.heaven7.databinding.core.xml.elements.VariableElement;
import com.heaven7.databinding.util.ArrayUtil;
import com.heaven7.databinding.util.Logger;
import com.heaven7.databinding.util.Objects;
import com.heaven7.databinding.util.ResourceUtil;
import com.heaven7.databinding.viewhelper.ViewHelper;
import com.heaven7.xml.Array;
import com.heaven7.xml.ObjectMap;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by heaven7 on 2015/8/11.
 */
/*public*/ class DataBindParser implements IDataResolver.IEventEvaluateCallback{

    private static final boolean sDebug = true ;

    private static final String TAG = "DataBinding";

    private final BaseDataResolver mDataResolver;
    private final InternalElementParserListener mParserListenerImpl;
    private final EventParseCaretaker mEventCareTaker;

    /** key is combination of view id and property name */
    private final SparseArray<ListenerImplContext> mListenerMap;

    private SparseArray<Array<PropertyBindInfo>> mBindMap_viewId;        //key is view id
    private SparseArray<Array<PropertyBindInfo>> mBindMap_variable;      //key is varaible name's hashCode
    private ViewHelper mViewHelper;

    /**
     *  <variable name="user"  classname="com.heaven7.databinding.demo.bean.User"  type="bean"/>
     *  key,value -> $classname,$user
     */
    private final ObjectMap<String,String> mVariableBeanMap ;
    private final ObjectMap<String,String> mVariableCallbakMap;
    private ObjectMap<String,String> mVariableBeansMap;


    /** the cached variable infos for later reuse  */
    //--> id,<propertyName.hashCode().Array<VariableInfo>>
    private final SparseArray<SparseArray<Array<VariableInfo>>> mViewVariableInfos; //used for notifyDataSetChanged()

    //==================start for tmp use , just avoid reallocate memory ============//
    private final Array<VariableInfo> mTmpVariables ;
    private HashSet<String> mTmpVarStrs;
    //================== end for tmp use , just avoid reallocate memory ============//

    public DataBindParser(@NonNull View root,BaseDataResolver resolver) {
        mViewHelper = new ViewHelper(root);
        mDataResolver = resolver ;
        resolver.setEventEvaluateCallback(this);

        mBindMap_viewId = new SparseArray<>();
        mVariableBeanMap = new ObjectMap<String,String>(8);
        mVariableCallbakMap = new ObjectMap<String,String>(8);
        mViewVariableInfos = new SparseArray<>(8);

        mParserListenerImpl = new InternalElementParserListener();
        mTmpVariables = new Array<>(6);

        mListenerMap = new SparseArray<>();
        mEventCareTaker = new EventParseCaretaker();
    }

    /** reset the bind data cache */
    public void reset(View root){
        if(root == null)
            mViewHelper.clearCache();
        else
           mViewHelper = new ViewHelper(root);
        mDataResolver.reset();
        mVariableBeanMap.clear();
        mVariableCallbakMap.clear();
        mListenerMap.clear();

        releasePropertyBindInfos(mBindMap_viewId);
        releasePropertyBindInfos(mBindMap_variable);

        SparseArray<SparseArray<Array<VariableInfo>>> mViewVariableInfos = this.mViewVariableInfos;
        for(int i =0,size = mViewVariableInfos.size() ;i < size ;i++){
            mViewVariableInfos.valueAt(i).clear();
        }
        mViewVariableInfos.clear();
    }

    public void reset(){
         reset(null);
    }

    public Context getContext(){
        return mViewHelper.getContext();
    }

    public DataBindingElement.IElementParseListener getElementParserListener(){
       return mParserListenerImpl;
    }
    private void doWithImportElement(ImportElement ie) {
        String alias = ie.getAlias();
        String classname = ie.getClassname();//full class name
        if(!classname.contains(".")) throw new RuntimeException("class name must be full name.");
        if(TextUtils.isEmpty(alias)){
            alias = classname.substring(classname.lastIndexOf(".")+1);
        }
        mDataResolver.putClassname(alias, classname);
    }

    private void doWithBindElement(BindElement be) {
        int id = ResourceUtil.getResId(getContext(), be.getId(), ResourceUtil.ResourceType.Id);

        List<PropertyElement> propEles = be.getPropertyElements();
        if(propEles!=null && propEles.size()>0){
            PropertyBindInfo info ;
            SparseArray<Array<PropertyBindInfo>> mBindMap = this.mBindMap_viewId;
            final Array<PropertyBindInfo> infos = new Array<>(8);
            mBindMap.put(id,infos);

            for(int i =0,size = propEles.size() ; i <size ;i++){
                info = new PropertyBindInfo();
                convert(info,propEles.get(i));
                infos.add(info);
            }
        }
    }

    private void doWithVariableElement(VariableElement ve) {
        if(sDebug){
            System.out.println("doWithVariableElement(): " + ve.toString());
        }
        final String type = ve.getType();
        if(VariableType.BEAN.equals(type)){
            mVariableBeanMap.put(ve.getClassname(),ve.getName());
        }else if(VariableType.BEANS.equals(type)){
            //means list
            if(mVariableBeansMap == null)
                mVariableBeansMap = new ObjectMap<String,String>(3);
            mVariableBeansMap.put(ve.getClassname(),ve.getName());
        }else if(VariableType.CALLBACK.equals(type)){
            //event
            mVariableCallbakMap.put(ve.getClassname(),ve.getName());
            mDataResolver.addEventHandlerVariable(ve.getName());
        }else {
            throw new DataBindException("unsupport VariableType = " + type);
        }
    }

    /**
     * apply the datas to the target propertyName of id.
     * @param id  the view to populate
     * @param type   {@link DataBinder#TYPE_BEAN} and etc. not use now
     * @param propertyName  the property name to bind,must declared it in xml , or else an exception will throw
     * @param datas  the datas to populate
     * @param checkStrictly check the all variable defined in xml can find mapping, if not throw an Exception
     */
    public void applyData(int id,int type,String propertyName ,boolean checkStrictly,boolean cacheData,
                          Object...datas){
        checkDatasExist(datas);
        mDataResolver.setCurrentBindingView(mViewHelper.getView(id));
        final PropertyBindInfo bindInfo = getBindInfo(id, propertyName);
        final Array<VariableInfo> mTmpVariables = getAllVariables(datas);

        applyDataInternal(id, bindInfo, mTmpVariables,checkStrictly);
        if(cacheData)
             addToVariableInfoCache(id, mTmpVariables, bindInfo);
        mTmpVariables.clear();
        //clear data
        mDataResolver.clearObjects();
    }

    /**
     *  add to variable info cache for later reuse. eg: {@link #notifyDataSetChanged(int)} or
     *  {@link #notifyDataSetChangedByTargetProperty(int, String)}
     * @param id   view id
     * @param varInfos    VariableInfo to map the property
     * @param bindInfo         the target property
     */
    private void addToVariableInfoCache(int id, Array<VariableInfo> varInfos, PropertyBindInfo bindInfo) {
        final int secondKey = bindInfo.propertyName.hashCode();
        SparseArray<Array<VariableInfo>> arr = mViewVariableInfos.get(id);
        if(arr != null){
            //check if have the target property record
            Array<VariableInfo> infos = arr.get(secondKey);
            if(infos == null){
                infos = new Array<>(4);
                arr.put(secondKey,infos);
            }else {
                infos.clear();
            }
            infos.addAll(varInfos);
        }else{
            //put to 1 map
            arr = new SparseArray<>(4);
            mViewVariableInfos.put(id,arr);
            //put to 2 map
            Array<VariableInfo> infos = new Array<>(4);
            arr.put(secondKey,infos);
            infos.addAll(varInfos);
        }
    }

    /**
     * @param id   the view id
     * @param propertyName  the property name to bind,must declared it in xml , or else an exception will throw
     * @return PropertyBindInfo
     */
    private PropertyBindInfo getBindInfo(int id, String propertyName) {
        final Array<PropertyBindInfo> array = mBindMap_viewId.get(id);
        PropertyBindInfo info;
        for(int i =0 ,size = array.size ; i<size ; i++){
            info = array.get(i);
            if(info.propertyName.equals(propertyName)){
                return info;
            }
        }
        throw new DataBindException("can't find the bind info , have you declared it in xml ? ");
    }

    /**
     * notify the data set changed with the target view
     * @param id the view id
     */
    public void notifyDataSetChanged(int id){
        final Array<VariableInfo> variables = getAllVariableInfosById(id);
        if(variables == null){
            throw new DataBindException("the id = " + id +" haven't bind any data yet," +
                    " so can't call notifyDataSetChanged() method !");
        }
        applyDataInternal(id, null, variables,false);
        mTmpVariables.clear();
        mDataResolver.clearObjects();
    }

    /**
     * apply data with the all property expression of target id .
     * @param id  view id
     * @param variables  the pairs of variable and bind data to apply
     * @param info  the property info to bind  or null to bind all property of target view
     * @param checkStrictly check the all variable defined in xml can find mapping, if not throw an Exception
     */
    private void applyDataInternal(int id, PropertyBindInfo info, Array<VariableInfo> variables,boolean checkStrictly) {
        //check , put data and apply
        if(info == null) {
            final ViewHelper mViewHelper = this.mViewHelper;
            final BaseDataResolver mDataResolver = this.mDataResolver;
            final SparseArray<ListenerImplContext> mListenerMap = this.mListenerMap;
            final EventParseCaretaker caretaker = this.mEventCareTaker;

            Array<PropertyBindInfo> array = mBindMap_viewId.get(id);
            for (int i = 0, size = array.size; i < size; i++) {
                applyDataInternal0(id, variables, array.get(i), mViewHelper, mDataResolver,
                        checkStrictly,mListenerMap,caretaker);
            }
        }else{
            applyDataInternal0(id, variables, info, mViewHelper, mDataResolver,
                    checkStrictly,mListenerMap,mEventCareTaker);
        }
    }

    /** notify data set changed by target property of target view(id indicate)  */
    public void notifyDataSetChangedByTargetProperty(int id ,String propertyName){
        final SparseArray<Array<VariableInfo>> varInfos = this.mViewVariableInfos.get(id);
        if(varInfos == null){
            throw new DataBindException("the id = " + id +" haven't bind any data yet," +
                    " so can't call notifyDataSetChangedByTargetProperty() method !");
        }
        final Array<VariableInfo> infos = varInfos.get(propertyName.hashCode());
        if(infos == null){
            throw new DataBindException("the id ( id = "+id+" ) with target property ( propertyName = " + propertyName
                    +" ) haven't bind any data yet, so can't call notifyDataSetChangedByTargetProperty() method !");
        }
        applyDataInternal(id, getBindInfo(id, propertyName), infos, false);
        mDataResolver.clearObjects();
    }

    private Array<VariableInfo> getAllVariableInfosById(int id) {
        final SparseArray<Array<VariableInfo>> varInfos = this.mViewVariableInfos.get(id);
        if(varInfos == null){
            return null;
        }
        final Array<VariableInfo> mTempVarInfos = mTmpVariables;
        final int size = varInfos.size();
        for(int i = size - 1 ; i >= 0 ; i-- ){
            mTempVarInfos.addAll(varInfos.valueAt(i));
        }
        return mTempVarInfos;
    }

    /***
     * bind the all data to all property of view
     * @param datas the data to bind
     * @param type  {@link DataBinder#TYPE_BEAN} and etc. now not used.
     * @param id   the view id
     * @param checkStrictly  check the all variable defined in xml can find mapping, if not throw Exception
     */
    public void applyData(int id, int type, boolean checkStrictly ,boolean cacheData,Object... datas) {
        checkDatasExist(datas);
        //check , put data and apply
        mDataResolver.setCurrentBindingView(mViewHelper.getView(id));
        if(cacheData){
            final ViewHelper mViewHelper = this.mViewHelper;
            final BaseDataResolver mDataResolver = this.mDataResolver;
            final EventParseCaretaker caretaker = this.mEventCareTaker;
            final SparseArray<ListenerImplContext> mListenerMap = this.mListenerMap;

            final Array<VariableInfo> propVarInfos = new Array<>(4);

            Array<PropertyBindInfo> array = mBindMap_viewId.get(id);
            if(checkStrictly) {
                checkReferVariables(id, array, datas);
            }

            PropertyBindInfo info ;
            for (int i = 0, size = array.size; i < size; i++) {
                info = array.get(i);
                getReferVariableInfos(info.referVariables, datas, propVarInfos);
                //here  checkStrictly must be false, to ignore unnecessary exception
                applyDataInternal0(id, propVarInfos, info, mViewHelper, mDataResolver,
                        false, mListenerMap,caretaker);
                addToVariableInfoCache(id,propVarInfos,info);
                propVarInfos.clear();
            }
        }else {
            if(checkStrictly) {
                checkReferVariables(id, mBindMap_viewId.get(id), datas);
            }
            Array<VariableInfo> mTmpVariables = getAllVariables(datas);
            //here checkStrictly must be false, to ignore unnecessary exception
            applyDataInternal(id, null, mTmpVariables, false);
        }

        this.mTmpVariables.clear();
        //clear datas refer
        mDataResolver.clearObjects();
    }

    private void checkReferVariables(int id,Array<PropertyBindInfo> array, Object... datas) {
        if(datas==null || datas.length==0 || array.size == 0)
             return;
        final Array<VariableInfo> mTmpVariables = getAllVariables(datas);
        Set<String> vars = mTmpVarStrs!=null ? mTmpVarStrs : (mTmpVarStrs = new HashSet<String>());
        PropertyBindInfo info;
        for(int i=0,size = array.size ;i < size ;i++){
            info = array.get(i);
            if(info.referVariables!=null && info.referVariables.length > 0) {
                vars.addAll(Arrays.asList(info.referVariables));
            }
        }
        if(!containsAll(mTmpVariables,vars)){
            String msg = "the property [ id = "+ id +"] defined in xml can't be apply ," +
                    "caused by some data mapping is missing !";
            throw new DataBindException(msg);
        }
        mTmpVariables.clear();
        vars.clear();
    }

    /** find the all refer variable info
     * @throws DataBindException if any data can't find mapping */
    private void getReferVariableInfos(String[] referVars, Object[] datas,
                                       Array<VariableInfo> outVarInfos) throws DataBindException {
        if(sDebug) {
            Logger.i(TAG, "getReferVariableInfos", "referVars : " + Arrays.toString(referVars));
        }
        String varName;
        for(int i=0,size = datas.length ; i<size ;i++){
            varName = getVariableName(datas[i].getClass().getName());
            if(sDebug){
                Logger.i(TAG, "getReferVariableInfos", "varName : " + varName);
            }
            if(ArrayUtil.contains(referVars,varName)){
                outVarInfos.add(new VariableInfo(varName,datas[i]));
            }
        }
    }

    private Array<VariableInfo> getAllVariables(Object[] datas) throws DataBindException{
        final Array<VariableInfo> mTmpVariables = this.mTmpVariables;
        Object data;
        for(int i=0 ,size = datas.length ; i<size ;i++){
            data = datas[i];
            if(data == null){
                throw new DataBindException("array datas can't contains null element !");
            }
            mTmpVariables.add(new VariableInfo(getVariableName(
                    data.getClass().getName()), data));
        }
        return mTmpVariables;
    }

    @android.support.annotation.NonNull
    private String getVariableName(String className) throws DataBindException{
        //final String className = obj.getClass().getName();
        String var = mVariableBeanMap.get(className);
        if(var == null){
            var = mVariableCallbakMap.get(className);
            if(var == null){
                var = mVariableBeansMap.get(className);
            }
        }
        if(var == null){
            throw new DataBindException("There is no mapping of the data's class, the classname = " +className
            +" , have you declare the <variable> ?");
        }
        return var;
    }

    /**
     *  apply the data to the all view of binds
     * @param data  the data
     * @param ids the ids of view ,can be null
     */
    public void applyData(Object data, int... ids) {
        applyData(null, data, ids);
    }
    public void applyData(String variable,Object data, int... ids) {
        if(data == null){
            throw new NullPointerException();
        }
        if(variable == null) {
            variable = mVariableBeanMap.get(data.getClass().getName());
            if (variable == null) {
                throw new DataBindException("can't find the mapping, have you declare the variable in  '<data>'?");
            }
        }
        final Array<PropertyBindInfo> infos = mBindMap_variable.get(variable.hashCode());
        if(infos == null || infos.size==0){
            throw new DataBindException("can't find any mapping of variable ( "+ variable+" ) in <bind> element");

        }
        final ViewHelper mViewHelper = this.mViewHelper;
        final BaseDataResolver mDataResolver = this.mDataResolver;
        final SparseArray<ListenerImplContext> mListenerMap = this.mListenerMap;
        final EventParseCaretaker caretaker = this.mEventCareTaker;
        mDataResolver.putObject(variable,data);

        PropertyBindInfo info;
        final boolean checkId = ids!=null && ids.length > 0 ;
        for(int i=0,size = infos.size ; i<size ;i++){
            info = infos.get(i);
            if(!checkId || ArrayUtil.contains(ids,info.viewId)){
                mDataResolver.setCurrentBindingView(mViewHelper.getView(info.viewId));
                applyDataReally(info.viewId, info, mViewHelper, mDataResolver,mListenerMap,caretaker);
            }
        }
        mDataResolver.clearObjects();
    }

    private void doWithVariableBindElement(BindElement be) {
        if(mBindMap_variable == null)
            mBindMap_variable = new SparseArray<>();

        List<PropertyElement> propEles = be.getPropertyElements();
        if(propEles!=null && propEles.size()>0){
            PropertyBindInfo info ;
            SparseArray<Array<PropertyBindInfo>> mBindMap = this.mBindMap_variable;
            final Array<PropertyBindInfo> infos = new Array<>(8);
            mBindMap.put(be.getVariable().hashCode(),infos);

            for(int i =0,size = propEles.size() ; i <size ;i++){
                PropertyElement e = propEles.get(i);
                info = new PropertyBindInfo();
                convert(info, e);

                info.viewId = ResourceUtil.getResId(getContext(), e.getId(), ResourceUtil.ResourceType.Id);
                infos.add(info);
            }
        }
    }

    private static void convert(PropertyBindInfo outInfo, PropertyElement inElement) {
        String var;//referVariables
        var = inElement.getReferVariable();
        if(var!=null){
            outInfo.referVariables = var.trim().split(",");
        }
        //referImports
        var = inElement.getReferImport();
        if(var!=null){
            outInfo.referImports = var.trim().split(",");
        }

        outInfo.expression = inElement.getText().trim();
        outInfo.propertyName = inElement.getName();
        outInfo.expressionValueType = inElement.getValueType();
    }

    /** apply data of one target PropertyBindInfo , this will auto add obj(the bind data) to BaseDataResolver,
     * so you might call clear after call this. */
    private static void applyDataInternal0(int id, Array<VariableInfo> mTmpVariables,
                                           PropertyBindInfo info, ViewHelper mViewHelper,
                                           BaseDataResolver mDataResolver, boolean checkStrictly,
                                           SparseArray<ListenerImplContext> mListenerMap ,
                                           EventParseCaretaker caretaker) {
        if(!checkStrictly || containsAll(mTmpVariables,info.referVariables)){
            if(!checkStrictly) {
                if(sDebug) {
                    String msg = "the property [ id = " + id + " ,name = " + info.propertyName
                            + "] defined in xml may couldn't be apply , you should be careful!";
                    Logger.d(TAG, msg);
                }
            }
            VariableInfo varInfo;
            for(int j = 0, len = mTmpVariables.size ; j< len ; j++){
                varInfo = mTmpVariables.get(j);
                mDataResolver.putObject(varInfo.variableName, varInfo.data);
            }
            applyDataReally(id, info, mViewHelper, mDataResolver,mListenerMap,caretaker);
        } else{
            String msg = "the property [ id = "+ id +" ,name = "+info.propertyName
                    +"] defined in xml can't be apply ,caused by some data mapping is missing !";
            throw new DataBindException(msg);
        }
    }

    /***
     */
    private static void applyDataReally(int id, PropertyBindInfo info, ViewHelper vp,
                                        IDataResolver dr,SparseArray<ListenerImplContext> mListenerMap,
                                        EventParseCaretaker caretaker) {
        caretaker.beginParse(id, info.propertyName, mListenerMap);
        if(info.realExpr != null){
            final Object val = info.realExpr.evaluate(dr);
            caretaker.endParse();
            PropertyUtil.apply(vp, id, info.propertyName, val, mListenerMap);
        }else {
            try {
                info.realExpr =  ExpressionParser.parse(info.expression);
                Object val = info.realExpr.evaluate(dr);
                caretaker.endParse();
                PropertyUtil.apply(vp, id, info.propertyName, val,mListenerMap);
            } catch (ExpressionParseException e) {
                throw new DataBindException(e);
            }
        }
    }

    private static boolean containsAll(Array<VariableInfo> container, String[] variables) {
        for(int i = 0,size = variables.length ;  i<size ;i++){
            if(!container.contains(new VariableInfo(variables[i],null),false)){
                Logger.w(TAG, "the variable = " + variables[i] + " can't find the mapping data!");
                return false;
            }
        }
        return true;
    }
    private static boolean containsAll(Array<VariableInfo> container, Collection<String> vars) {
        for(String var: vars){
            if(!container.contains(new VariableInfo(var,null),false)){
                Logger.w(TAG, "the variable = " + var + " can't find the mapping data!");
                return false;
            }
        }
        return true;
    }

    private static void checkDatasExist(Object[] datas) {
        if(datas == null || datas.length == 0)
            throw new RuntimeException("datas can't be empty");
    }

    private static void releasePropertyBindInfos(SparseArray<Array<PropertyBindInfo>> mBindMap) {
        if(mBindMap == null ) return;
        Array<PropertyBindInfo> infos;
        for(int i =0 , size = mBindMap.size() ;i<size ;i++){
            infos = mBindMap.get(i);
            for(int j=0,len = infos.size ;j<len ;j++){
                ExpressionParser.recycleIfNeed(infos.get(j).realExpr);
            }
            infos.clear();
        }
        mBindMap.clear();
    }

    @Override
    public void onEvaluateCallback(Object holder, Method method, View v, Object... params) {
        //if(!v.hasOnClickListeners() //api 15
        mEventCareTaker.update(method,holder,params);
    }

    private static class PropertyBindInfo{
        public String propertyName;
        public String [] referVariables;
        /** current not use */
        public String [] referImports;
        public String expressionValueType;
        public String expression;
        public IExpression realExpr;

        public int viewId;//just is valid in  variable bind element
    }

    private static class VariableInfo{
        String variableName;
        Object data;
        public VariableInfo(String variableName, Object data) {
            this.variableName = variableName;
            this.data = data;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            VariableInfo that = (VariableInfo) o;
            return Objects.equals(variableName, that.variableName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(variableName);
        }
    }

    private class InternalElementParserListener implements DataBindingElement.IElementParseListener{

        @Override
        public void onParseDataElement(DataElement e) {
            if(e.getImportElements()!=null){
                for (ImportElement ie : e.getImportElements()){
                    doWithImportElement(ie);
                }
            }
            if(e.getVariableElements()!=null){
                for (VariableElement ie : e.getVariableElements()){
                    doWithVariableElement(ie);
                }
            }
        }

        @Override
        public void onParseBindElements(List<BindElement> e) {
            if(e!=null){
                for (BindElement ie : e) {
                    doWithBindElement(ie);
                }
            }
        }

        @Override
        public void onParseVariableBindElements(List<BindElement> e) {
            if(e!=null){
                for (BindElement ie : e) {
                    doWithVariableBindElement(ie);
                }
            }
        }
    }

    /**
     * the caretaker of event parser
     */
    static class EventParseCaretaker{

        private Method method;
        private Object holder;
        private Object[] params;

        private String propertyName;
        private int id;
        private SparseArray<ListenerImplContext> mListenerMap;

        void beginParse(int viewId, String proppertyName, SparseArray<ListenerImplContext> listenerMap){
            this.id = viewId;
            this.propertyName = proppertyName;
            this.mListenerMap = listenerMap;
        }

        /**
         * must call after {@link IExpression#evaluate(IDataResolver)}
         * and befor {@link PropertyUtil#apply(ViewHelper, int, String, Object, SparseArray)}
         */
        void endParse(){
            if(PropertyUtil.isEventProperty(propertyName)) {
                final int key = PropertyUtil.getEventKey(id, propertyName);
                ListenerImplContext l = mListenerMap.get(key);
                if (l == null) {
                    l = PropertyUtil.createEventListener(propertyName);
                    mListenerMap.put(key, l);
                }
                l.set(method,holder,params);
            }
            reset();
        }

        private void reset() {
            mListenerMap = null;
            propertyName = null;
            update(null,null,null);
        }

        public void update(Method method, Object holder, Object[] params){
            this.method = method;
            this.holder = holder;
            this.params = params;
        }
    }


}
