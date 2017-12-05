package tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;


public class Algorithm
{
    private static final DebugMessager console = DebugMessager.getInstance();


    public static <T> Map<T, Integer> counter(Stream<T> stream)
    {
        Map<T, Integer> ret_value = new HashMap<>();

        stream . forEach(x -> {
            Integer t = ret_value . get(x);
            if (t == null)
            {
                ret_value . put(x, 1);
            }
            else
            {
                ret_value.put(x, t + 1);
            }
        });

        return ret_value;
    }

    public static <T> Map<T, Integer> counter(List<T> list)
    {
        return counter(list . stream());
    }

    public static <T, F> Map<F, Integer> counter(List<T> list, Function<T, F> transform)
    {
        return counter(list . stream() . map(transform));
    }

    public static <T, F, K> Map<F, List<K>> groupBy(List<T> input, Function<T, F> value, Function<T, K> key)
    {
        Map<F, List<K>> ret_value = new HashMap<>();

        input . forEach(
                elem -> {
                    F elem_val = value.apply(elem);
                    if (ret_value . containsKey(elem_val))
                    {
                        ret_value . get(elem_val) . add(key.apply(elem));
                    }
                    else
                    {
                        ret_value . put(elem_val,
                                new ArrayList<>(Collections.singleton(key.apply(elem))));
                    }
                }
        );

        return ret_value;
    }

    public static <T> int linearSearch(T[] arr, T target)
    {
        for (int i = 0 ; i < arr.length; ++i)
        {
            if (arr[i].equals(target))
            {
                return i;
            }
        }
        return -1;
    }

    public static <K, V> V searchInMap(Map<K, V> map, K key, Function<K, K> transform, V def)
    {
        for (K k : map.keySet())
        {
            if (transform.apply(k).equals(transform.apply(key)))
            {
                return map.get(k);
            }
        }
        return def;
    }

    private static int _levenshtein(String word1, String word2)
    {
        int len1 = word1.length();
        int len2 = word2.length();

        // len1+1, len2+1, because finally return dp[len1][len2]
        int[][] dp = new int[len1 + 1][len2 + 1];

        for (int i = 0; i <= len1; i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= len2; j++) {
            dp[0][j] = j;
        }

        //iterate though, and check last char
        for (int i = 0; i < len1; i++) {
            char c1 = word1.charAt(i);
            for (int j = 0; j < len2; j++) {
                char c2 = word2.charAt(j);

                //if last two chars equal
                if (c1 == c2) {
                    //update dp value for +1 length
                    dp[i + 1][j + 1] = dp[i][j];
                } else {
                    int replace = dp[i][j] + 1;
                    int insert = dp[i][j + 1] + 1;
                    int delete = dp[i + 1][j] + 1;

                    int min = replace > insert ? insert : replace;
                    min = delete > min ? min : delete;
                    dp[i + 1][j + 1] = min;
                }
            }
        }

        return dp[len1][len2];
    }


    public static boolean shouldAccept(String target, String actual)
    {
        int distance = _levenshtein(target, actual);

        double target_length = (double) target.length();

        return distance / target_length < 0.3;
    }
}
