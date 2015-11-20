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
package com.heaven7.databinding.util;

import android.content.Context;

public class ResourceUtil {

	/** if you use shard uid(eg：  android:sharedUserId="com.heaven7.skin")，
	 * package name must be target package name*/
	public static int getRemoteResId(Context ctx,String packageName,String resName,ResourceType type){
		return ctx.getResources().getIdentifier(resName, type.name,packageName);
	}
	public static int getResId(Context ctx,String resName,ResourceType type){
		return ctx.getResources().getIdentifier(resName, type.name,ctx.getPackageName());
	}

	public enum ResourceType{
		Layout("layout"),Id("id"),Style("style"),String("string"),
		Drawable("drawable"),Color("color"),Dimen("dimen"),
		Raw("raw"),StringArray("array"),Anim("anim"),Menu("menu"),
		Animator("animator"),Interpolator("interpolator"),Xml("xml"),
		Transition("transition")
		;
		
		public final String name;
		ResourceType(String name) {
			this.name = name;
		}
	}
}
