package datastructures;

import org.json.JSONException;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class SongSuite
{
    private Wordlist wordlist;
    private Map<String, Placemarks> placemarks;
    private Map<String, GuessedWords> guessedWords;
    private boolean played;

    public SongSuite(InputStream wordlistStream)
    {
        this . wordlist = new Wordlist(wordlistStream);
        placemarks = new HashMap<>();
        guessedWords = new HashMap<>();
        played = false;
    }

    public void setPlacemarksForCategory(String category, InputStream input)
    {
        if (this . wordlist == null)
        {
            throw new IllegalStateException("Trying to set placemarks on object without wordlist");
        }
        this . placemarks . put(
                category,
                new Placemarks(this . wordlist, input)
        );
    }

    public String serialise()
        throws JSONException
    {
        return null;
    }

    public void deserialise(String jsonString)
    {

    }

}
