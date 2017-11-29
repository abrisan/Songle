package tools;



import android.os.AsyncTask;
import android.renderscript.ScriptGroup;

import java.util.function.Function;
import java.io.InputStream;
import java.net.URL;
import java.util.function.Consumer;

import globals.GlobalLambdas;

public class DownloadFunction<T> extends AsyncTask<URL, Void, T>
{

    private Function<InputStream, T> function;
    private Consumer<T> callback;

    public DownloadFunction(Function<InputStream, T> function, Consumer<T> callback)
    {
        this . function = function;
        this . callback = callback;
    }


    @Override
    protected T doInBackground(URL... urls)
    {
        DebugMessager.getInstance().debug_output(urls[0]);
        return function.apply(
                GlobalLambdas.get_stream.apply(urls[0])
        );
    }

    @Override
    protected void onPostExecute(T result)
    {
        this . callback . accept(result);
    }

}
