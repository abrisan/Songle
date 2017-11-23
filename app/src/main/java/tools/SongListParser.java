package tools;


import android.util.Xml;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class SongListParser
{
    private static final DebugMessager console = DebugMessager.getInstance();

    public static class SongDescriptor
    {
        private String songName;
        private String artistName;
        private String link;
        private int number;

        public SongDescriptor() {}

        public SongDescriptor(SongDescriptor d)
        {
            this . songName = d . songName;
            this . artistName = d . artistName;
            this . link = d . link;
            this . number = d . number;
        }

        public void clear()
        {
            this . songName = null;
            this . artistName = null;
            this . link = null;
            this . number = -1;
        }


        public void setName(String name)  {  this . songName = name;  }

        public void setArtistName(String artistName) { this . artistName = artistName; }

        public void setLink(String link)  { this . link = link; }

        public void setNumber(int number) {this . number = number;}

        public String getSongName() {return this . songName;}
        public String getArtistName() {return this . artistName;}
        public String getLink() {return this . link;}
        public int getNumber() {return this . number;}

        public String serialise()
            throws JSONException
        {
            JSONObject ret = new JSONObject();

            ret . put("name", this . songName);
            ret . put("artist", this . artistName);
            ret . put("link", this . link);
            ret . put("number", this.number);

            return ret . toString(2);
        }

    }

    private static String _read_text(XmlPullParser parser, String tagCat)
            throws IOException, XmlPullParserException
    {
        parser . require(XmlPullParser.START_TAG, null, tagCat);
        if (parser . next() == XmlPullParser.TEXT)
        {
            String result = parser . getText();
            parser . nextTag();
            parser . require(XmlPullParser.END_TAG, null, tagCat);
            parser . nextTag();
            return result;
        }
        return "";
    }

    private static void _parse_song(XmlPullParser parser,
                                    SongDescriptor r_value)
            throws IOException, XmlPullParserException
    {
        parser . require(XmlPullParser.START_TAG, null, "Song");
        parser . nextTag();

        r_value . setNumber(
                Integer.parseInt(
                        _read_text(parser, "Number")
                )
        );

        r_value . setArtistName(
                _read_text(parser, "Artist")
        );

        r_value . setName(
                _read_text(parser, "Title")
        );

        r_value . setLink(
                _read_text(parser, "Link")
        );

        parser . require(XmlPullParser.END_TAG, null, "Song");
    }

    private static void _parse_input(XmlPullParser parser,
                                     List<SongDescriptor> r_value)
            throws IOException, XmlPullParserException
    {
        SongDescriptor descriptor = new SongDescriptor();

        while (parser . next() != XmlPullParser . END_DOCUMENT)
        {
            if (parser . getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }

            console . info("Attempting to parse song");
            _parse_song(parser, descriptor);

            r_value . add(new SongDescriptor(descriptor));
            descriptor . clear();

            console . info("OK");
        }
    }


    public static void parse(InputStream inp,
                             List<SongDescriptor> r_value)
            throws IOException, XmlPullParserException
    {
        try
        {
            XmlPullParser parser = Xml.newPullParser();

            parser . setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);

            parser . setInput(inp, null);

            parser . nextTag();

            parser . require(XmlPullParser.START_TAG, null, "Songs");

            _parse_input(parser, r_value);
        }
        finally
        {
            inp . close();
        }
    }
}
