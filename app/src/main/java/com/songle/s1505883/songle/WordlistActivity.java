package com.songle.s1505883.songle;

import android.app.Activity;
import android.support.v4.app.NavUtils;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import datastructures.WordlistCardInformation;

public class WordlistActivity extends Activity
{

    private RecyclerView wordlistView;
    private RecyclerView.Adapter wAdapter;
    private RecyclerView.LayoutManager wLayoutManager;

    private static class WordlistAdapter extends RecyclerView.Adapter<WordlistAdapter.ViewHolder>
    {

        private static List<WordlistCardInformation> generate_dummy_data()
        {
            List<WordlistCardInformation> ret = new ArrayList<>();

            for (int i = 0 ; i < 100 ; ++i)
            {
                ret . add(new WordlistCardInformation());
            }

            return ret;
        }

        private List<WordlistCardInformation> dataset;

        static class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView categoryNameView;
            TextView wordListView;
            ViewHolder(View v)
            {
                super(v);
                this . categoryNameView = v.findViewById(R.id.wordlist_category_name);
                this . wordListView = v.findViewById(R.id.wordlist_category_words);
            }
        }

        public WordlistAdapter(List<WordlistCardInformation> dataset)
        {
            this . dataset = dataset;
        }

        public WordlistAdapter()
        {
            this . dataset = generate_dummy_data();
        }

        @Override
        public WordlistAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.word_card, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final WordlistAdapter.ViewHolder holder, int position)
        {
            WordlistCardInformation card_at_position = this . dataset . get(position);
            holder . categoryNameView . setText(
                    card_at_position . getCategory_name()
            );

            holder . wordListView . setText(
                    card_at_position . getFound_words_in_category() . toString()
            );
        }

        @Override
        public int getItemCount()
        {
            return this . dataset . size();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordlist);

        try
        {
            getActionBar() . setDisplayHomeAsUpEnabled(true);
        }
        catch (NullPointerException e)
        {
            e . printStackTrace();
        }

        wordlistView = new RecyclerView(this);


        wordlistView . setHasFixedSize(true);

        wLayoutManager = new LinearLayoutManager(this);
        wordlistView.setLayoutManager(wLayoutManager);

        wAdapter = new WordlistAdapter();
        wordlistView . setAdapter(wAdapter);

        this . addContentView(wordlistView, new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.MATCH_PARENT
                ));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item . getItemId())
        {
            case android.R.id.home:
            {
                NavUtils.navigateUpFromSameTask(this);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
