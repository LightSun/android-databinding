package com.heaven7.databinding.util;

import android.text.TextUtils;

import com.heaven7.databinding.core.xml.AbsElement;
import com.heaven7.xml.XmlWriter;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

/**
 * Created by heaven7 on 2015/11/26.
 */
public class DataBindUtil {

    static final HashSet<String> sSet = new HashSet<>();

    public static String [] convertRefer(String refer){
        return TextUtils.isEmpty(refer) ? null : refer.split(",");
    }

    /**
     * merge the refer variable by target mainRefer and otherRefer, refer variable is something like: 'user,handler''
     * @param mainRefer the main refer variable
     * @param otherRefer the other refer variable
     * @return the new he refer variable
     */
    public static String mergeReferVariable(String mainRefer,String otherRefer){
        if(mainRefer == null) return otherRefer;
        if(otherRefer == null) return mainRefer;
        if(mainRefer.equals(otherRefer)) return mainRefer;

        final String[] mainStrs = mainRefer.split(",");
        final String[] otherStrs = otherRefer.split(",");

        for(int i = 0 ,size = mainStrs.length; i<size ;i++){
            sSet.add(mainStrs[i]);
        }
        for(int i = 0 ,size = otherStrs.length ; i<size ;i++){
            sSet.add(otherStrs[i]);
        }
        StringBuilder sb = new StringBuilder();
        for(String str : sSet){
            sb.append(str).append(",");
        }
        //delete last
        sb.deleteCharAt(sb.length() - 1);
        sSet.clear();
        return sb.toString();
    }


    public static <T extends AbsElement>void writeElements(XmlWriter writer, List<T> list) throws IOException {
        int len = list.size();
        for (int i = len - 1; i >=0 ; i--) {
            list.get(i).write(writer);
        }
    }

    /**
     * @param val  the value to check
     * @param tag   the tag to log
     */
    public static void checkEmpty(String val,String tag){
        if(TextUtils.isEmpty(val)){
            throw new RuntimeException(tag+" can't be empty");
        }
    }
}
