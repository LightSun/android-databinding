/*
 * Copyright (C) 2015
 *            heaven7(donshine723@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.heaven7.databinding.viewhelper;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.util.Linkify;
import android.util.TypedValue;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import com.android.volley.extra.ExpandNetworkImageView;
import com.android.volley.extra.RoundedBitmapBuilder;
import com.heaven7.databinding.util.ViewCompatUtil;

/**
 * the really implements of ViewHelper. use this class to fast set property.
 * this only can be called on main thread.
 * Created by heaven7 on 2015/8/6.
 */
public class ViewHelperImpl{

    private View v;

    /**
     * create an instance of {@link ViewHelperImpl}
     * @param target  the target to view
     */
    public ViewHelperImpl(@Nullable View target) {
        this.v = target;
    }
    public ViewHelperImpl(){}

    /** change the current view to the target */
    public ViewHelperImpl view(View target){
        if(target==null)
            throw new NullPointerException("target view can;t be null!");
        this.v = target;
        return this;
    }

    /**
     * reverse to the  t
     * @param  t  the object to reverse.
     * @return
     */
    public <T>T reverse(T t ){
        return t;
    }

    public Context getContext(){
        return v.getContext();
    }

    public ViewHelperImpl setVisibility(boolean visible){
        v.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }
    public ViewHelperImpl setVisibility(int visibility){
        v.setVisibility(visibility);
        return this;
    }

    public ViewHelperImpl setText(CharSequence text){
        ((TextView)v).setText(text);
        return this;
    }

    public ViewHelperImpl setEnabled(boolean enable){
        v.setEnabled(enable);
        return this;
    }

    public ViewHelperImpl toogleVisibility(){
        View view = this.v;
        if(view.getVisibility() == View.VISIBLE){
            view.setVisibility(View.GONE);
        }else{
            view.setVisibility(View.VISIBLE);
        }
        return this;
    }

    public ViewHelperImpl setImageResource(int imageResId) {
        ((ImageView)v).setImageResource(imageResId);
        return this;
    }
    public ViewHelperImpl setBackgroundColor(int color) {
        v.setBackgroundColor(color);
        return this;
    }
    public ViewHelperImpl setBackgroundRes(int backgroundRes) {
        v.setBackgroundResource(backgroundRes);
        return this;
    }
    public ViewHelperImpl setBackgroundDrawable(Drawable d) {
        ViewCompatUtil.setBackgroundCompatible(v, d);
        return this;
    }
    public ViewHelperImpl setTextColor(int textColor) {
        ((TextView)v).setTextColor(textColor);
        return this;
    }
    public ViewHelperImpl setTextColor(ColorStateList colorList) {
        ((TextView)v).setTextColor(colorList);
        return this;
    }
    public ViewHelperImpl setTextColorRes(int textColorResId) {
        return setTextColor(getContext().getResources().getColor(textColorResId));
    }
    public ViewHelperImpl setTextColorStateListRes(int textColorStateListResId) {
        return setTextColor(getContext().getResources().getColorStateList(textColorStateListResId));
    }

    public ViewHelperImpl setImageDrawable(Drawable d) {
        ((ImageView)v).setImageDrawable(d);
        return this;
    }

    public ViewHelperImpl setImageUrl(String url,ViewHelper.IImageLoader loader) {
        loader.load(url, (ImageView) v);
        return this;
    }
    public ViewHelperImpl setImageUrl(String url,RoundedBitmapBuilder builder) {
        builder.url(url).into((ExpandNetworkImageView) v);
        return this;
    }
    public ViewHelperImpl setImageBitmap(Bitmap bitmap) {
        ((ImageView)v).setImageBitmap(bitmap);
        return this;
    }
    public ViewHelperImpl setAlpha(float alpha) {
        ViewCompatUtil.setAlpha(v, alpha);
        return this;
    }
    public ViewHelperImpl linkify() {
        Linkify.addLinks((TextView) v, Linkify.ALL);
        return this;
    }
    /** @see  Linkify#addLinks(TextView, int)  */
    public ViewHelperImpl linkify(int mask) {
        Linkify.addLinks((TextView) v, mask);
        return this;
    }
    public ViewHelperImpl setTypeface(Typeface typeface) {
        TextView view = (TextView) this.v;
        view.setTypeface(typeface);
        view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        return this;
    }
    public ViewHelperImpl setProgress(int progress) {
        ((ProgressBar)v).setProgress(progress);
        return this;
    }
    public ViewHelperImpl setProgress(int progress, int max) {
        ((ProgressBar)v).setProgress(progress);
        ((ProgressBar)v).setMax(max);
        return this;
    }
    public ViewHelperImpl setProgressMax(int max) {
        ((ProgressBar)v).setMax(max);
        return this;
    }
    public ViewHelperImpl setRating(float rating) {
        ((RatingBar)v).setRating(rating);
        return this;
    }
    public ViewHelperImpl setRating(float rating, int max) {
        ((RatingBar)v).setRating(rating);
        ((RatingBar)v).setMax(max);
        return this;
    }
    public ViewHelperImpl setTag(Object tag) {
        v.setTag(tag);
        return this;
    }
    public ViewHelperImpl setTag(int key,Object tag) {
        v.setTag(key, tag);
        return this;
    }
    public ViewHelperImpl setChecked(boolean checked) {
        ((Checkable)v).setChecked(checked);
        return this;
    }
    //======================= listener =========================//

    public ViewHelperImpl setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener l) {
        ((CompoundButton)v).setOnCheckedChangeListener(l);
        return this;
    }
    public ViewHelperImpl setOnClickListener(View.OnClickListener l) {
        v.setOnClickListener(l);
        return this;
    }
    public ViewHelperImpl setOnLongClickListener(View.OnLongClickListener l) {
        v.setOnLongClickListener(l);
        return this;
    }
    public ViewHelperImpl setOnTouchListener(View.OnTouchListener l) {
        v.setOnTouchListener(l);
        return this;
    }
    public ViewHelperImpl setAdapter(Adapter adapter) {
        ((AdapterView)v).setAdapter(adapter);
        return this;
    }

    public ViewHelperImpl setEnable(boolean enable) {
        v.setEnabled(enable);
        return this;
    }

    public ViewHelperImpl setTextSizeDp(float size) {
        ((TextView)v).setTextSize(size);
        return this;
    }
    public ViewHelperImpl setTextSize(float size) {
        ((TextView)v).setTextSize(TypedValue.COMPLEX_UNIT_PX,size);
        return this;
    }
}
