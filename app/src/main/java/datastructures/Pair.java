package datastructures;


import android.arch.persistence.room.ColumnInfo;

import org.json.JSONException;
import org.json.JSONObject;

import tools.PrettyPrinter;

public class Pair implements PrettyPrinter
{
    @Override
    public String serialise() throws JSONException
    {
        JSONObject obj = new JSONObject();

        obj.put("category", this.key);
        obj.put("count", this.value);

        return obj.toString(2);
    }

    @ColumnInfo(name = "category")
    private String key;

    @ColumnInfo(name = "COUNT(*)")
    private Integer value;

    public Pair(String key, Integer value)
    {
        this . key = key;
        this . value = value;
    }

    public String getKey()
    {
        return this . key;
    }

    public Integer getValue()
    {
        return this . value;
    }
}
