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

    static
    {
        categoryColors . put("Interesting", "#138D75");
        categoryColors . put("Boring", "#85929E");
        categoryColors . put("NotBoring", "#E59866");
        categoryColors . put("Unclassified", "#A569BD");
        categoryColors . put("VeryInteresting", "#CB4335");
    }

    public final static int SONGLE_PERMISSIONS_REQUEST_LOCATION = 1;
    public final static float SONGLE_DISTANCE_WORD_GUESSED_TOLERANCE = 7;

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

}
