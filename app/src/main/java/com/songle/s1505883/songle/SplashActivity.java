package com.songle.s1505883.songle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.net.MalformedURLException;
import globals.GlobalLambdas;
import globals.DownloadLinks;
import tools.DownloadConsumer;

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
