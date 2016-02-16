
package summea.kanjoto.activity;

import summea.kanjoto.data.KanjotoDatabaseHelper;

import summea.kanjoto.R;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * DatabaseDumperActivity is an Activity which provides a dump of database data.
 * <p>
 * Directly working with database files on Android seems to be surprisingly difficult. Until a
 * better way is found, the Database Dumper allows for data from the database to be dumped to a page
 * for debug and informational purposes.
 * </p>
 */
public class DatabaseDumperActivity extends ListActivity {

    static final String[] database_tables = new String[] {
            KanjotoDatabaseHelper.TABLE_ACHIEVEMENTS,
            KanjotoDatabaseHelper.TABLE_APPRENTICES,
            KanjotoDatabaseHelper.TABLE_APPRENTICE_SCORECARDS,
            KanjotoDatabaseHelper.TABLE_APPRENTICE_SCORES,
            KanjotoDatabaseHelper.TABLE_BOOKMARKS,
            KanjotoDatabaseHelper.TABLE_EDGES,
            KanjotoDatabaseHelper.TABLE_EMOTIONS,
            KanjotoDatabaseHelper.TABLE_GRAPHS,
            KanjotoDatabaseHelper.TABLE_KEY_SIGNATURES,
            KanjotoDatabaseHelper.TABLE_KEY_NOTES,
            KanjotoDatabaseHelper.TABLE_LABELS,
            KanjotoDatabaseHelper.TABLE_LEARNING_STYLES,
            KanjotoDatabaseHelper.TABLE_NOTES,
            KanjotoDatabaseHelper.TABLE_NOTESETS,
            KanjotoDatabaseHelper.TABLE_NOTEVALUES,
            KanjotoDatabaseHelper.TABLE_PATHS,
            KanjotoDatabaseHelper.TABLE_PATH_EDGES,
            KanjotoDatabaseHelper.TABLE_VERTICES,
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new ArrayAdapter<String>(this, R.layout.list_database_table, database_tables));

        ListView listView = getListView();

        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = null;
                switch (position) {
                    case 0:
                        intent = new Intent(DatabaseDumperActivity.this,
                                DatabaseDumperAchievementsActivity.class);
                        break;
                    case 1:
                        intent = new Intent(DatabaseDumperActivity.this,
                                DatabaseDumperApprenticesActivity.class);
                        break;
                    case 2:
                        intent = new Intent(DatabaseDumperActivity.this,
                                DatabaseDumperApprenticeScorecardsActivity.class);
                        break;
                    case 3:
                        intent = new Intent(DatabaseDumperActivity.this,
                                DatabaseDumperApprenticeScoresActivity.class);
                        break;
                    case 4:
                        intent = new Intent(DatabaseDumperActivity.this,
                                DatabaseDumperBookmarksActivity.class);
                        break;
                    case 5:
                        intent = new Intent(DatabaseDumperActivity.this,
                                DatabaseDumperEdgesActivity.class);
                        break;
                    case 6:
                        intent = new Intent(DatabaseDumperActivity.this,
                                DatabaseDumperEmotionsActivity.class);
                        break;
                    case 7:
                        intent = new Intent(DatabaseDumperActivity.this,
                                DatabaseDumperGraphsActivity.class);
                        break;
                    case 8:
                        intent = new Intent(DatabaseDumperActivity.this,
                                DatabaseDumperKeySignaturesActivity.class);
                        break;
                    case 9:
                        intent = new Intent(DatabaseDumperActivity.this,
                                DatabaseDumperKeyNotesActivity.class);
                        break;
                    case 10:
                        intent = new Intent(DatabaseDumperActivity.this,
                                DatabaseDumperLabelsActivity.class);
                        break;
                    case 11:
                        intent = new Intent(DatabaseDumperActivity.this,
                                DatabaseDumperLearningStylesActivity.class);
                        break;
                    case 12:
                        intent = new Intent(DatabaseDumperActivity.this,
                                DatabaseDumperNotesActivity.class);
                        break;
                    case 13:
                        intent = new Intent(DatabaseDumperActivity.this,
                                DatabaseDumperNotesetsActivity.class);
                        break;
                    case 14:
                        intent = new Intent(DatabaseDumperActivity.this,
                                DatabaseDumperNotevaluesActivity.class);
                        break;
                    case 15:
                        intent = new Intent(DatabaseDumperActivity.this,
                                DatabaseDumperPathsActivity.class);
                        break;
                    case 16:
                        intent = new Intent(DatabaseDumperActivity.this,
                                DatabaseDumperPathEdgesActivity.class);
                        break;
                    case 17:
                        intent = new Intent(DatabaseDumperActivity.this,
                                DatabaseDumperVerticesActivity.class);
                        break;
                }

                if (intent != null) {
                    startActivity(intent);
                }
            }
        });
    }
}
