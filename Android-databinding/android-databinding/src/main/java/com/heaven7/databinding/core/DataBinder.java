package com.heaven7.databinding.core;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.heaven7.databinding.core.xml.XmlElementNames;
import com.heaven7.databinding.core.xml.elements.DataBindingElement;
import com.heaven7.xml.XmlReader;

import java.io.IOException;
import java.io.InputStream;

/**
 * the data bind controller
 * Created by heaven7 on 2015/8/10.
 */
public final class DataBinder implements IDataBinder{

    private final DataBindParser mDataBindParser;
    private int mBindRawResId;
    private XmlReader.Element mCacheXml;

    /**
     * @param activity    the activity
     * @param bindsRawResId    the id of raw resource
     * @param cacheXml     true to cache xml data for reuse
     */
    public DataBinder(Activity activity,int bindsRawResId,boolean cacheXml){
        this(activity.getWindow().getDecorView(), bindsRawResId, cacheXml);
    }

    /** default not cache xml ,@see #DataBinder(Activity, int, boolean)  */
    public DataBinder(Activity activity,int bindsRawResId){
        this(activity.getWindow().getDecorView(), bindsRawResId, false);
    }

    /** @see #DataBinder(Activity, int, boolean)
     * @param root  the root view
     * @param bindsRawResId    the id of raw resource
     * @param cacheXml     true to cache xml data for reuse
     * */
    public DataBinder(View root,int bindsRawResId,boolean cacheXml){
        this.mBindRawResId = bindsRawResId;
        BaseDataResolver bdr = new BaseDataResolver(){};
        this.mDataBindParser = new DataBindParser(root, bdr);
        parseXml(root.getContext(), bindsRawResId, cacheXml);
        // property change listener ( attach and detach) notifyDataChange(user)
    }

    private void parseXml(Context context, int bindsRawResId,boolean cacheXml) {
        DataBindingElement dbe = new DataBindingElement(XmlElementNames.DATA_BINDING);
        dbe.addElementParseListener(mDataBindParser.getElementParserListener());
        if(mCacheXml!=null){
            dbe.parse(mCacheXml);
            dbe.clearElementParseListeners();
            return;
        }
        // parse bind xml
        InputStream in = context.getResources().openRawResource(bindsRawResId);
        try {
            if (!cacheXml){
                 dbe.parse(new XmlReader().parse(in));
            }else{
                mCacheXml = new XmlReader().parse(in);
                dbe.parse(mCacheXml);
            }
            dbe.clearElementParseListeners();
        } catch (IOException e) {
           throw new DataBindException(e);
        }finally{
            try {
                in.close();
            } catch (IOException e) {
            }
        }
    }

    public void onDestroy(){
        mCacheXml = null;
        if(mBindRawResId != 0 ) {
            mDataBindParser.reset();
        }
    }

    @Override
    public void reset(){
        if(mBindRawResId != 0 ){
            mDataBindParser.reset();
            parseXml(mDataBindParser.getContext(),mBindRawResId,false);
        }
        mCacheXml = null;
    }

    @Override
    public void bind(int id, String propertyName, boolean cacheData, Object... datas){
        mDataBindParser.applyData(id, 0, propertyName, true,cacheData, datas);
    }

    @Override
    public void bind(int id , boolean cacheData, Object... datas){
        mDataBindParser.applyData(id, 0, true,cacheData, datas);
    }

    @Override
    public void bind(Object data, int... ids){
        mDataBindParser.applyData(data, ids);
    }
    @Override
    public void bind(String variable, Object data, int... ids){
        mDataBindParser.applyData(variable,data, ids);
    }

    @Override
    public void notifyDataSetChanged(int viewId){
        mDataBindParser.notifyDataSetChanged(viewId);
    }

    @Override
    public void notifyDataSetChanged(int viewId, String propertyName){
        mDataBindParser.notifyDataSetChangedByTargetProperty(viewId,propertyName);
    }


}
