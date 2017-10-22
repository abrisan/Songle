package toolstest;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import tools.DebugMessager;

public class DebugMessagerTester
{
    DebugMessager d = DebugMessager.getInstance();
    ByteArrayOutputStream out;

    @org.junit.Before
    public void setUp()
    {
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
    }

    @org.junit.Test
    public void testConstructor()
    {
        assertNotNull(d);
    }


    @org.junit.Test
    public void testInfo()
    {
        d . info("TEST");

        assertEquals(
                out.toString(),
                "Information : TEST\n"
        );
    }

    @org.junit.Test
    public void testWarning()
    {
        d . warning("TEST");

        assertEquals(
                out.toString(),
                "Warning : TEST\n"
        );
    }

    @org.junit.Test
    public void testError()
    {
        d . error("TEST");

        assertEquals(
                out.toString(),
                "ERROR : TEST\n"
        );
    }
}
