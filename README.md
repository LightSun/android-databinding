# android-databinding
this framework is now can be use in your app. By test and test,by find bug and fix i think it is now can work with well. it helps you fast bind data and event. Plugin and xml schema file for this framework can be used now. 

## Why I developed this ?
at google-io-2015. android databindingnow can be used, it have some problem, even if this is not the reason. because i think the engineer of google's is one of the  best of the world. so it can be better and better i believe.  I absolutely can not be compared with them. but i don't like to writing databinding expression in android layout file(in javaweb I hate it too ). so I developed 
this framework. 

## support plugin and xml schema
 android studio plugin and the xml schema of config file which are used for android-databinding framework is now available  .

## Features
- supported by plugin and xml schema file. 
- support for bind data and event handler by xml. but you must call bind method in java code.
- support for notify data change.
- Support for custom attr and listener
- support for bind adapter for any child of Adapter view  or RecyclerView
- suooprt for bind image 
- The common support attrs: 

  ``` java
    //event name
    String ON_CLICK                =    "onClick";
    String ON_LONG_CLICK           =    "onLongClick";
    String TEXT_CHANGE_BEFORE      =    "textChange_before";
    String TEXT_CHANGE             =    "textChange";
    String TEXT_CHANGE_AFTER       =    "textChange_after";
    String ON_FOCUS_CHANGE         =    "onFocusChange";

   // String ON_TOUCH                =    "onTouch";

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
    String IMGAE_URL               =    "img_url";
    String IMGAE_BITMAP            =    "img_bitmap";
    String IMGAE_DRAWABLE          =    "img_drawable";
    String IMGAE_ROUND_BUILDER     =    "img_round_builder";
    
   //ps:  and also support self-attrs 
  ```

- Support multi expressions. eg: java calling expression (only nested ternary expression is not support), 
   android resource reference expression {@drawable/ic_default} or R.drawable.ic_default , dp and sp like "15dp",
   and color like "#ff0000" and so on.
- the image full config was supported  now (round/circle/borderWith/borderColor/placeHolder).  
- here is the demo: 
  ``` java
   <bind id="eniv2">
        <property name="onClick" referVariable="eventHandler" > eventHandler.onClickImage()</property>
        <imageProperty type="round" referVariable="imageParam">  <!-- round / circle / oval -->
            <roundSize>{@dimen/corner_size}</roundSize>
            <borderWidth>5dp</borderWidth>
            <borderColor>#ff0000</borderColor>

            <url>imageParam.link</url>
            <default>{@drawable/ic_default}</default>     <!-- support drawable ,bitmap,  resource id -->
            <errorResId>R.drawable.ic_error</errorResId>  <!-- only support resource id -->

        </imageProperty>
    </bind>
  ```
    the more to see in sample/RoundImageBindTest Activity.
- blog and pdf is at the bottom of this page.


## How to use plugin and xml schema ?

