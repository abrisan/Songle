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
import java.util.List;

import database.AppDatabase;
import database.DatabaseReadTask;
import database.DatabaseWriteTask;
import datastructures.SongDescriptor;
import globals.GlobalLambdas;
import globals.DownloadLinks;
import tools.DebugMessager;
import tools.DownloadConsumer;

public class SplashActivity extends Activity
{

    public final static String FIRST_LAUNCH_KEY = "firstLaunch";

    private boolean _shouldDisplayWelcome()
    {
        SharedPreferences prefs = getSharedPreferences(
                getString(R.string.shared_prefs_key),
                Context.MODE_PRIVATE
        );

        // check if it is the first launch (i.e. do we need to display the onboarding)
        return prefs.getBoolean(FIRST_LAUNCH_KEY, true);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // queryNewGame(); // uncomment for debug purposes

        this . continueWorkflow();
    }

    public void queryNewGame()
    {
        // Create an alert dialg (only for debug purposes)
        new AlertDialog.Builder(this)
                .setTitle("New gane")
                .setMessage("Would you like to erase the sharedprefs?")
                .setPositiveButton("Yes", this::onWantNewGame)
                .setNegativeButton("No", this::onNoNewGame)
                .show();
    }

    public void onWantNewGame(DialogInterface dialog, int which)
    {
        // Clear the shared preferences
        getSharedPreferences(
                getString(
                        R.string.shared_prefs_key
                ),
                Context.MODE_PRIVATE
        ).edit().clear().commit();

        // Nuke the DB
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
            new DownloadConsumer(
                    this,
                    GlobalLambdas.initialCheckAndDownload,
                    this::haveDownloaded
            ).execute(
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
    }

    private void haveDownloaded()
    {
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
