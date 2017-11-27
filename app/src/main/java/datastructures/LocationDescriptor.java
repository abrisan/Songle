package datastructures;

import org.json.JSONException;
import org.json.JSONObject;

import tools.WordLocationParser;

/**
 * Created by alexandrubrisan on 27/11/2017.
 */
public class LocationDescriptor
{
    private String word;
    private String category;
    private String coordinates;

    public LocationDescriptor()
    {
    }

    public LocationDescriptor(LocationDescriptor des)
    {
        this.word = des.word;
        this.category = des.category;
        this.coordinates = des.coordinates;
    }

    public void clear()
    {
        this.word = "";
        this.category = "";
        this.coordinates = "";
    }

    public void setWord(String word)
    {
        this.word = word;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public void setCoordinates(String coordinates)
    {
        this.coordinates = coordinates;
    }

    public String getCoordinates()
    {
        return this.coordinates;
    }

    public String getCategory()
    {
        return this.category;
    }

    public String getWord()
    {
        return this.word;
    }

    public String serialise()
            throws JSONException
    {
        JSONObject ret = new JSONObject();

        ret.put("word", this.word);
        ret.put("category", this.category);
        ret.put("coordinates", this.coordinates);

        return ret.toString(2);
    }
}