- 1, click [here](https://github.com/LightSun/android-databinding-plugin/releases) to donwload the zip. It contains a xml       schema file and a jar of android-databinding plugin.
 
- 2 , Add the xml schema file (android_databinding.xsd )to android studio. 
   and the URI must be 'http://schemas.android.com/heaven7/android-databinding/1'. And the xsd file can be found in the          downloaded zip .
  <img src="/2.jpg" alt="add xsd file to as" width="1163px" height="711px"/>

- 3, install the plugin 'android_databinding_plugin'. step:
   in android studio click file-> setting->plugins -> install plugin from disk 
    and select the file android_databinding_plugin.jar which can be found in the downloaded zip file.

- 4, at the res directory of android studio, right-click and select android-databinding then follow it.
  and then you can enjoy the android-databinding framework.
  <img src="/3.png" alt="use demo " width="1027px" height="768px"/> 

## version log
  released [1.03](https://github.com/LightSun/android-databinding/releases)

## how to use ?

 gradle config:
``` java
dependencies {
    compile 'com.heaven7.databinding:android-databinding:1.0.3'
}

``` 

## Samples

   <img src="/databinding_1.gif" alt="Demo Screen Capture" width="300px" />
   <img src="/databinding_2.gif" alt="Demo Screen Capture" width="300px" />

base BaseBehaviour

``` java
//1, write some of your code
//the event handler called by databinding
public class MainEventHandler extends EventContext{

    public MainEventHandler(IDataBinder binder) {
        super(binder);
    }

    public void onClickChangeUsername(View v,User user){
        Util.changeUserName(user,"by_MainEventHandler_OnClick");
        getDataBinder().notifyDataSetChanged(R.id.bt);
    }

    public void onLongClickChangeUsername(View v,User user){
        Toast t =  Toast.makeText(v.getContext(), "------------ onLongClick ---------", Toast.LENGTH_SHORT);
        t.setGravity(Gravity.CENTER,0,0);
        t.show();
        Util.changeUserName(user,"by_MainEventHandler_OnLongClick");
        getDataBinder().notifyDataSetChanged(R.id.bt, PropertyNames.TEXT);
    }
}
//2. define databinding xml below raw folder,like this 

<DataBinding>
    <data>
        <variable name="user"  classname="com.heaven7.databinding.demo.bean.User"  type="bean"/>
        <variable name="mainHanlder" classname="com.heaven7.databinding.demo.callback.MainEventHandler" type="callback"/>
       <!-- <variable name="userList" classname="com.example.User" type="beans"/>-->
        <import classname="android.view.View" alias="View"/>
    </data>

    <bind id="bt">
        <property name="text" referVariable="user">@{user.username}</property>
        <property name="textColor" referVariable="user" >user.male ? {@color/red} : {@color/random}</property>
    </bind>
    <bind id="bt0">
        <property name="onClick" referVariable="user,mainHanlder" >mainHanlder.onClickChangeUsername(user)</property>
        <property name="onLongClick" referVariable="user,mainHanlder" >mainHanlder.onLongClickChangeUsername(user)</property>
    </bind>

    <bind variable="user">
        <property id ="bt2" name="text"  valueType="string">@{user.username}</property>
        <property id ="bt3" name="text" >user.getNickname()</property>
    </bind>


</DataBinding>

//3,call databinding in java code , eg: in onCreate()...
 private void doBind() {
        //init DataBinder
        mDataBinder = new DataBinder(this, R.raw.databinding_main);

        //bind a User and cache it for latter call notify.
        mDataBinder.bind(R.id.bt, true, mUser = new User("heaven7", false));

        //bind onClick event and onLongClick event and not cache any data
        mDataBinder.bind(R.id.bt0, false, mUser,new MainEventHandler(mDataBinder));

        //bind a data to multi views. but cache
        mDataBinder.bind(new User("joker", true,"xxx_joker"));
    }

```
below is bind adapter 
``` java
//1, declare your main layout  like this
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical" android:layout_width="match_parent"
    android:layout_height="match_parent">

    <ListView
        android:id="@+id/lv"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

    </ListView>

</LinearLayout>

// 2, declare item layout like this 
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical" android:layout_width="match_parent"
    android:layout_height="match_parent"
    >

    <com.android.volley.extra.ExpandNetworkImageView
        android:id="@+id/eniv"
        android:layout_width="match_parent"
        android:layout_height="200dp"
        android:scaleType="centerCrop"
        />

    <TextView
        android:id="@+id/tv"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textSize="18sp"
        android:padding="10dp"
        />

</LinearLayout>

//3 , declare the config of bind adapter like this:
<DataBinding>
    <data>
        <variable name="imageInfo"  classname="com.heaven7.databinding.demo.bean.ImageInfo"  type="bean"/>
        <variable name="itemHandler" classname="com.heaven7.databinding.demo.samples.ListViewBindAdapterTest$ItemHandler"
            type="callback"/>
    </data>

    <!-- bean must implements the interface ISelectable in bind adapter  -->
    <!-- selectMode="1"means single , 2  means multi , if not declared default is 1 -->

    <bindAdapter id="lv" referVariable="imageInfo">
        <item layout="item_image"  referVariable="itemHandler">
            <!-- root onClickListener-->
            <property name="onClick" >itemHandler.onItemClick()</property>
            <bind id="tv">
                <property name="text" >imageInfo.desc</property>
                <property name="textColor" >imageInfo.isSelected() ? {@color/red} : {@color/random}</property>
                <property name="onClick" >itemHandler.onTextClick()</property>
            </bind>
            <bind id="eniv">
                <property name="img_url" >imageInfo.url</property>
            </bind>
        </item>
    </bindAdapter>

</DataBinding>

//4,  java code -> bindAdapter
public class ListViewBindAdapterTest extends BaseActivity {

    AdapterManager<ImageInfo> mAM;

    @Override
    protected int getBindRawId() {
        return R.raw.db_test_simple_listview;
    }
    @Override
    protected int getlayoutId() {
        return R.layout.activity_listview;
    }

    @Override
    protected void onFinalInit(Bundle savedInstanceState) {
    }

    @Override
    public void doBind() {
        List<ImageInfo> infos = new ArrayList<>();
        for(int i=0 , size = Test.URLS.length ; i < size  ;i++){
            infos.add(new ImageInfo(Test.URLS[i],"desc_"+i));
        }
        mAM = mDataBinder.bindAdapter(R.id.lv, infos, new ItemHandler(getToaster()));
    }

    public static class ItemHandler {
        private final Toaster mToaster;
        public ItemHandler(Toaster mToaster) {
            this.mToaster = mToaster;
        }

        //every param must not be primitive
        public void onItemClick(View v, Integer position,ImageInfo item, AdapterManager<?> am){
            mToaster.show("ItemHandler_onItemClick: position = " + position + " ,item = " + item);
            if(item.isSelected()){
                am.getSelectHelper().setUnselected(position);
            }else{
                am.getSelectHelper().setSelected(position);
            }
        }
        public void onTextClick(View v, Integer position,ImageInfo item, AdapterManager<?> am){
            mToaster.show("on text click: position = " + position + " ,item = " + item);
        }
    }
}

```

bind multi item in adapter
``` java

//1, first declare layout xml , like this:

<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical" android:layout_width="match_parent"
    android:layout_height="match_parent">

    <ListView
        android:id="@+id/lv"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

    </ListView>

</LinearLayout>

//2, bind xml config like this: 
//you should care about the ImageInfo class must implement ITag interface. it will used to bind multi items.

<DataBinding>
    <data>
        <variable name="imageInfo"  classname="com.heaven7.databinding.demo.bean.ImageInfo"  type="bean"/>
        <variable name="itemHandler" classname="com.heaven7.databinding.demo.samples.MultiItemAdapterTest$ItemHandler2"
            type="callback"/>
    </data>

    <bindAdapter id="lv" referVariable="imageInfo" selectMode="1">

        <item layout="item_image"  tag = "1" referVariable="itemHandler">
            <property name="onClick" >itemHandler.onItemClick()</property>
            <bind id="tv">
                <property name="text" >imageInfo.desc</property>
                <property name="textColor" >imageInfo.isSelected() ? {@color/red} : {@color/random}</property>
                <property name="onClick" >itemHandler.onTextClick()</property>
            </bind>
            <bind id="eniv">
                <property name="img_url" >imageInfo.url</property>
            </bind>
        </item>

        <item layout="item_txt"  tag = "2" referVariable="itemHandler">
            <property name="onClick" >itemHandler.onTitleClick()</property>
            <bind id="tv">
                <property name="text" >imageInfo.title</property>
                <property name="textColor" >imageInfo.isSelected() ? {@color/green} : {@color/blue}</property>
            </bind>
        </item>

    </bindAdapter>
</DataBinding>

//3, relative java code 
 // (1) the imageInfo class like this:
/**
 * as the item of bind adapter . ImageInfo must implement ISelectable interface.
 * if multi item. must implement ITag interface
 * Created by heaven7 on 2015/11/30.
 */
public class ImageInfo implements ISelectable,ITag{

    private String url;

    private String desc;

    private String title;

    private boolean selected;
    private int tag ;

    public ImageInfo(String url, String desc) {
        this.url = url;
        this.desc = desc;
    }

    public ImageInfo(String url, String desc,String title) {
        this.url = url;
        this.desc = desc;
        this.title = title;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public int getTag() {
        return tag;
    }

    @Override
    public void setTag(int tag) {
        this.tag = tag;
    }
}

// (2) the sample like this:
public class MultiItemAdapterTest extends BaseActivity {
    @Override
    protected int getlayoutId() {
        return R.layout.activity_listview;
    }

    @Override
    protected int getBindRawId() {
        return R.raw.db_test_multi_item_listview;
    }

    @Override
    protected void onFinalInit(Bundle savedInstanceState) {

    }

    @Override
    protected void doBind() {
        List<ImageInfo> infos = new ArrayList<>();
        for(int i=0 , size = Test.URLS.length ; i < size  ;i++){
            final ImageInfo info = new ImageInfo(Test.URLS[i], "desc_" + i, "_title_" + i);
            info.setTag(i % 2 == 0 ? 1: 2 );
            infos.add(info);
        }
        mDataBinder.bindAdapter(R.id.lv, infos, new ItemHandler2(getToaster()));
    }
    public static class ItemHandler2 {

        private final Toaster mToaster;

        public ItemHandler2(Toaster mToaster) {
            this.mToaster = mToaster;
        }
        /** this is bind in item: item_image */
        public void onItemClick(View v, Integer position,ImageInfo item, AdapterManager<?> am){
            mToaster.show("ItemHandler_onItemClick: position = " + position + " ,item = " + item);
            if(item.isSelected()){
                am.getSelectHelper().setUnselected(position);
            }else{
                am.getSelectHelper().setSelected(position);
            }
        }
        /** this is bind in item: item_image */
        public void onTextClick(View v, Integer position,ImageInfo item, AdapterManager<?> am){
            mToaster.show("on text click: position = " + position + " ,item = " + item);
        }

        /** this is bind in item: item_txt */
        public void onTitleClick(View v, Integer position,ImageInfo item, AdapterManager<?> am){
            mToaster.show("[ this is called on item2-> 'item_txt' ] on title click: position = " +
                    position + " ,item = " + item);
        }
    }
}

``` 

below is bind image to ExpandNetworkImageView , support corner ,border,circle and so on.
   here is the main code.
``` java
 // xml conifg in like this:
 <DataBinding>
    <data>
        <variable name="imageParam" classname="com.heaven7.databinding.demo.samples.RoundImageBindTest$ImageParam" type="bean"/>
        <import classname="com.heaven7.databinding.demo.test.Test" alias="Test"/>
    </data>

    <bind id="eniv">
        <property name="img_round_builder" referVariable="imageParam" >
            Test.createRoundBuilder(imageParam.roundSize,imageParam.url)
        </property>
    </bind>
</DataBinding>

// current use expression combine java code. later will support full config in xml

 public static RoundedBitmapBuilder createRoundBuilder(float radius,String url){
        return new RoundedBitmapBuilder().url(url).cornerRadius(radius)
                .borderWidth(2f)
                .borderColor(Color.RED)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher);
    }
    
/**
 * later will support full config in xml
 * Created by heaven7 on 2015/12/2.
 */
public class RoundImageBindTest extends BaseActivity {

    @Override
    protected int getBindRawId() {
        return R.raw.db_round_image_test;
    }
    @Override
    protected int getlayoutId() {
        return R.layout.activity_round_image_test;
    }
    @Override
    protected void onFinalInit(Bundle savedInstanceState) {
    }
    @Override
    protected void doBind() {
        mDataBinder.bind(R.id.eniv,false,new ImageParam(30f, Test.URLS[0]) );
    }

    public static class ImageParam{
        float roundSize;
        String url;
        public ImageParam(float roundSize, String url) {
            this.roundSize = roundSize;
            this.url = url;
        }
    }

}

``` 
   
... and so on. 

the more to see in  [android-databinding/sample](https://github.com/LightSun/android-databinding/tree/master/Android-databinding/sample).

thanks !

## blog and pdf resource
if you are a chinese developer or know chinese.  please click  [here](http://blog.csdn.net/pkjjun2012/article/details/50286621) to see more easier.
[chinese pdf](http://download.csdn.net/detail/pkjjun2012/9352397)

## TODO
   * optimize the plugin to use easier.
   
## issue
   * if you have a good suggestion  about this, please tell me. Thanks! 
   * This is just a beginning, may have bugs ,  so if you have any question , please tell me . i will do my best to resolve it. Thanks !
   
## About me
   * heaven7 
   * email: donshine723@gmail.com or 978136772@qq.com   
   
## hope
i like technology. especially the open-source technology.And previous i didn't contribute to it caused by i am a little lazy, but now i really want to do some for the open-source. So i hope to share and communicate with the all of you.


## License

    Copyright 2015   
                    heaven7(donshine723@gmail.com)

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


