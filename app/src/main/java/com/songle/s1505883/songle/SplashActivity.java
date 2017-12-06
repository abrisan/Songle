package com.songle.s1505883.songle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Debug;

import java.net.MalformedURLException;

import database.AppDatabase;
import database.DatabaseReadTask;
import database.DatabaseWriteTask;
import globals.GlobalLambdas;
import globals.DownloadLinks;
import tools.DebugMessager;
import tools.DownloadConsumer;

public class SplashActivity extends Activity
{

    private final static String FIRST_LAUNCH_KEY = "firstLaunch";

    private boolean _shouldDisplayWelcome()
    {
        SharedPreferences prefs = getSharedPreferences(
                getString(R.string.shared_prefs_key),
                Context.MODE_PRIVATE
        );

        boolean is_first_launch = prefs.getBoolean(FIRST_LAUNCH_KEY, true);

        if (is_first_launch)
        {
            DebugMessager.getInstance().error("Setting new shared prefs");
            SharedPreferences.Editor editor = prefs.edit();
            editor . putBoolean(FIRST_LAUNCH_KEY, false);
            if (!editor . commit())
            {
                throw new RuntimeException("Could not save first launch to persistent storage");
            }
        }

        DebugMessager.getInstance().error(String.valueOf(is_first_launch));
        DebugMessager.getInstance().error(String.valueOf(prefs.getBoolean(FIRST_LAUNCH_KEY, true)));

        return is_first_launch;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        queryNewGame();

    }

    public void queryNewGame()
    {
        new AlertDialog.Builder(this)
                .setTitle("New gane")
                .setMessage("Would you like to erase the sharedprefs?")
                .setPositiveButton("Yes", this::onWantNewGame)
                .setNegativeButton("No", this::onNoNewGame)
                .show();
    }

    public void onWantNewGame(DialogInterface dialog, int which)
    {
        getSharedPreferences(
                getString(
                        R.string.shared_prefs_key
                ),
                Context.MODE_PRIVATE
        ).edit().clear().commit();

        try
        {
            new DatabaseWriteTask<>(
                    AppDatabase.getAppDatabase(this),
                    (db, x) ->
                    {
                        db.songDao().nukeDB();
                        db.locationDao().nukeDB();
                    }
            ).execute((Void) null);

            continueWorkflow();
        }
        catch (Exception e)
        {
            e . printStackTrace();
        }

    }

    public void onNoNewGame(DialogInterface dialog, int which)
    {
        continueWorkflow();
    }

    public void continueWorkflow()
    {
        try
        {
            new DownloadConsumer(this, GlobalLambdas.initialCheckAndDownload).execute(
                    DownloadLinks.getSongListLink()
            );
        }
        catch (MalformedURLException e)
        {
            e . printStackTrace();
        }
        catch (Exception e)
        {
            throw e;
        }

        Intent transition;

        if (_shouldDisplayWelcome())
        {
            transition = new Intent(this, WelcomeActivity.class);
        }
        else
        {
            transition = new Intent(this, MainActivity.class);
        }

        startActivity(transition);
        finish();
    }
}
