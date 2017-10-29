package tools;

import android.graphics.Color;
import globals.GlobalConstants;


public class GenTools
{
    public static int getColorFromCategory(String category)
    {
        if (!GlobalConstants.categoryColors.containsKey(category))
        {
            return Color.parseColor("#FFFFFF");
        }
        else
        {
            return Color.parseColor(
                    GlobalConstants.categoryColors.get(category)
            );
        }
    }
}
