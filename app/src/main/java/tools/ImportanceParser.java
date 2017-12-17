package tools;

import android.content.Context;
import android.os.Debug;

import com.songle.s1505883.songle.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alexandrubrisan on 16/12/2017.
 */

public class ImportanceParser
{
    private static DebugMessager console = DebugMessager.getInstance();

    public static Map<String, Double> parse(Context ctxt)
    {
        Map<String, Double> ret_value = new HashMap<>();
        String string_resources = ctxt . getString(R.string.computed_importances);

        String[] lines = string_resources.split("\n");

        for (String line : lines)
        {
            String[] split = line.split(":");
            if (split.length > 2)
            {
                console . error("Line is malformed : " + line);
            }
            else
            {
                String[] word = split[0].split("\'");
                if (word.length < 2)
                {
                    console . error("Word is malformed " + split[0]);
                }
                else
                {
                    ret_value.put(
                            word[1],
                            Double.parseDouble(
                                    split[1].substring(1, split[1].length() - 1)
                            )
                    );
                }
            }
        }
        return ret_value;
    }
}
