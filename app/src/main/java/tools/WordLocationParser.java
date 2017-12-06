package tools;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import datastructures.LocationDescriptor;
import datastructures.MarkerDescriptor;
import datastructures.SongLyricsDescriptor;

public class WordLocationParser
{
    private static final DebugMessager console = DebugMessager.getInstance();

    public static String _readText(XmlPullParser parser)
        throws IOException, XmlPullParserException
    {
        if (parser . next() == XmlPullParser.TEXT)
        {
            String result = parser . getText();
            parser . nextTag();
            return result;
        }
        return "";
    }

    public static void _readIcon(XmlPullParser parser,
                                 MarkerDescriptor descriptor,
                                 List<MarkerDescriptor> r_value)
            throws XmlPullParserException, IOException
    {
        parser . require(XmlPullParser.START_TAG, null, "IconStyle");

        parser .nextTag();

        parser . require(XmlPullParser.START_TAG, null, "scale");
        descriptor .setScaleFromString(_readText(parser));
        parser . require(XmlPullParser.END_TAG, null, "scale");

        parser . nextTag();

        parser . require(XmlPullParser.START_TAG, null, "Icon");

        parser . nextTag();

        parser . require(XmlPullParser.START_TAG, null, "href");
        descriptor . setIconLink(_readText(parser));


        parser . require(XmlPullParser.END_TAG, null, "href");

        parser . nextTag();

        parser . require(XmlPullParser.END_TAG, null, "Icon");

        parser . nextTag();

        parser . require(XmlPullParser.END_TAG, null, "IconStyle");

        r_value . add(new MarkerDescriptor(descriptor));
        descriptor . clear();
    }

    private static void _readWord(LocationDescriptor buffer,
                                  SongLyricsDescriptor descriptor,
                                  XmlPullParser parser)
            throws IOException, XmlPullParserException
    {
        parser . nextTag();

        parser . require(XmlPullParser.START_TAG, null, "name");
        String[] text = _readText(parser).split(":");
        parser . require(XmlPullParser.END_TAG, null, "name");
        parser . nextTag();

        buffer . setWord(
                descriptor . getWordAtIndex(
                        Integer . parseInt(text[0]),
                        Integer . parseInt(text[1])
                )
        );

    }

    private static void _readCategory(LocationDescriptor buffer,
                                      XmlPullParser parser)
        throws IOException, XmlPullParserException
    {
        parser . require(XmlPullParser.START_TAG, null, "description");
        buffer . setCategory(
                _readText(parser)
        );
        parser . require(XmlPullParser.END_TAG, null, "description");
        parser . nextTag();
        String useless = _readText(parser);
        parser . nextTag();
    }

    private static void _readPoint(LocationDescriptor buffer,
                                   XmlPullParser parser)
            throws IOException, XmlPullParserException
    {
        parser . require(XmlPullParser.START_TAG, null, "Point");
        parser . nextTag();

        parser . require(XmlPullParser.START_TAG, null, "coordinates");
        buffer . setCoordinates(_readText(parser));
        parser . require(XmlPullParser.END_TAG, null, "coordinates");

        parser . nextTag();
        parser . nextTag();
    }

    private static void _readPlacemark(SongLyricsDescriptor descriptor,
                                       XmlPullParser parser,
                                       LocationDescriptor buffer,
                                       List<LocationDescriptor> r_value)
            throws XmlPullParserException, IOException
    {
        parser . require(XmlPullParser.START_TAG, null, "Placemark");

        _readWord(buffer, descriptor, parser);
        _readCategory(buffer, parser);
        _readPoint(buffer, parser);

        r_value . add(new LocationDescriptor(buffer));

        buffer . clear();
        parser . require(XmlPullParser.END_TAG, null, "Placemark");
    }

    private static void _parse_input(XmlPullParser parser,
                                     SongLyricsDescriptor descriptor,
                                     List<LocationDescriptor> r_value,
                                     List<MarkerDescriptor> r_value_2,
                                     int map_number,
                                     int song_id)
            throws XmlPullParserException, IOException
    {
        parser . require(XmlPullParser.START_TAG, null, "Document");

        MarkerDescriptor descBuffer = new MarkerDescriptor();

        LocationDescriptor locBuffer = new LocationDescriptor();

        locBuffer . setMap_number(map_number);
        locBuffer . setSongId(song_id);

        while (parser . next() != XmlPullParser.END_DOCUMENT)
        {
            if (parser . getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }

            String name = parser . getName();

            switch(name)
            {
                case "Style":
                {
                    descBuffer . setCategory(
                            parser . getAttributeValue(null, "id")
                    );
                    break;
                }
                case "IconStyle":
                {
                    _readIcon(parser, descBuffer, r_value_2);
                    break;
                }
                case "Placemark":
                {
                    _readPlacemark(descriptor, parser, locBuffer, r_value);
                    break;
                }
                default:
                {

                }

            }
        }

    }

    public static void parse(InputStream inp,
                             SongLyricsDescriptor descriptor,
                             List<LocationDescriptor> r_value_1,
                             List<MarkerDescriptor> r_value_2,
                             int map_number,
                             int song_id)
            throws IOException, XmlPullParserException
    {
        try
        {
            XmlPullParser parser = Xml.newPullParser();

            parser . setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser . setInput(inp , null);
            parser . nextTag();

            parser . require(XmlPullParser.START_TAG, null, "kml");

            parser . nextTag();

            _parse_input(parser, descriptor, r_value_1, r_value_2, map_number, song_id);
        }
        finally
        {
            inp . close();
        }

    }
}
