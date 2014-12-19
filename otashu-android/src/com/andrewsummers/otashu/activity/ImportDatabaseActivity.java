
package com.andrewsummers.otashu.activity;

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
import android.util.Log;
import android.widget.Toast;

public class ImportDatabaseActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // load preferences
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String importDatabaseLocationText = sharedPref.getString("pref_import_database_location",
                "");

        Log.d("MYLOG", "importing: " + importDatabaseLocationText);

        File externalStorage = Environment.getExternalStorageDirectory();

        if (externalStorage.canWrite()) {
            Context context = this;
            File backupDB = new File(importDatabaseLocationText);
            File currentDB = context.getDatabasePath("otashu_collection.db");

            Log.d("MYLOG", "backupDB: " + backupDB.toString());
            Log.d("MYLOG", "currentDB: " + currentDB.toString());

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

        // return to main menu
        finish();
    }

}
