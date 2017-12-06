package tools;

import android.app.Activity;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class DebugMessager
{
    private static DebugMessager ourInstance = new DebugMessager();

    public static DebugMessager getInstance()
    {
        return ourInstance;
    }

    private class MethodTracer
    {
        private List<String> messages = new ArrayList<>();

        public <T> void addMethodCall(T caller, String method, String... message)
        {
            messages.add(
                    "[" + caller.getClass().getSimpleName() + " " + method + "] with message " + Arrays.toString(message) + "\n"
            );
        }

        public void flush()
        {
            StringBuilder builder = new StringBuilder();
            this . messages . forEach(
                    builder::append
            );
            _print_with_title("MethodTracer", builder.toString());
            this . messages .clear();
        }
    }

    private MethodTracer tracer = new MethodTracer();

    private DebugMessager()
    {
    }

    private static void _print_with_title(final String title, final String message)
    {
        System.out.printf("%s >>> %s\n", title, message);
    }

    public void info(final String info)
    {
        _print_with_title("Information", info);
    }

    public <T> void debug_output(final List<T> info)
    {
        for (Object o : info)
        {
            _print_with_title("DEBUG OUTPUT", o.toString());
        }
    }

    void debug_output(final Set<? extends Object> objects)
    {
        for (Object o : objects)
        {
            _print_with_title("DEBUG OUTPUT", o.toString());
        }
    }

    public void debug_output(Object object)
    {
        if (object == null)
        {
            _print_with_title("DEBUG OUTPUT", "null");
        }
        else
        {
            _print_with_title("DEBUG OUTPUT", object.toString());
        }
    }

    public <K, V> void debug_map(Map<K, V> map)
    {
        StringBuilder str = new StringBuilder();

        str . append("{");

        BiConsumer<K, V> output = (k, v) -> {
            str . append("(");
            str . append(k . toString());
            str . append(" , ");
            str . append(v . toString());
            str . append(") ; ");
        };

        map . forEach(output);

        str . append("}");

        info(str . toString());
    }

    public void warning(final String warning)
    {
        _print_with_title("Warning", warning);
    }

    public void error(final String error)
    {
        _print_with_title("ERROR", error);
    }

    public <T> void debug_trace(T caller, String method, String...varargs)
    {
        _print_with_title(
                "TRACE",
                "[" +
                        caller.getClass().getSimpleName() +
                        " " +
                        method +
                        "] with args " + (varargs.length == 0 ? "" : Arrays.toString(varargs))
        );
    }

    public <T> void debug_method_trace(T caller, String method, String...varargs)
    {
        this . tracer . addMethodCall(caller, method, varargs);
    }

    public void flushMethodTrace()
    {
        this . tracer . flush();
    }

    public <T> void debug_output(Stream<T> str)
    {
        str . forEach(this::debug_output);
    }

    public <T extends PrettyPrinter> void debug_output_json(List<T> elems)
    {
        debug_output(elems.stream().map(x -> {
            try
            {
                return x . serialise();
            }
            catch(JSONException e)
            {
                return e . getStackTrace()[0].toString();
            }
        }));
    }

    public <T extends PrettyPrinter> void debug_json_singleton(T elem)
    {
        try
        {
            debug_output(elem.serialise());
        }
        catch(JSONException e)
        {
            e . printStackTrace();
        }
    }
}
