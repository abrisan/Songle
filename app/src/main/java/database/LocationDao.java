package database;

import android.arch.persistence.room.Query;

import java.util.List;

import datastructures.LocationDescriptor;

public interface LocationDao
{
    @Query(Queries.getUndiscoveredLocations)
    List<LocationDescriptor> getUndiscoveredActiveLocations();

    @Query(Queries.countUndiscoveredLocations)
    int countUndiscoveredLocations();

    @Query(Queries.nukeLocationsDB)
    void nukeDB();
}
