
package summea.kanjoto.activity;

import java.util.List;

import summea.kanjoto.data.EmotionsDataSource;
import summea.kanjoto.data.KanjotoDatabaseHelper;
import summea.kanjoto.model.Emotion;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

import summea.kanjoto.R;

public class DatabaseDumperEmotionsActivity extends Activity {
    private SharedPreferences sharedPref;
    private long apprenticeId = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_database_dumper);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        apprenticeId = Long.parseLong(sharedPref.getString(
                "pref_selected_apprentice", "1"));

        EmotionsDataSource eds = new EmotionsDataSource(this);
        List<Emotion> allEmotions = eds.getAllEmotions(apprenticeId);
        eds.close();

        TextView debugText = (TextView) findViewById(R.id.debug_text);

        debugText.setText(debugText.getText().toString() + "Table: Emotions\n"
                + KanjotoDatabaseHelper.COLUMN_ID + "|" + KanjotoDatabaseHelper.COLUMN_NAME + "|"
                + KanjotoDatabaseHelper.COLUMN_LABEL_ID + "|"
                + KanjotoDatabaseHelper.COLUMN_APPRENTICE_ID + "\n");

        for (Emotion emotion : allEmotions) {

            String newText = debugText.getText().toString();
            newText += emotion.getId() + "|" + emotion.getName() + "|" + emotion.getLabelId()
                    + "|" + emotion.getApprenticeId() + "\n";

            debugText.setText(newText);
        }
    }
}
