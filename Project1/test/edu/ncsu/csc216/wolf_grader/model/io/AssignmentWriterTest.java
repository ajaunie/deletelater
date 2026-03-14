package edu.ncsu.csc216.wolf_grader.model.io;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import edu.ncsu.csc216.wolf_grader.model.manager.Assignment;

/**
 * Provides tests for the system's ability to save assignment data back to a file.
 * @author Ajaunie White
 */
public class AssignmentWriterTest {

    /**
     * Checks if the output file is created successfully.
     */
    @Test
    public void testWriteFile() {
        ArrayList<Assignment> assignments = new ArrayList<Assignment>();
        assignments.add(new Assignment("Final", "Project", 200));
        
        try {
            AssignmentWriter.writeAssignmentsToFile("test_save.txt", assignments);
        } catch (Exception e) {
            fail("Should not throw an exception.");
        }
    }
}