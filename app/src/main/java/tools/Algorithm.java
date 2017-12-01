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

    private static int _levenshtein(String target, int t_i, String actual, int a_i)
    {
        if (Math.min(t_i, a_i) == 0)
        {
            return Math.max(t_i, a_i);
        }

        int cost = target.charAt(t_i) == actual.charAt(a_i-1) ? 0 : 1;

        return Math.min(
                Math.min(
                        _levenshtein(
                                target,
                                t_i - 1,
                                actual,
                                a_i
                        ) + 1,
                        _levenshtein(
                                target,
                                t_i,
                                actual,
                                a_i - 1
                        ) + 1
                ),
                _levenshtein(
                        target,
                        t_i - 1,
                        actual,
                        a_i - 1
                ) + cost
        );
    }


    public static boolean shouldAccept(String target, String actual)
    {
        int distance = _levenshtein(target, target.length(), actual, actual.length());

        double target_length = (double) target.length();

        return distance / target_length < 0.3;
    }
}
