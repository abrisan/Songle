package com.songle.s1505883.songle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import database.AppDatabase;
import database.DatabaseReadTask;
import database.DatabaseWriteTask;
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
    private String text = "Congratulations! Would you like to start a new game?";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        console . debug_method_trace(this, "onCreate", "starting to eval onCreate");

        // Load profile pic, user name and current song descriptor from the preferences
        _load_from_prefs();

        // handle a saved instance
        if (savedInstanceState == null)
        {
            console . debug_method_trace(this, "onCreate", "savedInstanceState is null");
            this . des = CurrentGameDescriptor.getInstanceForContext(this);
            if (this . des == null)
            {
                Toast.makeText(this, "No game saved. Selecting random game", Toast.LENGTH_SHORT).show();
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
        // inflate the menu
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
                // move to the settings activity if user presses that button
                Intent move_to_settings = new Intent(this, SettingsActivity.class);
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
        // move to the map activity
        Intent move_to_map = new Intent(this, PlayActivity.class);
        move_to_map . putExtra(GlobalConstants.gameDescriptor, this . des);
        startActivity(move_to_map);
    }

    public void wordlistClicked(View view)
    {
        // move to guessed words activity
        Intent move_to_wordlist = new Intent(this, WordlistActivity.class);
        move_to_wordlist . putExtra(GlobalConstants.gameDescriptor, this . des);
        startActivityForResult(move_to_wordlist, 1);
    }

    public void guessedClicked(View view)
    {
        // move to guessed songs activity (Player)
        Intent move_to_player = new Intent(this, GuessedSongsActivity.class);
        startActivity(move_to_player);
    }

    public void tradeClicked(View view)
    {
        // move to trade activity
        Intent move_to_trade = new Intent(this, TradeActivity.class);
        move_to_trade . putExtra(GlobalConstants.gameDescriptor, this . des);
        startActivity(move_to_trade);
    }


    protected void onLyricsDownloaded(SongLyricsDescriptor lyricsDescriptor)
    {
        console . debug_method_trace(this, "onLyricsDownloaded");
        try
        {
            // we have the lyrics, so we cand downaload the maps
            new DownloadConsumer(
                    this,
                    GlobalLambdas.getMaps.apply(this . des . getMapNumber(), this.des.getSongNumber(), lyricsDescriptor)
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
            // download lyrics for new game, with the appropriate callback
            new DownloadFunction<>(
                    GlobalLambdas.getLyrics,
                    this::onLyricsDownloaded
            ).execute(
                    DownloadLinks.getSongLyricsLinkForSong(
                            this.des.getSongNumber()
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

        // get the username
        String userName = prefs . getString(
                GlobalConstants.userName,
                "John Doe"
        );

        // get the image uri. this will be appropriate if the user selected a pic from the
        // gallery on the device
        String userURI = prefs . getString(
                GlobalConstants.imageURI,
                null
        );

        // get an online url, in case the user used FB to log in
        String onlineURL = prefs . getString(
                GlobalConstants.onlineImageURL,
                null
        );

        // get the current difficulty
        this . difficulty = prefs . getString(
                GlobalConstants.diffKey,
                "Beginner"
        );

        console . debug_output(this.difficulty);
        console . debug_output(userName);

        if (userURI != null && !userURI.equals("null"))
        {
            console . debug_output(userURI);
            Uri uri = Uri.parse(userURI);
            ((ImageView) this . findViewById(R.id.main_profile_pic)).setImageURI(
                    uri
            );
        }

        else if (onlineURL != null && !onlineURL.equals("null"))
        {
            try
            {
                // download the image and then create the profile pic
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
        ((ImageView) findViewById(R.id.main_profile_pic)).setImageURI(
                null
        );

        ((ImageView) this . findViewById(R.id.main_profile_pic)).setImageDrawable(
                drw
        );
    }

    private void setDes(SongDescriptor des)
    {
        // got new random song
        if (des == null)
        {
            console . error("Got null from query");
        }

        // create the game descriptor
        this . des = new CurrentGameDescriptor(des, this . difficulty);

        // save the state
        saveState();

        // handle the game change
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
            // serialise the current game descriptor and save the string
            // in the editor
            // there is not really a reason that we should expect this to fail.
            editor.putString(
                    getString(R.string.current_game_index),
                    this . des . getDescriptor() . serialise()
            );
            editor.commit();
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
        console . debug_trace(this, "onPause");
        saveState();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        console . debug_trace(this, "onStop");
        saveState();
    }

    public void queryNewGame()
    {
        new AlertDialog.Builder(this)
                .setTitle("New gane")
                .setMessage(this . text)
                .setPositiveButton("Yes", this::onWantNewGame)
                .setNegativeButton("No", this::onNoNewGame)
                .show();

        this . text = "Congratulations! Would you like to start a new game?";
    }

    public void onWantNewGame(DialogInterface dialog, int which)
    {
        // get a random unguessed song
        new DatabaseReadTask<>(
                AppDatabase.getAppDatabase(this),
                this::setDes
        ).execute(t -> t.songDao().getRandomUnguessedSong());

        ((ImageView) findViewById(R.id.main_profile_pic)).setOnClickListener(
                v -> console . debug_output("Clicked profile pic")
        );
    }

    public void onNoNewGame(DialogInterface dialog, int which)
    {
        Toast.makeText(
                this,
                "You can create a new game at any time by clicking the profile picture",
                Toast.LENGTH_SHORT
        ).show();

        ((ImageView) findViewById(R.id.main_profile_pic)).setOnClickListener(
                v -> this.onWantNewGame(null, 0)
        );
        // do nothing
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        console . debug_trace(this, "onActivityResult");
        new DatabaseReadTask<>(
                AppDatabase.getAppDatabase(this),
                (list) -> console . debug_output(list)
        ).execute(db -> db . locationDao() . getDiscoveredLocations());
        if (requestCode == 1)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                if (data.getExtras().getBoolean("guessed"))
                {
                    updateSongDb();
                }
            }
        }
    }

    public void updateSongDb()
    {
        SongDescriptor currentDes = this . des . getDescriptor();
        currentDes.setGuessed(true);

        new DatabaseWriteTask<SongDescriptor>(
                AppDatabase.getAppDatabase(this),
                (db, song) -> db.songDao().updateSong(song),
                this::queryNewGame
        ).execute(currentDes);
    }

    @Override
    public void onResume()
    {
        super . onResume();
        console . debug_trace(this, "onResume");
        _load_from_prefs();
    }

}
