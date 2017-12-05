package datastructures;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

import com.songle.s1505883.songle.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import globals.DownloadLinks;
import globals.GlobalConstants;
import tools.Algorithm;
import tools.DebugMessager;

public class CurrentGameDescriptor implements Parcelable
{
    private int songNumber;
    private SongDescriptor song;
    private List<String> difficulties;
    private static DebugMessager console = DebugMessager.getInstance();

    public CurrentGameDescriptor(String songJson, String newDifficulty)
    {
        try
        {
            this . song = SongDescriptor.deserialise(songJson);
        }
        catch (JSONException|NullPointerException e)
        {
            this . song = new SongDescriptor();
            console . error(e . getStackTrace()[0].toString());
        }

        this . songNumber = this . song . getNumber();
        this . difficulties = Arrays.asList(
                newDifficulty.split(";")
        );
    }

    public CurrentGameDescriptor(SongDescriptor des, String difficulty)
    {
        this . song = des;
        this . songNumber = this . song . getNumber();
        this . difficulties = Arrays.asList(
                difficulty.split(";")
        );
    }

    public CurrentGameDescriptor(Parcel in)
    {
        this(in.readString(), in.readString());
    }

    public static CurrentGameDescriptor getInstanceForContext(Context ctxt)
    {
        SharedPreferences prefs = ctxt.getSharedPreferences(
                ctxt.getString(R.string.shared_prefs_key),
                Context.MODE_PRIVATE
        );

        String diffLevel = prefs.getString(
                ctxt.getString(
                        R.string.difficulty_level
                ),
                GlobalConstants.difficulty_levels[0]
        );

        String currentGame = prefs.getString(
                ctxt.getString(
                        R.string.current_game_index
                ),
                null
        );

        if (currentGame == null)
        {
            return null;
        }

        return new CurrentGameDescriptor(currentGame, diffLevel);
    }

    public int getSongNumber() {return this .songNumber;}
    public int getMapNumber()
    {
        int index = Algorithm.linearSearch(
                GlobalConstants.difficulty_levels,
                this.difficulties.get(
                        this.difficulties.size() - 1
                )
        );

        if (index < 0)
        {
            throw new IllegalStateException("Unkown difficulty");
        }

        return 5 - index;
    }

    public URL getCurrentDifficulty()
            throws MalformedURLException
    {
        int index = Algorithm.linearSearch(
                GlobalConstants.difficulty_levels,
                this.difficulties.get(
                        this.difficulties.size() - 1
                )
        );

        if (index < 0)
        {
            throw new IllegalStateException("Unkown difficulty");
        }
        return DownloadLinks.getMapLinkForSongForMapNumber(
                this .songNumber + 1,
                5 - index
        );
    }

    public URL addDifficulty(String difficulty)
            throws MalformedURLException
    {
        if (!this.difficulties.contains(difficulty))
        {
            return DownloadLinks.getMapLinkForSongForMapNumber(
                    this.songNumber,
                    5  - Algorithm.linearSearch(GlobalConstants.difficulty_levels, difficulty)
            );
        }
        return null;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        StringBuilder diffLevels = new StringBuilder();
        this . difficulties . forEach(
                x -> {
                    diffLevels.append(x);
                    diffLevels.append(";");
                }
        );
        diffLevels.deleteCharAt(diffLevels.length()-1);
        try
        {
            dest . writeString(this.song.serialise());
        }
        catch (JSONException e)
        {
            dest . writeString(null);
        }
        dest . writeString(diffLevels.toString());
    }

    public static final Parcelable.Creator<CurrentGameDescriptor> CREATOR
            = new Parcelable.Creator<CurrentGameDescriptor>() {
        public CurrentGameDescriptor createFromParcel(Parcel in) {
            return new CurrentGameDescriptor(in);
        }

        public CurrentGameDescriptor[] newArray(int size) {
            return new CurrentGameDescriptor[size];
        }
    };

    @Override
    public String toString()
    {
        try
        {
            JSONObject o = new JSONObject();
            o . put("level", this.songNumber);
            o . put("difficulty", this . difficulties.toString());
            return o.toString(2);
        }
        catch(JSONException e)
        {
            e . printStackTrace();
        }
        return null;
    }

    public SongDescriptor getDescriptor()
    {
        return this . song;
    }

}
