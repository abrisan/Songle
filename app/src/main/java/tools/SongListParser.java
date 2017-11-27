package tools;


import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import datastructures.SongDescriptor;

public class SongListParser
{
    private static final DebugMessager console = DebugMessager.getInstance();

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

        r_value . setSongName(
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


    public static String parse(InputStream inp,
                               List<SongDescriptor> r_value,
                               String current_timestamp)
            throws IOException, XmlPullParserException
    {
        String timestamp = null;
        try
        {
            XmlPullParser parser = Xml.newPullParser();

            parser . setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);

            parser . setInput(inp, null);

            parser . nextTag();

            parser . require(XmlPullParser.START_TAG, null, "Songs");

            timestamp = parser . getAttributeValue(null, "timestamp");

            if (!timestamp.equals(current_timestamp))
            {
                _parse_input(parser, r_value);
                return timestamp;
            }
            else
            {
                return null;
            }
        }
        finally
        {
            inp . close();
        }
    }
}
