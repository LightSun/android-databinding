package com.heaven7.databinding.core;


import com.heaven7.adapter.AdapterManager;
import com.heaven7.adapter.ISelectable;
import com.heaven7.core.util.ViewHelper;

import java.util.List;

/**
 * the data binder interface
 * Created by heaven7 on 2015/11/18.
 */
public interface IDataBinder{

    int TYPE_BEAN      = 1 ;

    /**
     * called when you want to destroy this data binder.
     */
    void onDestroy();

    /**
     * reset databinder
     */
    IDataBinder reset();

    /**
     * bind the data to target view of id,but only bind target propertyName
     * @param id  the view id
     * @param propertyName the property name of view, eg: {@link PropertyNames#TEXT}.
     * @param cacheData  true to cache it for later call {@link #notifyDataSetChanged(int)} or {@link #notifyDataSetChanged(int, String)}
     * false otherwise.
     * @param datas  the data to bind
     */
    IDataBinder bind(int id, String propertyName, boolean cacheData, Object... datas);

    /**
     * bind the data to target view of id
     * @param id  the view id
     * @param cacheData  true to cache it for later call
     *     {@link #notifyDataSetChanged(int)} or {@link #notifyDataSetChanged(int, String)} false otherwise.
     * @param datas  the data to bind
     */
    IDataBinder bind(int id, boolean cacheData, Object... datas);

    /**
     * bind a data to multi views , this data will not cache. so by call this later can't call
     * {@link #notifyDataSetChanged(int)} or {@link #notifyDataSetChanged(int, String)}
     * <li><b>Note</b> the data must mapping a variable that you declare it in xml.</li>
     * <li>xml like this: <pre>
     &lt;data&gt;
     &lt;variable name="user"  classname="com.heaven7.databinding.demo.bean.User"  type="bean"/ &lt;
     &lt;/data&gt;
     &lt;bind variable="user" &lt;
     &lt;property id ="bt1" name="text"  valueType="string"&lt; @{user.username} &lt;/property&gt;
     &lt;property id ="bt2" name="text" &gt;user.nickname &lt;/property&gt;
     &lt;/bind>
     *     </pre></li>
     * @param data  the data to bind
     * @param ids the view's id
     */
    IDataBinder bind(Object data, int... ids);

    /**
     * bind a data to multi views , this data will not cache. so by call this later can't call
     * {@link #notifyDataSetChanged(int)} or {@link #notifyDataSetChanged(int, String)}
     * <li><b>Note</b> the data must mapping a variable that you declare it in xml.</li>
     * <li>xml like this: <pre>
     &lt;bind variable="user"&gt;
     &lt;property id ="bt1" name="text"  valueType="string"&lt; @{user.username} &lt;/property&gt;
     &lt;property id ="bt2" name="text" &gt;user.nickname &lt;/property&gt;
     &lt;/bind>
     *     </pre></li>
     *     and call like this: <br><pre>
     *                mDataBinder.bind("user",new User("heaven7","xxx"),R.id.xxx,R.id.xxx2);
     *     </pre>
     * @param data  the data to bind
     * @param ids the view's id
     */
    IDataBinder bind(String variable, Object data, int... ids);

    /**
     * notify the data changed, but previous you  must call
     * {@link #bind(int, boolean, Object...)} or {@link #bind(int, String, boolean, Object...)} and cache data.
     * <br><li>note: this can't used to notify adapter data changed , or else have no effect.</li>
     * @param viewId  the view id
     */
    void notifyDataSetChanged(int viewId);

    /**
     * notify the data changed, but previous you  must call
     * {@link #bind(int, boolean, Object...)} or {@link #bind(int, String, boolean, Object...)} and cache data.
     * <br><li>note: this can't used to notify adapter data changed , or else have no effect.</li>
     * @param viewIds  the all view's id
     */
    void notifyDataSetChanged(int...viewIds);
    /**
     * <li>use {@link #notifyDataSetChanged(String, int)} instead !</li>
     * notify the data changed, but previous you  must call
     * {@link #bind(int, boolean, Object...)} or {@link #bind(int, String, boolean, Object...)}
     * and cache data is true.
     * <br><li>note: this can't used to notify adapter data changed , or else have no effect.</li>
     * @param viewId  the view id which view will be notify .
     * @param propertyName often is one of {@link PropertyNames}, eg: {@link PropertyNames#TEXT}
     */
    @Deprecated
    void notifyDataSetChanged(int viewId, String propertyName);
    /**
     * notify the data changed, but previous you  must call
     * {@link #bind(int, boolean, Object...)} or {@link #bind(int, String, boolean, Object...)} and cache data.
     * <br><li>note: this can't used to notify adapter data changed , or else have no effect.</li>
     * @param viewId  the view id which view will be notify .
     * @param propertyName often is one of {@link PropertyNames}, eg: {@link PropertyNames#TEXT}
     */
    void notifyDataSetChanged(String propertyName,int viewId);

    /**
     * notify the data changed, but previous you  must call
     * {@link #bind(int, boolean, Object...)} or {@link #bind(int, String, boolean, Object...)} and cache data.
     * <br><li>note: this can't used to notify adapter data changed , or else have no effect.</li>
     * @param ids the all views id which view will be notify.
     * @param propertyName often is  one of {@link PropertyNames}, eg: {@link PropertyNames#TEXT}
     */
    void notifyDataSetChanged(String propertyName, int ...ids);

    /**
     * bind adapter and return the AdapterManager ( can manage adapter) .
     * @param adapterViewId  the id of adapter view ,eg: ListView or RecyclerView
     * @param data  the list data to bind
     * @param extras the extras objects.often is the relative listener to bind
     * @param <T> the T
     * @return AdapterManager to manage the adapter
     */
    <T extends ISelectable> AdapterManager<T> bindAdapter(int adapterViewId, List<T> data,Object...extras);


    /*
     * void notifyAdapterDataSetChanged(int id); // moved to  AdapterManager
     */

    /**
     *  get the view helper for quick access the  all view in activity or fragment.
     * @return ViewHelper
     */
    ViewHelper getViewHelper();

}
