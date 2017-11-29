package tools;

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
}
