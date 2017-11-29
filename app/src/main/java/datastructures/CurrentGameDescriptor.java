package datastructures;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Debug;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.Settings;

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
    private int gameLevel;
    private boolean isFirstEverGame;
    private List<String> difficulties;
    private DebugMessager console = DebugMessager.getInstance();

    public CurrentGameDescriptor(int gameLevel, String newDifficulty)
    {
        this . gameLevel = gameLevel;
        this . difficulties = Arrays.asList(
                newDifficulty.split(";")
        );
    }

    public CurrentGameDescriptor(Parcel in)
    {
        this(in.readInt(), in.readString());
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

        int currentGame = prefs.getInt(
                ctxt.getString(
                        R.string.current_game_index
                ),
                0
        );

        return new CurrentGameDescriptor(currentGame, diffLevel);
    }

    public int getGameLevel() {return this . gameLevel;}
    public void setGameLevel(int gameLevel) {this . gameLevel = gameLevel;}

    public List<String> getDifficulties() {return this . difficulties;}
    public void setDifficulties(List<String> newList) {this . difficulties = newList;}

    public void setFirstEverGame(boolean val) {this . isFirstEverGame = val;}

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
                this . gameLevel + 1,
                5 - index
        );
    }

    public URL addDifficulty(String difficulty)
            throws MalformedURLException
    {
        if (!this.difficulties.contains(difficulty))
        {
            return DownloadLinks.getMapLinkForSongForMapNumber(
                    this.gameLevel,
                    5  - Arrays.binarySearch(GlobalConstants.difficulty_levels, difficulty)
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
        dest . writeInt(this.gameLevel);
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
            o . put("level", this.gameLevel);
            o . put("difficulty", this . difficulties.toString());
            return o.toString(2);
        }
        catch(JSONException e)
        {
            e . printStackTrace();
        }
        return null;
    }

}
