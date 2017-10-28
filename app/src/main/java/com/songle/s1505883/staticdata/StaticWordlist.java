package com.songle.s1505883.staticdata;


import android.content.Context;
import android.content.res.Resources;
import android.os.Debug;

import com.songle.s1505883.songle.R;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import datastructures.WordlistCardInformation;
import tools.DebugMessager;
import tools.SongLyricsParser;


public class StaticWordlist
{

    // Member variables
    private SongLyricsParser.SongLyricsDescriptor lyrics;
    private Map<String, WordlistCardInformation> cardDescriptors;

    public StaticWordlist(Context context)
    {
        try
        {
            InputStream stream = context.getResources().openRawResource(R.raw.examplelyrics);
            this . lyrics = SongLyricsParser.parseLyrics(stream);
            this . cardDescriptors = new HashMap<>();
            constructMap();
        }
        catch (IOException e)
        {
            DebugMessager.getInstance().info(System.getProperty("user.dir"));
            e . printStackTrace();
        }
    }

    private void constructMap()
    {
        String[] names = new String[]{
                "Unclassified",
                "Boring",
                "Not Boring",
                "Interesting",
                "Very Interesting"
        };

        Random gen = new Random(0);
        for (String word : this . lyrics . getWords())
        {
            String category = names[gen.nextInt(5)];
            WordlistCardInformation card_for_category = this . cardDescriptors .  get(category);
            if (card_for_category == null)
            {
                WordlistCardInformation to_add = new WordlistCardInformation();
                to_add.setCategoryName(category);
                to_add.addGuessedWord(word);
                this . cardDescriptors . put(category, to_add);
            }
            else
            {
                this . cardDescriptors . get(category) . addGuessedWord(word);
            }
        }

    }

    public List<WordlistCardInformation> asList()
    {
        List<WordlistCardInformation> return_list = new ArrayList<>();

        for (String cat : this . cardDescriptors . keySet())
        {
            return_list . add(this . cardDescriptors . get(cat));
        }

        return return_list;
    }




}
