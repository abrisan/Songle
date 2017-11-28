package globals;


import android.content.Context;
import android.content.SharedPreferences;

import com.songle.s1505883.songle.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import database.AppDatabase;
import datastructures.SongDescriptor;
import tools.DebugMessager;
import tools.SongListParser;

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
            (db, l) -> l.forEach(x ->
            {
                try
                {
                    db.songDao().nukeDB();
                    db.songDao().insertSong(x);
                } catch (android.database.sqlite.SQLiteConstraintException e)
                {
                    DebugMessager.getInstance().error(
                            "Trying to insert duplicate key " +
                                    x.getNumber() +
                                    " into database. Skipping"
                    );
                } catch (Exception e)
                {
                    throw e;
                }
            });

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

            /*if (new_timestamp == null)
            {
                DebugMessager.getInstance().info("NO NEW SONGS ADDED");
                return;
            }*/

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

    public static final Function<AppDatabase, Boolean> shouldDownloadLocation = db -> {
        return false;
    };

}
