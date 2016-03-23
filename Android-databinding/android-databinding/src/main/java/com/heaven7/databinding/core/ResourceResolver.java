package com.heaven7.databinding.core;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.util.ArrayMap;

import com.heaven7.databinding.core.expression.ExpressionParser;
import com.heaven7.databinding.util.ResCompatUtil;
import com.heaven7.databinding.util.ResourceUtil;


/**
 * this class help to expand some thing relate {@link android.content.res.Resources.Theme}.
 * while get resource.
 * like <pre>{@color/xxxx} </pre>
 * <p><li>add new support for android internal res reference, eg:
 * <pre>
  int color  = getResources().getColor(StringUtil2.getResId("android.R.color.holo_red_light", this));
  mDataBinder.getViewHelper().setTextColor(R.id.bt3, color);
  mDataBinder.getViewHelper().setTextColor(R.id.bt3, (Integer) ResourceResolver.getResValue(
            this,"{@android:color/holo_red_light}"));
 * </pre>
 * </li></p>
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
    protected Resources.Theme mTheme;
    protected  String mResName;

    /*public*/ ResourceResolver(Context context, String mResName) {
        this.mAppcontext = context.getApplicationContext();
        this.mResName = mResName;
    }

    public abstract ResourceUtil.ResourceType getResType();
    public abstract Object getResValue();

    public void setResName(String resName){
         this.mResName = resName;
    }

    public Resources.Theme getTheme() {
        return mTheme;
    }
    public void setTheme(Resources.Theme theme) {
        this.mTheme = theme;
    }

    public int getResId() {
        return ResourceUtil.getResId(mAppcontext,mResName,getResType());
    }

    /** is the android internal resource ,default is false */
    public boolean isAndroidInternal(){
        return false;
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

   /* public static Object getResValue2(Context ctx,String expr)throws UnsupportedOperationException{
        if(expr == null)
            throw new NullPointerException();
        final String rawExpr = expr;
        if(expr.startsWith(BIG_QUOTE_LEFT) &&  expr.endsWith(BIG_QUOTE_RIGHT)){
            expr = expr.substring(1,expr.length()-1);
        }
        // AbstractThreadedSyncAdapter
        if(!expr.contains(AT+"")){
            throw new IllegalArgumentException("expr must contains @");
        }
        final int index = expr.indexOf(BACK_SLANT);
        String folderName = expr.substring(expr.indexOf(AT + "") + 1, index);
        String resName = expr.substring(index + 1);

        final boolean androidInternal = folderName.startsWith("android:");
        int resId;
        if(androidInternal){
            folderName = folderName.substring(folderName.indexOf(":") + 1);
            try {
                resId = Class.forName("android.R$"+folderName).getField(resName).getInt(null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else{
            resId = ctx.getResources().getIdentifier(resName, folderName, ctx.getPackageName());
        }
        if(resId == 0){
            throw  new RuntimeException("can't find the resource : " + rawExpr );
        }

        if(ResourceUtil.ResourceType.Color.name.equals(folderName)){
            return ctx.getResources().getColor(resId);
        }
        if(ResourceUtil.ResourceType.String.name.equals(folderName)){
            return ctx.getResources().getString(resId);
        }
        if(ResourceUtil.ResourceType.StringArray.name.equals(folderName)){
            return ctx.getResources().getStringArray(resId);
        }
        if(ResourceUtil.ResourceType.Drawable.name.equals(folderName)){
            return ctx.getResources().getDrawable(resId);
        }
        if(ResourceUtil.ResourceType.Dimen.name.equals(folderName)) {
            return ctx.getResources().getDimension(resId);
        }
        throw new UnsupportedOperationException();
    }
*/
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
        String resName = expr.substring(index + 1);

        final boolean androidInternal = folderName.startsWith("android:");
        final String key = folderName ; //eg: android:color
        if(androidInternal){
            folderName = folderName.substring(folderName.indexOf(":")+1);
        }

        ResourceResolver resolver = sResolvers.get(key);
        if(resolver == null){
            resolver = ResourceResolver.create(ctx,folderName, resName, androidInternal);
            sResolvers.put(key,resolver);
        }else{
            resolver.setResName(resName); //case this or make bug
        }
        return resolver;
    }

    private static ResourceResolver create(Context ctx, String folderName, String resName, boolean androidInternal) {
        if(ResourceUtil.ResourceType.Color.name.equals(folderName))
            return !androidInternal ? new ColorResolver(ctx,resName) :
                    new AndroidInternalColorResolver(ctx,folderName,resName);
        if(ResourceUtil.ResourceType.String.name.equals(folderName))
            return !androidInternal ? new StringResolver(ctx,resName) :
                    new AndroidInternalStringResolver(ctx,folderName,resName) ;
        if(ResourceUtil.ResourceType.StringArray.name.equals(folderName))
            return !androidInternal ? new StringArrayResolver(ctx,resName) :
                    new AndroidInternalStringArrayResolver(ctx,folderName,resName) ;
        if(ResourceUtil.ResourceType.Drawable.name.equals(folderName))
            return !androidInternal ? new DrawableResolver(ctx,resName) :
                    new AndroidInternalDrawableResolver(ctx,folderName,resName) ;
        if(ResourceUtil.ResourceType.Dimen.name.equals(folderName))
            return !androidInternal ? new DimenResolver(ctx,resName) :
                    new AndroidInternalDimenResolver(ctx,folderName,resName) ;
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

        @SuppressWarnings("deprecation")
        @Override
        public Object getResValue() {
           return ResCompatUtil.getDrawable(mAppcontext,getResId(),mTheme);
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

    //================================== android internal res resolver =====================================

    private static class AndroidInternalDrawableResolver extends AbsAndroidInternalResolver{
        AndroidInternalDrawableResolver(Context context,String folderName ,String mResName) {
            super(context,folderName ,mResName);
        }
        @Override
        public ResourceUtil.ResourceType getResType() {
            return ResourceUtil.ResourceType.Drawable;
        }
        @Override
        public Object getResValue() {
           return ResCompatUtil.getDrawable(mAppcontext,getResId(),mTheme);
        }
    }

    private static class AndroidInternalStringArrayResolver extends AbsAndroidInternalResolver{
        AndroidInternalStringArrayResolver(Context context,String folderName ,String mResName) {
            super(context,folderName ,mResName);
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

    private static class AndroidInternalStringResolver extends AbsAndroidInternalResolver{
        AndroidInternalStringResolver(Context context,String folderName ,String mResName) {
            super(context,folderName ,mResName);
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

    private static class AndroidInternalDimenResolver extends AbsAndroidInternalResolver{
        AndroidInternalDimenResolver(Context context,String folderName ,String mResName) {
            super(context,folderName ,mResName);
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

    private static class AndroidInternalColorResolver extends AbsAndroidInternalResolver{
        AndroidInternalColorResolver(Context context,String folderName ,String mResName) {
            super(context,folderName ,mResName);
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

    private static abstract class AbsAndroidInternalResolver extends ResourceResolver{

        private final String mFolderName;

        AbsAndroidInternalResolver(Context context,String folderName ,String mResName) {
            super(context, mResName);
            this.mFolderName = folderName;
        }

        @Override
        public int getResId() {
            //android.R.color.holo_red_light
            try {
                return Class.forName("android.R$"+mFolderName).getField(mResName).getInt(null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public boolean isAndroidInternal() {
            return true;
        }
    }
}
