
package summea.kanjoto.activity;

import java.util.List;

import summea.kanjoto.data.KanjotoDatabaseHelper;
import summea.kanjoto.data.LabelsDataSource;
import summea.kanjoto.model.Label;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import summea.kanjoto.R;

public class DatabaseDumperLabelsActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_database_dumper);

        LabelsDataSource lds = new LabelsDataSource(this);
        List<Label> allLabels = lds.getAllLabels();
        lds.close();

        TextView debugText = (TextView) findViewById(R.id.debug_text);

        debugText.setText(debugText.getText().toString() + "Table: Labels\n"
                + KanjotoDatabaseHelper.COLUMN_ID + "|" + KanjotoDatabaseHelper.COLUMN_NAME + "|"
                + KanjotoDatabaseHelper.COLUMN_COLOR + "\n");

        for (Label label : allLabels) {

            String newText = debugText.getText().toString();
            newText += label.getId() + "|" + label.getName() + "|" + label.getColor() + "\n";

            debugText.setText(newText);
        }
    }
}
