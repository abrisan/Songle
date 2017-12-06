package com.songle.s1505883.songle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class TradePopupActivity extends Activity
{

    String from;
    String to;
    double conversionRate;
    int available;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_popup);

        this . from = getIntent().getExtras().getString("from");
        this . to = getIntent().getExtras().getString("to");
        this . available = getIntent().getExtras().getInt("available");
        this . conversionRate = getIntent().getExtras().getDouble("rate");


        ((TextView) this . findViewById(R.id.conversion_rate)).setText(
                getText()
        );
    }

    public String getText()
    {
        if (conversionRate < 1)
        {
            return String.format("The conversion rate is %d %s for 1 %s",
                    (int) (1/conversionRate),
                    from,
                    to);
        }
        else
        {
            return String.format("The conversion rate is %d %s from 1 %s",
                    (int) conversionRate,
                    to,
                    from);
        }
    }

    public void makeTradeClicked(View view)
    {
        int wantToTrade = Integer.parseInt(
                ((EditText) this.findViewById(R.id.source_trade_count))
                .getText().toString()
        );

        if (wantToTrade > available)
        {
            wantToTrade = available;
            ((EditText) this.findViewById(R.id.source_trade_count))
                    .setText(String.valueOf(wantToTrade));
        }

        Intent extras = new Intent();
        extras.putExtra("src_qty", wantToTrade);

        if (getParent() == null)
        {
            setResult(Activity.RESULT_OK, extras);
        }
        else
        {
            getParent().setResult(Activity.RESULT_OK, extras);
        }
        finish();
    }
}
