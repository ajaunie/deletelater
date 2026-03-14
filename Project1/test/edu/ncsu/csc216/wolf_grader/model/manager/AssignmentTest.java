package edu.ncsu.csc216.wolf_grader.model.manager;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import edu.ncsu.csc216.wolf_grader.model.submission.Submission;

/**
 * Provides tests for how the Assignment class manages its list of student submissions
 * and maintains the ID sorting.
 * @author Ajaunie White
 */
public class AssignmentTest {

    /**
     * Checks adding, deleting, and finding submissions within an assignment.
     */
    @Test
    public void testAssignmentManagement() {
        Assignment a = new Assignment("Project 1", "Project", 100);
        assertEquals("Project 1", a.getAssignmentName());
        
        // Add submissions and check sorting
        a.addSubmission("Bob", "b1"); // ID 0
        a.addSubmission("Alice", "a1"); // ID 1
        
        assertEquals(2, a.getSubmissions().size());
        assertEquals("Bob", a.getSubmissionById(0).getName());
        
        // Add existing submission manually with higher ID
        Submission s3 = new Submission(10, "Upload", "Charlie", "c1", false, "Not Done", false, "", "");
        a.addSubmission(s3);
        assertEquals(s3, a.getSubmissionById(10));
        
        // Check for duplicate ID failure
        assertThrows(IllegalArgumentException.class, () -> a.addSubmission(s3));

        // Delete and verify
        a.deleteSubmissionById(0);
        assertNull(a.getSubmissionById(0));
        assertEquals(2, a.getSubmissions().size());
    }
}