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

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextWatcher;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;

import com.android.volley.data.RequestManager;
import com.android.volley.extra.ExpandNetworkImageView;
import com.android.volley.extra.RoundedBitmapBuilder;

/**
 * for better use same view's mMethod. cached it automatic for reuse.
 * @author heaven7
 *
 */
public class ViewHelper {

	private final SparseArray<View> mViewMap;
	private final ViewHelperImpl mImpl;
	private final View mRootView;
	private LayoutInflater mInflater;


	/**
	 * the loader to load image
	 * @author heaven7
	 */
	public interface IImageLoader{
		
		  void load(String url, ImageView iv);
	}
	
	public ViewHelper(View root) {
		this.mRootView = root;
		mInflater = LayoutInflater.from(root.getContext());
		mViewMap = new SparseArray<View>();
		mImpl = new ViewHelperImpl(null);
	}
	public ViewHelper setRootOnClickListener(View.OnClickListener l){
		mRootView.setOnClickListener(l);
		return this;
	}
	public View getRootView() {
		return mRootView;
	}
	public Context getContext(){
		return mRootView.getContext();
	}
	public Resources getResources(){
		return getContext().getResources();
	}
	public LayoutInflater getLayoutInflater(){
		return mInflater;
	}

	public void clearCache(){
		mViewMap.clear();
	}

	public ViewHelper addTextChangedListener(int viewId ,TextWatcher watcher){
		return view(viewId).addTextChangedListener(watcher).reverse(this);
	}

	public ViewHelper setText(int viewId,CharSequence text){
		return view(viewId).setText(text).reverse(this);
	}

	public ViewHelper setEnable(int viewId, boolean enable) {
		return view(viewId).setEnable(enable).reverse(this);
	}

	public ViewHelper setTextSizeDp(int viewId, float size) {
		return view(viewId).setTextSizeDp(size).reverse(this);
	}
	public ViewHelper setTextSize(int viewId, float size) {
		return view(viewId).setTextSize(size).reverse(this);
	}
	/**
	 * toogle the visibility of the view.such as: VISIBLE to gone or gone to VISIBLE
	 * @param viewId  the id of view
	 */
	public ViewHelper toogleVisibility(int viewId){
		return view(viewId).toogleVisibility().reverse(this);
	}

	/** get the view in current layout . return null if the viewid can't find in current layout*/
	@SuppressWarnings("unchecked")
	public <T extends View > T getView(int viewId) {
		View view = mViewMap.get(viewId);
		if(view == null){
			view = mRootView.findViewById(viewId);
			if(view ==null)
				throw new IllegalStateException("can't find the view ,id = " +viewId);
			mViewMap.put(viewId, view);
		}
		return (T) view;
	}
	/**
	 * Will set the image of an ImageView from a resource id.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param imageResId
	 *            The image resource id.
	 * @return The ViewHelper for chaining.
	 */
	public ViewHelper setImageResource(int viewId, int imageResId) {
		return  view(viewId).setImageResource(imageResId).reverse(this);
	}
	/**
	 * Will set background color of a view.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param color
	 *            A color, not a resource id.
	 * @return The ViewHelper for chaining.
	 */
	public ViewHelper setBackgroundColor(int viewId, int color) {
		return view(viewId).setBackgroundColor(color).reverse(this);
	}

	/**
	 * Will set background of a view.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param backgroundRes
	 *            A resource to use as a background. 0 to remove it.
	 * @return The ViewHelper for chaining.
	 */
	public ViewHelper setBackgroundRes(int viewId, int backgroundRes) {
		return view(viewId).setBackgroundRes(backgroundRes).reverse(this);
	}
	public ViewHelper setBackgroundDrawable(int viewId,Drawable drawable){
		return view(viewId).setBackgroundDrawable(drawable).reverse(this);
	}
	/**
	 * Will set text color of a TextView.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param textColor
	 *            The text color (not a resource id).
	 * @return The ViewHelper for chaining.
	 */
	public ViewHelper setTextColor(int viewId, int textColor) {
		return view(viewId).setTextColor(textColor).reverse(this);
	}
	public ViewHelper setTextColor(int viewId, ColorStateList colors) {
		return view(viewId).setTextColor(colors).reverse(this);
	}
	/**
	 * Will set text color of a TextView.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param textColorRes
	 *            The text color resource id.
	 * @return The ViewHelper for chaining.
	 */
	public ViewHelper setTextColorRes(int viewId, int textColorRes) {
		return view(viewId).setTextColorRes(textColorRes).reverse(this);
	}
	
