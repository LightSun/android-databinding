package com.heaven7.databinding.util;

import android.content.Context;

import java.util.regex.Pattern;

import static com.heaven7.databinding.core.expression.ExpressionParser.BRACKET_BIG_LEFT;
import static com.heaven7.databinding.core.expression.ExpressionParser.BRACKET_BIG_RIGHT;
import static com.heaven7.databinding.core.expression.ExpressionParser.BRACKET_MINI_LEFT;
import static com.heaven7.databinding.core.expression.ExpressionParser.BRACKET_MINI_RIGHT;
import static com.heaven7.databinding.core.expression.ExpressionParser.BRACKET_SQUARE_LEFT;
import static com.heaven7.databinding.core.expression.ExpressionParser.BRACKET_SQUARE_RIGHT;
import static com.heaven7.databinding.core.expression.ExpressionParser.COMMA;
import static com.heaven7.databinding.core.expression.ExpressionParser.QUOTE;

/**
 * Created by heaven7 on 2015/8/12.
 */
public class StringUtil2 {
// PATTERN_PHONE = Pattern.compile("^[1][\\d]{10}" + "|" + "^[0]\\d{2,3}[\\-]*\\d{7,8}" +"|"+ "^[1-9]\\d{6,7}");
    private static final char[] UpperCases              = "ABCDEFGHIJKLMNOPQRSTWVUXYZ".toCharArray();
    private static final Pattern sIntParttern           = Pattern.compile("[0-9]+");
    private static final Pattern sFloatParttern         = Pattern.compile("[0-9]+[\\.][0-9]+");
    private static final Pattern sResPattern            = Pattern.compile("[R][\\.][a-z]+[\\.][a-z_0-9]+");
    private static final Pattern sAndroidResPattern     = Pattern.compile("(android)[\\.][R][\\.][a-z]+[\\.][a-z_0-9]+");

    public static boolean isFirstUpperCase(String target)throws NullPointerException{
        if(target ==null )  throw new NullPointerException();
        final char c = target.charAt(0);
        for(int i=0 , size = UpperCases.length ; i < size ;i++){
            if(UpperCases[i] == c){
                return true;
            }
        }
        return false;
    }

    public static boolean isInteger(String str){
        return sIntParttern.matcher(str).matches();
    }
    public static boolean isFloat(String str){
        return sFloatParttern.matcher(str).matches();
    }

    /** @return true if contains any of "{}()[]," */
    public static boolean containsSpecialSymbol(String str){
        char[] chs = str.toCharArray();
        char tmp;
        for(int i=0,size =chs.length ; i < size ; i++){
            tmp = chs[i];
            if(tmp == BRACKET_MINI_LEFT
                    || tmp == BRACKET_MINI_RIGHT
                    || tmp == BRACKET_SQUARE_LEFT
                    || tmp == BRACKET_SQUARE_RIGHT
                    || tmp == BRACKET_BIG_LEFT
                    || tmp == BRACKET_BIG_RIGHT
                    || tmp == COMMA
                    || tmp == QUOTE
                    )
                return true;
        }
        return false;
    }

    public static boolean isNull(String str) {
        return "null".equals(str);
    }

    public static boolean isResourceReferOfR(String str){
        return sResPattern.matcher(str).matches() || sAndroidResPattern.matcher(str).matches();
    }

    /** return the res id of android ( current only the calling package )*/
    public static int getResId(String str,Context context){
        String packageName ;
        if(str.startsWith("android.R.")){
            packageName = "android";
            //android.R.anim.xxx -> R.anim.xxx
            str = str.substring(str.indexOf(".")+1);
        }else{
            packageName = context.getApplicationInfo().packageName;
        }
        //final String packageName = context.getApplicationInfo().packageName;
        final String[] strs = str.split("\\.");
        try { //R.drawable.xxx_xx
            return Class.forName(packageName +".R$" + strs[1]).getField(strs[2]).getInt(null);
        } catch (Exception e) {
           throw new RuntimeException(e);
        }
    }

}
