package globals;


import java.net.MalformedURLException;
import java.net.URL;

public class DownloadLinks
{
    public final static class InvalidSongNumberException extends MalformedURLException
    {
        public InvalidSongNumberException(MalformedURLException e)
        {
            super(e.toString());
        }
    };

    private final static String songListLink =
            "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/songs.xml";

    private final static String songLyricsLink =
            "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/%s%d/words.txt";

    private final static String mapLink =
            "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/%s%d/map%d.kml";

    public static URL getSongListLink()
            throws MalformedURLException
    {
        return new URL(songListLink);
    }

    public static URL getSongLyricsLinkForSong(int number)
            throws InvalidSongNumberException
    {
        try
        {
            return new URL(
                    String.format(songLyricsLink, number < 10 ? "0" : "", number)
            );
        }
        catch(MalformedURLException e)
        {
            throw new InvalidSongNumberException(e);
        }

    }

    public static URL getMapLinkForSongForMapNumber(int songNumber, int mapNumber)
            throws InvalidSongNumberException
    {
        try
        {
            return new URL(
                    String.format(mapLink,
                            songNumber < 10 ? "0" : "",
                            songNumber,
                            mapNumber)
            );
        }
        catch(MalformedURLException e)
        {
            throw new InvalidSongNumberException(e);
        }
    }


}
