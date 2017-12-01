package com.songle.s1505883.songle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import database.AppDatabase;
import database.DatabaseReadTask;
import datastructures.GuessedWords;

import globals.GlobalConstants;
import globals.GlobalLambdas;
import tools.DebugMessager;

public class WordlistActivity extends Activity
{

    private RecyclerView wordlistView;
    private RecyclerView.Adapter wAdapter;
    private RecyclerView.LayoutManager wLayoutManager;
    private DebugMessager console = DebugMessager.getInstance();
    private WordlistActivity thisPtr = this;


    private class WordlistAdapter extends RecyclerView.Adapter<WordlistAdapter.ViewHolder>
    {

        private List<GuessedWords> dataset;

        class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView categoryNameView;
            TextView categoryPreview;

            private String fullString;
            private String preview;
            private boolean longForm = false;

            ViewHolder(View v)
            {
                super(v);
                this . categoryNameView = v.findViewById(R.id.wordlist_category_name);
                this . categoryPreview = v . findViewById(R . id . wordlist_category_preview);
                v . setOnClickListener((view) -> this . flipState());
            }

            void setFullString(String fullString)
            {
                this . fullString = fullString;
            }

            void setPreviewString(String previewString)
            {
                this . preview = previewString;
            }

            void flipState()
            {
                if (longForm)
                {
                    longForm = false;
                    this . categoryPreview . setText(preview);
                }
                else
                {
                    longForm = true;
                    this . categoryPreview . setText(fullString);
                }
            }
        }

        public WordlistAdapter(List<GuessedWords> gw)
        {
            this . dataset = gw;
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
            GuessedWords card_at_position = this . dataset . get(position);

            holder . categoryNameView . setText(
                    card_at_position . getCategory_name()
            );

            holder . categoryNameView . setBackgroundColor(
                    GlobalConstants.getColorFromCategory(
                            card_at_position . getCategory_name()
                    )
            );


            holder . categoryPreview . setText(
                    card_at_position . get_found_words_preview()
            );

            holder . setFullString(
                    card_at_position . get_full_words()
            );

            holder . setPreviewString(
                    card_at_position . get_found_words_preview()
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



        new DatabaseReadTask<>(
                AppDatabase.getAppDatabase(this),
                this::gotGuessedWordsCallback
        ).execute(GlobalLambdas.gw.apply(0));
    }

    public void guessClicked(View v)
    {
        Intent showPopup = new Intent(this, GuessSongActivity.class);
        startActivity(showPopup);
    }

    public void gotGuessedWordsCallback(List<GuessedWords> guessedWords)
    {
        console . debug_trace(this, "gotGuessedWordsCallback");

        console . debug_output_json(guessedWords);

        wordlistView = (RecyclerView) findViewById(R.id.wordlist_recycler_view);
        wordlistView . setHasFixedSize(true);

        wLayoutManager = new LinearLayoutManager(this);
        wordlistView.setLayoutManager(wLayoutManager);

        wAdapter = new WordlistAdapter(guessedWords);
        wordlistView . setAdapter(wAdapter);
    }


}
