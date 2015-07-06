
package com.andrewsummers.otashu.activity;

import java.io.File;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.AchievementsDataSource;
import com.andrewsummers.otashu.data.ApprenticeScorecardsDataSource;
import com.andrewsummers.otashu.data.ApprenticeScoresDataSource;
import com.andrewsummers.otashu.data.EdgesDataSource;
import com.andrewsummers.otashu.data.EmotionsDataSource;
import com.andrewsummers.otashu.data.NotesDataSource;
import com.andrewsummers.otashu.data.NotesetsDataSource;
import com.andrewsummers.otashu.data.VerticesDataSource;
import com.andrewsummers.otashu.model.Achievement;
import com.andrewsummers.otashu.model.Apprentice;
import com.andrewsummers.otashu.model.ApprenticeScore;
import com.andrewsummers.otashu.model.ApprenticeScorecard;
import com.andrewsummers.otashu.model.ApprenticeState;
import com.andrewsummers.otashu.model.Edge;
import com.andrewsummers.otashu.model.Emotion;
import com.andrewsummers.otashu.model.Note;
import com.andrewsummers.otashu.model.Noteset;
import com.andrewsummers.otashu.model.NotesetAndRelated;
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

/**
 * The ApprenticeEmotionTestActivity class provides a specific test for the Apprentice with test
 * results noted as judged by the User.
 * <p>
 * In this activity, the Apprentice chooses a random emotion and chooses notesets (either randomly
 * or using previously-learned data) and presents the noteset-emotion combination to the User to
 * check for accuracy. If the noteset-emotion combination is correct (or passing), the Apprentice
 * will go and save this noteset-emotion combination information in a database graph table. This
 * noteset-emotion combination is also saved in the User's noteset collection (if enabled in the
 * program settings).
 * </p>
 * <p>
 * If the Apprentice incorrectly chooses a noteset-emotion combination (as determined by the User),
 * the noteset-emotion combination information is noted by raising the related path edge weights in
 * the database graph table. Also, the incorrect noteset-emotion combination is not saved in the
 * User's noteset collection.
 * </p>
 */
