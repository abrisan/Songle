package database;

import android.os.AsyncTask;

import java.util.function.BiConsumer;
import tools.DebugMessager;


public class DatabaseWriteTask<T> extends AsyncTask<T, Void, Void>
{
    private AppDatabase db;
    private final DebugMessager console = DebugMessager.getInstance();
    private BiConsumer<AppDatabase, T> operation;

    public DatabaseWriteTask(AppDatabase db, BiConsumer<AppDatabase, T> operation)
    {
        this . db = db;
        this . operation = operation;
    }

    @Override
    protected Void doInBackground(T... voids)
    {
        this . operation . accept(this . db, voids[0]);
        return null;
    }

}
