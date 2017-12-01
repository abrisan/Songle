package database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import datastructures.LocationDescriptor;

@Dao
public interface LocationDao
{
    @Query(Queries.getUndiscoveredLocations)
    List<LocationDescriptor> getUndiscoveredActiveLocations(int map_number);

    @Query(Queries.countUndiscoveredLocations)
    int countUndiscoveredLocations();

    @Query(Queries.nukeLocationsDB)
    void nukeDB();

    @Query(Queries.getAllLocations)
    List<LocationDescriptor> getSampleLocations(int map_number);

    @Query(Queries.getGuessedWords)
    List<LocationDescriptor> getGuessedWords(int id);

    @Insert
    void insertLocations(LocationDescriptor... locs);
}
