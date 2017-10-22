package com.songle.s1505883.songle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Intent transition_to_main_screen = new Intent(this, MainActivity.class);
        startActivity(transition_to_main_screen);
        finish();
    }
}
