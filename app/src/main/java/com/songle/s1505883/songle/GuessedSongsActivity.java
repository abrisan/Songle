package com.songle.s1505883.songle;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import com.google.android.youtube.player.*;

import datastructures.WordlistCardInformation;
import globals.GlobalConstants;
import globals.GlobalState;
import tools.DebugMessager;
import tools.SongListParser;

public class GuessedSongsActivity extends YouTubeBaseActivity
    implements YouTubePlayer.OnInitializedListener
{

    private RecyclerView guessedSongsView;
    private RecyclerView.Adapter gAdapter;
    private RecyclerView.LayoutManager gLayoutManager;
    private DebugMessager console = DebugMessager.getInstance();
    private YouTubePlayer player;

    private Consumer<String> play_film_id = x -> {
        String[] split_string = x . split("/");
        String id = split_string[split_string . length - 1];
        console . info(id);
        player . cueVideo(id);
    };

    private final String API_KEY = "AIzaSyAXRSnzTtv4JbxbBES3crh2JSwPQdwsKwc";




    private class GuessedSongsAdapter extends RecyclerView.Adapter<GuessedSongsAdapter.ViewHolder>
    {
        Random generator = new Random(0);

        private List<SongListParser.SongDescriptor> generate_dummy_data()
        {
            return GlobalState . getSongs() == null ? new ArrayList<>() : GlobalState.getSongs();

        }

        private List<SongListParser.SongDescriptor> dataset;

        List<ViewHolder> cards = new ArrayList<>();

        class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView songName;
            TextView artistName;
            CardView parent_card;

            private String link;
            private int original_color;

            private final int id;

            ViewHolder(View v)
            {
                super(v);
                this . songName = v . findViewById(R.id.guessedSongName);
                this . artistName = v . findViewById(R.id.guessedArtistName);
                this . parent_card = (CardView) v.findViewById(R.id.guessed_card_view);
                this . original_color = parent_card . getSolidColor();
                v . setOnClickListener((view) -> select());
                this . id = generator . nextInt();
                cards . add(this);
            }

            void setLink(String link)
            {
                this . link = link;
            }

            private int getId() {return this . id;}

            private void select()
            {
                console . info(String.format("%d cards in collection", cards.size()));
                cards . forEach(ViewHolder::deselect);

                this . parent_card . setCardBackgroundColor(
                        Color.parseColor(
                                GlobalConstants.COLOR_LIGHT_GRAY
                        )
                );


                this . songName . setTextColor(
                        Color.parseColor(
                                GlobalConstants.COLOR_WHITE
                        )
                );

                this . artistName . setTextColor(
                        Color.parseColor(
                                GlobalConstants.COLOR_WHITE
                        )
                );

                play_film_id . accept(this . link);
            }


            private void deselect() {

                this . parent_card . setCardBackgroundColor(
                        this . original_color
                );

                this . parent_card . setPreventCornerOverlap(false);


                this . songName . setTextColor(
                        Color.parseColor(
                                GlobalConstants.COLOR_BLACK
                        )
                );

                this . artistName . setTextColor(
                        Color.parseColor(
                                GlobalConstants.COLOR_BLACK
                        )
                );
            }

        }

        public GuessedSongsAdapter()
        {
            this . dataset = generate_dummy_data();
        }

        @Override
        public GuessedSongsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.player_card, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final GuessedSongsAdapter.ViewHolder holder, int position)
        {
            SongListParser.SongDescriptor songAtI = this . dataset . get(position);

            holder . artistName . setText(
                    songAtI . getArtistName()
            );

            holder . songName . setText(
                    songAtI . getSongName()
            );

            holder . setLink(
                    songAtI . getLink()
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
        setContentView(R.layout.activity_guessed_songs);

        guessedSongsView = (RecyclerView) findViewById(R.id.guessedSongs_recycler_view);
        guessedSongsView . setHasFixedSize(true);

        gLayoutManager = new LinearLayoutManager(this);
        guessedSongsView . setLayoutManager(gLayoutManager);

        gAdapter = new GuessedSongsAdapter();
        guessedSongsView . setAdapter(gAdapter);

        YouTubePlayerView player = (YouTubePlayerView) findViewById(R.id.youtubeView);
        player . initialize(API_KEY, this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b)
    {
        youTubePlayer . setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener()
        {
            @Override
            public void onLoading()
            {
                console . info("[onLoading]");
            }

            @Override
            public void onLoaded(String s)
            {
                console . info("[onLoaded]");
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
                console . debug_output(errorReason);
            }
        });

        youTubePlayer . setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener()
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

        this . player = youTubePlayer;
        youTubePlayer . cueVideo("fJ9rUzIMcZQ");
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult)
    {
        console . info("Failure when initialising Youtube Player");
    }
}
