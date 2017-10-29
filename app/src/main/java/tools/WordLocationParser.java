package tools;

import android.location.Location;
import android.os.Debug;
import android.util.Xml;

import com.google.android.gms.maps.model.Marker;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class WordLocationParser
{
    private static final DebugMessager console = DebugMessager.getInstance();

    public static class LocationDescriptor
    {
        private String word;
        private String category;
        private String coordinates;

        public LocationDescriptor(){}
        public LocationDescriptor(LocationDescriptor des)
        {
            this . word = des . word;
            this . category = des . category;
            this . coordinates = des . coordinates;
        }

        public void clear()
        {
            this . word = "";
            this . category = "";
            this . coordinates = "";
        }

        public void setWord(String word)
        {
            this . word = word;
        }

        public void setCategory(String category)
        {
            this . category = category;
        }

        public void setCoordinates(String coordinates)
        {
            this . coordinates = coordinates;
        }

        public String getCoordinates() {return this . coordinates;}
        public String getCategory() {return this . category;}
        public String getWord() {return this . word;}

        public String serialise()
                throws JSONException
        {
            JSONObject ret = new JSONObject();

            ret . put("word", this . word);
            ret . put("category", this . category);
            ret . put("coordinates", this . coordinates);

            return ret . toString(2);
        }
    }

    public static class MarkerDescriptor
    {
        private String category;
        private String icon_link;
        private double scale;

        public MarkerDescriptor(){}
        public MarkerDescriptor(MarkerDescriptor other)
        {
            this . category = other . category;
            this . icon_link = other . icon_link;
            this . scale = other . scale;
        }

        public void clear()
        {
            this . category = "";
            this . icon_link = "";
            this . scale = 0;
        }

        public void setCategory(String category)
        {
            this . category = category;
        }

        public void setIconLink(String icon_link)
        {
            this . icon_link = icon_link;
        }

        public void setScale(String scale)
        {
            this . scale = Double.parseDouble(scale);
        }

        public double getScale() {return this . scale;}
        public String getIconLink() {return this . icon_link;}
        public String getCategory() {return this . category;}

        public String serialise()
                throws JSONException
        {
            JSONObject ret = new JSONObject();

            ret . put("category", this . category);
            ret . put("icon_link", this . icon_link);
            ret . put("scale", this . scale);

            return ret . toString(2);
        }
    }

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
        descriptor . setScale(_readText(parser));
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
                                  SongLyricsParser.SongLyricsDescriptor descriptor,
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

    private static void _readPlacemark(SongLyricsParser.SongLyricsDescriptor descriptor,
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
                                    SongLyricsParser.SongLyricsDescriptor descriptor,
                                    List<LocationDescriptor> r_value,
                                    List<MarkerDescriptor> r_value_2)
            throws XmlPullParserException, IOException
    {
        parser . require(XmlPullParser.START_TAG, null, "Document");

        MarkerDescriptor descBuffer = new MarkerDescriptor();
        LocationDescriptor locBuffer = new LocationDescriptor();

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
                             SongLyricsParser.SongLyricsDescriptor descriptor,
                             List<LocationDescriptor> r_value_1,
                             List<MarkerDescriptor> r_value_2)
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

            _parse_input(parser, descriptor, r_value_1, r_value_2);
        }
        finally
        {
            inp . close();
        }

    }
}
