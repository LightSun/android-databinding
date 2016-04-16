package com.heaven7.databinding.core;

/**
 * the name of property
 * Created by heaven7 on 2015/8/10.
 */
public interface PropertyNames {

    //event name
    String ON_CLICK                =    "onClick";
    String ON_LONG_CLICK           =    "onLongClick";
    String TEXT_CHANGE_BEFORE      =    "textChange_before";
    String TEXT_CHANGE             =    "textChange";
    String TEXT_CHANGE_AFTER       =    "textChange_after";
    String ON_FOCUS_CHANGE         =    "onFocusChange";
    String ON_TOUCH                =    "onTouch";

    //common name
    String BACKGROUND              =    "background";       //drawable /color / res id
    String BACKGROUND_COLOR        =    "backgroundColor";
    String BACKGROUND_RES          =    "background_res";

    String TEXT                    =    "text";
    String TEXT_RES                =    "text_res";

    String TEXT_COLOR              =    "textColor";
    String TEXT_COLOR_RES          =    "textColor_res";
    String TEXT_COLOR_STATE        =    "textColor_state";
    String TEXT_COLOR_STATE_RES    =    "textColor_stateRes";
    String TEXT_SIZE               =    "textSize";
    String TEXT_SIZE_RES           =    "textSize_res";

    String VISIBILITY              =    "visibility";
    //image
    @Deprecated
    /**
     * use ImageProperty instead
     */
    String IMGAE_URL               =    "img_url";
    String IMGAE_BITMAP            =    "img_bitmap";
    String IMGAE_DRAWABLE          =    "img_drawable";
    @Deprecated
    /**
     * no longer use any more
     */
    String IMGAE_ROUND_BUILDER     =    "img_round_builder";

}
