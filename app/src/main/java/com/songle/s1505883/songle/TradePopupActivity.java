package com.songle.s1505883.songle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;

import datastructures.TradeDescriptor;
import tools.DebugMessager;

public class TradePopupActivity extends Activity
{

    String from;
    String to;
    TradeDescriptor.ActualTrade conversionRate;
    int available;
    int toAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_popup);

        this . from = getIntent().getExtras().getString("from");
        this . to = getIntent().getExtras().getString("to");
        this . available = getIntent().getExtras().getInt("available");
        this . toAvailable = getIntent().getExtras().getInt("toAvailable");

        try
        {
            this . conversionRate = TradeDescriptor.ActualTrade.deserialise(
                    getIntent().getExtras().getString("rate")
            );
        }
        catch (JSONException e)
        {
            e . printStackTrace();
        }



        ((TextView) this . findViewById(R.id.conversion_rate)).setText(
                getText()
        );
    }

    public String getText()
    {
        return String.format(
                "The conversion rate is %d %s for %d %s",
                this . conversionRate . from,
                from,
                this . conversionRate . to,
                to
        );
    }

    public void makeTradeClicked(View view)
    {
        Intent extras = new Intent();

        int wantToTrade = Integer.parseInt(
                ((EditText) findViewById(R.id.source_trade_count)).getText().toString()
        );

        if (wantToTrade > available)
        {
            wantToTrade = available;
        }

        if (wantToTrade >= this . conversionRate . from)
        {
            wantToTrade -= wantToTrade % conversionRate . from;

            int toCount = (wantToTrade / this . conversionRate . from) * this . conversionRate . to;

            toCount = toCount <= this.toAvailable ? toCount : this . toAvailable;

            extras . putExtra("from", wantToTrade);
            extras . putExtra("to", toCount);
            extras . putExtra("success", true);
        }
        else
        {
            extras . putExtra("success", false);
        }

        if (getParent() == null)
        {
            this . setResult(Activity.RESULT_OK, extras);
        }
        else
        {
            getParent() . setResult(Activity.RESULT_OK, extras);
        }

        finish();
    }
}
