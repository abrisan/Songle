package com.songle.s1505883.songle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.io.FileNotFoundException;
import java.io.InputStream;

import globals.GlobalConstants;
import tools.DebugMessager;
import tools.PrettyPrinter;

public class WelcomeActivity extends Activity
{
    LoginButton loginButton;
    CallbackManager callbackManager;
    private final static DebugMessager console = DebugMessager.getInstance();
    private static final int PICK_IMAGE=1;
    private Uri profile_pic;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();

        this . setImageDrawable(
                getDrawable(R.mipmap.ic_launcher)
        );

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
        saveStateToPrefs();
        Intent next = new Intent(this, GuideActivity.class);
        startActivity(next);
        // finish();
    }

    public void onImageClick(View v)
    {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI
        );
        i . setType("image/*");
        startActivityForResult(
                Intent.createChooser(i, "Select Profile Picture"),
                PICK_IMAGE
        );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        console . debug_trace(this, "onActivityResult");
        if (resultCode != RESULT_OK)
        {
            console . debug_trace(this, "onActivityResult", "not ok");
            return;
        }
        if (requestCode == PICK_IMAGE)
        {
            if (data != null)
            {
                Uri imageUri  = data . getData();
                Drawable drawable;
                try
                {
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);

                    drawable = Drawable.createFromStream(inputStream, imageUri.toString() );

                }
                catch (FileNotFoundException e)
                {
                    drawable = getDrawable(R.mipmap.ic_launcher);
                }

                this . setImageDrawable(drawable);

                this . profile_pic = imageUri;
            }
        }
    }

    private void setImageDrawable(Drawable drawable)
    {
        ImageView view = (ImageView) this . findViewById(R.id.profile_pic_chooser);
        view . setBackground(
                drawable
        );
    }

    private String _get_username()
    {
        return ((EditText) this . findViewById(R.id.userName)).getText().toString();
    }

    private void saveStateToPrefs()
    {
        SharedPreferences prefs = getSharedPreferences(
                getString(R.string.shared_prefs_key),
                Context.MODE_PRIVATE
        );

        SharedPreferences.Editor edit = prefs.edit();

        String user_name = _get_username();

        edit.putString(
                GlobalConstants.userName,
                user_name
        );

        edit . putString(
                GlobalConstants.imageURI,
                this . profile_pic . toString()
        );

        edit . apply();
    }
}
