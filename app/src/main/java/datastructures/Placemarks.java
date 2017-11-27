package datastructures;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.json.JSONException;

import tools.DebugMessager;
import tools.WordLocationParser;

public class Placemarks
{
    private List<LocationDescriptor> descriptors;
    private List<MarkerDescriptor> markers;

    private DebugMessager console = DebugMessager.getInstance();

    public Placemarks(Wordlist wList, InputStream stream)
    {
        this . markers = new ArrayList<>();
        this . descriptors = new ArrayList<>();
        try
        {
            WordLocationParser.parse(
                    stream,
                    wList.getLyrics(),
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

    public List<LocationDescriptor> getDescriptors()
    {
        return this . descriptors;
    }

    public String getMarkerURLForCategory(String category)
    {
        for (MarkerDescriptor m : this . markers)
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
        for (MarkerDescriptor m : this . markers)
        {
            if (m . getCategory() . equals(m))
            {
                return m . getScale();
            }
        }

        return -1;
    }



}
