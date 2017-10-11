package songparser;


import com.alexandrubrisan.songle.debug.Debug;
import com.alexandrubrisan.songle.exceptions.CouldNotAddSongException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Song
{
    private final int number;
    private final String artist;
    private final String title;
    private final String link;

    public Song(int number, String artist, String title, String link)
    {
        this.number = number;
        this.artist = artist;
        this.title = title;
        this.link = link;
    }

    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Song Object\n");
        builder.append(String.format("Song title %s\n", this.title));
        builder.append(String.format("Song artist %s\n", this.artist));
        builder.append(String.format("Song link %s\n", this.link));
        return builder.toString();
    }

    public final int getNumber()
    {
        return number;
    }

    public final String getArtist()
    {
        return artist;
    }

    public final String getTitle()
    {
        return title;
    }

    public final String getLink()
    {
        return link;
    }

    public static List<Song> parseXMLAsSongList(XmlPullParser parser)
            throws IOException, XmlPullParserException, CouldNotAddSongException
    {
        List<Song> return_list = new ArrayList<>();
        Stack<String> token_stack = new Stack<>();
        Map<String, String> song_components = new HashMap<>();
        int event = parser.getEventType();
        while (event != XmlPullParser.END_DOCUMENT)
        {
            switch (event)
            {
                case XmlPullParser.START_TAG:
                {
                    if (!parser.getName().equals("Songs") && !parser.getName().equals("Song"))
                    {
                        token_stack.push(parser.getName());
                    }
                    break;
                }
                case XmlPullParser.END_TAG:
                {
                    if (token_stack.size() > 0)
                    {
                        token_stack.pop();
                    }
                    break;
                }
                case XmlPullParser.TEXT:
                {
                    if (token_stack.size() > 0)
                    {
                        song_components.put(token_stack.peek(), parser.getText());
                    }
                    break;
                }
            }
            if (song_components.size() == 4)
            {
                boolean add_to_list = return_list.add(
                        new Song(
                                Integer.parseInt(song_components.get("Number")),
                                song_components.get("Artist"),
                                song_components.get("Title"),
                                song_components.get("Link")
                        )
                );
                if (!add_to_list)
                {
                    throw new CouldNotAddSongException();
                }
                song_components.clear();
            }
            event = parser.next();
        }

        return return_list;
    }
}
