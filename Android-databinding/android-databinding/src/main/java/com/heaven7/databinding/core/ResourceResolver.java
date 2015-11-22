package com.heaven7.databinding.core;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.util.ArrayMap;

import com.heaven7.databinding.core.expression.ExpressionParser;
import com.heaven7.databinding.util.ResourceUtil;


/**
 * like <pre>{@color/xxxx} </pre>
 * Created by heaven7 on 2015/10/27.
 */
/*public*/ abstract class ResourceResolver {

    private static final String BIG_QUOTE_LEFT  = ExpressionParser.BRACKET_BIG_LEFT+"";
    private static final String BIG_QUOTE_RIGHT = ExpressionParser.BRACKET_BIG_RIGHT+"";
    private static final String   AT            = ExpressionParser.AT+"";
    private static final String BACK_SLANT      = "/";

    private static final ArrayMap<String,ResourceResolver>
               sResolvers = new ArrayMap<>();

    protected final Context mAppcontext;
    protected final String mResName;
    protected Resources.Theme mTheme;

    /*public*/ ResourceResolver(Context context, String mResName) {
        this.mAppcontext = context.getApplicationContext();
        this.mResName = mResName;
    }

    public abstract ResourceUtil.ResourceType getResType();
    public abstract Object getResValue();

    public Resources.Theme getTheme() {
        return mTheme;
    }
    public void setTheme(Resources.Theme theme) {
        this.mTheme = theme;
    }

    public final int getResId() {
        return ResourceUtil.getResId(mAppcontext,mResName,getResType());
    }

    /** return the str is resource reference or not.*/
    public static boolean isResourceReference(String str){
        return str.startsWith(BIG_QUOTE_LEFT) && str.endsWith(BIG_QUOTE_RIGHT)
                && str.charAt(1) == AT.charAt(0) ;
    }

    /***
     * @return the resource value by the expr.
     * @throws UnsupportedOperationException  if not find the collect resolver
     */
    public static Object getResValue(Context ctx,String expr)throws UnsupportedOperationException{
        return getResolver(ctx,expr).getResValue();
    }

    /**
     * @param ctx
     * @param expr  like <pre>"{@color/c_999999}" </pre>
     * @throws UnsupportedOperationException if not find the collect resolver
     */
    public static ResourceResolver getResolver(Context ctx,String expr) throws UnsupportedOperationException{
        if(expr == null)
            throw new NullPointerException();
        if(expr.startsWith(BIG_QUOTE_LEFT) &&  expr.endsWith(BIG_QUOTE_RIGHT)){
            expr = expr.substring(1,expr.length()-1);
        }
       // AbstractThreadedSyncAdapter
        if(!expr.contains(AT+"")){
            throw new IllegalArgumentException("expr must contains @");
        }
        final int index = expr.indexOf(BACK_SLANT);
        String folderName = expr.substring(expr.indexOf(AT + "") + 1, index);
        String resName = expr.substring(index+1);

        ResourceResolver resolver = sResolvers.get(folderName);
        if(resolver == null){
            resolver = ResourceResolver.create(ctx,folderName,resName);
            sResolvers.put(folderName,resolver);
        }
        return resolver;
    }

    private static ResourceResolver create(Context ctx,String folderName,String resName) {
        if(ResourceUtil.ResourceType.Color.name.equals(folderName))
            return new ColorResolver(ctx,resName);
        if(ResourceUtil.ResourceType.String.name.equals(folderName))
            return new StringResolver(ctx,resName);
        if(ResourceUtil.ResourceType.StringArray.name.equals(folderName))
            return new StringArrayResolver(ctx,resName);
        if(ResourceUtil.ResourceType.Drawable.name.equals(folderName))
            return new DrawableResolver(ctx,resName);
        if(ResourceUtil.ResourceType.Dimen.name.equals(folderName))
            return new DimenResolver(ctx,resName);
        throw new UnsupportedOperationException("resource folder name = " + folderName+" is not support at present!");
    }

    private static class ColorResolver extends ResourceResolver{
        public ColorResolver(Context context, String mResName) {
            super(context, mResName);
        }
        @Override
        public ResourceUtil.ResourceType getResType() {
            return ResourceUtil.ResourceType.Color;
        }

        @Override
        public Object getResValue() {
            return mAppcontext.getResources().getColor(getResId());
        }
    }
    private static class DimenResolver extends ResourceResolver{
        public DimenResolver(Context context, String mResName) {
            super(context, mResName);
        }
        @Override
        public ResourceUtil.ResourceType getResType() {
            return ResourceUtil.ResourceType.Dimen;
        }

        @Override
        public Object getResValue() {
            return mAppcontext.getResources().getDimension(getResId());
        }
    }
    private static class DrawableResolver extends ResourceResolver{
        public DrawableResolver(Context context, String mResName) {
            super(context, mResName);
        }
        @Override
        public ResourceUtil.ResourceType getResType() {
            return ResourceUtil.ResourceType.Drawable;
        }
        @Override
        public Object getResValue() {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                return mAppcontext.getResources().getDrawable(getResId(),mTheme);
            }else{
                return mAppcontext.getResources().getDrawable(getResId());
            }
        }
    }
    private static class StringResolver extends ResourceResolver{
        public StringResolver(Context context, String mResName) {
            super(context, mResName);
        }
        @Override
        public ResourceUtil.ResourceType getResType() {
            return ResourceUtil.ResourceType.String;
        }
        @Override
        public Object getResValue() {
            return mAppcontext.getResources().getString(getResId());
        }
    }
    private static class StringArrayResolver extends ResourceResolver{
        public StringArrayResolver(Context context, String mResName) {
            super(context, mResName);
        }
        @Override
        public ResourceUtil.ResourceType getResType() {
            return ResourceUtil.ResourceType.StringArray;
        }

        @Override
        public Object getResValue() {
            return mAppcontext.getResources().getStringArray(getResId());
        }
    }
}
