package com.andrewsummers.otashu;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.HapticFeedbackConstants;
import android.view.View;

/**
 * DepthPageTransformer used for simple animation.
 * 
 * DepthPageTransformer structure based upon structure explained in this
 * tutorial: http://developer.android.com/training/animation/screen-slide.html
 * 
 * Portions of this page/code are modifications based on work created and shared
 * by the Android Open Source Project and used according to terms described in
 * the Creative Commons 2.5 Attribution License
 */
public class DepthPageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.5f;
    private boolean touchFeedbackEnabled = false;

    /**
     * DepthPageTransformer constructor primarily used to get general
     * application settings in order to use the values of the settings
     * throughout this class.
     * 
     * @param savedInstanceState
     *            Current application state data.
     */
    public DepthPageTransformer(Bundle savedInstanceState) {
        // get (and enable) current app settings
        if (savedInstanceState.getString("touchFeedbackEnabled").equals("true")) {
            touchFeedbackEnabled = true;
        }
    }

    /**
     * transformPage used to apply a custom transformation (animation) to page
     * view scrolling.
     * 
     * @param view
     *            Incoming view.
     * @param position
     *            Current position of page relative to current
     *            "front and center" page. As per the official ViewPager
     *            documentation, possible values are: -1 is one page position to
     *            the left 0 is front and center. 1 is one page position to the
     *            right
     */
    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();

        // if application touch feedback setting is set, enable touch feedback
        if (touchFeedbackEnabled) {
            view.setHapticFeedbackEnabled(true);
            view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY,
                    HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
        }

        // current page setting: [-Infinity, -1)
        if (position < -1) {
            // this page is way off-screen to the left
            view.setAlpha(0);
        }
        // current page setting: [-1, 0]
        else if (position <= 0) {
            // use the default slide transition when moving to the left page
            view.setAlpha(1);
            // view.setHapticFeedbackEnabled(hapticFeedbackEnabled);
            view.setTranslationY(pageWidth * position);
            view.setScaleX(1);
            view.setScaleY(1);
        }
        // current page setting: (0, 1)
        else if (position <= 1) {
            // fade page out
            view.setAlpha(1 - position);

            // counteract the default slide transition
            view.setTranslationY(pageWidth * -position);

            // scale the page down (between MIN_SCALE and 1)
            float scaleFactor = MIN_SCALE + (1 - MIN_SCALE)
                    * (1 - Math.abs(position));
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
        }
        // current page setting: (1, +Infinity]
        else {
            // this page is way off-screen to the right
            view.setAlpha(0);
        }
    }
}