public class ApprenticeEmotionTestActivity extends Activity implements OnClickListener {
    private File path = Environment.getExternalStorageDirectory();
    private String externalDirectory = path.toString() + "/otashu/";
    private File musicSource = new File(externalDirectory + "otashu_preview.mid");
    private static MediaPlayer mediaPlayer;
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
    private int guessesCorrect = 0;
    private int guessesIncorrect = 0;
    private double guessesCorrectPercentage = 0.0;
    private int totalGuesses = 0;
    private long scorecardId = 0;
    private boolean autoMode = false;
    private Apprentice apprentice;
    private long apprenticeId = 0;
    private int programMode;
    private List<Edge> currentEdges = new ArrayList<Edge>();
    private float strongPathLevel = 0.4f;
    private long emotionFocusId = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_apprentice_test);

        if (apprentice == null) {
            apprentice = new Apprentice(ApprenticeState.IDLE);
        }

        buttonNo = (Button) findViewById(R.id.button_yes);
        buttonYes = (Button) findViewById(R.id.button_no);

        // get emotion graph id for Apprentice's note relationships graph
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        programMode = Integer.parseInt(sharedPref.getString(
                "pref_program_mode", "1"));
        emotionGraphId = Long.parseLong(sharedPref.getString(
                "pref_emotion_graph_for_apprentice", "1"));
        apprenticeId = Long.parseLong(sharedPref.getString(
                "pref_selected_apprentice", "1"));

        // get data from bundle
        Bundle bundle = getIntent().getExtras();
        emotionFocusId = bundle.getInt("emotion_focus_id");

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

        // get random emotion
        EmotionsDataSource eds = new EmotionsDataSource(this);
        int emotionCount = eds.getEmotionCount(apprenticeId);
        eds.close();
        
        // make sure there's at least one emotion for the spinner list
        if (emotionCount <= 0) {
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context,
                    context.getResources().getString(R.string.need_emotions),
                    duration);
            toast.show();

            finish();
        } else {
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
    }

    public List<Note> generateNotes(int fromIndex, int toIndex) {
        String[] noteValuesArray = getResources().getStringArray(R.array.note_values_array);
        notesToInsert.clear();

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
        }

        return notesToInsert;
    }

    public void askQuestion() {
        TextView apprenticeText = (TextView) findViewById(R.id.apprentice_text);

        apprenticeText.setText("Does this sound " + chosenEmotion.getName() + "?");
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

    @Override
    public void onClick(View v) {
        apprentice.setState(ApprenticeState.LISTEN);
        VerticesDataSource vds = new VerticesDataSource(this);
        EdgesDataSource edds = new EdgesDataSource(this);

        switch (v.getId()) {
            case R.id.button_no:
                apprentice.setState(ApprenticeState.MEMO);
                guessesIncorrect++;

                totalGuesses = guessesCorrect + guessesIncorrect;

                if (totalGuesses > 0) {
                    guessesCorrectPercentage = ((double) guessesCorrect / (double) totalGuesses) * 100.0;
                }

                // don't add generated noteset to user collection (even if Apprentice is allowed to
                // auto-add generated noteset)

                // examine notes for graph purposes
                for (int i = 0; i < notesToInsert.size() - 1; i++) {
                    long edgeId = 0;

                    // Examine note1 + note2
                    Note noteA = notesToInsert.get(i);
                    Note noteB = notesToInsert.get(i + 1);

                    // Do nodes exist?
                    Vertex nodeA = vds.getVertex(emotionGraphId, noteA.getNotevalue());
                    Vertex nodeB = vds.getVertex(emotionGraphId, noteB.getNotevalue());

                    // If nodes don't exist, create new nodes in graph
                    if (nodeA.getNode() <= 0) {
                        // nodeA doesn't exist... creating new vertex
                        Vertex newNodeA = new Vertex();
                        newNodeA.setGraphId(emotionGraphId);
                        newNodeA.setNode(noteA.getNotevalue());
                        vds.createVertex(newNodeA);
                        nodeA.setNode(noteA.getNotevalue());
                    }
                    if (nodeB.getNode() <= 0) {
                        // nodeB doesn't exist... creating new vertex
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
                        // edge doesn't exist... creating new edge between nodeA and nodeB

                        // If edge doesn't exist, create new edge in graph (and set weight at 0.5)
                        // [note: 0.0 = stronger edge / more likely to be chosen than a 1.0 edge]
                        Edge newEdge = new Edge();
                        newEdge.setGraphId(emotionGraphId);
                        newEdge.setEmotionId(emotionId);
                        newEdge.setFromNodeId(nodeA.getNode());
                        newEdge.setToNodeId(nodeB.getNode());
                        newEdge.setWeight(0.5f);
                        newEdge.setPosition(i + 1);
                        newEdge.setApprenticeId(apprenticeId);
                        newEdge = edds.createEdge(newEdge);
                        edgeId = newEdge.getId();
                    } else {
                        // edge exists between nodeA and nodeB, just update weight

                        // If edge does exist, update weight (weight + 0.1)
                        if ((edge.getWeight() + 0.1f) <= 1.0f) {
                            // adding 0.1f to weight... (lower weight is stronger)
                            // round float addition in order to avoid awkward zeros
                            BigDecimal bd = new BigDecimal(Float.toString(edge.getWeight() + 0.1f));
                            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                            edge.setWeight(bd.floatValue());
                            edds.updateEdge(edge);
                            edgeId = edge.getId();
                        }
                    }

                    // save score
                    saveScore(0, edgeId);
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
                apprentice.setState(ApprenticeState.MEMO);
                guessesCorrect++;

                totalGuesses = guessesCorrect + guessesIncorrect;

                if (totalGuesses > 0) {
                    guessesCorrectPercentage = ((double) guessesCorrect / (double) totalGuesses) * 100.0;
                }

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

                    // prepare noteset
                    notesetToInsert.setEmotion((int) chosenEmotion.getId());
                    notesetToInsert.setEnabled(1);
                    notesetToInsert.setApprenticeId(apprenticeId);

                    // check if noteset already exists, first
                    NotesetAndRelated notesetAndRelated = new NotesetAndRelated();
                    notesetAndRelated.setNoteset(notesetToInsert);
                    notesetAndRelated.setNotes(notesToInsert);
                    boolean notesetExists = doesNotesetExist(notesetAndRelated);

                    if (!notesetExists) {
                        // save noteset
                        saveNoteset(v, notesetToInsert);

                        // save notes
                        for (int i = 0; i < notesToInsert.size(); i++) {
                            Note note = notesToInsert.get(i);
                            note.setNotesetId(newlyInsertedNoteset.getId());

                            // TODO: these could be generated by Apprentice in the future
                            note.setVelocity(100);
                            note.setLength(0.5f);

                            note.setPosition(i + 1);

                            saveNote(v, notesToInsert.get(i));
                        }
                    }
                }

                // examine notes for graph purposes
                for (int i = 0; i < notesToInsert.size() - 1; i++) {
                    long edgeId = 0;

                    // Examine note1 + note2
                    Note noteA = notesToInsert.get(i);
                    Note noteB = notesToInsert.get(i + 1);

                    // Do nodes exist?
                    Vertex nodeA = vds.getVertex(emotionGraphId, noteA.getNotevalue());
                    Vertex nodeB = vds.getVertex(emotionGraphId, noteB.getNotevalue());

                    // If nodes don't exist, create new nodes in graph

                    // if nodeA doesn't exist... create new vertex
                    if (nodeA.getNode() <= 0) {
                        Vertex newNodeA = new Vertex();
                        newNodeA.setGraphId(emotionGraphId);
                        newNodeA.setNode(noteA.getNotevalue());
                        vds.createVertex(newNodeA);
                        nodeA.setNode(noteA.getNotevalue());
                    }

                    // if nodeB doesn't exist... create new vertex
                    if (nodeB.getNode() <= 0) {
                        Vertex newNodeB = new Vertex();
                        newNodeB.setGraphId(emotionGraphId);
                        newNodeB.setNode(noteB.getNotevalue());
                        vds.createVertex(newNodeB);
                        nodeB.setNode(noteB.getNotevalue());
                    }

                    // Does an edge exist between these two nodes?
                    Edge edge = edds.getEdge(emotionGraphId, emotionId, nodeA.getNode(),
                            nodeB.getNode());

                    // if edge doesn't exist... create new edge between nodeA and nodeB
                    if (edge.getWeight() < 0.0f || edge.getWeight() > 1.0f) {

                        // If edge doesn't exist, create new edge in graph (and set weight at 0.5)
                        // [note: 0.0 = stronger edge / more likely to be chosen than a 1.0 edge]
                        Edge newEdge = new Edge();
                        newEdge.setGraphId(emotionGraphId);
                        newEdge.setEmotionId(emotionId);
                        newEdge.setFromNodeId(nodeA.getNode());
                        newEdge.setToNodeId(nodeB.getNode());
                        newEdge.setWeight(0.5f);
                        newEdge.setPosition(i + 1);
                        newEdge.setApprenticeId(apprenticeId);
                        newEdge = edds.createEdge(newEdge);
                        edgeId = newEdge.getId();

                        currentEdges.add(newEdge);
                    } else {
                        // edge exists between nodeA and nodeB, just update weight

                        // If edge does exist, update weight (weight - 0.1)
                        if ((edge.getWeight() - 0.1f) >= 0.0f) {
                            // subtracting 0.1f from weight... (lower weight is stronger)
                            // round float addition in order to avoid awkward zeros
                            BigDecimal bd = new BigDecimal(Float.toString(edge.getWeight() - 0.1f));
                            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                            edge.setWeight(bd.floatValue());
                            edds.updateEdge(edge);
                            edgeId = edge.getId();
                        }

                        currentEdges.add(edge);
                    }

                    // save score
                    saveScore(1, edgeId);
                }

                // check if achievement was earned in play mode
                if (programMode == 2) {

                    Log.d("MYLOG", "current edges: " + currentEdges.toString());

                    // check if this is a strong noteset
                    String currentEdgesKey = "";
                    boolean strongPathFound = true;
                    for (int i = 0; i < currentEdges.size(); i++) {
                        if (i == (currentEdges.size() - 1)) {
                            currentEdgesKey += currentEdges.get(i).getFromNodeId() + "_";
                            currentEdgesKey += currentEdges.get(i).getToNodeId();
                        } else {
                            currentEdgesKey += currentEdges.get(i).getFromNodeId() + "_";
                        }

                        if (currentEdges.get(i).getWeight() > strongPathLevel) {
                            strongPathFound = false;
                        }
                    }

                    Log.d("MYLOG", "strong path found? " + strongPathFound);
                    Log.d("MYLOG", "current edges key: " + currentEdgesKey);

                    if (strongPathFound) {
                        String key = String.valueOf(currentEdgesKey);
                        // check if achievement key for this already exists
                        AchievementsDataSource ads = new AchievementsDataSource(this);
                        Achievement achievement = ads.getAchievementByKey(key);

                        if (achievement.getId() > 0) {
                            // pass
                        } else {
                            TimeZone timezone = TimeZone.getTimeZone("UTC");
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'",
                                    Locale.getDefault());
                            dateFormat.setTimeZone(timezone);
                            String earnedOnISO = dateFormat.format(new Date());

                            // save achievement if this is a new key
                            achievement = new Achievement();
                            achievement.setName("found_strong_path");
                            achievement.setApprenticeId(apprenticeId);
                            achievement.setEarnedOn(earnedOnISO);
                            achievement.setKey(key);

                            ads.createAchievement(achievement);

                            Context context = getApplicationContext();
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(
                                    context,
                                    context.getResources().getString(
                                            R.string.achievement_found_strong_path),
                                    duration);
                            toast.show();
                        }

                        ads.close();
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

    private boolean doesNotesetExist(NotesetAndRelated notesetAndRelated) {
        boolean notesetExists = false;
        NotesDataSource nds = new NotesDataSource(this);
        notesetExists = nds.doesNotesetExist(notesetAndRelated);
        return notesetExists;
    }

    public void apprenticeAskProcess() {
        apprentice.setState(ApprenticeState.CHOOSE_EMOTION);

        // get random emotion
        EmotionsDataSource eds = new EmotionsDataSource(this);
        int emotionCount = eds.getEmotionCount(apprenticeId);

        // are we focusing on a specific emotion?
        if (emotionFocusId > 0) {
            chosenEmotion = eds.getEmotion(emotionFocusId);
        } else {
            chosenEmotion = eds.getRandomEmotion(apprenticeId);
        }

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
        String approach = "";

        apprentice.setState(ApprenticeState.CHOOSE_NOTESET);
        try {
            // Using Learned Data Approach (thoughtfully-generated noteset)
            Random rnd = new Random();
            int randomOption = rnd.nextInt((2 - 1) + 1) + 1;
            int randomNotevalue = rnd.nextInt((71 - 60) + 1) + 60;

            approach = "Learned Data";
            Edge edgeOne = edds.getRandomEdge(apprenticeId, emotionGraphId, emotionId, 0, 0, 1, 0);
            Edge edgeTwo = edds.getRandomEdge(apprenticeId, emotionGraphId, emotionId,
                    edgeOne.getToNodeId(), 0, 2, 3);
            Edge edgeThree = edds.getRandomEdge(apprenticeId, emotionGraphId, emotionId,
                    edgeTwo.getToNodeId(), 0, 3, 3);

            if (randomOption == 1) {
                approach = "Learned Data +";
                edgeOne.setFromNodeId(randomNotevalue);
                edgeThree.setToNodeId(randomNotevalue);
            }

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
        } catch (Exception e) {
            // Using Random Approach
            approach = "Random";
            // stay within 39..50 for now (C4..B4)
            notes = generateNotes(39, 50);
        }

        // make sure notes are filled in
        if ((notes.get(0).getNotevalue() == 0) || (notes.get(1).getNotevalue() == 0)
                || (notes.get(2).getNotevalue() == 0) || (notes.get(3).getNotevalue() == 0)) {
            // Using Random Approach
            approach = "Random";
            // stay within 39..50 for now (C4..B4)
            notes = generateNotes(39, 50);
        }

        Log.d("MYLOG", "checking generated notes in emotion test: " + notes.toString());

        notesToInsert = notes;

        // get default instrument for playback
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String defaultInstrument = sharedPref.getString("pref_default_instrument", "");
        int playbackSpeed = Integer.valueOf(sharedPref.getString("pref_default_playback_speed",
                "120"));

        apprentice.setState(ApprenticeState.GENERATE_NOTES);
        GenerateMusicActivity generateMusic = new GenerateMusicActivity();
        generateMusic.generateMusic(notes, musicSource, defaultInstrument, playbackSpeed);

        // does generated noteset sounds like chosen emotion?
        apprentice.setState(ApprenticeState.ASK);
        askQuestion();

        TextView apprenticeGuessMethod = (TextView) findViewById(R.id.apprentice_guess_method);
        apprenticeGuessMethod.setText(approach);

        String guessesCorrectPercentageString = String.format(Locale.getDefault(), "%.02f",
                guessesCorrectPercentage);

        TextView apprenticeTotalGuesses = (TextView) findViewById(R.id.apprentice_total_guesses);
        apprenticeTotalGuesses.setText(guessesCorrect + "/" + totalGuesses + " ("
                + guessesCorrectPercentageString + "%)");

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
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                // stop playing music
                mediaPlayer.stop();
            }
            mediaPlayer.release();
        }

        super.onBackPressed();
    }

    public void saveScore(int isCorrect, long edgeId) {
        boolean autoSaveScorecard = sharedPref.getBoolean(
                "pref_auto_save_scorecard", false);

        if (autoSaveScorecard) {
            // check if scorecard already exists
            if (scorecardId <= 0) {
                TimeZone timezone = TimeZone.getTimeZone("UTC");
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'",
                        Locale.getDefault());
                dateFormat.setTimeZone(timezone);
                String takenAtISO = dateFormat.format(new Date());

                // String takenAtISO = new Date().toString();

                // if scorecard doesn't yet exist, create it
                ApprenticeScorecardsDataSource asds = new ApprenticeScorecardsDataSource(this);
                ApprenticeScorecard aScorecard = new ApprenticeScorecard();
                aScorecard.setTakenAt(takenAtISO);
                aScorecard.setApprenticeId(apprenticeId);
                aScorecard = asds.createApprenticeScorecard(aScorecard);
                asds.close();

                // then get scorecard_id for the score to save
                scorecardId = aScorecard.getId();
            }

            // also, update scorecard question totals
            ApprenticeScorecardsDataSource ascds = new ApprenticeScorecardsDataSource(this);
            ApprenticeScorecard scorecard = new ApprenticeScorecard();
            scorecard = ascds.getApprenticeScorecard(scorecardId);
            if (isCorrect == 1) {
                scorecard.setCorrect(guessesCorrect);
            }
            scorecard.setTotal(totalGuesses);
            ascds.updateApprenticeScorecard(scorecard);
            ascds.close();

            // save Apprentice's score results to database
            ApprenticeScore aScore = new ApprenticeScore();
            aScore.setScorecardId(scorecardId);
            aScore.setQuestionNumber(totalGuesses);
            aScore.setCorrect(isCorrect);
            aScore.setEdgeId(edgeId);
            aScore.setApprenticeId(apprenticeId);
            aScore.setGraphId(emotionGraphId);

            Log.d("MYLOG", "saving emotion score: " + aScore.toString());
            
            ApprenticeScoresDataSource asds = new ApprenticeScoresDataSource(this);
            asds.createApprenticeScore(aScore);
            asds.close();
        } else {
            Log.d("MYLOG", "Not saving scorecard.");
        }
    }

    public boolean isAutoMode() {
        return autoMode;
    }

    public void setAutoMode(boolean autoMode) {
        this.autoMode = autoMode;
    }
}
