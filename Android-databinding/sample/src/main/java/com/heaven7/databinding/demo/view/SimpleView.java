package com.heaven7.databinding.demo.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by heaven7 on 2016/2/3.
 */
public class SimpleView extends TextView {
    public SimpleView(Context context) {
        super(context);
    }

    public SimpleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public SimpleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /***
     * this is just for test self-attribute. 这个只是告诉你如何绑定自定义的属性.
     * 如果属性名称是newText 那么被绑定的view对象必须要有 setNewText/newText/addNewText 3个中其中一个.
     * 参数只要可以接收 数据绑定表达式的值即可.
     */
    public void setNewText(CharSequence text){
         setText(text);
    }
}
