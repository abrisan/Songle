package tools;

import android.app.Activity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public class DebugMessager
{
    private static DebugMessager ourInstance = new DebugMessager();

    public static DebugMessager getInstance()
    {
        return ourInstance;
    }

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

    public void debug_output(final List<? extends Object> info)
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
        _print_with_title("DEBUG OUTPUT", object.toString());
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

    public <T extends Activity> void debug_trace(T caller, String method, String...varargs)
    {
        _print_with_title(
                "TRACE",
                "[" +
                        caller.getClass().getSimpleName() +
                        " " +
                        method +
                        (varargs.length == 0 ? "" : Arrays.toString(varargs)) +
                        "]"
        );
    }
}
