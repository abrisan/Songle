package com.songle.s1505883.staticdata;


import android.content.Context;
import android.os.Debug;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.songle.s1505883.songle.R;

import org.json.JSONException;

import tools.DebugMessager;
import tools.SongLyricsParser;
import tools.WordLocationParser;

public class StaticPlacemarks
{
    private List<WordLocationParser.LocationDescriptor> descriptors;
    private List<WordLocationParser.MarkerDescriptor> markers;

    private DebugMessager console = DebugMessager.getInstance();

    public StaticPlacemarks(Context context)
    {
        this . markers = new ArrayList<>();
        this . descriptors = new ArrayList<>();
        try
        {
            InputStream stream = context . getResources() . openRawResource(R.raw.examplekml);
            WordLocationParser.parse(
                    stream,
                    new StaticWordlist(context).getLyrics(),
                    this.descriptors,
                    this.markers
            );
        }
        catch(Exception e)
        {
            e . printStackTrace();
        }

    }

    public void test()
    {
        Stream<String> res = markers . stream() . map((x) -> {
            try
            {
                return x . serialise();
            }
            catch(JSONException e)
            {
                e . printStackTrace();
            }
            return "";
        });

        res . forEach(x -> console.info(x));

        Stream<String> res2 = this . descriptors . stream() . map((x) -> {
           try
           {
               return x . serialise();
           }
           catch (JSONException e)
           {
               e . printStackTrace();
           }
            return "";
        });

        res2 . forEach(x -> console.info(x));


    }

    public List<WordLocationParser.LocationDescriptor> getDescriptors()
    {
        return this . descriptors;
    }

    public String getMarkerURLForCategory(String category)
    {
        for (WordLocationParser.MarkerDescriptor m : this . markers)
        {
            if (m . getCategory() . equals(category))
            {
                return m . getIconLink();
            }
        }

        return null;
    }

    public double getScaleForCategory(String category)
    {
        for (WordLocationParser.MarkerDescriptor m : this . markers)
        {
            if (m . getCategory() . equals(m))
            {
                return m . getScale();
            }
        }

        return -1;
    }



}
