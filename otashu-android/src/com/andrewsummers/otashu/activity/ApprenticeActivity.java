
package com.andrewsummers.otashu.activity;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.EdgesDataSource;
import com.andrewsummers.otashu.data.EmotionsDataSource;
import com.andrewsummers.otashu.data.GraphsDataSource;
import com.andrewsummers.otashu.data.NotesDataSource;
import com.andrewsummers.otashu.data.NotesetsDataSource;
import com.andrewsummers.otashu.data.VerticesDataSource;
import com.andrewsummers.otashu.model.Edge;
import com.andrewsummers.otashu.model.Emotion;
import com.andrewsummers.otashu.model.Note;
import com.andrewsummers.otashu.model.Noteset;
import com.andrewsummers.otashu.model.Vertex;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.Toast;

public class ApprenticeActivity extends Activity implements OnClickListener {

    private File path = Environment.getExternalStorageDirectory();
    private String externalDirectory = path.toString() + "/otashu/";
    private File musicSource = new File(externalDirectory + "otashu_preview.mid");
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private List<Note> notesToInsert = new ArrayList<Note>();
    private Noteset newlyInsertedNoteset = new Noteset();
    private Noteset notesetToInsert = new Noteset();
    private Emotion chosenEmotion = new Emotion();
    private Button buttonYes = null;
    private Button buttonNo = null;
    private Button buttonPlayNoteset = null;
    private SharedPreferences sharedPref;
    private long emotionGraphId;
    private long emotionId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_apprentice);

        buttonNo = (Button) findViewById(R.id.button_yes);
        buttonYes = (Button) findViewById(R.id.button_no);

        // get emotion graph id for Apprentice's note relationships graph
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        emotionGraphId = Long.parseLong(sharedPref.getString(
                "pref_emotion_graph_for_apprentice", "2"));

        try {
            // add listeners to buttons
            buttonNo.setOnClickListener(this);
            buttonYes.setOnClickListener(this);

            Button buttonPlayNoteset = (Button) findViewById(R.id.button_play_noteset);
            buttonPlayNoteset.setOnClickListener(this);
        } catch (Exception e) {
            Log.d("MYLOG", e.getStackTrace().toString());
        }

        // disable buttons while playing
        buttonYes.setClickable(false);
        buttonNo.setClickable(false);
        buttonPlayNoteset = (Button) findViewById(R.id.button_play_noteset);
        buttonPlayNoteset.setClickable(false);

        apprenticeAskProcess();

        mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer aMediaPlayer) {
                // enable play button again
                buttonYes.setClickable(true);
                buttonNo.setClickable(true);
                buttonPlayNoteset.setClickable(true);
            }
        });
    }

    public List<Note> generateNotes(int fromIndex, int toIndex) {
        String[] noteValuesArray = getResources().getStringArray(R.array.note_values_array);

        int randomNoteIndex = 0;
        String randomNote = "";
        float randomLength = 0.0f;
        int randomVelocity = 100;
        float lengthValues[] = {
                0.25f, 0.5f, 0.75f, 1.0f
        };

        for (int i = 0; i < 4; i++) {
            randomNoteIndex = new Random().nextInt((toIndex - fromIndex) + 1) + fromIndex;
            randomNote = noteValuesArray[randomNoteIndex];
            int randomLengthIndex = new Random().nextInt(lengthValues.length);
            randomLength = lengthValues[randomLengthIndex];
            randomVelocity = new Random().nextInt(120 - 60 + 1) + 60;

            Note note = new Note();
            note.setNotevalue(Integer.valueOf((randomNote)));
            note.setLength(randomLength);
            note.setVelocity(randomVelocity);
            note.setPosition(i + 1);

            notesToInsert.add(note);

            Log.d("MYLOG", "random note: " + randomNote);
            Log.d("MYLOG", "random length: " + randomLength);
            Log.d("MYLOG", "random velocity: " + randomVelocity);
        }

        return notesToInsert;
    }

    public void askQuestion() {
        TextView apprenticeText = (TextView) findViewById(R.id.apprentice_text);

        apprenticeText.setText("Does this sound " + chosenEmotion.getName() + "?");
    }

    public void playMusic(File musicSource) {
        // get media player ready
        mediaPlayer = MediaPlayer.create(this, Uri.fromFile(musicSource));

        // play music
        mediaPlayer.start();
    }

    @Override
    public void onClick(View v) {
        VerticesDataSource vds = new VerticesDataSource(this);
        EdgesDataSource edds = new EdgesDataSource(this);

        switch (v.getId()) {
            case R.id.button_no:
                // TODO: do something with "no" response (learning)

                // don't add generated noteset to user collection (even if Apprentice is allowed to
                // auto-add generated noteset)

                // examine notes for graph purposes

                Log.d("MYLOG", "> how many notes to insert? " + notesToInsert.size());

                Log.d("MYLOG", "> examining noteset...");
                for (int i = 0; i < notesToInsert.size() - 1; i++) {
                    // Examine note1 + note2
                    Note noteA = notesToInsert.get(i);
                    Note noteB = notesToInsert.get(i + 1);

                    // Do nodes exist?
                    Vertex nodeA = vds.getVertex(emotionGraphId, noteA.getNotevalue());
                    Vertex nodeB = vds.getVertex(emotionGraphId, noteB.getNotevalue());

                    // If nodes don't exist, create new nodes in graph
                    if (nodeA.getNode() <= 0) {
                        Log.d("MYLOG", "> nodeA doesn't exist... creating new vertex");
                        Vertex newNodeA = new Vertex();
                        newNodeA.setGraphId(emotionGraphId);
                        newNodeA.setNode(noteA.getNotevalue());
                        vds.createVertex(newNodeA);
                        nodeA.setNode(noteA.getNotevalue());
                    }
                    if (nodeB.getNode() <= 0) {
                        Log.d("MYLOG", "> nodeB doesn't exist... creating new vertex");
                        Vertex newNodeB = new Vertex();
                        newNodeB.setGraphId(emotionGraphId);
                        newNodeB.setNode(noteB.getNotevalue());
                        vds.createVertex(newNodeB);
                        nodeB.setNode(noteB.getNotevalue());
                    }

                    // Does an edge exist between these two nodes?
                    Edge edge = edds.getEdge(emotionGraphId, emotionId, nodeA.getNode(),
                            nodeB.getNode());

                    if (edge.getWeight() < 0.0f || edge.getWeight() > 1.0f) {
                        Log.d("MYLOG",
                                "> edge doesn't exist... creating new edge between "
                                        + nodeA.getNode() + " and " + nodeB.getNode());
                        // If edge doesn't exist, create new edge in graph (and set weight at 0.5)
                        // [note: 0.0 = stronger edge / more likely to be chosen than a 1.0 edge]
                        Edge newEdge = new Edge();
                        newEdge.setGraphId(emotionGraphId);
                        newEdge.setEmotionId(emotionId);
                        newEdge.setFromNodeId(nodeA.getNode());
                        newEdge.setToNodeId(nodeB.getNode());
                        newEdge.setWeight(0.5f);
                        newEdge.setPosition(i + 1);
                        edds.createEdge(newEdge);
                    } else {
                        Log.d("MYLOG",
                                "> edge exists between " + nodeA.getNode() + " and "
                                        + nodeB.getNode()
                                        + " ... just updating weight which is currently: "
                                        + edge.getWeight());
                        // If edge does exist, update weight (weight + 0.1)
                        if ((edge.getWeight() + 0.1f) <= 1.0f) {
                            Log.d("MYLOG", "> adding 0.1f to weight...");
                            // round float addition in order to avoid awkward zeros
                            BigDecimal bd = new BigDecimal(Float.toString(edge.getWeight() + 0.1f));
                            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                            edge.setWeight(bd.floatValue());
                            edds.updateEdge(edge);
                        }
                    }

                }

                // disable buttons while playing
                buttonYes = (Button) findViewById(R.id.button_yes);
                buttonYes.setClickable(false);
                buttonNo = (Button) findViewById(R.id.button_no);
                buttonNo.setClickable(false);
                buttonPlayNoteset = (Button) findViewById(R.id.button_play_noteset);
                buttonPlayNoteset.setClickable(false);

                // try another noteset
                apprenticeAskProcess();

                mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer aMediaPlayer) {
                        // enable play button again
                        buttonYes.setClickable(true);
                        buttonNo.setClickable(true);
                        buttonPlayNoteset.setClickable(true);
                    }
                });

                break;
            case R.id.button_yes:

                // disable buttons while playing
                buttonYes = (Button) findViewById(R.id.button_yes);
                buttonYes.setClickable(false);
                buttonNo = (Button) findViewById(R.id.button_no);
                buttonNo.setClickable(false);
                buttonPlayNoteset = (Button) findViewById(R.id.button_play_noteset);
                buttonPlayNoteset.setClickable(false);

                // check if Apprentice is allowed to auto-add generated noteset into User's
                // collection
                boolean apprenticeCanAutoAddNotset = sharedPref.getBoolean(
                        "pref_apprentice_auto_add_noteset", false);

                if (apprenticeCanAutoAddNotset) {
                    // save noteset
                    notesetToInsert.setEmotion((int) chosenEmotion.getId());
                    saveNoteset(v, notesetToInsert);

                    // save notes
                    for (int i = 0; i < notesToInsert.size(); i++) {
                        Note note = notesToInsert.get(i);
                        note.setNotesetId(newlyInsertedNoteset.getId());

                        saveNote(v, notesToInsert.get(i));
                    }
                }

                // examine notes for graph purposes
                // get default graph id for Apprentice's note relationships graph
                // long defaultGraphId = Long.parseLong(sharedPref.getString(
                // "pref_default_graph_for_apprentice", "1"));

                Log.d("MYLOG", "> examining noteset...");
                for (int i = 0; i < notesToInsert.size() - 1; i++) {
                    // Examine note1 + note2
                    Note noteA = notesToInsert.get(i);
                    Note noteB = notesToInsert.get(i + 1);

                    // Do nodes exist?
                    Vertex nodeA = vds.getVertex(emotionGraphId, noteA.getNotevalue());
                    Vertex nodeB = vds.getVertex(emotionGraphId, noteB.getNotevalue());

                    // If nodes don't exist, create new nodes in graph
                    if (nodeA.getNode() <= 0) {
                        Log.d("MYLOG", "> nodeA doesn't exist... creating new vertex");
                        Vertex newNodeA = new Vertex();
                        newNodeA.setGraphId(emotionGraphId);
                        newNodeA.setNode(noteA.getNotevalue());
                        vds.createVertex(newNodeA);
                        nodeA.setNode(noteA.getNotevalue());
                    }
                    if (nodeB.getNode() <= 0) {
                        Log.d("MYLOG", "> nodeB doesn't exist... creating new vertex");
                        Vertex newNodeB = new Vertex();
                        newNodeB.setGraphId(emotionGraphId);
                        newNodeB.setNode(noteB.getNotevalue());
                        vds.createVertex(newNodeB);
                        nodeB.setNode(noteB.getNotevalue());
                    }

                    // Does an edge exist between these two nodes?
                    Edge edge = edds.getEdge(emotionGraphId, emotionId, nodeA.getNode(),
                            nodeB.getNode());

                    if (edge.getWeight() < 0.0f || edge.getWeight() > 1.0f) {
                        Log.d("MYLOG",
                                "> edge doesn't exist... creating new edge between "
                                        + nodeA.getNode() + " and " + nodeB.getNode());
                        // If edge doesn't exist, create new edge in graph (and set weight at 0.5)
                        // [note: 0.0 = stronger edge / more likely to be chosen than a 1.0 edge]
                        Edge newEdge = new Edge();
                        newEdge.setGraphId(emotionGraphId);
                        newEdge.setEmotionId(emotionId);
                        newEdge.setFromNodeId(nodeA.getNode());
                        newEdge.setToNodeId(nodeB.getNode());
                        newEdge.setWeight(0.5f);
                        newEdge.setPosition(i + 1);
                        edds.createEdge(newEdge);
                    } else {
                        Log.d("MYLOG",
                                "> edge exists between " + nodeA.getNode() + " and "
                                        + nodeB.getNode()
                                        + " ... just updating weight which is currently: "
                                        + edge.getWeight());
                        // If edge does exist, update weight (weight - 0.1)
                        if ((edge.getWeight() - 0.1f) >= 0.0f) {
                            Log.d("MYLOG", "> subtracting 0.1f from weight...");
                            // round float addition in order to avoid awkward zeros
                            BigDecimal bd = new BigDecimal(Float.toString(edge.getWeight() - 0.1f));
                            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                            edge.setWeight(bd.floatValue());
                            edds.updateEdge(edge);
                        }
                    }

                }

                // try another noteset
                apprenticeAskProcess();

                mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer aMediaPlayer) {
                        // enable play button again
                        buttonYes.setClickable(true);
                        buttonNo.setClickable(true);
                        buttonPlayNoteset.setClickable(true);
                    }
                });

                break;
            case R.id.button_play_noteset:

                // disable buttons while playing
                buttonYes = (Button) findViewById(R.id.button_yes);
                buttonYes.setClickable(false);
                buttonNo = (Button) findViewById(R.id.button_no);
                buttonNo.setClickable(false);
                buttonPlayNoteset = (Button) findViewById(R.id.button_play_noteset);
                buttonPlayNoteset.setClickable(false);

                // play generated notes for user
                playMusic(musicSource);

                mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer aMediaPlayer) {
                        // enable play button again
                        buttonYes.setClickable(true);
                        buttonNo.setClickable(true);
                        buttonPlayNoteset.setClickable(true);
                    }
                });

                break;
        }
    }

    public void apprenticeAskProcess() {

        // get random emotion
        EmotionsDataSource eds = new EmotionsDataSource(this);
        chosenEmotion = eds.getRandomEmotion();
        eds.close();

        emotionId = chosenEmotion.getId();

        // clear old generated notes
        notesToInsert.clear();

        List<Note> notes = new ArrayList<Note>();

        // draw from past learned emotion graph data for generating noteset for question
        // for example:
        // node_from_id: 1, node_to_id: 2, position: 1
        // node_from_id: 2, node_to_id: 3, position: 2
        // node_from_id: 3, node_to_id: 4, position: 3
        //
        // process:
        // 1. select random position1 set within selected emotion
        // 2. select random position2 set within selected emotion and including node_to_id from
        // position1
        // 3. select random position3 set within selected emotion and including node_to_id from
        // position2

        EdgesDataSource edds = new EdgesDataSource(this);

        try {
            Log.d("MYLOG", ">> Using Learned Data Approach");
            Edge edgeOne = edds.getRandomEdge(emotionGraphId, emotionId, 0, 0, 1, 0);
            Edge edgeTwo = edds.getRandomEdge(emotionGraphId, emotionId, edgeOne.getFromNodeId(),
                    edgeOne.getToNodeId(), 2, 3);
            Edge edgeThree = edds.getRandomEdge(emotionGraphId, emotionId, edgeOne.getFromNodeId(),
                    edgeOne.getToNodeId(), 3, 3);

            Log.d("MYLOG", "^^ edgeOne: " + edgeOne);
            Log.d("MYLOG", "^^ edgeTwo: " + edgeTwo);
            Log.d("MYLOG", "^^ edgeThree: " + edgeThree);

            Note note1 = new Note();
            note1.setNotevalue(edgeOne.getFromNodeId());
            notes.add(note1);
            Note note2 = new Note();
            note2.setNotevalue(edgeTwo.getFromNodeId());
            notes.add(note2);
            Note note3 = new Note();
            note3.setNotevalue(edgeThree.getFromNodeId());
            notes.add(note3);
            Note note4 = new Note();
            note4.setNotevalue(edgeThree.getToNodeId());
            notes.add(note4);

            Log.d("MYLOG", ">> thoughtfully-generated noteset: " + notes.toString());

        } catch (Exception e) {
            Log.d("MYLOG", ">> Using Random Approach");
            // stay within 39..50 for now (C4..B4)
            notes = generateNotes(39, 50);
        }

        // get default instrument for playback
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String defaultInstrument = sharedPref.getString("pref_default_instrument", "");

        GenerateMusicActivity generateMusic = new GenerateMusicActivity();
        generateMusic.generateMusic(notes, musicSource, defaultInstrument);

        // does generated noteset sounds like chosen emotion?
        askQuestion();

        // disable play button while playing
        buttonPlayNoteset = (Button) findViewById(R.id.button_play_noteset);
        buttonPlayNoteset.setClickable(false);

        // play generated notes for user
        playMusic(musicSource);

        // return to previous activity when done playing
        mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer aMediaPlayer) {
                // enable play button again
                buttonPlayNoteset.setClickable(true);
            }
        });
    }

    /**
     * Save noteset data.
     * 
     * @param v Incoming view.
     * @param data Incoming string of data to be saved.
     */
    private void saveNoteset(View v, Noteset noteset) {

        // save noteset in database
        NotesetsDataSource nds = new NotesetsDataSource(this);
        newlyInsertedNoteset = nds.createNoteset(noteset);
        nds.close();

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context,
                context.getResources().getString(R.string.noteset_saved),
                duration);
        toast.show();
    }

    private void saveNote(View v, Note note) {

        Log.d("MYLOG", Long.toString(note.getNotesetId()));
        Log.d("MYLOG", Integer.toString(note.getNotevalue()));

        // save noteset in database
        NotesDataSource nds = new NotesDataSource(this);
        nds.createNote(note);
        nds.close();

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context,
                context.getResources().getString(R.string.noteset_saved),
                duration);
        toast.show();
    }

    /**
     * onBackPressed override used to stop playing music when done with activity
     */
    @Override
    public void onBackPressed() {
        if (mediaPlayer.isPlaying()) {
            // stop playing music
            mediaPlayer.stop();
        }

        super.onBackPressed();
    }
}
