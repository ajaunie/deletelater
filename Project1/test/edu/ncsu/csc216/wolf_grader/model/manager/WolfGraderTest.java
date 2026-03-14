package edu.ncsu.csc216.wolf_grader.model.manager;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Provides tests for the WolfGrader singleton manager and global application state.
 * @author Ajaunie White
 */
public class WolfGraderTest {

    /**
     * Resets the WolfGrader manager before each test to ensure isolation.
     */
    @BeforeEach
    public void setUp() {
        WolfGrader.getInstance().resetManager();
    }

    /**
     * Checks that the manager correctly creates assignments and filters data for the UI.
     */
    @Test
    public void testManagerFunctionality() {
        WolfGrader wg = WolfGrader.getInstance();
        assertNull(wg.getActiveAssignmentName());

        wg.addNewAssignment("Lab 1", "Lab", 10);
        assertEquals("Lab 1", wg.getActiveAssignmentName());
        
        wg.addSubmissionToAssignment("Devin", "d1");
        String[][] arrayData = wg.getSubmissionsAsArray("All");
        assertEquals(1, arrayData.length);
        assertTrue(arrayData[0][2].contains("Devin"));

        // Error checking for duplicates
        assertThrows(IllegalArgumentException.class, () -> wg.addNewAssignment("Lab 1", "Lab", 10));
    }
}