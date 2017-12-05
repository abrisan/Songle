package com.songle.s1505883.songle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URL;

import database.AppDatabase;
import database.DatabaseReadTask;
import datastructures.CurrentGameDescriptor;
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
    String difficulty;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        console . debug_method_trace(this, "onCreate", "starting to eval onCreate");

        _load_from_prefs();

        if (savedInstanceState == null)
        {
            console . debug_method_trace(this, "onCreate", "savedInstanceState is null");
            this . des = CurrentGameDescriptor.getInstanceForContext(this);
            if (this . des == null)
            {
                /*Snackbar.make(
                        this.findViewById(R.id.activity_main),
                        "No game saved. Selecting random game",
                        10
                );
                */
                console . debug_method_trace(this, "onCreate", "creating random game");
                new DatabaseReadTask<>(
                        AppDatabase.getAppDatabase(this),
                        this::setDes
                ).execute(t -> t.songDao().getRandomUnguessedSong());
            }
        }
        else
        {
            console . debug_method_trace(this, "onCreate", "usingParcelable");
            this . des = savedInstanceState.getParcelable(GlobalConstants.currentGameKey);
        }

        console . flushMethodTrace();

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
        console . debug_method_trace(this, "onLyricsDownloaded");
        try
        {
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
        console . debug_method_trace(this, "onGameChanged");
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
        console . debug_method_trace(this, "load_from_prefs");
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

        String onlineURL = prefs . getString(
                GlobalConstants.onlineImageURL,
                null
        );

        this . difficulty = prefs . getString(
                GlobalConstants.diffKey,
                "Beginner"
        );

        if (userURI != null)
        {
            Uri uri = Uri.parse(userURI);
            ((ImageView) this . findViewById(R.id.main_profile_pic)).setImageURI(
                    uri
            );
        }

        if (onlineURL != null && !onlineURL.equals("null"))
        {
            try
            {
                new DownloadFunction<>(
                        x -> BitmapDrawable.createFromStream(x, "fbPhoto"),
                        this::setImageDrawable
                ).execute(
                        new URL(onlineURL)
                );
            }
            catch(MalformedURLException e)
            {
                e . printStackTrace();
            }

        }

        ((TextView) this . findViewById(R.id.main_user_name)).setText(userName);
    }

    private void setImageDrawable(Drawable drw)
    {
        ((ImageView) this . findViewById(R.id.main_profile_pic)).setBackground(
                drw
        );
    }

    private void setDes(SongDescriptor des)
    {
        if (des == null)
        {
            console . error("Got null from query");
        }
        this . des = new CurrentGameDescriptor(des, this . difficulty);
        onGameChanged();
    }

    private void saveState()
    {
        console . debug_method_trace(this, "saveState");
        SharedPreferences prefs = getSharedPreferences(
                getString(
                        R.string.shared_prefs_key
                ),
                Context.MODE_PRIVATE
        );

        SharedPreferences.Editor editor = prefs.edit();

        try
        {
            editor.putString(
                    getString(R.string.current_game_index),
                    this . des . getDescriptor() . serialise()
            );
            editor.apply();
        }
        catch (JSONException e)
        {
            e . printStackTrace();
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        saveState();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        saveState();
    }

}
