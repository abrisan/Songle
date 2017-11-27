package tools;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import datastructures.SongLyricsDescriptor;


public class SongLyricsParser
{
    private static final DebugMessager console = DebugMessager.getInstance();


    public static SongLyricsDescriptor parseLyrics(InputStream lyricsFile)
            throws IOException
    {

        SongLyricsDescriptor return_value = new SongLyricsDescriptor();

        int current_byte = lyricsFile.read();

        boolean multiple_whitespace = false;
        boolean multiple_newline = false;

        StringBuilder word_builder = new StringBuilder();

        while (current_byte != -1)
        {
            char byte_as_char = (char) current_byte;

            switch (byte_as_char)
            {
                case '\n':
                {
                    if (multiple_newline)
                    {
                        current_byte = lyricsFile . read();
                        continue;
                    }
                    if (word_builder.length() > 0)
                    {
                        return_value . add_word(word_builder . toString() . toUpperCase());
                        word_builder . setLength(0);

                    }
                    return_value . newline();
                    multiple_newline = true;
                    break;
                }
                case ' ':
                {
                    if (multiple_whitespace)
                    {
                        current_byte = lyricsFile . read();
                        continue;
                    }
                    else
                    {
                        if (word_builder . length() > 0)
                        {
                            return_value . add_word(word_builder . toString() . toUpperCase());
                            word_builder.setLength(0);
                        }
                        multiple_whitespace = true;
                        break;
                    }
                }
                default:
                {
                    if (!Character.isLetter(byte_as_char))
                    {
                        multiple_newline = false;
                        multiple_whitespace = false;
                        current_byte = lyricsFile . read();
                        continue;
                    }
                    else
                    {
                        multiple_newline = false;
                        multiple_whitespace = false;
                        word_builder . append(byte_as_char);
                        break;
                    }
                }

            }

            current_byte = lyricsFile.read();
        }

        return_value . flushBuffer();

        return return_value;
    }



    public static void test()
    {
        try
        {
            InputStream i = new ByteArrayInputStream("This is an example\n".getBytes());
            SongLyricsDescriptor o = parseLyrics(i);
        }
        catch(IOException e)
        {
            e . printStackTrace();
        }

    }

    public static void main(String[] args)
            throws IOException
    {

    }
}
