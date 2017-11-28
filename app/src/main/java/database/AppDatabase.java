package database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import datastructures.LocationDescriptor;
import datastructures.SongDescriptor;

@Database(
        entities = {
                SongDescriptor.class,
                LocationDescriptor.class
        },
        version=3
)
public abstract class AppDatabase extends RoomDatabase
{
    private static AppDatabase INSTANCE;
    public abstract SongDao songDao();
    public abstract LocationDao locationDao();

    public static AppDatabase getAppDatabase(Context context)
    {
        if (INSTANCE == null)
        {
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "user-database")
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance()
    {
        INSTANCE = null;
    }

}
