package globals;

import java.util.HashMap;
import java.util.Map;

public class GlobalConstants
{
    public final static String defaultCategory = "Unkown Category";
    public final static Map<String, String> categoryColors = new HashMap<String, String>();

    static
    {
        categoryColors . put("Interesting", "#138D75");
        categoryColors . put("Boring", "#85929E");
        categoryColors . put("Not Boring", "#E59866");
        categoryColors . put("Unclassified", "#A569BD");
        categoryColors . put("Very Interesting", "#CB4335");
    }

    public final static int SONGLE_PERMISSIONS_REQUEST_LOCATION = 1;
    public final static float SONGLE_DISTANCE_WORD_GUESSED_TOLERANCE = 7;
}
