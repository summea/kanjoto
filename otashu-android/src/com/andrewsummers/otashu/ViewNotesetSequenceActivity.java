package com.andrewsummers.otashu;

import java.util.List;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

public class ViewNotesetSequenceActivity extends FragmentActivity {

	private static final int NUM_PAGES = 4;
	private ViewPager mPager;
	private PagerAdapter mPagerAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_noteset_sequence);
		
		NotesetCollectionOpenHelper db = new NotesetCollectionOpenHelper(this);
		
		List<String> allNotesetsData = db.getAllNotesetListPreviews();
		String[] allNotesets = allNotesetsData.toArray(new String[allNotesetsData.size()]);
		
		// demo data
		String data = allNotesets[0];
		
		// instantiate a ViewPager and a PagerAdapter
		mPager = (ViewPager)findViewById(R.id.pager);
		mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), data);
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

		private String data = null;
		
		public ScreenSlidePagerAdapter(FragmentManager fm, String data) {
			super(fm);
			this.data = data;
		}
		
		@Override
		public Fragment getItem(int position) {
			
			Bundle bundle = new Bundle();
			
			switch (position) {
				case 0:
					bundle.putString("textforfragment", Character.toString(data.charAt(0)));
					break;
				case 1:
					bundle.putString("textforfragment", Character.toString(data.charAt(1)));
					break;
				case 2:
					bundle.putString("textforfragment", Character.toString(data.charAt(2)));
					break;
				case 3:
					bundle.putString("textforfragment", Character.toString(data.charAt(3)));
					break;
			}
			
			ViewNotesetSequencePageFragment sspf = new ViewNotesetSequencePageFragment();
			sspf.setArguments(bundle);
			return sspf;
		}
		
		@Override
		public int getCount() {
			return NUM_PAGES;
		}
	}
}