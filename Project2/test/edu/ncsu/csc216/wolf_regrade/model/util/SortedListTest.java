package edu.ncsu.csc216.wolf_regrade.model.util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class SortedListTest {
    @Test
    public void testSortedList() {
        SortedList<String> list = new SortedList<>();
        assertEquals(0, list.size());
    }
}