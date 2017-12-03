package com.songle.s1505883.songle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Collections;
import java.util.List;

import database.AppDatabase;
import database.DatabaseReadTask;
import datastructures.CurrentGameDescriptor;
import datastructures.Pair;
import datastructures.SongDescriptor;
import datastructures.SongLyricsDescriptor;
import globals.DownloadLinks;
import globals.GlobalConstants;
import globals.GlobalLambdas;
import tools.DebugMessager;
import tools.DownloadConsumer;
import tools.DownloadFunction;

public class MainActivity extends Activity
{
    private static DebugMessager console = DebugMessager.getInstance();
    CurrentGameDescriptor des;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _load_from_prefs();

        new DatabaseReadTask<SongDescriptor>(
                AppDatabase.getAppDatabase(this),
                t -> {
                    if (t == null)
                    {
                        console . error("Null from query");
                    }
                    else
                    {
                        console .debug_output_json(Collections.singletonList(t));
                    }
                }
        ).execute(t -> t.songDao().getRandomUnguessedSong());


        if (savedInstanceState == null)
        {
            this . des = CurrentGameDescriptor.getInstanceForContext(this);
            onGameChanged();
        }
        else
        {
            this . des = savedInstanceState.getParcelable(GlobalConstants.currentGameKey);
        }

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
        move_to_map . putExtra(GlobalConstants.gameDescriptor, this . des);
        startActivity(move_to_map);
    }

    public void wordlistClicked(View view)
    {
        Intent move_to_wordlist = new Intent(this, WordlistActivity.class);
        move_to_wordlist . putExtra(GlobalConstants.gameDescriptor, this . des);
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
        move_to_trade . putExtra(GlobalConstants.gameDescriptor, this . des);
        startActivity(move_to_trade);
    }


    protected void onLyricsDownloaded(SongLyricsDescriptor lyricsDescriptor)
    {
        try
        {
            console . debug_trace(this, "onLyricsDownloaded");
            new DownloadConsumer(
                    this,
                    GlobalLambdas.getMaps.apply(this . des . getMapNumber(), lyricsDescriptor)
            ).execute(
                    this . des . getCurrentDifficulty()
            );
        }
        catch (Exception e)
        {
            e . printStackTrace();
        }
    }

    protected void onGameChanged()
    {
        console . debug_trace(this, "onGameChanged");
        try
        {
            new DownloadFunction<>(
                    GlobalLambdas.getLyrics,
                    this::onLyricsDownloaded
            ).execute(
                    DownloadLinks.getSongLyricsLinkForSong(
                            this.des.getSongNumber() + 1
                    )
            );
        }
        catch (MalformedURLException e)
        {
            e . printStackTrace();
        }
    }

    private void _load_from_prefs()
    {
        SharedPreferences prefs = getSharedPreferences(
                getString(R.string.shared_prefs_key),
                Context.MODE_PRIVATE
        );

        String userName = prefs . getString(
                GlobalConstants.userName,
                "John Doe"
        );


        String userURI = prefs . getString(
                GlobalConstants.imageURI,
                null
        );

        if (userURI != null)
        {
            Uri uri = Uri.parse(userURI);
            ((ImageView) this . findViewById(R.id.main_profile_pic)).setImageURI(
                    uri
            );
        }

        ((TextView) this . findViewById(R.id.main_user_name)).setText(userName);
    }

}
