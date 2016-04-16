package com.heaven7.databinding.core;


import com.heaven7.core.util.ViewHelper;

/**
 * the adapter item data binder
 */
/*public*/ interface IAdapterItemDataBinder {

    /**
     *
     * @param position  the position of adapter
     * @param helper   the view helper
     * @param item    the item data
     * @param bindInfo the bind info of ItemBindInfo
     * @param mMainRefer the main refer
     * @param hashCode the hashcode of adapter
     */
    void onBindItemData(int position, ViewHelper helper, Object item,
                           DataBindParser.ItemBindInfo bindInfo,
                           String mMainRefer, int hashCode);
}
