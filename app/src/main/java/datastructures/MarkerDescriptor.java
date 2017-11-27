package datastructures;

import org.json.JSONException;
import org.json.JSONObject;

import tools.WordLocationParser;

/**
 * Created by alexandrubrisan on 27/11/2017.
 */
public class MarkerDescriptor
{
    private String category;
    private String icon_link;
    private double scale;

    public MarkerDescriptor()
    {
    }

    public MarkerDescriptor(MarkerDescriptor other)
    {
        this.category = other.category;
        this.icon_link = other.icon_link;
        this.scale = other.scale;
    }

    public void clear()
    {
        this.category = "";
        this.icon_link = "";
        this.scale = 0;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public void setIconLink(String icon_link)
    {
        this.icon_link = icon_link;
    }

    public void setScale(String scale)
    {
        this.scale = Double.parseDouble(scale);
    }

    public double getScale()
    {
        return this.scale;
    }

    public String getIconLink()
    {
        return this.icon_link;
    }

    public String getCategory()
    {
        return this.category;
    }

    public String serialise()
            throws JSONException
    {
        JSONObject ret = new JSONObject();

        ret.put("category", this.category);
        ret.put("icon_link", this.icon_link);
        ret.put("scale", this.scale);

        return ret.toString(2);
    }
}
