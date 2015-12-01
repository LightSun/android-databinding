# android-databinding
this is the databinding framework of android .help to binding data to the view. this is a little similar to google's databinding .

## Why I developed this ?
at google-io-2015. android databindingnow can be used, it have some problem, even if this is not the reason. because i think the engineer of google's is one of the  best of the world. so it can be better and better i believe.  I absolutely can not be compared with them. but i don't like to writing databinding expression in android layout file(in javaweb I hate it too ). so I developed 
this framework. 

## Features
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


    //common name
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

- Expression support like java , only nested ternary  expression is not support.
- the more later i will write blog and doc to describe this.


## TODO
   * test , write doc detail , find bug and fix.
   
## issue
   * if you have a good suggestion  about this, please tell me. Thanks! 
   * This is just a beginning, may have bugs ,  so if you have any question , please tell me . i will do my best to resolve it. Thanks !
   
## About me
   * heaven7 
   * email: donshine723@gmail.com or 978136772@qq.com   
   
## hope
i like technology. especially the open-source technology.And previous i didn't contribute to it caused by i am a little lazy, but now i really want to do some for the open-source. So i hope to share and communicate with the all of you.

## how to use or demo ?
gradle config will support latter. 

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
        <import classname="android.view.View" alias="View"/> <!-- this type of alias  can hide (but must uppercase) -->
    </data>

    <bind id="bt">
        <property name="text" referVariable="user" valueType="string">@{user.username}</property>
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

the more to see in  [android-databinding/sample](https://github.com/LightSun/android-databinding/tree/master/Android-databinding/sample).


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


