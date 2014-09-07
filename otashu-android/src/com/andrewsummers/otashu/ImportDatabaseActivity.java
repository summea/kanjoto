package com.andrewsummers.otashu;

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
import android.util.Log;

public class ImportDatabaseActivity extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String settingsFileName = getResources().getString(
                R.string.settings_file_name);
        SharedPreferences settings = getSharedPreferences(settingsFileName,
                0);
        String importDatabaseLocationText = settings.getString("importDatabaseLocation", "");  
        
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
                }
            }
        }

        // return to main menu
        finish();
    }
    
}