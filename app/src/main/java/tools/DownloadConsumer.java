package tools;


import android.content.Context;
import android.os.AsyncTask;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.function.BiConsumer;

import globals.GlobalLambdas;

public class DownloadConsumer extends AsyncTask<URL, Void, Void>
{
    private BiConsumer<Context, InputStream> cons;
    private WeakReference<Context> ctxt;
    private Runnable callback;


    public DownloadConsumer(Context ctxt, BiConsumer<Context, InputStream> consumer)
    {
        this . cons = consumer;
        this . ctxt = new WeakReference<>(ctxt);
    }

    public DownloadConsumer(Context ctxt, BiConsumer<Context, InputStream> consumer, Runnable callback)
    {
        this(ctxt, consumer);
        this . callback = callback;
    }

    @Override
    protected Void doInBackground(URL... strings)
    {
        DebugMessager.getInstance().debug_output(strings[0]);
        cons . accept(
                ctxt.get(),
                GlobalLambdas.get_stream.apply(strings[0])
        );
        return null;
    }

    @Override
    protected void onPostExecute(Void vd)
    {
        if (this.callback != null)
        {
            this.callback.run();
        }
        this . ctxt = null;
    }

}
