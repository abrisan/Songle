package datastructures;


import android.provider.Settings;

import java.util.ArrayList;
import java.util.List;

import globals.GlobalConstants;

public class WordlistCardInformation
{
    private String category_name;
    private List<String> found_words_in_category;

    public WordlistCardInformation()
    {
        this . category_name = GlobalConstants.defaultCategory;
        this . found_words_in_category = new ArrayList<>();
    }

    public WordlistCardInformation(String category_name)
    {
        this . category_name = category_name;
    }

    public WordlistCardInformation(String category_name, List<String> found_words_in_category)
    {
        this . category_name = category_name;
        this . found_words_in_category = found_words_in_category;
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
        return ret . toString();
    }

    public String get_full_words()
    {
        return this . toString();
    }
}
