## android-databinding

这是一个android数据绑定框架， 它可以帮助你快速绑定数据和事件。 拥有xml约束支持and插件支持。目前已经发布release版本。

 <img src="/databinding_1.gif" alt="Demo Screen Capture" width="300px" />
 <img src="/databinding_2.gif" alt="Demo Screen Capture" width="300px" />
 <img src="/imgs/databinding_3.gif" alt="Demo Screen Capture" width="300px" />
 
   <b>如果你把源代码下载下来了 ，首先你需要导入xml约束的xsd文件, 否则会build失败。
   </b>

## 我为什么要开发这个框架 ?
在google 2015年io大会的时候，Google公布了一个新的框架。android数据绑定的框架。 虽然它目前有些问题。 但是我相信会越来好，因为google的工程师是世界最好的工程师之一。 我不能他们比 。 但是我对该框架的设计中，在xml布局
文件中绑定数据文件，这点我是不敢苟同的。它使得layout文件的可读性大影响。而且一旦布局复杂了就更甚
（在javaweb的时候就讨厌了）。我喜欢纯粹的东西。所以我设计了这个框架。它的数据绑定配置文件在res/raw目录下。


## 支持 as 插件和 xml约束文件
 

## 框架特点

- 图片属性如何应用可以自定义了。也就是说你可以用自己喜欢的库，glide或者facebook fresco 或者image loader等库去加载。

- 支持android开发中常用的数据绑定，比如控件的背景，文字，文字颜色，文字大小，图片（圆角边框，默认图,错误图等）.
-
- 支持常用事件的绑定，支持自定义属性，自定义事件的绑定（需要注册）。支持对象数据改变后调用notify通知.
- 
- 支持ListView,GridView,RecyclerView adapter数据的绑定(支持多种item), 事件回调对象AdapterManage   可对adapter的数据进行crud操作,支持单选多选模式.
- 
- 插件支持 和 xml约束文件支持

- 丰富的表达式支持. 
   比如: java 表达式的调用(均可嵌套),字段，方法，数组的调用. 支持android 资源的引用,比如 表达式 为'{@drawable/ic_default} '
    支持sp,dp，16进制颜色，比如 "15dp", "15sp", "#ff0000" 等等

- <b> 图片完整的配置如果还不清楚请看demo </b>  
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
- 数据绑定, 常用的属性如下: 

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
    String IMGAE_ROUND_BUILDER     =    "img_round_builder";
    
   //ps:  and also support self-attrs 
  ```

#### 支持 as 插件和 xml约束文件, 如何使用插件和xsd约束文件 ?

- 1, 点击去下载压缩包。
    [here](https://github.com/LightSun/android-databinding-plugin/releases). 它包含 插件jar 和 xml约束xsd文件.
 
- 2，添加xml约束到android studio中. 
  按下面配置，需填写的uri必须为 'http://schemas.android.com/heaven7/android-databinding/1'. 
  <img src="/2.jpg" alt="add xsd file to as" width="1163px" height="711px"/>

- 3, 安装好插件'android_databinding_plugin', 步骤：
   in android studio click file-> setting->plugins -> install plugin from disk 
    and select the file android_databinding_plugin.jar which can be found in the downloaded zip file.

- 4, 以上完成后，在res目录右击后选择 android-databinding ， 然后你就可以 使用数据绑定的框架了. 示例：
  <img src="/3.png" alt="use demo " width="1027px" height="768px"/> 
  <img src="/imgs/xsd_1.png" alt="use demo " width="884px" height="599px"/> 


## version log
 - released [1.1.0](https://github.com/LightSun/android-databinding/tags)
 - click [here](https://github.com/LightSun/android-databinding/blob/master/readmes/version_log.md) to see more .

## Gradle

 gradle config:
``` java
dependencies {
    compile 'com.heaven7.databinding:android-databinding:1.1.5'
}

``` 

## 使用
   
使用详情. [Usage Detail](https://github.com/LightSun/android-databinding/blob/master/readmes/sample.md)
 
## 博客和 关于框架的pdf
如果你是个中国的开发者，或者熟悉汉语。 请点击。 [here](http://blog.csdn.net/pkjjun2012/article/details/50286621) to see more easier.
[中文文档](http://download.csdn.net/detail/pkjjun2012/9352397)

## TODO
   
   * optimize the plugin to use easier.
   
## issue
   * 这个框架我会尽力去维护。如果你有好的建议或者idea,请告诉我。Thanks! 
   
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


