package edu.ncsu.csc216.wolf_regrade.model.util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.NoSuchElementException;

/**
 * Tests the BackLog implementation.
 */
public class BackLogTest {

    @Test
    public void testQueueBehavior() {
        BackLog<String> rapList = new BackLog<>();
        
        // Test Add
        rapList.add("Kendrick");
        rapList.add("Cole");
        rapList.add("Drake");
        assertEquals(3, rapList.size());
        
        // Test Get and Set
        assertEquals("Kendrick", rapList.get(0));
        rapList.set(1, "J. Cole");
        assertEquals("J. Cole", rapList.get(1));
        
        // Test Remove from middle
        String removed = rapList.remove(1);
        assertEquals("J. Cole", removed);
        assertEquals(2, rapList.size());
        assertEquals("Drake", rapList.get(1));
        
        // Test Remove from front
        assertEquals("Kendrick", rapList.remove(0));
        assertEquals(1, rapList.size());
    }

    @Test
    public void testIterator() {
        BackLog<String> artists = new BackLog<>();
        artists.add("Future");
        artists.add("Thugger");
        
        Iterator<String> it = artists.iterator();
        assertTrue(it.hasNext());
        assertEquals("Future", it.next());
        assertTrue(it.hasNext());
        assertEquals("Thugger", it.next());
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, () -> it.next());
    }

    @Test
    public void testExceptions() {
        BackLog<String> list = new BackLog<>();
        assertThrows(NullPointerException.class, () -> list.add(null));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(-1));
        
        list.add("Travis");
        assertThrows(NullPointerException.class, () -> list.set(0, null));
    }
}