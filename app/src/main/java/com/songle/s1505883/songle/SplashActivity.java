package com.songle.s1505883.songle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import database.DatabaseWriteTask;
import database.AppDatabase;
import datastructures.SongDescriptor;
import globals.GlobalLambdas;
import tools.DebugMessager;
import tools.DownloadLinks;
import tools.Downloader;
import tools.SongListParser;

public class SplashActivity extends Activity
{

    private boolean _shouldDisplayWelcome()
    {
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        try
        {
            new Downloader(this, GlobalLambdas.initialCheckAndDownload).execute(
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
