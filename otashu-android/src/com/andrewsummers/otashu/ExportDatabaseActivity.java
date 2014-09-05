package com.andrewsummers.otashu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

public class ExportDatabaseActivity extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // make sure destination directory exists
        File path = Environment.getExternalStorageDirectory();
        String newDirectory = path.toString() + "/otashu/";
        File newFile = new File(newDirectory);
        if (!newFile.exists()) {
            newFile.mkdirs();
        }
        
        File externalStorage = Environment.getExternalStorageDirectory();
        
        if (externalStorage.canWrite()) {
            Context context = this;
            File currentDB = context.getDatabasePath("otashu_collection.db");
            File backupDB = new File(newFile, "otashu_collection_backup.db");
            
            if (currentDB.exists()) {
                Log.d("MYLOG", "current database exists!");
            }
            
            if (backupDB.exists()) {
                Log.d("MYLOG", "new file exists!");
            }
            
            Log.d("MYLOG", currentDB.toString());
            Log.d("MYLOG", backupDB.toString());
            
            FileChannel src = null;
            FileChannel dst = null;
            
            try {
                src = new FileInputStream(currentDB).getChannel();
                dst = new FileOutputStream(backupDB).getChannel();
                
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

        // return to main menu
        finish();
    }
    
}
