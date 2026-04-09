package edu.ncsu.csc216.wolf_regrade.model.io;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Tests for the RegradeWriter class.
 */
public class RegradeWriterTest {
    @Test
    public void testWriteFileSkeleton() {
        // Basic check for the writer class
        assertNotNull(new RegradeWriter());
    }
}