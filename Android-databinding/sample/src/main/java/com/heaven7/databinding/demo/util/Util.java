package com.heaven7.databinding.demo.util;

import com.heaven7.databinding.demo.bean.User;

/**
 * Created by heaven7 on 2015/11/20.
 */
public class Util {

    public static void changeUserName(User user,String suffix){
        String username = user.getUsername();
        if(username.contains("_")) {
            username = username.substring(0,username.indexOf("_"));
        }
        if(suffix==null){
            suffix = System.currentTimeMillis()+"";
        }
        user.setUsername(username+"_"+suffix);
    }
}
