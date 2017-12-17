package toolstest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;

import static org.junit.Assert.*;

import tools.Algorithm;


public class AlgorithmTest
{
    @org.junit.Test
    public void testCounter()
    {
        String[] array = "a a a a a a b b b b b b b c c c c".split(" ");
        List<String> lst = Arrays.asList(array);

        Map<String, Integer> counter = Algorithm.Collections.counter(lst);

        assertEquals(counter.size(), 3);
        assertEquals((int) counter.get("a"), 6);
        assertEquals((int) counter.get("b"), 7);
        assertEquals((int) counter.get("c"), 4);
    }

    @org.junit.Test
    public void testCounterWithPredicate()
    {
        String[] array = "a a a a a a b b b b b b b c c c c".split(" ");
        List<String> lst = Arrays.asList(array);

        Map<String, Integer> counter = Algorithm.Collections.counter(
                lst,
                x -> x + "1"
        );

        assertEquals(counter.size(), 3);
        assertEquals((int) counter.get("a1"), 6);
        assertEquals((int) counter.get("b1"), 7);
        assertEquals((int) counter.get("c1"), 4);
    }

    @org.junit.Test
    public void testGroupBy()
    {
        List<String> input = Arrays.asList("a", "bb", "ccc");

        Map<Integer, List<String>> grouped = Algorithm.Collections.groupBy(
                input,
                String::length,
                x -> x
        );

        assertEquals(grouped.size(), 3);
        assertEquals(grouped.get(1), new ArrayList<>(Collections.singleton("a")));
        assertEquals(grouped.get(2), new ArrayList<>(Collections.singleton("bb")));
        assertEquals(grouped.get(3), new ArrayList<>(Collections.singleton("ccc")));
    }

    @org.junit.Test
    public void testLinearSearch()
    {
        String[] arr = new String[]{"a", "b", "c"};
        assertEquals(Algorithm.Collections.linearSearch(arr, "a"), 0);
        assertEquals(Algorithm.Collections.linearSearch(arr, "b"), 1);
        assertEquals(Algorithm.Collections.linearSearch(arr, "c"), 2);
        assertEquals(Algorithm.Collections.linearSearch(arr, "d"), -1);
    }

    @org.junit.Test
    public void searchInMap()
    {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("a", "a");
        hashMap.put("b", "b");

        assertEquals(
                Algorithm.Collections.searchInMap(hashMap, "a", x -> x + "a", "c"),
                "a"
        );

        assertEquals(
                Algorithm.Collections.searchInMap(hashMap, "", x -> x + "c", "d"),
                "d"
        );
    }

    @org.junit.Test
    public void testLevenshteinDist()
    {
        assertTrue(Algorithm.StringUtils.shouldAccept("a", "a"));
        assertFalse(Algorithm.StringUtils.shouldAccept("ab", "a"));
        assertFalse(Algorithm.StringUtils.shouldAccept("abc", "a"));
        assertFalse(Algorithm.StringUtils.shouldAccept("abcd", "a"));
        assertFalse(Algorithm.StringUtils.shouldAccept("abcde", "a"));
    }

    @org.junit.Test
    public void testJoin()
    {
        List<String> test_list = Arrays.asList("awesome", "is", "Songle");
        assertEquals(
                Algorithm.StringUtils.join(test_list, " "),
                "awesome   is   Songle "
        );
    }
}
