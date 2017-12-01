package datastructures;


import com.google.android.gms.maps.model.Marker;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.json.JSONException;

import tools.DebugMessager;
import tools.WordLocationParser;

public class Placemarks
{
    private List<LocationDescriptor> descriptors;
    private List<MarkerDescriptor> markers;

    private DebugMessager console = DebugMessager.getInstance();

    public Placemarks(List<LocationDescriptor> descriptors, List<MarkerDescriptor> markers)
    {
        this . descriptors = descriptors;
        this . markers = markers;
    }

    public List<LocationDescriptor> getDescriptors()
    {
        return this . descriptors;
    }

    public String getMarkerURLForCategory(String category)
    {
        for (MarkerDescriptor m : this . markers)
        {
            if (m . getCategory() . equals(category))
            {
                return m . getIconLink();
            }
        }

        return null;
    }

    public double getScaleForCategory(String category)
    {
        for (MarkerDescriptor m : this . markers)
        {
            if (m . getCategory() . equals(m))
            {
                return m . getScale();
            }
        }

        return -1;
    }



}
