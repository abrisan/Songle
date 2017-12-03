package com.songle.s1505883.songle;

import android.app.Activity;
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

    private final DebugMessager console = DebugMessager.getInstance();

    private void _init_seekBar()
    {
        this . seekBar = (SeekBar) this . findViewById(R.id.seekBar2);

        this . seekBar . setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                console . info(String.format("Progress is %d", progress));
                name . setText(GlobalConstants.difficulty_levels[progress]);
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
        startActivity(
                new Intent(this, MainActivity.class)
        );
    }
}
