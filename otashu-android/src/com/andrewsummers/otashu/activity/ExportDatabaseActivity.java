
package com.andrewsummers.otashu.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.andrewsummers.otashu.data.NotesetsDataSource;
import com.andrewsummers.otashu.model.Noteset;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

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

            // get JSON folder path
            File jsonFolder = new File(Environment.getExternalStorageDirectory(), "json_folder");

            // create JSON folder if necessary
            if (!jsonFolder.exists()) {
                jsonFolder.mkdirs();
            }

            // TODO: possibly relocate this JSON part later on...
            // export JSON test
            // get all notesets from database
            NotesetsDataSource nsds = new NotesetsDataSource(this);
            List<Noteset> allNotesets = nsds.getAllNotesets();
            nsds.close();

            JSONObject mainJsonObj = new JSONObject();
            JSONArray jsonArr = new JSONArray();

            try {
                // add each noteset into JSON object
                for (Noteset noteset : allNotesets) {
                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put("id", noteset.getId());
                    jsonObj.put("emotion_id", noteset.getEmotion());
                    jsonArr.put(jsonObj);
                }

                mainJsonObj.put("notesets", jsonArr);

                // save JSON string to file
                FileOutputStream outputStream = new FileOutputStream(new File(jsonFolder,
                        "json_export.txt"));
                outputStream.write(mainJsonObj.toString().getBytes());
                outputStream.close();

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (currentDB.exists()) {
                Log.d("MYLOG", "current database exists!");
            }

            if (backupDB.exists()) {
                Log.d("MYLOG", "new file exists!");
            }

            FileChannel src = null;
            FileChannel dst = null;

            try {
                src = new FileInputStream(currentDB).getChannel();
                dst = new FileOutputStream(backupDB).getChannel();

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

                Toast.makeText(getBaseContext(), "Database exported!",
                        Toast.LENGTH_SHORT).show();
            }
        }

        // return to main menu
        finish();
    }

}
