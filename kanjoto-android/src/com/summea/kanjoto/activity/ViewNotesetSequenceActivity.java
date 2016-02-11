
package com.summea.kanjoto.activity;

import java.util.List;

import com.summea.kanjoto.R;
import com.summea.kanjoto.DepthPageTransformer;
import com.summea.kanjoto.data.NotesetsDataSource;
import com.summea.kanjoto.fragment.ViewNotesetSequencePageFragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
    // TODO: make NUM_PAGES more dynamic and less constant
    private static final int NUM_PAGES = 4;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private SharedPreferences sharedPref;
    private long apprenticeId = 0;

    /**
     * onCreate override used to gather data and display using a view pager and a pager adapter.
     * 
     * @param savedInstanceState Current application state data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_view_noteset_sequence);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        apprenticeId = Long.parseLong(sharedPref.getString(
                "pref_selected_apprentice", "1"));

        try {
            NotesetsDataSource nds = new NotesetsDataSource(this);

            // get string version of returned noteset list
            List<String> allNotesetsData = nds.getAllNotesetListPreviews(apprenticeId);
            nds.close();

            // prevent crashes due to lack of database data
            if (allNotesetsData.isEmpty())
                allNotesetsData.add("1234");

            String[] allNotesets = allNotesetsData
                    .toArray(new String[allNotesetsData.size()]);

            // demo data
            String data = allNotesets[0];

            // instantiate a ViewPager and a PagerAdapter
            mPager = (ViewPager) findViewById(R.id.pager);
            mPagerAdapter = new ScreenSlidePagerAdapter(
                    getSupportFragmentManager(), data);
            mPager.setAdapter(mPagerAdapter);

            Bundle bundle = new Bundle();

            // load preferences
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

            // check if touch feedback is enabled
            // pass current setting value to bundle
            // (for later delivery to fragment view)
            boolean pref_touch_feedback_enabled = sharedPref.getBoolean(
                    "pref_touch_feedback_enabled", true);

            if (pref_touch_feedback_enabled) {
                bundle.putString("pref_touch_feedback_enabled", "true");
            } else {
                bundle.putString("pref_touch_feedback_enabled", "false");
            }

            // set page animation
            mPager.setPageTransformer(true, new DepthPageTransformer(bundle));
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    /**
     * onBackPressed override used to page back through previously-viewed noteset pages (until
     * reaching the main menu).
     */
    @Override
    public void onBackPressed() {
        // do a normal "back" action if we are viewing page 0 (first sequence page)
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        }
        // otherwise, use "back" action to go to previous sequence page
        else {
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
         * @param fm <code>FragmentManager</code> object.
         * @param data <code>String</code> value of data to later display.
         */
        public ScreenSlidePagerAdapter(FragmentManager fm, String data) {
            super(fm);
            this.data = data;
        }

        /**
         * getItem override used to get next set of data for the specific page currently being
         * viewed.
         * 
         * @param position <code>int</code> of current page
         */
        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();

            // prepare data to be sent inside bundle for fragment view
            switch (position) {
                case 0:
                    bundle.putString("textforfragment",
                            Character.toString(data.charAt(0)));
                    break;
                case 1:
                    bundle.putString("textforfragment",
                            Character.toString(data.charAt(1)));
                    break;
                case 2:
                    bundle.putString("textforfragment",
                            Character.toString(data.charAt(2)));
                    break;
                case 3:
                    bundle.putString("textforfragment",
                            Character.toString(data.charAt(3)));
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
