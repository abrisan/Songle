package com.songle.s1505883.songle;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import globals.GlobalConstants;
import tools.DebugMessager;

public class GuessSongActivity extends Activity
{

    private final static DebugMessager console = DebugMessager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_song);
    }

    private boolean isCorrectAnswer(String name, String artist)
    {
        return name . toLowerCase() . equals("yes") && artist . toLowerCase() . equals("yes");
    }

    public void makeGuessClicked(View v)
    {
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

        TextView songNameView = (TextView) this . findViewById(R.id.songName);
        TextView artistNameView = (TextView) this . findViewById(R.id.artitst_name);

        String guessed = songNameView . getText() . toString();
        String artist = artistNameView . getText() . toString();

        console . debug_output(guessed);
        console . debug_output(artist);

        boolean isCorrect = isCorrectAnswer(guessed, artist);

        if (isCorrect)
        {
            setBackgroundColor.accept(
                    this . findViewById(R.id.guessPopup),
                    GlobalConstants.COLOR_GREEN_HEX
            );

        }
        else
        {
            setBackgroundColor.accept(
                    this . findViewById(R.id.guessPopup),
                    GlobalConstants.COLOR_RED_HEX
            );
        }
    }
}
