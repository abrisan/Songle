package tools;

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
        System.out.printf("%s : %s\n", title, message);
    }

    public void info(final String info)
    {
        _print_with_title("Information", info);
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
