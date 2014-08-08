package com.andrewsummers.otashu;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.HapticFeedbackConstants;
import android.view.View;

/* DepthPageTransformer used from http://developer.android.com/training/animation/screen-slide.html training
 * Portions of this page/code are modifications based on work created and shared by the
 * Android Open Source Project and used according to terms described in the
 * Creative Commons 2.5 Attribution License */

public class DepthPageTransformer implements ViewPager.PageTransformer {

	private static final float MIN_SCALE = 0.5f;
	private boolean touchFeedbackEnabled = false;
	
	public DepthPageTransformer(Bundle savedInstanceState) {
		if (savedInstanceState.getString("touchFeedbackEnabled").equals("true")) {
			touchFeedbackEnabled = true;
		}
	}
	
	public void transformPage(View view, float position) {	
		int pageWidth = view.getWidth();
		
		if (touchFeedbackEnabled) {
			view.setHapticFeedbackEnabled(true);
			view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
		}
		
		if (position < -1) { // [-Infinity, -1)
			// this page is way off-screen to the left
			view.setAlpha(0);
		} else if (position <= 0) { // [-1, 0]
			// use the default slide transition when moving to the left page
			view.setAlpha(1);
			//view.setHapticFeedbackEnabled(hapticFeedbackEnabled);
			view.setTranslationY(pageWidth * position);
			view.setScaleX(1);
			view.setScaleY(1);
		} else if (position <= 1) { // (0, 1)
			// fade page out
			view.setAlpha(1 - position);
			
			// counteract the default slide transition
			view.setTranslationY(pageWidth * -position);
			
			// scale the page down (between MIN_SCALE and 1)
			float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
			view.setScaleX(scaleFactor);
			view.setScaleY(scaleFactor);
		} else { // (1, +Infinity]
			// this page is way off-screen to the right
			view.setAlpha(0);
		}
	}
}
