package database;

import android.os.AsyncTask;

import java.util.List;

import datastructures.SongDescriptor;
import tools.DebugMessager;


public class AddSongDescriptorsTask extends AsyncTask<List<SongDescriptor>, Void, Void>
{
    private AppDatabase db;
    private final DebugMessager console = DebugMessager.getInstance();

    public AddSongDescriptorsTask(AppDatabase db)
    {
        this . db = db;
    }

    @Override
    protected Void doInBackground(List<SongDescriptor>... voids)
    {
        try
        {
            for (SongDescriptor d : voids[0])
            {
                this . db . userDao() . insertSong(d);
            }
        }
        catch (android.database.sqlite.SQLiteConstraintException e)
        {
            console . error("Trying to insert duplicate key into database");
        }
        catch (Exception e)
        {
            throw e;
        }
        return null;
    }

}
