package datastructures;

import org.json.JSONException;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class SongSuite
{
    private Wordlist wordlist;
    private Placemarks placemarks;
    private Map<String, GuessedWords> guessedWords;
    private boolean played;

    public SongSuite(InputStream wordlistStream, InputStream placeMarksStream)
    {
        wordlist = new Wordlist(wordlistStream);
        placemarks = new Placemarks(wordlist, placeMarksStream);
        guessedWords = new HashMap<>();
        played = false;
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
