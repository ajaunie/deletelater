package edu.ncsu.csc216.wolf_regrade.model.util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Tests the SortedList implementation.
 */
public class SortedListTest {

    @Test
    public void testSortingAndDuplicates() {
        SortedList<String> labels = new SortedList<>();
        
        labels.add("TDE");
        labels.add("Dreamville");
        labels.add("OVO");
        
        // Should be Dreamville, OVO, TDE
        assertEquals("Dreamville", labels.get(0));
        assertEquals("OVO", labels.get(1));
        assertEquals("TDE", labels.get(2));
        
        assertTrue(labels.contains("OVO"));
        assertFalse(labels.contains("Cactus Jack"));
        
        assertThrows(IllegalArgumentException.class, () -> labels.add("TDE"));
        assertEquals(3, labels.size());
    }

    @Test
    public void testRemovals() {
        SortedList<Integer> years = new SortedList<>();
        years.add(2014);
        years.add(2024);
        years.add(2010);
        
        assertEquals(2010, years.remove(0));
        assertEquals(2, years.size());
        assertEquals(2014, years.get(0));
    }
}