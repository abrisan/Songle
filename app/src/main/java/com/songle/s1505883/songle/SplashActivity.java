package com.songle.s1505883.songle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import globals.GlobalConstants;
import globals.GlobalState;
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
