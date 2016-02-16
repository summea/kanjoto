
package summea.kanjoto.activity;

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

import summea.kanjoto.data.NotesetsDataSource;
import summea.kanjoto.model.Noteset;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

/**
 * ExportDatabaseActivity is an Activity which provides users the ability to export the database.
 * <p>
 * This activity provides a way to export the program database to a file. This allows for database
 * backups and historical database archives that can later be imported back into the program if
 * desired.
 * </p>
 */
public class ExportDatabaseActivity extends Activity {
    private SharedPreferences sharedPref;
    private long apprenticeId = 0;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        apprenticeId = Long.parseLong(sharedPref.getString(
                "pref_selected_apprentice", "1"));
        
        // make sure destination directory exists
        File path = Environment.getExternalStorageDirectory();
        String newDirectory = path.toString() + "/kanjoto/";
        File newFile = new File(newDirectory);

        if (!newFile.exists()) {
            newFile.mkdirs();
        }

        File externalStorage = Environment.getExternalStorageDirectory();

        if (externalStorage.canWrite()) {
            Context context = this;
            File currentDB = context.getDatabasePath("kanjoto_collection.db");
            File backupDB = new File(newFile, "kanjoto_collection_backup.db");

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
            List<Noteset> allNotesets = nsds.getAllNotesets(apprenticeId);
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

            // TODO: address certain cases
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

        // close activity
        finish();
    }

}
