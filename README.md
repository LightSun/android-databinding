# android-databinding
this is the databinding framework of android .help to binding data to the view. this is similar to google's databinding .

## Why I developed this ?
at google-io-2015. android databindingnow can be used, it have some bugs, even if this is not the reason. because i think the engineer of google's is one of the  best of the world. so it can be better and better i believe.  I absolutely can not be compared with them. but i don't like to writing databinding expression in android layout file(in javaweb I hate it too ). so I developed 
this framework. 

## Features
- support for bind data and event handler by xml. but you must call bind method in java code.
- support for notify data change.
- Support for custom attributes
- adapter bind data ? will support latter.

## TODO
   * other idea
   * to support custom event handler 
   * to support adapter
   
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


