package edu.ncsu.csc216.wolf_regrade.model.util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.NoSuchElementException;
import edu.ncsu.csc216.wolf_regrade.model.regrade.ReviewRequest;

/**
 * Tests the stack used for completed regrades.
 */
public class CompletedRegradesStackTest {

    @Test
    public void testStackOperations() {
        CompletedRegradesStack recordStack = new CompletedRegradesStack();
        assertTrue(recordStack.isEmpty());
        
        ReviewRequest r1 = new ReviewRequest("Wayne", "carter5", "Birdman", "Bars", "Greatness");
        ReviewRequest r2 = new ReviewRequest("Savage", "21_21", "Metro", "Beat", "Hard");
        
        recordStack.push(r1);
        recordStack.push(r2);
        
        assertEquals(2, recordStack.size());
        assertSame(r2, recordStack.peek());
        
        assertSame(r2, recordStack.pop());
        assertEquals(1, recordStack.size());
        assertFalse(recordStack.isEmpty());
    }

    @Test
    public void testExpansionAndIterator() {
        CompletedRegradesStack many = new CompletedRegradesStack();
        // Force array expansion beyond 10
        for (int i = 0; i < 15; i++) {
            many.push(new ReviewRequest("Rapper" + i, "id" + i, "g", "item", "rat"));
        }
        assertEquals(15, many.size());
        
        Iterator<edu.ncsu.csc216.wolf_regrade.model.regrade.RegradeRequest> it = many.iterator();
        int count = 0;
        while (it.hasNext()) {
            assertNotNull(it.next());
            count++;
        }
        assertEquals(15, count);
    }

    @Test
    public void testStackExceptions() {
        CompletedRegradesStack empty = new CompletedRegradesStack();
        assertThrows(NoSuchElementException.class, () -> empty.pop());
        assertThrows(NoSuchElementException.class, () -> empty.peek());
        assertThrows(NullPointerException.class, () -> empty.push(null));
    }
}