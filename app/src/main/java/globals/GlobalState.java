package globals;


import android.content.Context;

import com.songle.s1505883.staticdata.StaticPlacemarks;
import com.songle.s1505883.staticdata.StaticWordlist;

import org.json.JSONException;

import java.util.List;
import java.util.stream.Stream;

import tools.DebugMessager;
import tools.SongListParser;
import tools.WordLocationParser;

public class GlobalState
{
    // Instance Declaration
    private GlobalState state = new GlobalState();

    // Instance Getter
    public GlobalState getState()
    {
        return this . state;
    }

    // Declarations of the global state
    private List<SongListParser.SongDescriptor> songs;
    private DebugMessager console = DebugMessager.getInstance();
    private StaticPlacemarks placemarks;
    private StaticWordlist words;
    private String currentDifficulty;


    // Getters
    public List<SongListParser.SongDescriptor> getSongs(Context ctxt)
    {
        if (this . songs == null)
        {
            this . _init_songs(ctxt);
        }
        return this . songs;
    }

    public StaticPlacemarks getPlacemarks(Context ctxt)
    {
        if (this . placemarks == null)
        {
            this . _init_placemarks(ctxt);
        }
        return placemarks;
    }

    public StaticWordlist getWordList(Context ctxt)
    {
        if (this . words == null)
        {
            this . _init_words(ctxt);
        }
        return words;
    }
    // END GETTERS

    // Private initializers for lazy getters
    private void _init_songs(Context ctxt)
    {

    }

    private void _init_placemarks(Context ctxt)
    {
        this . placemarks = new StaticPlacemarks(ctxt);
    }

    private void _init_words(Context ctxt)
    {
        this . words = new StaticWordlist(ctxt);
    }


    // Manipulation methods
    public void addSong(SongListParser.SongDescriptor song)
    {
        songs . add(song);
    }

    // Debug Methods
    public void test()
    {
        songs . stream() . forEach((x) -> {
            try
            {
                console . info(x . serialise());
            }
            catch (JSONException e)
            {
                e . printStackTrace();
            }
        });
    }
}
