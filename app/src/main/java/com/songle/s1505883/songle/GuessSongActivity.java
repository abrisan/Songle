package com.songle.s1505883.songle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import globals.GlobalConstants;
import tools.Algorithm;
import tools.DebugMessager;

public class GuessSongActivity extends Activity
{

    private final static DebugMessager console = DebugMessager.getInstance();
    private String targetArtist;
    private String targetSong;
    private int trialCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_song);
        try
        {
            // get target artist and song from intent, might throw a NullPointerException
            // so we just wrap it in a try/catch
            this . targetArtist = getIntent().getExtras().getString("artist");
            this . targetSong = getIntent().getExtras().getString("name");
        }
        catch (NullPointerException e)
        {
            e . printStackTrace();
        }
    }

    private boolean isCorrectAnswer(String name, String artist)
    {
        // Use the encapsulated shouldAccept method
        boolean isOk = Algorithm.StringUtils.shouldAccept(targetSong, name)
                && Algorithm.StringUtils.shouldAccept(targetArtist, artist);

        // create a result intent
        Intent result = new Intent();
        result.putExtra("accepted", isOk);

        // set the result of the appropriate parent
        if (getParent() == null)
        {
            setResult(Activity.RESULT_OK, result);
        }
        else
        {
            getParent().setResult(Activity.RESULT_OK, result);
        }

        return isOk;
    }

    public void makeGuessClicked(View v)
    {
        // Lambda for what happens when the view is clicked
        BiConsumer<View, String> setBackgroundColor = (view, x) ->
        {
            view . setBackgroundColor(
                    Color.parseColor(
                            x
                    )
            );

            ((TextView) view . findViewById(R.id.songName)) . setTextColor(
                    Color.parseColor(
                            GlobalConstants.COLOR_WHITE
                    )
            );

            ((TextView) view . findViewById(R.id.artitst_name)) . setTextColor(
                    Color.parseColor(
                            GlobalConstants.COLOR_WHITE
                    )
            );
        };


        console . info("Guess clicked");

        // Getting refs to the two fields
        TextView songNameView = (TextView) this . findViewById(R.id.songName);
        TextView artistNameView = (TextView) this . findViewById(R.id.artitst_name);

        // etting the guessed
        String guessed = songNameView . getText() . toString();
        String artist = artistNameView . getText() . toString();

        console . debug_output(guessed);
        console . debug_output(artist);

        // Should we accept?
        boolean isCorrect = isCorrectAnswer(guessed, artist);

        // Set the green/red color
        if (isCorrect)
        {
            setBackgroundColor.accept(
                    this . findViewById(R.id.guessPopup),
                    GlobalConstants.COLOR_GREEN_HEX
            );

            finish();

        }
        else
        {
            if (trialCount == 2)
            {
                // create a new result, indicating that the app should query whether the user wants
                // to give up on the current game
                Intent new_result = new Intent();
                new_result . putExtra("accepted", false);
                new_result . putExtra("queryCancel", true);

                if (getParent() == null)
                {
                    setResult(Activity.RESULT_OK, new_result);
                }
                else
                {
                    getParent().setResult(Activity.RESULT_OK, new_result);
                }

                finish();
            }
            setBackgroundColor.accept(
                    this . findViewById(R.id.guessPopup),
                    GlobalConstants.COLOR_RED_HEX
            );
            // do not finish just here, let the user guess more times
            ++trialCount;
        }
    }


}
