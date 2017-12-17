package globals;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import datastructures.SongDescriptor;
import datastructures.TradeDescriptor;
import tools.Algorithm;
import tools.DebugMessager;

public class GlobalConstants
{
    public final static String defaultCategory = "Unkown Category";
    public final static Map<String, String> categoryColors = new HashMap<String, String>();

    private final static List<TradeDescriptor> trade_values = new LinkedList<>();


    static
    {
        categoryColors . put("Interesting", "#138D75");
        categoryColors . put("Boring", "#85929E");
        categoryColors . put("NotBoring", "#E59866");
        categoryColors . put("Unclassified", "#A569BD");
        categoryColors . put("VeryInteresting", "#CB4335");

        trade_values . add(
                new TradeDescriptor("unclassified", "boring", 2, 1)
        );

        trade_values . add(
                new TradeDescriptor("boring", "notboring", 4, 1)
        );

        trade_values . add(
                new TradeDescriptor("notboring", "interesting", 8, 1)
        );

        trade_values . add(
                new TradeDescriptor("interesting", "veryinteresting", 4, 1)
        );
    }

    public static TradeDescriptor.ActualTrade getRate(String from, String to)
    {
        int fromPos = Algorithm.Collections.linearSearch(lower_word_cats, from.toLowerCase());
        int toPos = Algorithm.Collections.linearSearch(lower_word_cats, to.toLowerCase());

        if (fromPos > toPos)
        {
            DebugMessager.getInstance().info("Going backwards");
            DebugMessager.getInstance().info("Query is " + from + " to " + to);
            return Algorithm.Graph.searchGraph(trade_values, from, to, 1);
        }
        TradeDescriptor.ActualTrade ret_value = Algorithm.Graph.searchGraph(trade_values, to, from, 1);
        if (ret_value != null)
        {
            int copy = ret_value.to;
            ret_value.to = ret_value.from;
            ret_value.from = copy;
        }
        return ret_value;
    }

    public static TradeDescriptor.ActualTrade getRate(String from, String to, int qty)
    {
        return Algorithm.Graph.searchGraph(trade_values, to, from, qty);
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

    private final static String[] lower_word_cats = new String[]{
            "unclassified",
            "boring",
            "notboring",
            "interesting",
            "veryinteresting"
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
                Algorithm.Collections.searchInMap(
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
