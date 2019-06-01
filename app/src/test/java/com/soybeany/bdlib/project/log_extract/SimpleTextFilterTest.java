package com.soybeany.bdlib.project.log_extract;

import com.soybeany.bdlib.log.extract.filter.SimpleTextFilter;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <br>Created by Soybeany on 2019/6/1.
 */
public class SimpleTextFilterTest {
    private String key1 = "abc";

    private List<String> source1 = Arrays.asList(key1, "def", "ggg", "sdf" + key1);
    private List<String> source2 = Arrays.asList("what is abc", "those are abc", "what is that", "ok");

    @Test
    public void isActive() {
        // null
        SimpleTextFilter.Include<List<String>> filter = new SimpleTextFilter.Include<>(null, x -> x);
        Assert.assertFalse(filter.isActive());
        // 空内容
        SimpleTextFilter.Include<List<String>> filter2 = new SimpleTextFilter.Include<>("  ", x -> x);
        Assert.assertFalse(filter2.isActive());
        // 正常
        SimpleTextFilter.Include<List<String>> filter3 = new SimpleTextFilter.Include<>(key1, x -> x);
        Assert.assertTrue(filter3.isActive());
    }

    @Test
    public void interceptWhenItemIsNull() {
        SimpleTextFilter<List<String>> filter = new SimpleTextFilter.Include<>(key1, x -> x);
        // 不开启
        Assert.assertFalse(filter.shouldIntercept(null));
        // 开启
        filter.interceptWhenItemIsNull(true);
        Assert.assertTrue(filter.shouldIntercept(null));
    }

    @Test
    public void singleIncludeAll() {
        SimpleTextFilter<List<String>> filter = new SimpleTextFilter.Include<>(key1, x -> x);
        List<String> testList = new ArrayList<>(source1);
        boolean shouldIntercept = filter.shouldIntercept(testList);
        Assert.assertFalse(shouldIntercept);
        Assert.assertEquals(source1.toString(), testList.toString());
    }

    @Test
    public void singleIncludeKeep() {
        SimpleTextFilter<List<String>> filter = new SimpleTextFilter.Include<List<String>>(key1, x -> x).onlyKeepInclude(true);
        List<String> testList = new ArrayList<>(source1);
        filter.shouldIntercept(testList);
        Assert.assertEquals("[abc, sdfabc]", testList.toString());
    }

    @Test
    public void singleExcludeAll() {
        SimpleTextFilter<List<String>> filter = new SimpleTextFilter.Exclude<>(key1, x -> x);
        List<String> testList = new ArrayList<>(source1);
        boolean shouldIntercept = filter.shouldIntercept(testList);
        Assert.assertTrue(shouldIntercept);
    }

    @Test
    public void singleExcludeRemove() {
        SimpleTextFilter<List<String>> filter = new SimpleTextFilter.Exclude<List<String>>(key1, x -> x).onlyRemoveExclude(true);
        List<String> testList = new ArrayList<>(source1);
        boolean shouldIntercept = filter.shouldIntercept(testList);
        Assert.assertEquals("[def, ggg]", testList.toString());
    }

    @Test
    public void multiIncludeInclude() {
        SimpleTextFilter<List<String>> filter1 = new SimpleTextFilter.Include<List<String>>("what", x -> x).onlyKeepInclude(true);
        SimpleTextFilter<List<String>> filter2 = new SimpleTextFilter.Include<List<String>>("abc", x -> x).onlyKeepInclude(true);
        List<String> testList = new ArrayList<>(source2);
        filter1.shouldIntercept(testList);
        filter2.shouldIntercept(testList);
        Assert.assertEquals("[what is abc]", testList.toString());
    }

    @Test
    public void multiExcludeExclude() {
        SimpleTextFilter<List<String>> filter1 = new SimpleTextFilter.Exclude<List<String>>("is", x -> x).onlyRemoveExclude(true);
        SimpleTextFilter<List<String>> filter2 = new SimpleTextFilter.Exclude<List<String>>("abc", x -> x).onlyRemoveExclude(true);
        List<String> testList = new ArrayList<>(source2);
        filter1.shouldIntercept(testList);
        filter2.shouldIntercept(testList);
        Assert.assertEquals("[ok]", testList.toString());
    }

    @Test
    public void multiIncludeExclude() {
        SimpleTextFilter<List<String>> filter1 = new SimpleTextFilter.Include<List<String>>("is", x -> x).onlyKeepInclude(true);
        SimpleTextFilter<List<String>> filter2 = new SimpleTextFilter.Exclude<List<String>>("abc", x -> x).onlyRemoveExclude(true);
        List<String> testList = new ArrayList<>(source2);
        filter1.shouldIntercept(testList);
        filter2.shouldIntercept(testList);
        Assert.assertEquals("[what is that]", testList.toString());
    }

    @Test
    public void multiExcludeInclude() {
        SimpleTextFilter<List<String>> filter1 = new SimpleTextFilter.Exclude<List<String>>("is", x -> x).onlyRemoveExclude(true);
        SimpleTextFilter<List<String>> filter2 = new SimpleTextFilter.Include<List<String>>("abc", x -> x).onlyKeepInclude(true);
        List<String> testList = new ArrayList<>(source2);
        filter1.shouldIntercept(testList);
        filter2.shouldIntercept(testList);
        Assert.assertEquals("[those are abc]", testList.toString());
    }
}
