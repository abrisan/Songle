package datastructures;


import android.provider.Settings;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import globals.GlobalConstants;
import tools.PrettyPrinter;

public class GuessedWords implements PrettyPrinter
{
    private String category_name;
    private List<String> found_words_in_category;

    public GuessedWords()
    {
        this . category_name = GlobalConstants.defaultCategory;
        this . found_words_in_category = new ArrayList<>();
    }

    public GuessedWords(String category_name)
    {
        this . category_name = category_name;
    }

    public GuessedWords(String category_name, List<String> found_words_in_category)
    {
        this . category_name = category_name;
        Set<String> unique_words = new HashSet<>();
        unique_words . addAll(found_words_in_category);
        this . found_words_in_category = new ArrayList<>(unique_words);
    }

    public String getCategory_name()
    {
        return this . category_name;
    }

    public List<String> getFound_words_in_category()
    {
        return this . found_words_in_category;
    }

    public boolean setCategoryName(String category_name)
    {
        if (!this . category_name . equals(GlobalConstants.defaultCategory))
        {
            return false;
        }

        this . category_name = category_name;
        return true;
    }

    public boolean addGuessedWord(String word)
    {
        return this . found_words_in_category . contains(word) ||
                this . found_words_in_category . add(word);
    }


    @Override
    public String toString()
    {
        StringBuilder ret = new StringBuilder();
        for (String s : this . found_words_in_category)
        {
            ret . append(s);
            ret . append('\n');
        }
        return ret . toString();
    }

    public String get_found_words_preview()
    {
        int number = Math.min(
                4,
                this . found_words_in_category . size()
        );

        StringBuilder ret = new StringBuilder();

        for (int i = 0 ; i < number ; ++i)
        {
            ret . append(this . found_words_in_category . get(i));
            ret . append('\n');
        }
        ret . append("...");
        return ret . toString();
    }

    public String get_full_words()
    {
        return this . toString();
    }

    public String serialise()
            throws JSONException
    {
        JSONObject obj = new JSONObject();

        obj.put("category", this.category_name);
        obj.put("words", this.found_words_in_category);

        return obj.toString(2);
    }
}
