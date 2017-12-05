package com.songle.s1505883.songle;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import com.google.android.youtube.player.*;

import database.AppDatabase;
import database.DatabaseReadTask;
import datastructures.SongDescriptor;
import globals.GlobalConstants;
import globals.GlobalLambdas;
import tools.DebugMessager;

public class GuessedSongsActivity extends YouTubeBaseActivity
    implements YouTubePlayer.OnInitializedListener
{

    private RecyclerView guessedSongsView;
    private RecyclerView.Adapter gAdapter;
    private RecyclerView.LayoutManager gLayoutManager;
    private DebugMessager console = DebugMessager.getInstance();
    private YouTubePlayer player;

    // Simple lambda for playing a youtube film, given the link
    private Consumer<String> play_film_id = x ->
    {
        String[] split_string = x.split("/");
        String id = split_string[split_string.length - 1];
        player.cueVideo(id);
    };

    // Youtube API key
    private final String API_KEY = "AIzaSyAXRSnzTtv4JbxbBES3crh2JSwPQdwsKwc";

    // Data adapter for CardView
    private class GuessedSongsAdapter extends RecyclerView.Adapter<GuessedSongsAdapter.ViewHolder>
    {

        private List<SongDescriptor> dataset;

        List<WeakReference<ViewHolder>> cards = new ArrayList<>();

        public void updateDataset(List<SongDescriptor> new_data)
        {
            this.dataset.addAll(new_data);
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView songName;
            TextView artistName;
            CardView parent_card;

            private String link;
            private int original_color;


            ViewHolder(View v)
            {
                super(v);
                this.songName = v.findViewById(R.id.guessedSongName);
                this.artistName = v.findViewById(R.id.guessedArtistName);
                this.parent_card = (CardView) v.findViewById(R.id.guessed_card_view);
                this.original_color = parent_card.getSolidColor();

                // Set the on click listener
                v.setOnClickListener((view) -> select());

                // Add this ref to a list of cards so we know what to deselect
                // use weak ref such that we don't leak
                cards.add(new WeakReference<>(this));
            }

            void setLink(String link)
            {
                this.link = link;
            }

            // Just some fancy coloring
            private void select()
            {
                // Make sure nothing is selected
                cards.forEach(x -> x . get() . deselect());

                // Set each colors
                this.parent_card.setCardBackgroundColor(
                        Color.parseColor(
                                GlobalConstants.COLOR_LIGHT_GRAY
                        )
                );


                this.songName.setTextColor(
                        Color.parseColor(
                                GlobalConstants.COLOR_WHITE
                        )
                );

                this.artistName.setTextColor(
                        Color.parseColor(
                                GlobalConstants.COLOR_WHITE
                        )
                );

                // Play the film
                play_film_id.accept(this.link);
            }


            private void deselect()
            {
                // Go back
                this.parent_card.setCardBackgroundColor(
                        this.original_color
                );

                this.parent_card.setPreventCornerOverlap(false);


                this.songName.setTextColor(
                        Color.parseColor(
                                GlobalConstants.COLOR_BLACK
                        )
                );

                this.artistName.setTextColor(
                        Color.parseColor(
                                GlobalConstants.COLOR_BLACK
                        )
                );
            }

        }

        public GuessedSongsAdapter(List<SongDescriptor> dataset)
        {
            this.dataset = dataset;
        }

        @Override
        public GuessedSongsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            // instantiate the card view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.player_card, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final GuessedSongsAdapter.ViewHolder holder, int position)
        {
            // grab the song
            SongDescriptor songAtI = this.dataset.get(position);


            // set the relevant things
            holder.artistName.setText(
                    songAtI.getArtistName()
            );

            holder.songName.setText(
                    songAtI.getSongName()
            );

            holder.setLink(
                    songAtI.getLink()
            );
        }

        @Override
        public int getItemCount()
        {
            return this.dataset.size();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guessed_songs);

        // Initialise the card view
        guessedSongsView = (RecyclerView) findViewById(R.id.guessedSongs_recycler_view);
        guessedSongsView.setHasFixedSize(true);

        gLayoutManager = new LinearLayoutManager(this);
        guessedSongsView.setLayoutManager(gLayoutManager);

        gAdapter = new GuessedSongsAdapter(new ArrayList<>());
        guessedSongsView.setAdapter(gAdapter);


        // Read from the database
        // Varargs creation is safe, the List is read-only
        new DatabaseReadTask<>(
                AppDatabase.getAppDatabase(this), this::receivedGuessedSongListCallback
        ).execute(GlobalLambdas.getGuessedDescriptors);

        // Initialise the youtube player
        YouTubePlayerView player = (YouTubePlayerView) findViewById(R.id.youtubeView);
        player.initialize(API_KEY, this);
    }

    private void receivedGuessedSongListCallback(List<SongDescriptor> des)
    {
        List<SongDescriptor> toAdd = des . size() == 0 ? GlobalConstants.placeholderVideos : des;
        ((GuessedSongsAdapter) this.gAdapter).updateDataset(toAdd);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b)
    {
        youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener()
        {
            @Override
            public void onLoading()
            {

            }

            @Override
            public void onLoaded(String s)
            {

            }

            @Override
            public void onAdStarted()
            {

            }

            @Override
            public void onVideoStarted()
            {

            }

            @Override
            public void onVideoEnded()
            {

            }

            @Override
            public void onError(YouTubePlayer.ErrorReason errorReason)
            {

            }
        });

        youTubePlayer.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener()
        {
            @Override
            public void onPlaying()
            {

            }

            @Override
            public void onPaused()
            {

            }

            @Override
            public void onStopped()
            {

            }

            @Override
            public void onBuffering(boolean b)
            {

            }

            @Override
            public void onSeekTo(int i)
            {

            }
        });

        this.player = youTubePlayer;
        youTubePlayer.cueVideo("fJ9rUzIMcZQ");
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult)
    {

    }

}
