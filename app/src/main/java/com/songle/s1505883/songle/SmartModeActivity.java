package com.songle.s1505883.songle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import globals.GlobalConstants;
import tools.DebugMessager;
import tools.ImportanceParser;

public class SmartModeActivity extends Activity
{

    private Map<String, Double> word_importances;
    private List<String> ordered_words;
    private int current_index = 0;
    private DebugMessager console = DebugMessager.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_mode);

        // importances as computed from NLTK
        this . word_importances = ImportanceParser.parse(this);

        this . ordered_words = new ArrayList<>();

        // create a list of words that is ordered
        this . ordered_words.addAll(
                this . word_importances . keySet()
        );

        this.ordered_words.sort(
                Comparator.comparingDouble(this.word_importances::get)
        );

        ((TextView) findViewById(R.id.current_word_view)).setText(
                this.ordered_words.get(
                        current_index
                )
        );
    }

    public void onCancelClicked(View view)
    {
        // cancel, set beginner as difficulty
        getSharedPreferences(
                getString(R.string.shared_prefs_key),
                Context.MODE_PRIVATE
        ).edit().putString(GlobalConstants.diffKey, "Beginner").commit();

        startActivity(
                new Intent(this, MainActivity.class)
        );
    }

    public void onNextWordClicked(View view)
    {
        // present new word
        if (current_index + 1 < ordered_words.size())
        {
            ++current_index;
            ((TextView) findViewById(R.id.current_word_view)).setText(
                    this.ordered_words.get(
                            current_index
                    )
            );
        }
    }

    public void onGuessClicked(View view)
    {
        // use the guess song activity, functionlity should be the same
        Intent move = new Intent(this, GuessSongActivity.class);

        move . putExtra("artist", "abba");
        move . putExtra("name","sos");

        startActivityForResult(
                move,
                1
        );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 1)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                try
                {
                    if (!data.getExtras().getBoolean("accepted"))
                    {
                        // incorrect guess
                        Toast.makeText(this, "Wrong guess", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        // handle a correct guess
                        _guessed();
                    }
                }
                catch (NullPointerException e)
                {

                }
            }
        }
    }

    public void _guessed()
    {
        // see where we stopped
        int progress = (int) Math.ceil(
                ((double) this . current_index) / (this . ordered_words . size()) * 100
        );

        int index = 4 - (int) Math.ceil(((double) progress) / 25);

        // put the relevant difficulty

        getSharedPreferences(
                getString(R.string.shared_prefs_key),
                Context.MODE_PRIVATE
        ).edit().putString(GlobalConstants.diffKey, GlobalConstants.difficulty_levels[index]).commit();

        // move to the game
        startActivity(
                new Intent(this, MainActivity.class)
        );

    }
}
