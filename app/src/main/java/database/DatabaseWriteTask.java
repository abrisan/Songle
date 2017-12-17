package database;

import android.os.AsyncTask;

import java.util.function.BiConsumer;
import tools.DebugMessager;


public class DatabaseWriteTask<T> extends AsyncTask<T, Void, Void>
{
    // A generic DB read task

    private AppDatabase db;
    private final DebugMessager console = DebugMessager.getInstance();
    private BiConsumer<AppDatabase, T> operation;
    private Runnable callback;

    public DatabaseWriteTask(AppDatabase db, BiConsumer<AppDatabase, T> operation)
    {
        this . db = db;
        this . operation = operation;
    }

    public DatabaseWriteTask(AppDatabase db, BiConsumer<AppDatabase, T> operation, Runnable callback)
    {
        this(db, operation);
        this . callback = callback;
    }

    @Override
    protected Void doInBackground(T... voids)
    {
        this . operation . accept(this . db, voids[0]);
        return null;
    }

    @Override
    protected void onPostExecute(Void t)
    {
        if (this . callback != null)
        {
            this . callback . run();
        }
    }

}
