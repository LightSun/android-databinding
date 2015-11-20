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

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.animation.AlphaAnimation;

public class ViewCompatUtil {

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static void setBackgroundCompatible(View v, Drawable d) {
		if (Build.VERSION.SDK_INT >= 16)
			v.setBackground(d);
		else
			v.setBackgroundDrawable(d);
	}

	public static void setAlpha(View v, float value) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			v.setAlpha(value);
		} else {
			// Pre-honeycomb hack to set Alpha value
			AlphaAnimation alpha = new AlphaAnimation(value, value);
			alpha.setDuration(0);
			alpha.setFillAfter(true);
			v.startAnimation(alpha);
		}
	}
}
