package songparser;

import android.os.AsyncTask;

import com.alexandrubrisan.songle.debug.Debug;
import com.alexandrubrisan.songle.exceptions.CouldNotAddSongException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import songparser.XMLDownloader;
import songparser.XMLParser;


public class SongFetcher extends AsyncTask<String, Void, List<Song>>
{
    @Override
    protected List<Song> doInBackground(String... params)
    {
        if (params.length < 1)
        {
            return null;
        }
        Debug.printBanner("Fetching XML songlist");
        try
        {
            return Song.parseXMLAsSongList(
                    XMLParser.parseString(
                            XMLDownloader.downloadXMLFromUrl(
                                    params[0]
                            )
                    )
            );
        }
        catch (IOException io_exception)
        {
            return null;
        }
        catch (XmlPullParserException exc)
        {
            return null;
        }
        catch (CouldNotAddSongException exc)
        {
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Song> result)
    {

    }
}
