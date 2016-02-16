
package summea.kanjoto.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.widget.Toast;

/**
 * ImportDatabaseActivity is an Activity which provides users the ability to import a database.
 * <p>
 * This activity provides a way to import a program database back into the program. Note: existing
 * database will be overwritten if another database is imported. Valid database format is required
 * for import to be used successfully; a valid database format can be found by using the
 * ExportDatabaseActivity.
 * </p>
 */
public class ImportDatabaseActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // load preferences
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String importDatabaseLocationText = sharedPref.getString("pref_import_database_location",
                "");

        File externalStorage = Environment.getExternalStorageDirectory();

        if (externalStorage.canWrite()) {
            Context context = this;
            File backupDB = new File(importDatabaseLocationText);
            File currentDB = context.getDatabasePath("kanjoto_collection.db");

            if (backupDB.exists()) {
                FileChannel src = null;
                FileChannel dst = null;

                try {
                    src = new FileInputStream(backupDB).getChannel();
                    dst = new FileOutputStream(currentDB).getChannel();

                    // copy source file to destination
                    dst.transferFrom(src, 0, src.size());
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        src.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        dst.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(getBaseContext(), "Database imported!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }

        // close activity
        finish();
    }
}
