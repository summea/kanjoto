
package com.andrewsummers.otashu.activity;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.AchievementsDataSource;
import com.andrewsummers.otashu.data.ApprenticeScorecardsDataSource;
import com.andrewsummers.otashu.data.ApprenticeScoresDataSource;
import com.andrewsummers.otashu.data.EmotionsDataSource;
import com.andrewsummers.otashu.data.KeyNotesDataSource;
import com.andrewsummers.otashu.data.KeySignaturesDataSource;
import com.andrewsummers.otashu.model.Achievement;
import com.andrewsummers.otashu.model.ApprenticeScore;
import com.andrewsummers.otashu.model.ApprenticeScorecard;
import com.andrewsummers.otashu.model.Emotion;
import com.andrewsummers.otashu.model.KeyNote;
import com.andrewsummers.otashu.model.KeySignature;
import com.andrewsummers.otashu.model.Note;

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
    private long currentKeySignatureId = 0;
    private long apprenticeId = 0;
    private int programMode;
    private int notesToCompleteScale = 7;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_apprentice_test);

        buttonNo = (Button) findViewById(R.id.button_yes);
        buttonYes = (Button) findViewById(R.id.button_no);

        // get emotion graph id for Apprentice's note relationships graph
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        programMode = Integer.parseInt(sharedPref.getString(
                "pref_program_mode", "1"));
        setScaleGraphId(Long.parseLong(sharedPref.getString(
                "pref_scale_graph_for_apprentice", "3")));
        apprenticeId = Long.parseLong(sharedPref.getString(
                "pref_selected_apprentice", "1"));

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

    public Note getRandomNote(int fromIndex, int toIndex) {
        String[] noteValuesArray = getResources().getStringArray(R.array.note_values_array);

        int randomNoteIndex = 0;
        String randomNote = "";

        randomNoteIndex = new Random().nextInt((toIndex - fromIndex) + 1) + fromIndex;
        randomNote = noteValuesArray[randomNoteIndex];

        Note note = new Note();
        note.setNotevalue(Integer.valueOf((randomNote)));
        note.setLength(1.0f);
        note.setVelocity(100);
        note.setPosition(0);

        return note;
    }

    public List<Note> generateNotes(int fromIndex, int toIndex) {
        String[] noteValuesArray = getResources().getStringArray(R.array.note_values_array);
        notesToInsert.clear();

        int randomNoteIndex = 0;
        String randomNote = "";

        for (int i = 0; i < 3; i++) {
            randomNoteIndex = new Random().nextInt((toIndex - fromIndex) + 1) + fromIndex;
            randomNote = noteValuesArray[randomNoteIndex];

            Note note = new Note();
            note.setNotevalue(Integer.valueOf((randomNote)));
            note.setLength(1.0f);
            note.setVelocity(100);
            note.setPosition(i + 1);

            notesToInsert.add(note);
        }

        return notesToInsert;
    }

    public void askQuestion() {
        TextView apprenticeText = (TextView) findViewById(R.id.apprentice_text);

        apprenticeText.setText("Do these notes fit for a "
                + chosenEmotion.getName() + " mood?");
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
        KeyNotesDataSource knds = new KeyNotesDataSource(this);

        switch (v.getId()) {
            case R.id.button_no:
                Log.d("MYLOG", "10. if 'no':");
                guessesIncorrect++;

                totalGuesses = guessesCorrect + guessesIncorrect;

                if (totalGuesses > 0) {
                    guessesCorrectPercentage = ((double) guessesCorrect / (double) totalGuesses) * 100.0;
                }

                // iterate through notes shown to user
                for (int i = 0; i < notesToInsert.size(); i++) {
                    // check if note is already in table set
                    // knds.getKeyNoteNotevaluesByKeySignature(currentKeySignatureId);
                    List<KeyNote> keyNotes = knds.getKeyNotesByKeySignature(currentKeySignatureId);

                    for (KeyNote kn : keyNotes) {
                        if (kn.getNotevalue() == notesToInsert.get(i).getNotevalue()) {
                            // note found in this key signature
                            if ((kn.getWeight() + 0.1f) >= 1.0f) {
                                // prune note from table set (once weight gets past certain value)
                                knds.deleteKeyNote(kn);
                                Log.d("MYLOG", "deleting keynote -- weight is >= 1.0f");
                            } else {
                                // raise weight of note in table set
                                kn.setWeight(kn.getWeight() + 0.1f);
                                knds.updateKeyNote(kn);
                                Log.d("MYLOG",
                                        "updating key note -- weight is raised to: "
                                                + kn.getWeight());
                            }
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
                Log.d("MYLOG", "11. if 'yes':");
                guessesCorrect++;

                totalGuesses = guessesCorrect + guessesIncorrect;

                if (totalGuesses > 0) {
                    guessesCorrectPercentage = ((double) guessesCorrect / (double) totalGuesses) * 100.0;
                }

                // iterate through notes shown to user
                for (int i = 0; i < notesToInsert.size(); i++) {
                    // check if note is already in table set
                    // knds.getKeyNoteNotevaluesByKeySignature(currentKeySignatureId);
                    List<KeyNote> keyNotes = knds.getKeyNotesByKeySignature(currentKeySignatureId);

                    boolean found = false;
                    for (KeyNote kn : keyNotes) {
                        if (kn.getNotevalue() == notesToInsert.get(i).getNotevalue()) {
                            found = true;
                            // note found in this key signature
                            if ((kn.getWeight() + 0.1f) >= 0.0f) {
                                // lower weight of note in table set
                                kn.setWeight(kn.getWeight() - 0.1f);
                                knds.updateKeyNote(kn);
                                Log.d("MYLOG",
                                        "updating key note -- weight is lowered to: "
                                                + kn.getWeight());
                            }
                        }
                    }
                    if (!found) {
                        // note not found in this key signature
                        // add note to table set
                        KeyNote newNote = new KeyNote();
                        newNote.setKeySignatureId(currentKeySignatureId);
                        newNote.setNotevalue(notesToInsert.get(i).getNotevalue());
                        newNote.setWeight(0.5f);
                        newNote.setApprenticeId(apprenticeId);
                        knds.createKeyNote(newNote);
                        Log.d("MYLOG", "adding new key note: " + newNote.getNotevalue());
                    }
                }

                // check if achievement was earned in play mode
                if (programMode == 2) {
                    List<KeyNote> keyNotes = knds.getKeyNotesByKeySignature(currentKeySignatureId);

                    if (keyNotes.size() >= notesToCompleteScale) {
                        String key = String.valueOf(currentKeySignatureId);
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
                            achievement.setName("completed_scale");
                            achievement.setApprenticeId(apprenticeId);
                            achievement.setEarnedOn(earnedOnISO);
                            achievement.setKey(key);

                            ads.createAchievement(achievement);

                            Context context = getApplicationContext();
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(
                                    context,
                                    context.getResources().getString(
                                            R.string.achievement_completed_scale),
                                    duration);
                            toast.show();
                        }

                        ads.close();
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
        chosenEmotion = eds.getRandomEmotion(apprenticeId);
        eds.close();

        emotionId = chosenEmotion.getId();

        // clear old generated notes
        notesToInsert.clear();

        // 1. select random notevalue
        Log.d("MYLOG", "1. select random notevalue");
        // 60..71 (C4..B4)
        // stay within 39..50 for now (C4..B4)
        Note anchorNote = getRandomNote(39, 50);

        // 2. check to see if a key signature contains notevalue
        Log.d("MYLOG", "2. check to see if a key signature contains notevalue");
        KeyNotesDataSource knds = new KeyNotesDataSource(this);
        List<Long> keySignatureIds = knds.keySignatureIdsThatContain(apprenticeId,
                anchorNote.getNotevalue());

        List<Note> notes = new ArrayList<Note>();
        // long currentKeySignature = 0;
        String approach = "";

        // 3. if no key signatures exist, create one and put notevalue into key signature
        Log.d("MYLOG",
                "3. if no key signatures exist, create one and put notevalue into key signature");
        if (keySignatureIds.isEmpty()) {
            approach = "Random";
            KeySignature ks = new KeySignature();
            ks.setEmotionId(emotionId);
            KeySignaturesDataSource ksds = new KeySignaturesDataSource(this);
            ks = ksds.createKeySignature(ks);
            KeyNote kn = new KeyNote();
            kn.setKeySignatureId(ks.getId());
            kn.setNotevalue(anchorNote.getNotevalue());
            kn.setWeight(0.5f);
            kn.setApprenticeId(apprenticeId);
            knds.createKeyNote(kn);
            currentKeySignatureId = ks.getId();
        } else {
            approach = "Learned Data";
            if (keySignatureIds.size() > 1) {
                // get random id from list
                int randomPosition = new Random().nextInt(((keySignatureIds.size() - 1) - 0));
                currentKeySignatureId = keySignatureIds.get(randomPosition);
            } else {
                currentKeySignatureId = keySignatureIds.get(0);
            }
        }

        // 4. select all notes from selected key signature
        Log.d("MYLOG", "4. select all notes from selected key signature");
        List<Integer> currentKeySignatureNotes = knds
                .getKeyNoteNotevaluesByKeySignature(currentKeySignatureId);

        // 5. sort notes in list
        Log.d("MYLOG", "5. sort notes in list");
        Collections.sort(currentKeySignatureNotes);

        Log.d("MYLOG", "current key signature: " + currentKeySignatureId);
        Log.d("MYLOG", "current key signature notes: " + currentKeySignatureNotes.toString());

        // 6. choose an extra note
        Log.d("MYLOG", "6. choose an extra note");

        boolean foundExistingNote = false;

        // avoid duplicate notes in key signature list
        Note extraNote = new Note();
        for (int i = 0; i < 3; i++) {
            Log.d("MYLOG", "attempting to avoid duplicate notes in key signature list");
            // random approach
            extraNote = getRandomNote(39, 50);

            for (int notevalue : currentKeySignatureNotes) {
                if (notevalue == extraNote.getNotevalue()) {
                    foundExistingNote = true;
                }
            }

            if (!foundExistingNote) {
                notesToInsert.clear();
                notesToInsert.add(extraNote);
            }
        }

        // 7. add extra note to key signature notes list
        Log.d("MYLOG", "7. add extra note to key signature notes list");
        currentKeySignatureNotes.add(extraNote.getNotevalue());

        // 8. play all notes from set for user
        Log.d("MYLOG", "8. play all notes from set for user");
        for (int i = 0; i < currentKeySignatureNotes.size(); i++) {
            Note note = new Note();
            note.setNotevalue(currentKeySignatureNotes.get(i));
            note.setLength(1.0f);
            note.setVelocity(100);
            note.setPosition(i + 1);
            notes.add(note);
        }

        // get default instrument for playback
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String defaultInstrument = sharedPref.getString("pref_default_instrument", "");
        int playbackSpeed = Integer.valueOf(sharedPref.getString("pref_default_playback_speed",
                "120"));

        GenerateMusicActivity generateMusic = new GenerateMusicActivity();
        generateMusic.generateMusic(notes, musicSource, defaultInstrument, playbackSpeed);

        // 9. does last generated note fit in selected key set for this emotion?
        Log.d("MYLOG", "9. does last generated note fit in selected key set for this emotion?");
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

            ApprenticeScoresDataSource asds = new ApprenticeScoresDataSource(this);
            asds.createApprenticeScore(aScore);
            asds.close();
        } else {
            Log.d("MYLOG", "Not saving scorecard.");
        }
    }

    public long getScaleGraphId() {
        return scaleGraphId;
    }

    public void setScaleGraphId(long scaleGraphId) {
        this.scaleGraphId = scaleGraphId;
    }
}
