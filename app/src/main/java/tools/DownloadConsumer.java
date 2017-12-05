package tools;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Debug;
import android.os.HandlerThread;
import android.provider.Settings;

import com.songle.s1505883.songle.BuildConfig;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.logging.Handler;

import datastructures.DownloadTypes;
import globals.GlobalLambdas;

public class DownloadConsumer extends AsyncTask<URL, Void, Void>
{
    private BiConsumer<Context, InputStream> cons;
    private WeakReference<Context> ctxt;

    public DownloadConsumer(Context ctxt, BiConsumer<Context, InputStream> consumer)
    {
        this . cons = consumer;
        this . ctxt = new WeakReference<>(ctxt);
    }

    @Override
    protected Void doInBackground(URL... strings)
    {
        cons . accept(
                ctxt.get(),
                GlobalLambdas.get_stream.apply(strings[0])
        );
        return null;
    }

    @Override
    protected void onPostExecute(Void vd)
    {
        this . ctxt = null;
    }

}
