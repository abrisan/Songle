package datastructures;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

@Entity(tableName = "markers")
public class MarkerDescriptor
{
    @PrimaryKey
    private String category;

    @ColumnInfo
    private String iconLink;

    @ColumnInfo
    private double scale;


    public MarkerDescriptor()
    {
    }

    public MarkerDescriptor(MarkerDescriptor other)
    {
        this.category = other.category;
        this.iconLink = other.iconLink;
        this.scale = other.scale;
    }

    public void clear()
    {
        this.category = "";
        this.iconLink = "";
        this.scale = 0;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public void setIconLink(String icon_link)
    {
        this.iconLink = icon_link;
    }

    public void setScaleFromString(String scale)
    {
        this.scale = Double.parseDouble(scale);
    }

    public void setScale(double scale) {this.scale = scale;}

    public double getScale()
    {
        return this.scale;
    }

    public String getIconLink()
    {
        return this.iconLink;
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
        ret.put("iconLink", this.iconLink);
        ret.put("scale", this.scale);

        return ret.toString(2);
    }
}
