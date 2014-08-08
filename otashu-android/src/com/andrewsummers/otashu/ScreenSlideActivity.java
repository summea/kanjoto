package com.andrewsummers.otashu;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

public class ScreenSlideActivity extends FragmentActivity {

	private static final int NUM_PAGES = 4;
	private ViewPager mPager;
	private PagerAdapter mPagerAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_screen_slide);
		
		// instantiate a ViewPager and a PagerAdapter
		mPager = (ViewPager)findViewById(R.id.pager);
		mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mPagerAdapter);
		
		Bundle bundle = new Bundle();
		
		String settingsFileName = getResources().getString(R.string.settings_file_name);
		SharedPreferences settings = getSharedPreferences(settingsFileName, 0);
		boolean touchFeedbackEnabled = settings.getBoolean("touchFeedbackEnabled", true);
		
		if (touchFeedbackEnabled) {
			bundle.putString("touchFeedbackEnabled", "true");
		} else {
			bundle.putString("touchFeedbackEnabled", "false");
		}
		
		mPager.setPageTransformer(true, new DepthPageTransformer(bundle));
	}
	
	@Override
	public void onBackPressed() {
		if (mPager.getCurrentItem() == 0) {
			super.onBackPressed();
		} else {
			mPager.setCurrentItem(mPager.getCurrentItem() - 1);
		}
	}
	
	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

		public ScreenSlidePagerAdapter(FragmentManager fm) {
			super(fm);
		}
		
		@Override
		public Fragment getItem(int position) {
			
			Bundle bundle = new Bundle();
			
			switch (position) {
				case 0:
					bundle.putString("textforfragment", "this is some text for fragment1");
					break;
				case 1:
					bundle.putString("textforfragment", "this is some text for fragment2");
					break;
				case 2:
					bundle.putString("textforfragment", "this is some text for fragment3");
					break;
				case 3:
					bundle.putString("textforfragment", "this is some text for fragment4");
					break;
			}
			
			ScreenSlidePageFragment sspf = new ScreenSlidePageFragment();
			sspf.setArguments(bundle);
			return sspf;
		}
		
		@Override
		public int getCount() {
			return NUM_PAGES;
		}
	}
}