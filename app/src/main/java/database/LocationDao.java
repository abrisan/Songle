package database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import datastructures.LocationDescriptor;

@Dao
public interface LocationDao
{
    @Query(Queries.getUndiscoveredLocations)
    List<LocationDescriptor> getUndiscoveredActiveLocations();

    @Query(Queries.countUndiscoveredLocations)
    int countUndiscoveredLocations();

    @Query(Queries.nukeLocationsDB)
    void nukeDB();
}
