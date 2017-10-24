package com.songle.s1505883.songle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

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
        Intent move_to_preferences = new Intent(this, PreferencesActivity.class);
        startActivity(move_to_preferences);
    }

    public void playClicked(View view)
    {
        Intent move_to_map = new Intent(this, PlayActivity.class);
        startActivity(move_to_map);
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
