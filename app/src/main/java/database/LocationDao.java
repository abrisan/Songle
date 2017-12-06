package database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import datastructures.LocationDescriptor;
import datastructures.Pair;

@Dao
public interface LocationDao
{
    @Query(Queries.getUndiscoveredLocations)
    List<LocationDescriptor> getUndiscoveredActiveLocations(int map_number, int id);

    @Query(Queries.countUndiscoveredLocations)
    int countUndiscoveredLocations();

    @Query(Queries.nukeLocationsDB)
    void nukeDB();

    @Query(Queries.getAllLocations)
    List<LocationDescriptor> getSampleLocations(int map_number);

    @Query(Queries.getGuessedWords)
    List<LocationDescriptor> getGuessedWords(int id);

    @Query(Queries.countByCategory)
    List<Pair> countUndiscoveredWordsByCategory(int id);

    @Query(Queries.getRandomLocations)
    List<LocationDescriptor> getTradeWords(int id, String... cats);

    @Query(Queries.getDiscoveredLocations)
    List<LocationDescriptor> getDiscoveredLocations();

    @Update
    void updateLocation(LocationDescriptor des);

    @Insert
    void insertLocations(LocationDescriptor... locs);
}
