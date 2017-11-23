package com.songle.s1505883.songle;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import datastructures.WordlistCardInformation;
import globals.GlobalConstants;
import globals.GlobalState;
import tools.Algorithm;
import tools.DebugMessager;
import tools.WordLocationParser;

public class TradeActivity extends Activity
{
    private static final DebugMessager console = DebugMessager.getInstance();
    private Map<String, Integer> existingWords;
    private List<String> existingWordsList;
    private Map<String, Integer> trades;

    private RecyclerView catsView;
    private RecyclerView.Adapter cAdapter;
    private RecyclerView.LayoutManager cLayoutManager;

    private CategoryListAdapter.ViewHolder from;
    private CategoryListAdapter.ViewHolder to;

    private class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder>
    {
        class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView categoryNameView;
            TextView categoryCount;

            String s_categoryNameView;
            int i_category_count;

            CardView parent_card;
            boolean is_from;
            int color;

            ViewHolder(View v)
            {
                super(v);
                this . categoryNameView = v.findViewById(R . id . categoryName);
                this . categoryCount = v . findViewById(R . id . availableWords);
                this . parent_card = (CardView) v.findViewById(R.id.trade_category_view);
                this . color = v . getSolidColor();
                v . setOnClickListener((view) -> {
                    if (from == this)
                    {
                        from = null;
                        deselect();
                        is_from = false;
                    }
                    else if (to == this)
                    {
                        to = null;
                        deselect();
                    }
                    else if (from != null && to != null) {}
                    else if (from == null)
                    {
                        is_from = true;
                        from = this;
                        select();
                    }
                    else
                    {
                        is_from = false;
                        to = this;
                        select();
                    }
                });
            }

            void select()
            {
                String t_color = is_from ?
                        GlobalConstants.COLOR_RED_HEX : GlobalConstants.COLOR_GREEN_HEX;

                this . parent_card . setCardBackgroundColor(
                        Color.parseColor(t_color)
                );

                this . categoryNameView . setTextColor(
                        Color.parseColor(GlobalConstants.COLOR_WHITE)
                );

                this . categoryCount . setTextColor(
                        Color.parseColor(GlobalConstants.COLOR_WHITE)
                );
            }

            void deselect()
            {
                this . parent_card . setCardBackgroundColor(
                        this . color
                );

                this . categoryNameView . setTextColor(
                        Color.parseColor(GlobalConstants.COLOR_BLACK)
                );

                this . categoryCount . setTextColor(
                        Color.parseColor(GlobalConstants.COLOR_BLACK)
                );
            }
        }



        @Override
        public CategoryListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.trade_card, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final CategoryListAdapter.ViewHolder holder, int position)
        {
            String category = existingWordsList . get(position);
            String count = existingWords . get(category) . toString();

            holder . categoryNameView . setText(
                    category
            );


            holder . categoryCount . setText(
                    count
            );
        }

        @Override
        public int getItemCount()
        {
            return existingWords . size();
        }
    }


    private void _count_words()
    {
        this . existingWords = Algorithm.counter(
                GlobalState.getPlacemarks().getDescriptors(),
                WordLocationParser.LocationDescriptor::getCategory
        );

        this . existingWordsList = new ArrayList<>(this . existingWords . keySet());

        console . debug_map(existingWords);
    }

    private void _init_vars()
    {
        _count_words();
        this . trades = new HashMap<>();
    }


    private void _init_cardview()
    {
        this . catsView = (RecyclerView) findViewById(R.id.trade_cardview);
        this . catsView . setHasFixedSize(true);

        this . cLayoutManager = new GridLayoutManager(this, 2);
        this . catsView . setLayoutManager(this . cLayoutManager);

        this . cAdapter = new CategoryListAdapter();
        this . catsView . setAdapter(this . cAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade);

        _init_vars();
        _init_cardview();

        try
        {
            getActionBar() . setDisplayHomeAsUpEnabled(true);
        }
        catch (Exception e)
        {
            console . error("Null Pointer Exception cause in [TradeActivity onCreate]");
        }
    }

    public void mkTradeClicked(View view)
    {
        Intent make_trade = new Intent(this, TradePopupActivity.class);
        startActivity(make_trade);
    }
}
