package datastructures;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import tools.WordLocationParser;

@Entity(tableName = "locations", primaryKeys = {"coordinates", "word", "category", "songId", "map_number"})
public class LocationDescriptor
{
    @ColumnInfo
    private String word;

    @ColumnInfo
    private String category;

    @ColumnInfo
    private int map_number;

    @ColumnInfo
    private String coordinates;

    @ColumnInfo
    private boolean discovered;

    @ColumnInfo
    private boolean available;

    @ColumnInfo
    private int songId;


    public LocationDescriptor()
    {
        this.discovered = false;
        this.available = true;
    }

    public LocationDescriptor(LocationDescriptor des)
    {
        this . word = des.word;
        this . category = des.category;
        this . coordinates = des.coordinates;
        this . discovered = des.discovered;
        this . available = des.available;
        this . songId = des.songId;
        this . map_number = des . map_number;
    }

    public void clear()
    {
        this.word = null;
        this.category = null;
        this.coordinates = null;
        this.discovered = false;
        this.available = true;
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

    public void setAvailable(boolean available) {this.available = available;}

    public void setDiscovered(boolean discovered) {this.discovered = discovered;}

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

    public boolean getAvailable() {return this.available;}

    public boolean getDiscovered() {return this.discovered;}

    public int getSongId() {return this.songId;}

    public void setSongId(int songId) {this.songId = songId;}

    public void setMap_number(int map_number) {this .map_number = map_number;}

    public int getMap_number() {return this . map_number;}


    public String serialise()
            throws JSONException
    {
        JSONObject ret = new JSONObject();

        ret.put("word", this.word);
        ret.put("category", this.category);
        ret.put("coordinates", this.coordinates);
        ret.put("mapNumber", this . map_number);

        return ret.toString(2);
    }


}
