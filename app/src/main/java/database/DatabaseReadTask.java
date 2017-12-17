package database;

import android.arch.persistence.room.Query;
import android.os.AsyncTask;

import java.util.function.Consumer;
import java.util.function.Function;


public class DatabaseReadTask<T> extends AsyncTask<Function<AppDatabase, T>, Void, T>
{

    // A generic database read task
    private AppDatabase db;
    private Consumer<T> callback;

    public DatabaseReadTask(AppDatabase db, Consumer<T> callback)
    {
        this . db = db;
        this . callback = callback;
    }

    @Override
    @SafeVarargs
    protected final T doInBackground(Function<AppDatabase, T>... params)
    {
        return params[0].apply(this . db);
    }

    @Override
    protected void onPostExecute(T result)
    {
        this . callback . accept(result);
    }
}
