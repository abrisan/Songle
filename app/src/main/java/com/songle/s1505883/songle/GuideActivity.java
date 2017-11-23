package com.songle.s1505883.songle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

public class GuideActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ((TextView) findViewById(R.id.textView3)).setMovementMethod(new ScrollingMovementMethod());
    }

    public void nextClicked(View view)
    {
        Intent next = new Intent(this, DifficultyActivity.class);
        startActivity(next);
        // finish();
    }
}
