package com.heaven7.databinding.core;

/**
 * the data binder interface
 * Created by heaven7 on 2015/11/18.
 */
public interface IDataBinder{

    int TYPE_BEAN      = 1 ;
    int TYPE_BEANS     = 2 ;
    int TYPE_CALLBACK  = 3 ;

    /**
     * reset databinder
     */
    void reset();

    /**
     * bind the data to target view of id,but only bind target propertyName
     * @param id  the view id
     * @param propertyName the property name of view
     * @param cacheData  true to cache it for later call {@link #notifyDataSetChanged(int)} or {@link #notifyDataSetChanged(int, String)}
     * false otherwise.
     * @param datas  the data to bind
     */
    void bind(int id, String propertyName, boolean cacheData, Object... datas);

    /**
     * bind the data to target view of id
     * @param id  the view id
     * @param cacheData  true to cache it for later call
     *     {@link #notifyDataSetChanged(int)} or {@link #notifyDataSetChanged(int, String)} false otherwise.
     * @param datas  the data to bind
     */
    void bind(int id, boolean cacheData, Object... datas);

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
    void bind(Object data, int... ids);

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
     * @param data  the data to bind
     * @param ids the view's id
     */
    void bind(String variable, Object data, int... ids);

    /**
     * notify the data changed, but previous you  must call
     * {@link #bind(int, boolean, Object...)} or {@link #bind(int, String, boolean, Object...)}
     * @param viewId  the view id
     */
    void notifyDataSetChanged(int viewId);
    /**
     * notify the data changed, but previous you  must call
     * {@link #bind(int, boolean, Object...)} or {@link #bind(int, String, boolean, Object...)}
     * @param viewId  the view id
     */
    void notifyDataSetChanged(int viewId, String propertyName);
}
