package com.alexandrubrisan.songle.debug;

import static java.lang.System.out;

public class Debug
{
    public static void printBanner(String bannerName)
    {
        out.println(String.format("==== %s ====", bannerName));
    }

    public static void information(String information)
    {
        out.println(String.format("Info : %s", information));
    }

    public static void warning(String warning)
    {
        out.println(String.format("Warning : %s", warning));
    }

    public static void error(String error)
    {
        out.println(String.format("ERROR : %s", error));
    }
}
