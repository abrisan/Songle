package com.songle.s1505883.songle;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import tools.DebugMessager;

public class MainActivity extends Activity
{
    private static DebugMessager console = DebugMessager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void settingsClicked(View view)
    {
        console . info("Settings Clicked");
    }

    public void playClicked(View view)
    {
        console . info("Play clicked");
    }

    public void wordlistClicked(View view)
    {
        console . info("wordlist clicked");
    }

    public void guessedClicked(View view)
    {
        console.info("guessed clicked");
    }
}
