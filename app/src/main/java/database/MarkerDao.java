package database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import datastructures.MarkerDescriptor;


@Dao
public interface MarkerDao
{
    @Query(Queries.getMarkerForCategory)
    MarkerDescriptor getDescriptorForCategory(String category);

    @Query(Queries.getAllMarkers)
    List<MarkerDescriptor> getAllMarkers();

    @Insert
    void insertMarkers(MarkerDescriptor... markers);
}
