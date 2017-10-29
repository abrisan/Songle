package tools;


import android.os.Debug;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class SongLyricsParser
{
    private static final DebugMessager console = DebugMessager.getInstance();

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
                return this . raw_lyrics . get(row - 1) . get(word_number - 1);
            }
            catch (Exception e)
            {
                return "NO SUCH INDEX";
            }
        }

        public Set<String> getWords()
        {
            return this . words;
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

        public void debugList()
        {
            for (int i = 0 ; i < this . raw_lyrics . size() ; ++i)
            {
                console . info(
                        String.format("%d : %s",
                                i + 1,
                                this . raw_lyrics . get(i) . toString()
                        )
                );
            }
        }

    }


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
            SongLyricsParser.SongLyricsDescriptor o = parseLyrics(i);
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