	/**
	 * Will set the image of an ImageView from a drawable.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param drawable
	 *            The image drawable.
	 * @return The ViewHelper for chaining.
	 */
	public ViewHelper setImageDrawable(int viewId, Drawable drawable) {
		return view(viewId).setImageDrawable(drawable).reverse(this);
	}

	/**
	 * Will download an image from a URL and put it in an ImageView.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param imageUrl
	 *            The image URL.
	 * @param loader 
	 *             which to load image actually.
	 * @return The ViewHelper for chaining.
	 */
	public ViewHelper setImageUrl(int viewId, String imageUrl,IImageLoader loader) {
		return view(viewId).setImageUrl(imageUrl, loader).reverse(this);
	}
	/** use volley extra to load image（this support circle image and round）.
	 * <li>@Note RequestManager must call {@link RequestManager#init(Context)} before this.
	 * such as in {@link Application#onCreate()}
	 * @param builder  image param builder to control what bitmap to show!
	 * @param viewId must be {@link ExpandNetworkImageView}
	 * */
	public ViewHelper setImageUrl(int viewId,String url,RoundedBitmapBuilder builder){
		return view(viewId).setImageUrl(url,builder).reverse(this);
	}
	
	/**
	 * Add an action to set the image of an image view. Can be called multiple
	 * times.
	 */
	public ViewHelper setImageBitmap(int viewId, Bitmap bitmap) {
		return view(viewId).setImageBitmap(bitmap).reverse(this);
	}

	/**
	 * Add an action to set the alpha of a view. Can be called multiple times.
	 * Alpha between 0-1.
	 */
	@SuppressLint("NewApi")
	public ViewHelper setAlpha(int viewId, float value) {
		return view(viewId).setAlpha(value).reverse(this);
	}
	

	/**
	 * Set a view visibility to VISIBLE (true) or GONE (false).
	 * 
	 * @param viewId
	 *            The view id.
	 * @param visible
	 *            True for VISIBLE, false for GONE.
	 * @return The ViewHelper for chaining.
	 */
	public ViewHelper setVisibility(int viewId, boolean visible) {
		return view(viewId).setVisibility(visible).reverse(this);
	}
	public ViewHelper setVisibility(int viewId, int visibility) {
		return view(viewId).setVisibility(visibility).reverse(this);
	}

	
	/**
	 * Add links into a TextView. default is 
	 * 
	 * @param viewId
	 *            The id of the TextView to linkify.
	 * @return The ViewHelper for chaining.
	 */
	public ViewHelper linkify(int viewId) {
		return view(viewId).linkify().reverse(this);
	}
	private <T extends View> T retrieveView(int viewId) {
		return getView(viewId);
	}

	/** Add links into a TextView,
	 * @param  linkifyMask ,see {@link Linkify#ALL} and etc.*/
	public ViewHelper linkify(int viewId,int linkifyMask) {
		return view(viewId).linkify(linkifyMask).reverse(this);
	}

	/** Apply the typeface to the given viewId, and enable subpixel rendering. */
	public ViewHelper setTypeface(int viewId, Typeface typeface) {
		return view(viewId).setTypeface(typeface).reverse(this);
	}

	/**
	 * Apply the typeface to all the given viewIds, and enable subpixel
	 * rendering.
	 */
	public ViewHelper setTypeface(Typeface typeface, int... viewIds) {
		for (int viewId : viewIds) {
			setTypeface(viewId,typeface);
		}
		return this;
	}
	
	/**
	 * Sets the progress of a ProgressBar.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param progress
	 *            The progress.
	 * @return The ViewHelper for chaining.
	 */
	public ViewHelper setProgress(int viewId, int progress) {
		return view(viewId).setProgress(progress).reverse(this);
	}

