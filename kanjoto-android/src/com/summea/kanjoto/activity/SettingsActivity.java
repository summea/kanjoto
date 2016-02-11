
package com.summea.kanjoto.activity;

import com.summea.kanjoto.fragment.SettingsFragment;

import android.app.Activity;
import android.os.Bundle;

/**
 * Settings are general settings for the overall application.
 * <p>
 * Settings uses a fragment-based approach. As such, the majority of the settings logic is filed
 * away in the SettingsFragment class. The rest of the settings functionality comes from the Android
 * Preference class.
 * </p>
 */
public class SettingsActivity extends Activity {
    /**
     * onCreate override used to restore existing settings when viewing Settings activity.
     * 
     * @param savedInstanceState Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // display fragment as main content
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment()).commit();
    }
}
