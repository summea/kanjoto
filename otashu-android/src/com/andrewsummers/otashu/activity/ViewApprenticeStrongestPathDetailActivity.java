
package com.andrewsummers.otashu.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.EdgesDataSource;
import com.andrewsummers.otashu.data.EmotionsDataSource;
import com.andrewsummers.otashu.data.LabelsDataSource;
import com.andrewsummers.otashu.data.NotevaluesDataSource;
import com.andrewsummers.otashu.data.PathEdgesDataSource;
import com.andrewsummers.otashu.data.PathsDataSource;
import com.andrewsummers.otashu.model.Edge;
import com.andrewsummers.otashu.model.Emotion;
import com.andrewsummers.otashu.model.Label;
import com.andrewsummers.otashu.model.Note;
import com.andrewsummers.otashu.model.Notevalue;
import com.andrewsummers.otashu.model.Path;
import com.andrewsummers.otashu.model.PathEdge;

/**
 * View details of a particular Apprentice strongest path.
 * <p>
 * As the Apprentice learns more about which notesets fit well with certain emotions, this
 * information is saved in a graph in the database. As this data is "learned" by the Apprentice, the
 * Apprentice becomes more confident in making an educated guess towards certain noteset-emotion
 * combinations. This confidence comes from lower weights for particular edges in the graph (the
 * lower the weight, the less effort it takes to follow that path for the Apprentice when making
 * decisions). This activity displays the strongest paths (i.e. the paths with the lowest weights)
 * learned up to this point by the Apprentice.
 * </p>
 */
public class ViewApprenticeStrongestPathDetailActivity extends Activity implements OnClickListener {
    private SharedPreferences sharedPref;
    private long apprenticeId = 0;
    private long pathId;
    private List<PathEdge> pathEdges = new ArrayList<PathEdge>();
    private Button buttonPlayPath = null;
    private File path = Environment.getExternalStorageDirectory();
    private String externalDirectory = path.toString() + "/otashu/";
    private File musicSource = new File(externalDirectory + "otashu_preview.mid");
    private static MediaPlayer mediaPlayer;

    /**
     * onCreate override that provides emotion-choose view to user.
     * 
     * @param savedInstanceState Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_view_apprentice_strongest_path_detail);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        apprenticeId = Long.parseLong(sharedPref.getString(
                "pref_selected_apprentice", "1"));
        pathId = getIntent().getExtras().getLong("list_id");

        Log.d("MYLOG", "detail activity received pathId: " + pathId);

        TextView pathText = (TextView) findViewById(R.id.view_strongest_path_detail_title);

        try {
            PathEdgesDataSource peds = new PathEdgesDataSource(this);
            pathEdges = peds.getPathEdgesByPath(pathId);
            peds.close();

            EmotionsDataSource eds = new EmotionsDataSource(this);
            Emotion emotion = eds.getEmotion(pathEdges.get(0).getEmotionId());
            eds.close();

            LabelsDataSource lds = new LabelsDataSource(this);
            Label emotionLabel = lds.getLabel(emotion.getLabelId());
            lds.close();

            Log.d("MYLOG", "found emotion for detail activity: " + emotion.getName());

            TextView emotionName = (TextView) findViewById(R.id.strongest_path_detail_emotion_value);
            emotionName.setText(emotion.getName());
            String backgroundColor = "#ffffff";
            if (emotionLabel != null) {
                backgroundColor = emotionLabel.getColor();
            }
            emotionName.setBackgroundColor(Color.parseColor(backgroundColor));

            int[] textViewIds = {
                    R.id.path_detail_vertex1,
                    R.id.path_detail_vertex2,
                    R.id.path_detail_vertex3,
                    R.id.path_detail_vertex4,
            };

            Label label;
            lds = new LabelsDataSource(this);
            Notevalue notevalue;
            NotevaluesDataSource nvds = new NotevaluesDataSource(this);
            TextView vertex;

            for (int i = 0; i < textViewIds.length - 1; i++) {
                vertex = (TextView) findViewById(textViewIds[i]);
                backgroundColor = "#dddddd";

                // TODO: there's probably a more efficient way to match vertices with related note
                // labels
                notevalue = nvds.getNotevalueByNoteValue(pathEdges.get(i).getFromNodeId());
                label = lds.getLabel(notevalue.getLabelId());

                if (label != null) {
                    backgroundColor = label.getColor();
                }
                vertex.setBackgroundColor(Color.parseColor(backgroundColor));
                vertex.setText(notevalue.getNotelabel());
            }

            // add last vertex
            vertex = (TextView) findViewById(textViewIds[textViewIds.length - 1]);
            backgroundColor = "#dddddd";

            // TODO: there's probably a more efficient way to match vertices with related note
            // labels
            notevalue = nvds.getNotevalueByNoteValue(pathEdges.get(textViewIds.length - 2)
                    .getToNodeId());
            label = lds.getLabel(notevalue.getLabelId());

            if (label != null) {
                backgroundColor = label.getColor();
            }
            vertex.setBackgroundColor(Color.parseColor(backgroundColor));
            vertex.setText(notevalue.getNotelabel());

            nvds.close();
            lds.close();

        } catch (Exception e) {
            Log.d("MYLOG", e.getStackTrace().toString());
        }

        try {
            // add listeners to buttons
            buttonPlayPath = (Button) findViewById(R.id.button_play_path);
            buttonPlayPath.setOnClickListener(this);
        } catch (Exception e) {
            Log.d("MYLOG", e.getStackTrace().toString());
        }
    }

    /**
     * onClick override that acts as a router to start desired activities.
     * 
     * @param view Incoming view.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_play_path:

                // disable play button while playing
                buttonPlayPath = (Button) findViewById(R.id.button_play_path);
                buttonPlayPath.setClickable(false);

                List<Note> notes = new ArrayList<Note>();

                Note note;
                for (int i = 0; i < pathEdges.size(); i++) {
                    note = new Note();
                    note.setNotevalue(pathEdges.get(i).getFromNodeId());
                    note.setVelocity(100);
                    note.setPosition(i + 1);
                    notes.add(note);
                }
                note = new Note();
                note.setNotevalue(pathEdges.get(pathEdges.size() - 1).getToNodeId());
                note.setVelocity(100);
                note.setPosition(pathEdges.size() + 1);
                notes.add(note);

                Log.d("MYLOG", "> notes for playback: " + notes.toString());

                // get default instrument for playback
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                String defaultInstrument = sharedPref.getString("pref_default_instrument", "");
                int playbackSpeed = Integer.valueOf(sharedPref.getString(
                        "pref_default_playback_speed", "120"));

                GenerateMusicActivity generateMusic = new GenerateMusicActivity();
                generateMusic.generateMusic(notes, musicSource, defaultInstrument, playbackSpeed);

                // play generated notes for user
                playMusic(musicSource);

                // return to previous activity when done playing
                mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer aMediaPlayer) {
                        // enable play button again
                        buttonPlayPath.setClickable(true);
                    }
                });

                break;
        }
    }

    public void playMusic(File musicSource) {
        // get media player ready
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, Uri.fromFile(musicSource));
        } else {
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(this, Uri.fromFile(musicSource));
        }

        // play music
        mediaPlayer.start();
    }

    /**
     * onBackPressed override used to stop playing music when done with activity
     */
    @Override
    public void onBackPressed() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                // stop playing music
                mediaPlayer.stop();
            }
            mediaPlayer.release();
        }
        super.onBackPressed();
    }
}
