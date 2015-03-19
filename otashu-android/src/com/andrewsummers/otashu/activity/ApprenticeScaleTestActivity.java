
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
import com.andrewsummers.otashu.data.ApprenticeScorecardsDataSource;
import com.andrewsummers.otashu.data.ApprenticeScoresDataSource;
import com.andrewsummers.otashu.data.EdgesDataSource;
import com.andrewsummers.otashu.data.EmotionsDataSource;
import com.andrewsummers.otashu.data.NotevaluesDataSource;
import com.andrewsummers.otashu.data.VerticesDataSource;
import com.andrewsummers.otashu.model.ApprenticeScore;
import com.andrewsummers.otashu.model.ApprenticeScorecard;
import com.andrewsummers.otashu.model.Edge;
import com.andrewsummers.otashu.model.Emotion;
import com.andrewsummers.otashu.model.Note;
import com.andrewsummers.otashu.model.Notevalue;
import com.andrewsummers.otashu.model.Vertex;

import android.app.Activity;
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

/**
 * The ApprenticeEmotionTestActivity class provides a specific test for the Apprentice with test
 * results noted as judged by the User.
 */
public class ApprenticeScaleTestActivity extends Activity implements OnClickListener {
    private File path = Environment.getExternalStorageDirectory();
    private String externalDirectory = path.toString() + "/otashu/";
    private File musicSource = new File(externalDirectory + "otashu_preview.mid");
    private static MediaPlayer mediaPlayer;
    private List<Note> notesToInsert = new ArrayList<Note>();
    private Emotion chosenEmotion = new Emotion();
    private Button buttonYes = null;
    private Button buttonNo = null;
    private Button buttonPlayNoteset = null;
    private SharedPreferences sharedPref;
    private long scaleGraphId;
    private long emotionId;
    private int guessesCorrect = 0;
    private int guessesIncorrect = 0;
    private double guessesCorrectPercentage = 0.0;
    private int totalGuesses = 0;
    private long scorecardId = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_apprentice_test);

        buttonNo = (Button) findViewById(R.id.button_yes);
        buttonYes = (Button) findViewById(R.id.button_no);

        // get emotion graph id for Apprentice's note relationships graph
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        scaleGraphId = Long.parseLong(sharedPref.getString(
                "pref_scale_graph_for_apprentice", "3"));

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
        notesToInsert.clear();

        int randomNoteIndex = 0;
        String randomNote = "";
        float randomLength = 0.0f;
        int randomVelocity = 100;
        float lengthValues[] = {
                0.25f, 0.5f, 0.75f, 1.0f
        };

        for (int i = 0; i < 3; i++) {
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

        String notes = "";
        NotevaluesDataSource nvds = new NotevaluesDataSource(this);
        for (int i = 0; i < notesToInsert.size(); i++) {
            Notevalue note = nvds.getNotevalueByNoteValue(notesToInsert.get(i).getNotevalue());
            notes += note.getNotelabel() + " ";
        }

        apprenticeText.setText("Are these notes part of the same scale for "
                + chosenEmotion.getName() + "? (" + notes + ")");
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
        VerticesDataSource vds = new VerticesDataSource(this);
        EdgesDataSource edds = new EdgesDataSource(this);

        switch (v.getId()) {
            case R.id.button_no:
                guessesIncorrect++;

                totalGuesses = guessesCorrect + guessesIncorrect;

                if (totalGuesses > 0) {
                    guessesCorrectPercentage = ((double) guessesCorrect / (double) totalGuesses) * 100.0;
                }

                // examine notes for graph purposes
                for (int i = 0; i < notesToInsert.size() - 1; i++) {
                    long edgeId = 0;

                    // Examine note1 + note2
                    Note noteA = notesToInsert.get(i);
                    Note noteB = notesToInsert.get(i + 1);

                    // Do nodes exist?
                    Vertex nodeA = vds.getVertex(scaleGraphId, noteA.getNotevalue());
                    Vertex nodeB = vds.getVertex(scaleGraphId, noteB.getNotevalue());

                    // If nodes don't exist, create new nodes in graph
                    if (nodeA.getNode() <= 0) {
                        // nodeA doesn't exist... creating new vertex
                        Vertex newNodeA = new Vertex();
                        newNodeA.setGraphId(scaleGraphId);
                        newNodeA.setNode(noteA.getNotevalue());
                        vds.createVertex(newNodeA);
                        nodeA.setNode(noteA.getNotevalue());
                    }
                    if (nodeB.getNode() <= 0) {
                        // nodeB doesn't exist... creating new vertex
                        Vertex newNodeB = new Vertex();
                        newNodeB.setGraphId(scaleGraphId);
                        newNodeB.setNode(noteB.getNotevalue());
                        vds.createVertex(newNodeB);
                        nodeB.setNode(noteB.getNotevalue());
                    }

                    // Does an edge exist between these two nodes?
                    Edge edge = edds.getEdge(scaleGraphId, emotionId, nodeA.getNode(),
                            nodeB.getNode());

                    if (edge.getWeight() < 0.0f || edge.getWeight() > 1.0f) {
                        // edge doesn't exist... creating new edge between nodeA and nodeB

                        // If edge doesn't exist, create new edge in graph (and set weight at 0.5)
                        // [note: 0.0 = stronger edge / more likely to be chosen than a 1.0 edge]
                        Edge newEdge = new Edge();
                        newEdge.setGraphId(scaleGraphId);
                        newEdge.setEmotionId(emotionId);
                        newEdge.setFromNodeId(nodeA.getNode());
                        newEdge.setToNodeId(nodeB.getNode());
                        newEdge.setWeight(0.5f);
                        newEdge.setPosition(i + 1);
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

                // examine notes for graph purposes
                // get default graph id for Apprentice's note relationships graph
                // long defaultGraphId = Long.parseLong(sharedPref.getString(
                // "pref_default_graph_for_apprentice", "1"));

                for (int i = 0; i < notesToInsert.size() - 1; i++) {
                    long edgeId = 0;

                    // Examine note1 + note2
                    Note noteA = notesToInsert.get(i);
                    Note noteB = notesToInsert.get(i + 1);

                    // Do nodes exist?
                    Vertex nodeA = vds.getVertex(scaleGraphId, noteA.getNotevalue());
                    Vertex nodeB = vds.getVertex(scaleGraphId, noteB.getNotevalue());

                    // If nodes don't exist, create new nodes in graph
                    if (nodeA.getNode() <= 0) {
                        // nodeA doesn't exist... creating new vertex
                        Vertex newNodeA = new Vertex();
                        newNodeA.setGraphId(scaleGraphId);
                        newNodeA.setNode(noteA.getNotevalue());
                        vds.createVertex(newNodeA);
                        nodeA.setNode(noteA.getNotevalue());
                    }
                    if (nodeB.getNode() <= 0) {
                        // nodeB doesn't exist... creating new vertex
                        Vertex newNodeB = new Vertex();
                        newNodeB.setGraphId(scaleGraphId);
                        newNodeB.setNode(noteB.getNotevalue());
                        vds.createVertex(newNodeB);
                        nodeB.setNode(noteB.getNotevalue());
                    }

                    // Does an edge exist between these two nodes?
                    Edge edge = edds.getEdge(scaleGraphId, emotionId, nodeA.getNode(),
                            nodeB.getNode());

                    if (edge.getWeight() < 0.0f || edge.getWeight() > 1.0f) {
                        // edge doesn't exist... creating new edge between nodeA and nodeB

                        // If edge doesn't exist, create new edge in graph (and set weight at 0.5)
                        // [note: 0.0 = stronger edge / more likely to be chosen than a 1.0 edge]
                        Edge newEdge = new Edge();
                        newEdge.setGraphId(scaleGraphId);
                        newEdge.setEmotionId(emotionId);
                        newEdge.setFromNodeId(nodeA.getNode());
                        newEdge.setToNodeId(nodeB.getNode());
                        newEdge.setWeight(0.5f);
                        newEdge.setPosition(i + 1);
                        newEdge = edds.createEdge(newEdge);
                        edgeId = newEdge.getId();
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
                    }

                    // save score
                    saveScore(1, edgeId);
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

        EdgesDataSource edds = new EdgesDataSource(this);
        String approach = "";

        try {
            // Using Learned Data Approach
            Random rnd = new Random();
            int randomOption = rnd.nextInt((2 - 1) + 1) + 1;
            int randomNotevalue = rnd.nextInt((71 - 60) + 1) + 60;

            approach = "Learned Data";
            Edge edgeOne = edds.getRandomEdge(scaleGraphId, emotionId, 0, 0, 1, 0);
            Edge edgeTwo = edds.getRandomEdge(scaleGraphId, emotionId,
                    edgeOne.getToNodeId(), 0, 2, 3);

            Edge edgeThree = new Edge();

            if (randomOption == 1) {
                approach = "Learned Data +";
                edgeThree.setFromNodeId(edgeTwo.getToNodeId());
                edgeThree.setToNodeId(randomNotevalue);
            } else {
                edgeThree = edds.getRandomEdge(scaleGraphId, emotionId, edgeTwo.getToNodeId(),
                        0, 3, 3);
            }

            Note note1 = new Note();
            note1.setNotevalue(edgeOne.getFromNodeId());
            notes.add(note1);
            Note note2 = new Note();
            note2.setNotevalue(edgeTwo.getFromNodeId());
            notes.add(note2);
            Note note3 = new Note();
            note3.setNotevalue(edgeThree.getToNodeId());
            notes.add(note3);
        } catch (Exception e) {
            Log.d("MYLOG", e.toString());
            // Using Random Approach
            approach = "Random";
            // 60..71 (C4..B4)
            // stay within 39..50 for now (C4..B4)
            notes = generateNotes(39, 50);
        }

        notesToInsert = notes;

        // get default instrument for playback
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String defaultInstrument = sharedPref.getString("pref_default_instrument", "");
        int playbackSpeed = Integer.valueOf(sharedPref.getString("pref_default_playback_speed",
                "120"));

        GenerateMusicActivity generateMusic = new GenerateMusicActivity();
        generateMusic.generateMusic(notes, musicSource, defaultInstrument, playbackSpeed);

        // does generated noteset sounds like chosen emotion?
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

        ApprenticeScoresDataSource asds = new ApprenticeScoresDataSource(this);
        asds.createApprenticeScore(aScore);
        asds.close();
    }
}
