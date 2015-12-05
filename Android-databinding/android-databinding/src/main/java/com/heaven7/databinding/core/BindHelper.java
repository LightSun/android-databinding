package com.heaven7.databinding.core;

import android.content.Context;
import android.util.SparseArray;
import android.util.SparseIntArray;

import com.heaven7.xml.Array;

import org.heaven7.core.adapter.ISelectable;
import org.heaven7.core.adapter.MultiItemTypeSupport;
import org.heaven7.core.adapter.QuickAdapter;
import org.heaven7.core.adapter.QuickRecycleViewAdapter;
import org.heaven7.core.viewhelper.ViewHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heaven7 on 2015/11/26.
 */
/*public*/ class BindHelper {

    static  final  int  DEFAULT_OVER_ITEM_COUNT    =    3;

    /**
     * this class implements MultiItemTypeSupport ,and also support one item for easy use.
     * but when support multi items, the T must implements the interface ITag.
     * @param <T>
     */
    /*public*/ static class MultiItemTypeSupportImpl<T extends ISelectable> implements MultiItemTypeSupport<T>{

        final Array<DataBindParser.ItemBindInfo> mItemBindInfos ;
        SparseArray<DataBindParser.ItemBindInfo> mInfoMap ;
        SparseIntArray mViewTypeMap ;

        public MultiItemTypeSupportImpl(Array<DataBindParser.ItemBindInfo> infos) {
            this.mItemBindInfos = infos;
        }
        /** only put when mItemBindInfos.size > DEFAULT_OVER_ITEM_COUNT */
        private void putItemBindInfo(int layoutId , DataBindParser.ItemBindInfo info){
            if(mInfoMap == null){
                if(mItemBindInfos.size > DEFAULT_OVER_ITEM_COUNT ){
                    mInfoMap = new SparseArray<>();
                    mInfoMap.put(layoutId,info);
                }
            }else{
                mInfoMap.put(layoutId,info);
            }

        }
        public DataBindParser.ItemBindInfo getItemBindInfo(int layoutId){
            if(mInfoMap == null){ // not in map
                final Array<DataBindParser.ItemBindInfo> mItemBindInfos = this.mItemBindInfos;
                DataBindParser.ItemBindInfo info;
                for(int i=0,size = mItemBindInfos.size ; i<size ;i++){
                    info = mItemBindInfos.get(i);
                    if(info.layoutId == layoutId){
                        return info;
                    }
                }
                throw new IllegalStateException("can't find ItemBindInfo");
            }else{
                return mInfoMap.get(layoutId);
            }
        }

        @Override
        public int getLayoutId(int position, T t) {
            if(mItemBindInfos.size == 1){
                return mItemBindInfos.get(0).layoutId;
            }else{
                if(! (t instanceof ITag)){
                    throw new DataBindException("multi items in adapter view ,the class: "+t.getClass().getName()+
                            " must implements ITag , and the tag must correspond the declared tag" +
                            " in databinding xml, eg:  <item layout=\"item_xxx\" tag = \"1\" referVariable=\"user,itemHandler\"> ");
                }
                final ITag it = (ITag) t;
                final Array<DataBindParser.ItemBindInfo> mItemBindInfos = this.mItemBindInfos;
                DataBindParser.ItemBindInfo info;
                for(int i=0,size = mItemBindInfos.size ; i<size ;i++){
                    info = mItemBindInfos.get(i);
                    if(info.tag == it.getTag()){
                        putItemBindInfo(info.layoutId,info);
                        return info.layoutId;
                    }
                }
            }
            throw new DataBindException("can't find the layout id ");
        }

        @Override
        public int getViewTypeCount() {
            return mItemBindInfos.size ;
        }

        @Override
        public int getItemViewType(int position, T t) {
            //in BaseQuickAdapter  getItemViewType = 0 is occupied by IndeterminateProgress so need +1

            //can't use % , or else ,often cause bug
            if( mItemBindInfos.size == 1 ) return 1;
            if(! (t instanceof ITag)){
                throw new DataBindException("multi items in adapter view ,the class: "+t.getClass().getName()+
                        " must implements ITag , and the tag must correspond the declared tag" +
                        " in databinding xml, eg:  <item layout=\"item_xxx\" tag = \"1\" referVariable=\"user,itemHandler\"> ");
            }
            final int tag = ((ITag) t).getTag();
            if(mViewTypeMap == null)
                mViewTypeMap = new SparseIntArray();

            int itemType;
            if( (itemType = mViewTypeMap.get(tag, -1) )== -1){
                //value begin 1... size begin 1... too.
                final int size = mViewTypeMap.size();
                mViewTypeMap.put(tag , size + 1);
                return size + 1;
            }
            return itemType;
        }
    }

    /*public*/ static abstract class QuickAdapter2<T extends ISelectable> extends QuickAdapter<T> {

        public QuickAdapter2(List<T> data, Array<DataBindParser.ItemBindInfo> infos,int selectMode) {
            super(data instanceof ArrayList ? (ArrayList<T>) data : new ArrayList<T>(data),
                    new MultiItemTypeSupportImpl<T>(infos), selectMode);
        }

        private DataBindParser.ItemBindInfo getItemBindInfo( int itemLayoutId){
            return  ((MultiItemTypeSupportImpl) mMultiItemSupport).getItemBindInfo(itemLayoutId);
        }

        @Override
        protected void onBindData(Context context, int position, T item, int itemLayoutId, ViewHelper helper) {
            bindDataImpl(context,position,helper,itemLayoutId,item,getItemBindInfo(itemLayoutId));
        }

        protected abstract void bindDataImpl(Context context, int position, ViewHelper helper,
                                             int itemLayoutId, T item, DataBindParser.ItemBindInfo bindInfo);
    }

    /*public*/ static abstract class QuickRecycleAdapter2<T extends ISelectable> extends QuickRecycleViewAdapter<T>{

        final Array<DataBindParser.ItemBindInfo> mItemBindInfos;
        SparseArray<DataBindParser.ItemBindInfo> mInfoMap;

        public QuickRecycleAdapter2( List<T> mDatas, Array<DataBindParser.ItemBindInfo> infos,int selectMode) {
            super(0, mDatas, selectMode);
            this.mItemBindInfos = infos;
        }

        @Override
        protected void onBindData(Context context, int position, T item, int itemLayoutId, ViewHelper helper) {
            bindDataImpl(context,position,helper,itemLayoutId,item,getItemBindInfo(itemLayoutId));
        }
        protected abstract void bindDataImpl(Context context, int position, ViewHelper helper,
                                             int itemLayoutId, T item, DataBindParser.ItemBindInfo bindInfo);

        /** only put when mItemBindInfos.size > DEFAULT_OVER_ITEM_COUNT */
        private void putItemBindInfo(int layoutId , DataBindParser.ItemBindInfo info){
            if(mInfoMap == null){
                if(mItemBindInfos.size > DEFAULT_OVER_ITEM_COUNT ){
                    mInfoMap = new SparseArray<>();
                    mInfoMap.put(layoutId,info);
                }
            }else{
                mInfoMap.put(layoutId,info);
            }
        }
        public DataBindParser.ItemBindInfo getItemBindInfo(int layoutId){
            if(mInfoMap == null){ // not in map
                final Array<DataBindParser.ItemBindInfo> mItemBindInfos = this.mItemBindInfos;
                DataBindParser.ItemBindInfo info;
                for(int i=0,size = mItemBindInfos.size ; i<size ;i++){
                    info = mItemBindInfos.get(i);
                    if(info.layoutId == layoutId){
                        return info;
                    }
                }
                throw new IllegalStateException("can't find ItemBindInfo");
            }else{
                return mInfoMap.get(layoutId);
            }
        }


        @Override
        protected int getItemLayoutId(int position, T t) {
            if(mItemBindInfos.size == 1){
                return mItemBindInfos.get(0).layoutId;
            }else{
                if(! (t instanceof ITag)){
                    throw new DataBindException("multi items in adapter view ,the class: "+t.getClass().getName()+
                            " must implements ITag , and the tag must correspond the declared tag" +
                            " in databinding xml, eg:  <item layout=\"item_xxx\" tag = \"1\" referVariable=\"user,itemHandler\"> ");
                }
                final ITag it = (ITag) t;
                final Array<DataBindParser.ItemBindInfo> mItemBindInfos = this.mItemBindInfos;
                DataBindParser.ItemBindInfo info;
                for(int i=0,size = mItemBindInfos.size ; i<size ;i++){
                    info = mItemBindInfos.get(i);
                    if(info.tag == it.getTag()){
                        putItemBindInfo(info.layoutId, info);
                        return info.layoutId;
                    }
                }
            }
            throw new DataBindException("can't find the layout id ");
        }
    }
}
