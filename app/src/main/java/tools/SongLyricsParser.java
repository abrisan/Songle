package tools;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class SongLyricsParser
{
    public static class SongLyricsDescriptor
    {
        private List<List<String>> raw_lyrics;
        private List<String> buffer;
        private Set<String> words;

        public SongLyricsDescriptor()
        {
            raw_lyrics = new ArrayList<>();
            buffer = new ArrayList<>();
            words = new HashSet<>();
        }

        public void add_word(String word)
        {
            words . add(word);
            buffer . add(word);
        }

        public void newline()
        {
            raw_lyrics . add(new ArrayList<>(buffer));
            buffer . clear();
        }

        public void flushBuffer()
        {
            if (raw_lyrics . size() > 0)
            {
                raw_lyrics . add(new ArrayList<>(buffer));
                buffer.clear();
            }
        }

        public String getWordAtIndex(int row, int word_number)
        {
            try
            {
                return this . raw_lyrics . get(row) . get(word_number);
            }
            catch (Exception e)
            {
                return "NO SUCH INDEX";
            }
        }


        public String toString()
        {
            StringBuilder ret = new StringBuilder();
            ret . append("UNIQUE WORDS : \n");
            for (String s : this . words)
            {
                ret . append(String.format("%s\n", s));
            }

            return ret . toString();
        }

    }


    public static SongLyricsDescriptor parseLyrics(InputStream lyricsFile)
            throws IOException
    {

        SongLyricsDescriptor return_value = new SongLyricsDescriptor();

        int current_byte = lyricsFile.read();
        boolean multiple_whitespace = false;
        StringBuilder word_builder = new StringBuilder();


        while (current_byte != -1)
        {
            char byte_as_char = (char) current_byte;

            switch (byte_as_char)
            {
                case '\n':
                {

                    if (word_builder.length() > 0)
                    {
                        return_value . add_word(word_builder . toString());
                        word_builder . setLength(0);
                    }
                    return_value . newline();
                    break;
                }
                case ' ':
                {
                    if (multiple_whitespace)
                    {
                        continue;
                    }
                    else
                    {
                        return_value . add_word(word_builder . toString());
                        word_builder.setLength(0);
                        multiple_whitespace = true;
                        break;
                    }
                }
                default:
                {
                    multiple_whitespace = false;
                    word_builder.append(byte_as_char);
                    break;
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
            SongLyricsParser.SongLyricsDescriptor o = parseLyrics(i);
            DebugMessager.getInstance().debug_output(o);
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
