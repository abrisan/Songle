package globals;


import android.content.Context;
import android.content.SharedPreferences;

import com.songle.s1505883.songle.R;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import database.AppDatabase;
import datastructures.LocationDescriptor;
import datastructures.MarkerDescriptor;
import datastructures.Placemarks;
import datastructures.SongDescriptor;
import datastructures.SongLyricsDescriptor;
import tools.DebugMessager;
import tools.SongListParser;
import tools.SongLyricsParser;
import tools.WordLocationParser;

public class GlobalLambdas
{

    public static final Function<URL, InputStream> get_stream = (url) -> {
        try
        {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn . setReadTimeout(10000);
            conn . setConnectTimeout(15000);
            conn . setRequestMethod("GET");
            conn . setDoInput(true);

            conn . connect();
            return conn . getInputStream();
        }
        catch(Exception e)
        {
            return null;
        }
    };

    public static final BiConsumer<AppDatabase, List<SongDescriptor>> insertSongsConsumer =
            (db, l) ->
            {
                db.songDao().nukeDB();
                l.forEach(x ->
                {
                    try
                    {
                        db.songDao().insertSong(x);
                    }
                    catch (android.database.sqlite.SQLiteConstraintException e)
                    {
                        DebugMessager.getInstance().error(
                                "Trying to insert duplicate key " +
                                        x.getNumber() +
                                        " into database. Skipping"
                        );
                    }
                    catch (Exception e)
                    {
                        throw e;
                    }
                });
            };


    public static final BiConsumer<Context, InputStream> initialCheckAndDownload = (ctxt, inputStream) ->
    {
        try
        {
            List<SongDescriptor> r_value = new ArrayList<>();

            SharedPreferences prefs = ctxt.getSharedPreferences(
                    ctxt.getString(
                            R.string.shared_prefs_key
                    ),
                    Context.MODE_PRIVATE
            );

            String existingTimestamp =
                    prefs.getString(
                            ctxt.getString(
                                    R.string.timestamp_key
                            ),
                            null
                    );

            String new_timestamp = SongListParser.parse(inputStream, r_value, existingTimestamp);

            if (new_timestamp == null)
            {
                DebugMessager.getInstance().info("NO NEW SONGS ADDED");
                return;
            }

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(
                    ctxt . getString(R.string.timestamp_key),
                    new_timestamp
            );
            editor.commit();

            insertSongsConsumer.accept(
                    AppDatabase.getAppDatabase(ctxt),
                    r_value
            );
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    };


    public static final Function<AppDatabase, List<SongDescriptor>> getGuessedDescriptors =
            db -> db .songDao() . getGuessedSongs();

    public static final Function<InputStream, SongLyricsDescriptor> getLyrics = is -> {
        try
        {
            return SongLyricsParser.parseLyrics(is);
        }
        catch (IOException e)
        {
            e . printStackTrace();
            return null;
        }
    };

    public static final Function<SongLyricsDescriptor, BiConsumer<Context, InputStream>> getMaps = (des) -> (ctxt, is) -> {
        List<LocationDescriptor> r_value_1 = new ArrayList<>();
        List<MarkerDescriptor> r_value_2 = new ArrayList<>();
        AppDatabase db = AppDatabase.getAppDatabase(ctxt);

        try
        {
            WordLocationParser.parse(
                    is,
                    des,
                    r_value_1,
                    r_value_2
            );

            r_value_1 . forEach(x ->
            {
                try
                {
                    db.locationDao().insertLocations(x);
                }
                catch (android.database.sqlite.SQLiteConstraintException e)
                {
                    DebugMessager.getInstance().error(
                            "Trying to insert duplicate key (" +
                                    x.getCoordinates() + ", " +
                                    x.getWord() + ", " +
                                    x.getCategory() + ", " +
                                    x . getSongId() + ")" +
                                    " into database. Skipping"
                    );
                }
                catch(Exception e)
                {
                    throw e;
                }
            });

            r_value_2 . forEach(x ->
            {
                try
                {
                    db.markerDao().insertMarkers(x);
                }
                catch (android.database.sqlite.SQLiteConstraintException e)
                {
                    DebugMessager.getInstance().error(
                            "Trying to insert duplicate key " +
                                    x.getCategory() +
                                    " into database. Skipping"
                    );
                }
                catch(Exception e)
                {
                    throw e;
                }
            });
        }
        catch (IOException|XmlPullParserException e)
        {
            e . printStackTrace();
        }
        catch (Exception e)
        {
            throw e;
        }
    };

    public final static Function<AppDatabase, Placemarks> plm = db -> new Placemarks(
            db . locationDao() . getUndiscoveredActiveLocations(),
            db . markerDao() . getAllMarkers()
    );

}
