package com.songle.s1505883.songle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.function.Consumer;

import globals.GlobalConstants;
import tools.DebugMessager;

public class DifficultyActivity extends Activity
{

    SeekBar seekBar;
    TextView name;
    TextView description;
    String currentDifficulty = "Beginner";

    private final DebugMessager console = DebugMessager.getInstance();

    private void _init_seekBar()
    {
        this . seekBar = (SeekBar) this . findViewById(R.id.seekBar2);


        this . seekBar . setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                name . setText(GlobalConstants.difficulty_levels[progress]);
                currentDifficulty = GlobalConstants.difficulty_levels[progress];
                switch(currentDifficulty)
                {
                    case "Beginner":
                    {
                        description . setText(
                                R.string.beginner_description
                        );
                        break;
                    }
                    case "Intermediate":
                    {
                        description . setText(
                                R.string.intermediate
                        );
                        break;
                    }
                    case "Advanced":
                    {
                        description . setText(
                                R.string.advanced
                        );
                        break;
                    }
                    case "Expert":
                    {
                        description . setText(
                                R.string.expert
                        );
                        break;
                    }
                    case "Champion":
                    {
                        description . setText(
                                R.string.champion
                        );
                        break;
                    }
                    case "Smart Mode":
                    {
                        description . setText(
                                R.string.smart_mode_description
                        );
                        break;
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });
    }

    private void _init_textView()
    {
        this . name = (TextView) this . findViewById(R . id . difficultyName);
        this . description = (TextView) this . findViewById(R.id.difficultyDescription);
    }

    private void _init_widgets()
    {
        _init_textView();
        _init_seekBar();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);

        _init_widgets();
    }

    public void finishClicked(View view)
    {
        getSharedPreferences(
                getString(R.string.shared_prefs_key),
                Context.MODE_PRIVATE
        ).edit().putString(GlobalConstants.diffKey, this.currentDifficulty).commit();

        startActivity(
                new Intent(this, MainActivity.class)
        );
    }
}
