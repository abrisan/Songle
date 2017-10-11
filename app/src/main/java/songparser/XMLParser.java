package songparser;


// Android specific imports
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

// Java specific imports
import java.io.IOException;
import java.io.InputStream;

// Project specigic imports
import com.alexandrubrisan.songle.debug.Debug;

public class XMLParser
{
    public static XmlPullParser parseString(InputStream input)
            throws XmlPullParserException
    {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);

        XmlPullParser parser = factory.newPullParser();
        parser.setInput(input, "utf-8");

        return parser;
    }
}
