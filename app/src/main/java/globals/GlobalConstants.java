package globals;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import datastructures.SongDescriptor;
import tools.Algorithm;

public class GlobalConstants
{
    public final static String defaultCategory = "Unkown Category";
    public final static Map<String, String> categoryColors = new HashMap<String, String>();

    private static class CTPair
    {
        String cat_1;
        String cat_2;
        int conversionRate;
        CTPair(String cat_1, String cat_2, int conversionRate)
        {
            this . cat_1 = cat_1.toLowerCase();
            this . cat_2 = cat_2.toLowerCase();
            this . conversionRate = conversionRate;
        }
    }

    private final static List<CTPair> conversionRates = new ArrayList<>();


    static
    {
        categoryColors . put("Interesting", "#138D75");
        categoryColors . put("Boring", "#85929E");
        categoryColors . put("NotBoring", "#E59866");
        categoryColors . put("Unclassified", "#A569BD");
        categoryColors . put("VeryInteresting", "#CB4335");

        conversionRates.add(
                new CTPair("Boring", "NotBoring", 4)
        );

        conversionRates.add(
                new CTPair("NotBoring", "Interesting", 8)
        );

        conversionRates.add(
                new CTPair("Interesting", "VeryInteresting", 16)
        );

    }

    public static double getRate(String from, String to)
    {
        from = from.toLowerCase();
        to = to.toLowerCase();
        if (from.equals("unclassified") || to.equals("unclassified"))
        {
            return 1.0;
        }
        for (CTPair p : conversionRates)
        {
            if (p.cat_1.equals(from) && p.cat_2.equals(to))
            {
                return 1 / p.conversionRate;
            }
            else if(p.cat_2.equals(from) && p.cat_1.equals(to))
            {
                return p.conversionRate;
            }
            else if(p.cat_1.equals(from))
            {
                return 1 / (p.conversionRate * getRate(p.cat_2, to));
            }
            else if(p.cat_2.equals(from))
            {
                return p.conversionRate * getRate(p.cat_1, to);
            }
        }
        return -1;
    }

    public final static int SONGLE_PERMISSIONS_REQUEST_LOCATION = 1;
    public final static float SONGLE_DISTANCE_WORD_GUESSED_TOLERANCE = (float) 12.5;

    public final static String[] difficulty_levels = new String[]{
            "Beginner",
            "Intermediate",
            "Advanced",
            "Expert",
            "Champion",
            "Smart Mode"
    };


    public final static String COLOR_RED_HEX = "#b02323";
    public final static String COLOR_GREEN_HEX = "#007f00";
    public final static String COLOR_WHITE = "#ffffff";
    public final static String COLOR_LIGHT_GRAY = "#7B68EE";
    public final static String COLOR_BLACK = "#000000";

    private final static SongDescriptor d1 = new SongDescriptor(
      "Kogong",
            "Mark Forster",
            "https://youtu.be/7h7ntYLLrfQ",
            1,
            false
    );

    private final static SongDescriptor d2 = new SongDescriptor(
            "Lumea s-a schimbat",
            "Cheloo",
            "https://youtu.be/3gmJROB2Wzw",
            2,
            false
    );

    private final static SongDescriptor d3 = new SongDescriptor(
            "Dusk till Dawn",
            "Sia feat. Zayn",
            "https://youtu.be/tt2k8PGm-TI",
            3,
            false
    );

    private final static SongDescriptor d4 = new SongDescriptor(
            "Syrens in Paris",
            "Fytch",
            "https://youtu.be/3aeSiPZ9i9I",
            4,
            false
    );

    private final static SongDescriptor d5 = new SongDescriptor(
            "Big Girls Cry",
            "Sia",
            "https://www.youtube.com/watch?v=4NhKWZpkw1Q",
            5,
            false
    );

    public final static List<SongDescriptor> placeholderVideos = Arrays.asList(
            d1,
            d2,
            d3,
            d4,
            d5
    );

    public static final String currentGameKey = "current_game";
    public static final String diffKey = "difficulty";
    public static final String gameDescriptor = "game_descriptor";

    public static int getColorFromCategory(String category)
    {
        return Color.parseColor(
                Algorithm.searchInMap(
                        categoryColors,
                        category,
                        String::toLowerCase,
                        "#FFFFFF"
                )
        );
    }

    public static final String userName = "userName";
    public static final String imageURI = "userURI";
    public static final String onlineImageURL = "onlineURL";


}
