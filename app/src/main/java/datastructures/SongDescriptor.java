package datastructures;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import tools.SongListParser;

@Entity(tableName = "songs")
public class SongDescriptor
{
    @ColumnInfo
    private String songName;

    @ColumnInfo
    private String artistName;

    @ColumnInfo
    private String link;

    @PrimaryKey
    private int number;

    public SongDescriptor()
    {
    }

    public SongDescriptor(SongDescriptor d)
    {
        this.songName = d.songName;
        this.artistName = d.artistName;
        this.link = d.link;
        this.number = d.number;
    }

    public void clear()
    {
        this.songName = null;
        this.artistName = null;
        this.link = null;
        this.number = -1;
    }


    public void setSongName(String name)
    {
        this.songName = name;
    }

    public void setArtistName(String artistName)
    {
        this.artistName = artistName;
    }

    public void setLink(String link)
    {
        this.link = link;
    }

    public void setNumber(int number)
    {
        this.number = number;
    }

    public String getSongName()
    {
        return this.songName;
    }

    public String getArtistName()
    {
        return this.artistName;
    }

    public String getLink()
    {
        return this.link;
    }

    public int getNumber()
    {
        return this.number;
    }


    public String serialise()
            throws JSONException
    {
        JSONObject ret = new JSONObject();

        ret.put("name", this.songName);
        ret.put("artist", this.artistName);
        ret.put("link", this.link);
        ret.put("number", this.number);

        return ret.toString(2);
    }

}
