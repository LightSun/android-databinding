package com.heaven7.databinding.core;

import android.content.Context;
import android.util.SparseArray;

import com.heaven7.adapter.ISelectable;
import com.heaven7.core.util.ResourceUtil;
import com.heaven7.databinding.core.xml.elements.BindAdapterElement;
import com.heaven7.databinding.core.xml.elements.BindElement;
import com.heaven7.databinding.core.xml.elements.DataBindingElement;
import com.heaven7.databinding.core.xml.elements.DataElement;
import com.heaven7.databinding.core.xml.elements.ItemElement;
import com.heaven7.databinding.util.IResetable;
import com.heaven7.xml.Array;

import java.lang.ref.WeakReference;
import java.util.List;

import static com.heaven7.databinding.core.BindUtil.parseListItemBindInfos;
import static com.heaven7.databinding.core.BindUtil.parseListItemEventPropertyInfos;

/**
 * the adapter bind parser
 * Created by heaven7 on 2015/11/24.
 */
/*public*/ class BindAdapterParser implements DataBindingElement.IElementParseListener, IResetable {

    /**
     * key is id of adapter view eg: listView ,recyclerView
     */
     SparseArray<Array<DataBindParser.ItemBindInfo>> mItemBinds;
    /**
     * key is id of adapter view eg: listView ,recyclerView
     */
     SparseArray<DataBindParser.AdapterInfo> mOtherInfoMap;
    /**
     * key is id , value is adapter
     */
     SparseArray<Object> mAdapterMap;

    private WeakReference<Context> mWeakContext;

    public BindAdapterParser(Context context) {
        this.mWeakContext = new WeakReference<>(context.getApplicationContext());
        this.mItemBinds = new SparseArray<>(3);
        this.mOtherInfoMap = new SparseArray<>(3);
        this.mAdapterMap = new SparseArray<>(3);
    }

    @Override
    public void onParseBindAdapterElements(List<BindAdapterElement> list) {
        if (list == null || list.size() == 0)
            return;
        final Context context = this.mWeakContext.get();
        final SparseArray<Array<DataBindParser.ItemBindInfo>> mItemBinds = this.mItemBinds;
        final SparseArray<DataBindParser.AdapterInfo> mReferMap = this.mOtherInfoMap;

        List<ItemElement> ies;
        Array<DataBindParser.ItemBindInfo> infos;

        DataBindParser.ItemBindInfo info;
        boolean oneItem;
        int adapterViewId;
        int selectMode;

        for (BindAdapterElement bae : list) {
            ies = bae.getItemElements();
            if (ies == null || ies.size() == 0)
                continue;
            infos = new Array<>(3);
            oneItem = ies.size() == 1;

            adapterViewId = ResourceUtil.getResId(context, bae.getId(), ResourceUtil.ResourceType.Id);

            try {
                selectMode = Integer.parseInt(bae.getSelectMode());
                if (selectMode != ISelectable.SELECT_MODE_SINGLE && selectMode != ISelectable.SELECT_MODE_MULTI) {
                    throw new DataBindException("the value of selectMode can only be ISelectable.SELECT_MODE_SINGLE" +
                            " or ISelectable.SELECT_MODE_MULTI , please check the value of selectMode " +
                            "in <bindAdapter> element");
                }
            } catch (NumberFormatException e) {
                throw new DataBindException("the value of selectMode can only be ISelectable.SELECT_MODE_SINGLE" +
                        " or ISelectable.SELECT_MODE_MULTI , please check the value of selectMode " +
                        "in <bindAdapter> element");
            }

            mReferMap.put(adapterViewId, new DataBindParser.AdapterInfo(bae.getReferVariable(),
                    bae.getTotalRefers(), selectMode));
            mItemBinds.put(adapterViewId, infos);

            for (ItemElement ie : ies) {
                info = new DataBindParser.ItemBindInfo();
                info.layoutId = ResourceUtil.getResId(context, ie.getLayoutName(), ResourceUtil.ResourceType.Layout);
                //in multi item ,index must be declared
                if (!oneItem)
                    info.tag = Integer.parseInt(ie.getTag().trim());
                info.itemEvents = parseListItemEventPropertyInfos(ie.getPropertyElements());
                info.itemBinds = parseListItemBindInfos(context, ie.getBindElements());
                infos.add(info);
            }

        }
    }

    public void onParseDataElement(DataElement e) {
    }

    public void onParseBindElements(List<BindElement> e) {
    }

    public void onParseVariableBindElements(List<BindElement> e) {
    }

    @Override
    public void reset() {
        if (mItemBinds == null || mItemBinds.size() == 0)
            return;
        mOtherInfoMap.clear();
        mAdapterMap.clear();
        final SparseArray<Array<DataBindParser.ItemBindInfo>> mItemBinds = this.mItemBinds;
        Array<DataBindParser.ItemBindInfo> infos;
        for (int i = 0, size = mItemBinds.size(); i < size; i++) {
            infos = mItemBinds.valueAt(i);
            if (infos == null || infos.size == 0)
                continue;
            for (int j = 0, len = infos.size; j < len; j++) {
                infos.get(j).reset();
            }
            infos.clear();
        }
        mItemBinds.clear();
    }
}
