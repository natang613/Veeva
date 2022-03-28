package com.company;

import com.company.MapToFiles;
import com.company.model.Row;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class MapToFilesTest {


    @Test
    void quickSort() {
        MapToFiles mapToFiles = new MapToFiles(0, "", 1, "tempDirectory");
        List<Row> array = new ArrayList<>();
        array.add(new Row(new String[]{"a"}));
        array.add(new Row(new String[]{"c"}));
        array.add(new Row(new String[]{"y"}));
        array.add(new Row(new String[]{"w"}));
        array.add(new Row(new String[]{"b"}));
        array.add(new Row(new String[]{"h"}));
        array.add(new Row(new String[]{"g"}));
        array.add(new Row(new String[]{"bb"}));
        mapToFiles.quickSort(array, 0, array.size() - 1);
        Assert.assertEquals("a", array.get(0).getWords()[0]);
        Assert.assertEquals("b", array.get(1).getWords()[0]);
        Assert.assertEquals("bb", array.get(2).getWords()[0]);
        Assert.assertEquals("c", array.get(3).getWords()[0]);
        Assert.assertEquals("g", array.get(4).getWords()[0]);
        Assert.assertEquals("h", array.get(5).getWords()[0]);
        Assert.assertEquals("w", array.get(6).getWords()[0]);
        Assert.assertEquals("y", array.get(7).getWords()[0]);

        array.clear();
        array.add(new Row(new String[]{"b", "t"}));
        array.add(new Row(new String[]{"c", "l"}));
        array.add(new Row(new String[]{"y", "pp"}));
        array.add(new Row(new String[]{"w", "kk"}));
        array.add(new Row(new String[]{"a", "bb"}));
        array.add(new Row(new String[]{"h", "ml"}));
        array.add(new Row(new String[]{"g", "mm"}));
        array.add(new Row(new String[]{"bb", "nmm"}));
        mapToFiles.quickSort(array, 0, array.size() - 1);
        Assert.assertEquals("bb", array.get(0).getWords()[1]);
        Assert.assertEquals("t", array.get(1).getWords()[1]);
        Assert.assertEquals("nmm", array.get(2).getWords()[1]);
        Assert.assertEquals("l", array.get(3).getWords()[1]);
        Assert.assertEquals("mm", array.get(4).getWords()[1]);
        Assert.assertEquals("h", array.get(5).getWords()[0]);
        Assert.assertEquals("w", array.get(6).getWords()[0]);
        Assert.assertEquals("y", array.get(7).getWords()[0]);
    }

    @Test
    void getRandomNumberUsingInts() {
        int[] numbers = {45, 66, 77};
        for (int num : numbers) {
            int radndom = MapToFiles.getRandomNumberUsingInts(10, num);
            Assert.assertTrue(radndom >= 10 && radndom < num);
        }
    }

    @Test
    void swap() {
        List<Row> array = new ArrayList<>();
        array.add(new Row(new String[]{"b"}));
        array.add(new Row(new String[]{"c"}));
        array.add(new Row(new String[]{"y"}));
        array.add(new Row(new String[]{"w"}));
        array.add(new Row(new String[]{"a"}));
        array.add(new Row(new String[]{"h"}));
        array.add(new Row(new String[]{"g"}));
        array.add(new Row(new String[]{"bb"}));
        MapToFiles.quickSortSwap(array, 0, 4);
        MapToFiles.quickSortSwap(array, 1, 5);
        Assert.assertEquals("a", array.get(0).getWords()[0]);
        Assert.assertEquals("b", array.get(4).getWords()[0]);
        Assert.assertEquals("h", array.get(1).getWords()[0]);
        Assert.assertEquals("c", array.get(5).getWords()[0]);

    }
}