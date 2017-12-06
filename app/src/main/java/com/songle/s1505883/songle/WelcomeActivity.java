package com.songle.s1505883.songle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import globals.GlobalConstants;
import tools.DebugMessager;
import tools.DownloadFunction;

public class WelcomeActivity extends Activity
{
    LoginButton loginButton;
    CallbackManager callbackManager;
    private final static DebugMessager console = DebugMessager.getInstance();
    private static final int PICK_IMAGE=1;
    private Uri profile_pic;
    private URL online_profile_pic;
    private String name;

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
                console . debug_trace(this, "onSuccess");
                AccessToken token = loginResult.getAccessToken();
                GraphRequest request = GraphRequest.newMeRequest(token, (json_object, response) -> {
                    try
                    {
                        console . error(
                                json_object.toString(2)
                        );

                        setUserName(json_object.getString("name"));

                        Bundle params = new Bundle();
                        params.putInt("redirect", 0);
                        params.putString("type", "normal");

                        new GraphRequest(
                                token,
                                "/" + json_object.getString("id") +"/picture",
                                params,
                                HttpMethod.GET,
                                response1 -> {
                                    try
                                    {
                                        JSONObject obj = response1.getJSONObject();
                                        URL url = new URL(
                                                obj.getJSONObject("data").getString("url")
                                        );
                                        haveImageURLCallback(url);
                                    }
                                    catch (NullPointerException|JSONException|MalformedURLException e)
                                    {
                                        e . printStackTrace();
                                    }
                                }
                        ).executeAsync();
                    }
                    catch (JSONException e)
                    {
                        e . printStackTrace();
                    }
                });

                request . executeAsync();

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
        if (resultCode != RESULT_OK)
        {
            return;
        }
        if (requestCode == PICK_IMAGE)
        {
            if (data != null)
            {
                Uri imageUri  = data . getData();
                if (online_profile_pic != null)
                {
                    online_profile_pic = null;
                }
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
        else
        {
            callbackManager.onActivityResult(requestCode, resultCode, data);
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

    private void setUserName(String name)
    {
        this . name = name;
        ((EditText) this.findViewById(R.id.userName)).setText(name);
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

        String imgURI = this . profile_pic == null ? "null" : this . profile_pic . toString();

        edit . putString(
                GlobalConstants.imageURI,
                imgURI
        );

        String onlineURL = this . online_profile_pic == null ? "null" : this . online_profile_pic . toString();

        edit . putString(
                GlobalConstants.onlineImageURL,
                onlineURL
        );

        edit . apply();
    }

    private void haveImageURLCallback(URL url)
    {
        this . online_profile_pic = url;
        if (this . profile_pic != null)
        {
            this . profile_pic = null;
        }
        new DownloadFunction<>(
                x -> BitmapDrawable.createFromStream(x, "fbPhoto"),
                this::setImageDrawable
        ).execute(url);

    }

}
