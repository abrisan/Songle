package datastructures;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import tools.DebugMessager;
import tools.SongLyricsParser;

public class SongLyricsDescriptor
{
    private static final DebugMessager console = DebugMessager.getInstance();
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
        words.add(word);
        buffer.add(word);
    }

    public void newline()
    {
        raw_lyrics.add(new ArrayList<>(buffer));
        buffer.clear();
    }

    public void flushBuffer()
    {
        if (raw_lyrics.size() > 0)
        {
            raw_lyrics.add(new ArrayList<>(buffer));
            buffer.clear();
        }
    }

    public String getWordAtIndex(int row, int word_number)
    {
        try
        {
            return this.raw_lyrics.get(row - 1).get(word_number - 1);
        } catch (Exception e)
        {
            return "NO SUCH INDEX";
        }
    }

    public Set<String> getWords()
    {
        return this.words;
    }


    public String toString()
    {
        StringBuilder ret = new StringBuilder();
        ret.append("UNIQUE WORDS : \n");
        for (String s : this.words)
        {
            ret.append(String.format("%s\n", s));
        }

        return ret.toString();
    }

    public void debugList()
    {
        for (int i = 0; i < this.raw_lyrics.size(); ++i)
        {
            console.info(
                    String.format("%d : %s",
                            i + 1,
                            this.raw_lyrics.get(i).toString()
                    )
            );
        }
    }

}
