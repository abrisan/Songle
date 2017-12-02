package database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import datastructures.SongDescriptor;


@Dao
public interface SongDao
{
    @Query(Queries.getAllSongs)
    List<SongDescriptor> getAll();

    @Query(Queries.getSongForId)
    SongDescriptor getSongForId(int id);

    @Query(Queries.getGuessedSongs)
    List<SongDescriptor> getGuessedSongs();

    @Query(Queries.nukeSongsDB)
    void nukeDB();

    @Query(Queries.getRandomSong)
    SongDescriptor getRandomUnguessedSong();

    @Insert
    void insertSong(SongDescriptor songDescriptor);
}
