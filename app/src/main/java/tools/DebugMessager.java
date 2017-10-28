package tools;

import java.util.List;
import java.util.Set;

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

    public void warning(final String warning)
    {
        _print_with_title("Warning", warning);
    }

    public void error(final String error)
    {
        _print_with_title("ERROR", error);
    }
}
