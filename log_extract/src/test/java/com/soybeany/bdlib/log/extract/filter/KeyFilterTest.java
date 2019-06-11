package com.soybeany.bdlib.log.extract.filter;

import com.soybeany.bdlib.log.extract.model.IItem;
import com.soybeany.bdlib.log.extract.parser.IParser;
import com.soybeany.bdlib.log.extract.std.model.StdRow;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <br>Created by Soybeany on 2019/6/2.
 */
public class KeyFilterTest {
    private String key1 = "abc";

    private List<String> source1 = Arrays.asList(key1, "def", "ggg", "sdf" + key1);
    private List<String> source2 = Arrays.asList("What Is Abc", "Those Are aBc", "wHat iS thaT", "ok");

    private IParser<Item, List<String>> mProvider = x -> x.list;

    static class Item implements IItem<StdRow> {
        String input;

        List<String> list;

        Item(String input) {
            this.input = input;
        }

        Item(List<String> list) {
            this.list = list;
        }

        @Override
        public boolean cover(IItem iItem) {
            return false;
        }

        @Override
        public List<StdRow> getLogs() {
            return null;
        }
    }

    @Test
    public void defaultInclude() {
        KeyFilter<StdRow, Item, String> filter = new KeyFilter.Include<>(getNewContainer("abc"), x -> x.input);
        Assert.assertFalse(filter.shouldIntercept(new Item("there is something with Abc")));
    }

    @Test
    public void defaultExclude() {
        KeyFilter<StdRow, Item, String> filter = new KeyFilter.Exclude<>(getNewContainer("abc"), x -> x.input);
        Assert.assertTrue(filter.shouldIntercept(new Item("there is something with Abc")));
    }

    @Test
    public void isActive() {
        // null
        KeyFilter<StdRow, Item, List<String>> filter = new KeyFilter.ListInclude<>(getNewContainer(null), mProvider);
        Assert.assertFalse(filter.isActive());
        // 空内容
        KeyFilter<StdRow, Item, List<String>> filter2 = new KeyFilter.ListInclude<>(getNewContainer("  "), mProvider);
        Assert.assertFalse(filter2.isActive());
        // 正常
        KeyFilter<StdRow, Item, List<String>> filter3 = new KeyFilter.ListInclude<>(getNewContainer(key1), mProvider);
        Assert.assertTrue(filter3.isActive());
    }

    @Test
    public void interceptWhenItemIsNull() {
        KeyFilter<StdRow, Item, List<String>> filter = new KeyFilter.ListInclude<>(getNewContainer(key1), mProvider);
        // 不开启
        Assert.assertFalse(filter.shouldIntercept(null));
        // 开启
        filter.interceptWhenItemIsNull(true);
        Assert.assertTrue(filter.shouldIntercept(null));
    }

    @Test
    public void singleIncludeAll() {
        KeyFilter<StdRow, Item, List<String>> filter = new KeyFilter.ListInclude<>(getNewContainer(key1), mProvider);
        List<String> testList = new ArrayList<>(source1);
        boolean shouldIntercept = filter.shouldIntercept(new Item(testList));
        Assert.assertFalse(shouldIntercept);
        Assert.assertEquals(source1.toString(), testList.toString());
    }

    @Test
    public void singleIncludeKeep() {
        KeyFilter<StdRow, Item, List<String>> filter = new KeyFilter.ListInclude<>(getNewContainer(key1), mProvider).onlyKeepInclude(true);
        List<String> testList = new ArrayList<>(source1);
        filter.shouldIntercept(new Item(testList));
        Assert.assertEquals("[abc, sdfabc]", testList.toString());
    }

    @Test
    public void singleExcludeAll() {
        KeyFilter<StdRow, Item, List<String>> filter = new KeyFilter.ListExclude<>(getNewContainer(key1), mProvider);
        List<String> testList = new ArrayList<>(source1);
        boolean shouldIntercept = filter.shouldIntercept(new Item(testList));
        Assert.assertTrue(shouldIntercept);
    }

    @Test
    public void singleExcludeRemove() {
        KeyFilter<StdRow, Item, List<String>> filter = new KeyFilter.ListExclude<>(getNewContainer(key1), mProvider).onlyRemoveExclude(true);
        List<String> testList = new ArrayList<>(source1);
        boolean shouldIntercept = filter.shouldIntercept(new Item(testList));
        Assert.assertEquals("[def, ggg]", testList.toString());
    }

    @Test
    public void multiIncludeInclude() {
        KeyFilter<StdRow, Item, List<String>> filter1 = new KeyFilter.ListInclude<>(getNewContainer("what"), mProvider).onlyKeepInclude(true);
        KeyFilter<StdRow, Item, List<String>> filter2 = new KeyFilter.ListInclude<>(getNewContainer("abc"), mProvider)
                .onlyKeepInclude(true);
        List<String> testList = new ArrayList<>(source2);
        filter1.shouldIntercept(new Item(testList));
        filter2.shouldIntercept(new Item(testList));
        Assert.assertEquals("[What Is Abc]", testList.toString());
    }

    @Test
    public void multiExcludeExclude() {
        KeyFilter<StdRow, Item, List<String>> filter1 = new KeyFilter.ListExclude<>(getNewContainer("is"), mProvider).onlyRemoveExclude(true);
        KeyFilter<StdRow, Item, List<String>> filter2 = new KeyFilter.ListExclude<>(getNewContainer("abc"), mProvider).onlyRemoveExclude(true);
        List<String> testList = new ArrayList<>(source2);
        filter1.shouldIntercept(new Item(testList));
        filter2.shouldIntercept(new Item(testList));
        Assert.assertEquals("[ok]", testList.toString());
    }

    @Test
    public void multiIncludeExclude() {
        KeyFilter<StdRow, Item, List<String>> filter1 = new KeyFilter.ListInclude<>(getNewContainer("is"), mProvider).onlyKeepInclude(true);
        KeyFilter<StdRow, Item, List<String>> filter2 = new KeyFilter.ListExclude<>(getNewContainer("abc"), mProvider).onlyRemoveExclude(true);
        List<String> testList = new ArrayList<>(source2);
        filter1.shouldIntercept(new Item(testList));
        filter2.shouldIntercept(new Item(testList));
        Assert.assertEquals("[wHat iS thaT]", testList.toString());
    }

    @Test
    public void multiExcludeInclude() {
        KeyFilter<StdRow, Item, List<String>> filter1 = new KeyFilter.ListExclude<>(getNewContainer("is"), mProvider).onlyRemoveExclude(true);
        KeyFilter<StdRow, Item, List<String>> filter2 = new KeyFilter.ListInclude<>(getNewContainer("abc"), mProvider).onlyKeepInclude(true);
        List<String> testList = new ArrayList<>(source2);
        filter1.shouldIntercept(new Item(testList));
        filter2.shouldIntercept(new Item(testList));
        Assert.assertEquals("[Those Are aBc]", testList.toString());
    }

    private IKeyMatcher getNewContainer(String key) {
        return new SimpleKeyMatcher(key);
//        return new RegexKeyMatcher(key).patternFlag(Pattern.CASE_INSENSITIVE);
    }
}