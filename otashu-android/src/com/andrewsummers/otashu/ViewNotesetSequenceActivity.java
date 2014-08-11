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

/**
 * Noteset sequence view used to page through values of a given noteset.
 */
public class ViewNotesetSequenceActivity extends FragmentActivity {

	private static final int NUM_PAGES = 4;
	private ViewPager mPager;
	private PagerAdapter mPagerAdapter;
	
	/**
	 * onCreate override used to gather data and display
	 * using a view pager and a pager adapter.
	 * 
	 * @param savedInstanceState	Current application state data.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_noteset_sequence);
		
		try {
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
		} catch (Exception e) {
			e.getStackTrace();
		}
	}
	
	/**
	 * onBackPressed override used to page back through previously-viewed
	 * noteset pages (until reaching the main menu).
	 */
	@Override
	public void onBackPressed() {
		if (mPager.getCurrentItem() == 0) {
			super.onBackPressed();
		} else {
			mPager.setCurrentItem(mPager.getCurrentItem() - 1);
		}
	}
	
	/**
	 * Custom pager adapter that builds upon FragmentStatePagerAdapter.
	 */
	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

		private String data = null;
		
		/**
		 * ScreenSlidePagerAdapter constructor.
		 * 
		 * @param fm	<code>FragmentManager</code> object.
		 * @param data	<code>String</code> value of data to later display.
		 */
		public ScreenSlidePagerAdapter(FragmentManager fm, String data) {
			super(fm);
			this.data = data;
		}
		
		/**
		 * getItem override used to get next set of data for the specific page
		 * currently being viewed.
		 * 
		 * @param position	<code>int</code> of current page
		 */
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
			// save data to send along to fragment inside bundle
			sspf.setArguments(bundle);
			return sspf;
		}
		
		/**
		 * getCount override used to get total number of pages.
		 */
		@Override
		public int getCount() {
			return NUM_PAGES;
		}
	}
}