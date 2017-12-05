package com.songle.s1505883.songle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Debug;

import java.net.MalformedURLException;
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
            editor . apply();
        }

        DebugMessager.getInstance().error(String.valueOf(is_first_launch));

        return is_first_launch;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

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
