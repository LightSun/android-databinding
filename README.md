## android-databinding
this framework is now can be used in your app. By test and test,by find bug and fix i think it is now can work with well. it helps you fast bind data and event. <b>Plugin and xml schema file for this framework can be used now. </b>
如果你熟悉汉语, 请点击看[中文版](https://github.com/LightSun/android-databinding/blob/master/readmes/readme_zh.md)

 <img src="/databinding_1.gif" alt="Demo Screen Capture" width="300px" />
 <img src="/databinding_2.gif" alt="Demo Screen Capture" width="300px" />
 <img src="/imgs/databinding_3.gif" alt="Demo Screen Capture" width="300px" />
 
   <b>before build the sample project,you need to import the xml schema file of this databind framework, see it below.</b>
   see <b>[How to use plugin and xml schema ?]</b>

## Why I developed this ?
at google-io-2015. android databindingnow can be used, it have some problem, even if this is not the reason. because i think the engineer of google's is one of the  best of the world. so it can be better and better i believe.  I absolutely can not be compared with them. but i don't like to writing databinding expression in android layout file(in javaweb I hate it too ). so I developed 
this framework. 

## support plugin and xml schema
 android studio plugin and the xml schema of config file which are used for android-databinding framework is now available  .

## Features
- you can apply image property (round,borderColor,borderWidth,circle) by you like now.
  so to apply image property you must call like this. 
 ``` java
 DataBindingFactory.setImagePropertyApplier(new VolleyImageApplier()); //here i just use volley to load
 ```

- supported by plugin and xml schema file. 
- support for bind data and event handler by xml. but you must call bind method in java code.
- support for notify data change.
- Support for custom attr and listener
- support for bind adapter for any child of Adapter view  or RecyclerView
  ( contains multi items and multi select mode of adapter )
- support for bind image (like round/borderColor/borderWith/placeHolder/errorResId),see it bellow.
- The common support attrs: 

  ``` java
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
    String IMGAE_URL               =    "img_url";
    String IMGAE_BITMAP            =    "img_bitmap";
    String IMGAE_DRAWABLE          =    "img_drawable";
    
   //ps:  and also support self-attrs 
  ```

- Support multi expressions. eg: java calling expression (only nested ternary expression is not support), 
   android resource reference expression {@drawable/ic_default} or R.drawable.ic_default , dp and sp like "15dp",
   and color like "#ff0000" and so on.
- <b>the image full config was supported  now (round/circle/borderWith/borderColor/placeHolder).</b>  
  here is the demo: 
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


####How to use plugin and xml schema ?

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
  <img src="/imgs/xsd_1.png" alt="use demo " width="884px" height="599px"/> 


## version log
 - released [1.1.5](https://github.com/LightSun/android-databinding/tags)
 - click [here](https://github.com/LightSun/android-databinding/blob/master/readmes/version_log.md) to see more .

## Gradle

 gradle config:
``` java
dependencies {
    compile 'com.heaven7.databinding:android-databinding:1.1.5'
}

``` 

## Usage
   
The explanation of usage is moved to child directory. click to see it. [Usage Detail](https://github.com/LightSun/android-databinding/blob/master/readmes/sample.md)
 
## blog and pdf resource
if you are a chinese developer or know chinese.  please click  [here](http://blog.csdn.net/pkjjun2012/article/details/50286621) to see more easier.
[chinese pdf](http://download.csdn.net/detail/pkjjun2012/9352397)

## Compare to google's databinding
   see it [here](http://blog.csdn.net/pkjjun2012/article/details/50949306). this currently only have chinese doc.

## refer lib
[android-common-util-light](https://github.com/LightSun/android-common-util-light)

## TODO
   * support gesture image (also nested in viewpager)
   * support generate relative code. 
   * add demo for fragment .
   * optimize the plugin to use easier.
   
## issue
   * this framework i will try my best to uphold , and  if you have a good suggestion  about this, please tell me. Thanks! 
   
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


