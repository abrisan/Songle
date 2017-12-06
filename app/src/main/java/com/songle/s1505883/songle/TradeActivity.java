package com.songle.s1505883.songle;

import android.app.Activity;
import android.arch.persistence.room.Database;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import database.AppDatabase;
import database.DatabaseReadTask;
import database.DatabaseWriteTask;
import datastructures.CurrentGameDescriptor;
import datastructures.LocationDescriptor;
import datastructures.Pair;
import globals.GlobalConstants;
import tools.Algorithm;
import tools.DebugMessager;
import tools.WordLocationParser;

public class TradeActivity extends Activity
{
    private static final DebugMessager console = DebugMessager.getInstance();
    private Map<String, Integer> existingWords;
    private List<String> existingWordsList;
    private Map<String, Integer> trades;
    private List<LocationDescriptor> loc_buffer;

    private RecyclerView catsView;
    private RecyclerView.Adapter cAdapter;
    private RecyclerView.LayoutManager cLayoutManager;

    private CategoryListAdapter.ViewHolder from;
    private CategoryListAdapter.ViewHolder to;

    private String fromString;
    private String toString;

    private CurrentGameDescriptor des;

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
                        toString = null;
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

                if (is_from)
                {
                    fromString = this.s_categoryNameView;
                }
                else
                {
                    toString = this.s_categoryNameView;
                }
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

                if (is_from)
                {
                    fromString = null;
                }
                else
                {
                    toString = null;
                }
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

            holder.s_categoryNameView = category;
        }

        @Override
        public int getItemCount()
        {
            return existingWords . size();
        }
    }


    private void _init_vars(List<Pair> pairs)
    {
        this . existingWords = new HashMap<>();
        this . existingWordsList = new ArrayList<>();

        pairs . forEach(
                x -> {
                    this.existingWords.put(
                            x.getKey(),
                            x.getValue()
                    );

                    this . existingWordsList . add(
                            x . getKey()
                    );
                }
        );

        this . trades = new HashMap<>();

        _init_cardview();

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

        this . des = getIntent().getParcelableExtra(
                GlobalConstants.gameDescriptor
        );

        updateInformation();

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
        console.debug_output(fromString);
        console.debug_output(toString);
        if (fromString == null && toString == null)
        {
            return;
        }

        Intent make_trade = new Intent(this, TradePopupActivity.class);

        console . debug_output(fromString);
        Integer available = this.existingWords.get(fromString);


        make_trade.putExtra("from", fromString);
        make_trade.putExtra("to", toString);
        make_trade.putExtra("available", available);
        make_trade.putExtra("rate", GlobalConstants.getRate(
                fromString,
                toString
        ));

        startActivityForResult(make_trade, 1);
    }

    public void commitTradeClicked(View view)
    {
        List<String> keys = new ArrayList<>();
        keys.addAll(this.trades.keySet());

        new DatabaseReadTask<>(
                AppDatabase.getAppDatabase(this),
                this::haveLocs
        ).execute(
                db -> db . locationDao() . getTradeWords(
                        this.des.getSongNumber(),
                        (String[]) keys.toArray()
                )
        );
    }

    @Override
    public void onActivityResult(int request, int resultCode, Intent data)
    {
        if (request == 1)
        {
            if (Activity.RESULT_OK == resultCode)
            {
                try
                {
                    int qtyTraded = data.getExtras().getInt("src_qty");
                    add_trading_intent(qtyTraded);
                }
                catch (NullPointerException e)
                {
                    e . printStackTrace();
                }

            }
        }
    }

    private void add_trading_intent(int sourceQuantity)
    {
        String from = new String(fromString);
        String to = new String(toString);

        double conversion_factor = GlobalConstants.getRate(from, to);
        this.trades.put(from, -1 * sourceQuantity);
        this.trades.put(from, (int) conversion_factor * sourceQuantity + 1);
    }

    public void updateInformation()
    {
        new DatabaseReadTask<>(
                AppDatabase.getAppDatabase(this),
                this::_init_vars
        ).execute(
                db -> db . locationDao() . countUndiscoveredWordsByCategory(
                        des.getSongNumber()
                )
        );
    }

    public void haveLocs(List<LocationDescriptor> locs)
    {
        Map<String, List<LocationDescriptor>> groupedLocs = Algorithm.groupBy(
                locs,
                LocationDescriptor::getCategory,
                x -> x
        );

        List<LocationDescriptor> toUpdate = new ArrayList<>();

        for (String key : this . trades . keySet())
        {
            int factor = this . trades .get(key);
            int iter_limit = factor < 0 ? -1 * factor : factor;

            List<LocationDescriptor> db_locs = groupedLocs.get(key);

            iter_limit = iter_limit < db_locs.size() ? iter_limit : db_locs.size();

            for (int i = 0 ; i < iter_limit ; ++i)
            {
                LocationDescriptor current_des = db_locs.get(i);
                if (factor < 0)
                {
                    current_des.setAvailable(false);
                }
                else
                {
                    current_des.setDiscovered(true);
                }
                toUpdate.add(current_des);
            }
        }

        new DatabaseWriteTask<List<LocationDescriptor>>(
                AppDatabase.getAppDatabase(this),
                (db, lst) -> lst.forEach(x -> db . locationDao() . updateLocation(x)),
                this::updateInformation
        ).execute(
                toUpdate
        );
    }

}
