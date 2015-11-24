package com.heaven7.databinding.core;

import android.util.SparseArray;

import com.heaven7.databinding.core.xml.elements.BindAdapterElement;
import com.heaven7.databinding.core.xml.elements.BindElement;
import com.heaven7.databinding.core.xml.elements.DataBindingElement;
import com.heaven7.databinding.core.xml.elements.DataElement;
import com.heaven7.databinding.util.IResetable;
import com.heaven7.xml.Array;

import java.util.List;

/**
 * the adapter parser
 * Created by heaven7 on 2015/11/24.
 */
public class DataBindAdapterParser implements DataBindingElement.IElementParseListener, IResetable{

    /** key is id of adapter view eg: listView ,recyclerView */
    private SparseArray<Array<ItemBindInfo>> mItemBinds;

    @Override
    public void onParseBindAdapterElements(List<BindAdapterElement> list) {
        if(list == null || list.size() ==0)
            return;
        for(BindAdapterElement bae : list){
              //TODO
        }
    }
    @Override
    public void onParseDataElement(DataElement e) {
    }
    @Override
    public void onParseBindElements(List<BindElement> e) {
    }
    @Override
    public void onParseVariableBindElements(List<BindElement> e) {
    }

    @Override
    public void reset() {
        //TODO
    }

    public static class ItemBindInfo{
        int layoutId ;
        int tag ;
        String[] referVars;

        Array<Array<DataBindParser.PropertyBindInfo>> itemBinds;
        Array<DataBindParser.PropertyBindInfo> itemEvents;
    }
}
