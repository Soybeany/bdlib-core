package com.soybeany.bdlib.project.log_extract;

import com.soybeany.bdlib.log.extract.filter.IKeyContainer;
import com.soybeany.bdlib.log.extract.filter.LogFilter;
import com.soybeany.bdlib.log.extract.filter.SimpleKeyContainer;
import com.soybeany.bdlib.log.extract.model.IDataProvider;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <br>Created by Soybeany on 2019/6/1.
 */
public class LogFilterTest {
    private String key1 = "abc";

    private List<String> source1 = Arrays.asList(key1, "def", "ggg", "sdf" + key1);
    private List<String> source2 = Arrays.asList("What Is Abc", "Those Are aBc", "wHat iS thaT", "ok");

    private IDataProvider<List<String>, List<String>> mProvider = x -> x;

    @Test
    public void isActive() {
        // null
        LogFilter.Include<List<String>> filter = new LogFilter.Include<>(getNewContainer(null), mProvider);
        Assert.assertFalse(filter.isActive());
        // 空内容
        LogFilter.Include<List<String>> filter2 = new LogFilter.Include<>(getNewContainer("  "), mProvider);
        Assert.assertFalse(filter2.isActive());
        // 正常
        LogFilter.Include<List<String>> filter3 = new LogFilter.Include<>(getNewContainer(key1), mProvider);
        Assert.assertTrue(filter3.isActive());
    }

    @Test
    public void interceptWhenItemIsNull() {
        LogFilter<List<String>> filter = new LogFilter.Include<>(getNewContainer(key1), mProvider);
        // 不开启
        Assert.assertFalse(filter.shouldIntercept(null));
        // 开启
        filter.interceptWhenItemIsNull(true);
        Assert.assertTrue(filter.shouldIntercept(null));
    }

    @Test
    public void singleIncludeAll() {
        LogFilter<List<String>> filter = new LogFilter.Include<>(getNewContainer(key1), mProvider);
        List<String> testList = new ArrayList<>(source1);
        boolean shouldIntercept = filter.shouldIntercept(testList);
        Assert.assertFalse(shouldIntercept);
        Assert.assertEquals(source1.toString(), testList.toString());
    }

    @Test
    public void singleIncludeKeep() {
        LogFilter<List<String>> filter = new LogFilter.Include<>(getNewContainer(key1), mProvider).onlyKeepInclude(true);
        List<String> testList = new ArrayList<>(source1);
        filter.shouldIntercept(testList);
        Assert.assertEquals("[abc, sdfabc]", testList.toString());
    }

    @Test
    public void singleExcludeAll() {
        LogFilter<List<String>> filter = new LogFilter.Exclude<>(getNewContainer(key1), mProvider);
        List<String> testList = new ArrayList<>(source1);
        boolean shouldIntercept = filter.shouldIntercept(testList);
        Assert.assertTrue(shouldIntercept);
    }

    @Test
    public void singleExcludeRemove() {
        LogFilter<List<String>> filter = new LogFilter.Exclude<>(getNewContainer(key1), mProvider).onlyRemoveExclude(true);
        List<String> testList = new ArrayList<>(source1);
        boolean shouldIntercept = filter.shouldIntercept(testList);
        Assert.assertEquals("[def, ggg]", testList.toString());
    }

    @Test
    public void multiIncludeInclude() {
        LogFilter<List<String>> filter1 = new LogFilter.Include<>(getNewContainer("what"), mProvider).onlyKeepInclude(true);
        LogFilter<List<String>> filter2 = new LogFilter.Include<>(getNewContainer("abc"), mProvider)
                .onlyKeepInclude(true);
        List<String> testList = new ArrayList<>(source2);
        filter1.shouldIntercept(testList);
        filter2.shouldIntercept(testList);
        Assert.assertEquals("[What Is Abc]", testList.toString());
    }

    @Test
    public void multiExcludeExclude() {
        LogFilter<List<String>> filter1 = new LogFilter.Exclude<>(getNewContainer("is"), mProvider).onlyRemoveExclude(true);
        LogFilter<List<String>> filter2 = new LogFilter.Exclude<>(getNewContainer("abc"), mProvider).onlyRemoveExclude(true);
        List<String> testList = new ArrayList<>(source2);
        filter1.shouldIntercept(testList);
        filter2.shouldIntercept(testList);
        Assert.assertEquals("[ok]", testList.toString());
    }

    @Test
    public void multiIncludeExclude() {
        LogFilter<List<String>> filter1 = new LogFilter.Include<>(getNewContainer("is"), mProvider).onlyKeepInclude(true);
        LogFilter<List<String>> filter2 = new LogFilter.Exclude<>(getNewContainer("abc"), mProvider).onlyRemoveExclude(true);
        List<String> testList = new ArrayList<>(source2);
        filter1.shouldIntercept(testList);
        filter2.shouldIntercept(testList);
        Assert.assertEquals("[wHat iS thaT]", testList.toString());
    }

    @Test
    public void multiExcludeInclude() {
        LogFilter<List<String>> filter1 = new LogFilter.Exclude<>(getNewContainer("is"), mProvider).onlyRemoveExclude(true);
        LogFilter<List<String>> filter2 = new LogFilter.Include<>(getNewContainer("abc"), mProvider).onlyKeepInclude(true);
        List<String> testList = new ArrayList<>(source2);
        filter1.shouldIntercept(testList);
        filter2.shouldIntercept(testList);
        Assert.assertEquals("[Those Are aBc]", testList.toString());
    }

    private IKeyContainer<String> getNewContainer(String key) {
        return new SimpleKeyContainer(key);
//        return new RegexKeyContainer(key).patternFlag(Pattern.CASE_INSENSITIVE);
    }
}
