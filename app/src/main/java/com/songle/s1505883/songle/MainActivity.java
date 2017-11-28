package com.songle.s1505883.songle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import tools.DebugMessager;

public class MainActivity extends Activity
{
    private static DebugMessager console = DebugMessager.getInstance();
    private String difficulty;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this . prefs = getSharedPreferences(
                getString(R.string.shared_prefs_key),
                Context.MODE_PRIVATE
        );

        this . difficulty = this . prefs . getString(
                getString(R.string.difficulty_level),
                null
        );


    }

    @Override
    public boolean onCreateOptionsMenu(Menu m)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.barmenu, m);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item . getItemId())
        {
            case R.id.action_settings:
            {
                Intent move_to_settings = new Intent(this, SettingsActivity.class);
                move_to_settings . putExtra(SettingsActivity.EXTRA_NO_HEADERS, true);
                move_to_settings . putExtra(
                        SettingsActivity.EXTRA_SHOW_FRAGMENT,
                        SettingsActivity.GeneralPreferenceFragment.class.getName()
                );
                startActivity(move_to_settings);
            }
            default:
            {

            }
        }
        return true;
    }

    public void playClicked(View view)
    {
        Intent move_to_map = new Intent(this, PlayActivity.class);
        startActivity(move_to_map);
    }

    public void wordlistClicked(View view)
    {
        Intent move_to_wordlist = new Intent(this, WordlistActivity.class);
        startActivity(move_to_wordlist);
    }

    public void guessedClicked(View view)
    {
        Intent move_to_player = new Intent(this, GuessedSongsActivity.class);
        startActivity(move_to_player);
    }

    public void tradeClicked(View view)
    {
        Intent move_to_trade = new Intent(this, TradeActivity.class);
        startActivity(move_to_trade);
    }
}