	/**
	 * Sets the progress and max of a ProgressBar.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param progress
	 *            The progress.
	 * @param max
	 *            The max value of a ProgressBar.
	 * @return The ViewHelper for chaining.
	 */
	public ViewHelper setProgress(int viewId, int progress, int max) {
		return view(viewId).setProgress(progress,max).reverse(this);
	}

	/**
	 * Sets the range of a ProgressBar to 0...max.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param max
	 *            The max value of a ProgressBar.
	 * @return The ViewHelper for chaining.
	 */
	public ViewHelper setProgressMax(int viewId, int max) {
		return view(viewId).setProgressMax(max).reverse(this);
	}

	/**
	 * Sets the rating (the number of stars filled) of a RatingBar.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param rating
	 *            The rating.
	 * @return The ViewHelper for chaining.
	 */
	public ViewHelper setRating(int viewId, float rating) {
		return view(viewId).setRating(rating).reverse(this);
	}

	/**
	 * Sets the rating (the number of stars filled) and max of a RatingBar.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param rating
	 *            The rating.
	 * @param max
	 *            The range of the RatingBar to 0...max.
	 * @return The ViewHelper for chaining.
	 */
	public ViewHelper setRating(int viewId, float rating, int max) {
		return view(viewId).setRating(rating,max).reverse(this);
	}

	/**
	 * Sets the tag of the view.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param tag
	 *            The tag;
	 * @return The ViewHelper for chaining.
	 */
	public ViewHelper setTag(int viewId, Object tag) {
		return view(viewId).setTag(tag).reverse(this);
	}

	/**
	 * Sets the tag of the view.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param key
	 *            The value of tag;
	 * @param tag
	 *            The tag;
	 * @return The ViewHelper for chaining.
	 */
	public ViewHelper setTag(int viewId, int key, Object tag) {
		return view(viewId).setTag(key,tag).reverse(this);
	}

	/**
	 * Sets the checked status of a checkable.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param checked
	 *            The checked status;
	 * @return The ViewHelper for chaining.
	 */
	public ViewHelper setChecked(int viewId, boolean checked) {
		return view(viewId).setChecked(checked).reverse(this);
	}
	
	/** set OnCheckedChangeListener to CompoundButton or it's children. */
	public ViewHelper setOnCheckedChangeListener(int viewId, OnCheckedChangeListener l){
		return view(viewId).setOnCheckedChangeListener(l).reverse(this);
	}

	/**
	 * Sets the adapter of a adapter view.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param adapter
	 *            The adapter;
	 * @return The ViewHelper for chaining.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ViewHelper setAdapter(int viewId, Adapter adapter) {
		return view(viewId).setAdapter(adapter).reverse(this);
	}

	/**
	 * Sets the on click listener of the view.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param listener
	 *            The on click listener;
	 * @return The ViewHelper for chaining.
	 */
	public ViewHelper setOnClickListener(int viewId,
			View.OnClickListener listener) {
		return view(viewId).setOnClickListener(listener).reverse(this);
	}

	/**
	 * Sets the on touch listener of the view.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param listener
	 *            The on touch listener;
	 * @return The ViewHelper for chaining.
	 */
	public ViewHelper setOnTouchListener(int viewId,
			View.OnTouchListener listener) {
		return view(viewId).setOnTouchListener(listener).reverse(this);
	}

	/**
	 * Sets the on long click listener of the view.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param listener
	 *            The on long click listener;
	 * @return The ViewHelper for chaining.
	 */
	public ViewHelper setOnLongClickListener(int viewId,
			View.OnLongClickListener listener) {
		return view(viewId).setOnLongClickListener(listener).reverse(this);
	}

	/***
	 *  view the target,after call this you can call multi setXXX methods on the target view.
	 *  such as: <pre>
	 *      ViewHelper.view(xx).setText("sss").setOnclickListener(new OnClickListener()...);
	 *  </pre>
	 * @param viewId the id of target view
	 * @return  ViewHelperImpl
	 */
	public ViewHelperImpl view(int viewId){
		return mImpl.view(getView(viewId));
	}
}
