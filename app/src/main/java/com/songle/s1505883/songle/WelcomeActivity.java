package com.songle.s1505883.songle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import tools.DebugMessager;

public class WelcomeActivity extends Activity
{
    LoginButton loginButton;
    CallbackManager callbackManager;
    private final static DebugMessager console = DebugMessager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult)
            {
                console . info("Facebook success");
            }

            @Override
            public void onCancel()
            {
                console . info("Facebook cancel");
            }

            @Override
            public void onError(FacebookException error)
            {
                console . info("Facebook error");
            }
        });

    }

    public void nextClicked(View v)
    {
        Intent next = new Intent(this, GuideActivity.class);
        startActivity(next);
        // finish();
    }
}
