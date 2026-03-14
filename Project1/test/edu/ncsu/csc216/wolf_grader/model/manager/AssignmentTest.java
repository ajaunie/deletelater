package edu.ncsu.csc216.wolf_grader.model.manager;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import edu.ncsu.csc216.wolf_grader.model.command.Command;
import edu.ncsu.csc216.wolf_grader.model.command.Command.CommandValue;
import edu.ncsu.csc216.wolf_grader.model.submission.Submission;

/**
 * Provides tests for how the Assignment class manages its list of student submissions
 * and maintains the ID sorting.
 * @author Ajaunie White
 */
public class AssignmentTest {

    /**
     * Resets the submission counter before each test for predictable ids.
     */
    @BeforeEach
    public void setUp() {
        Submission.setCounter(0);
    }

    // ---------------------------------------------------------------
    // Constructor tests
    // ---------------------------------------------------------------

    /**
     * Tests creating a valid assignment with name, category, and max points.
     */
    @Test
    public void testValidConstructor() {
        Assignment a = new Assignment("Project 1", "Project", 100);
        assertEquals("Project 1", a.getAssignmentName());
        assertEquals("Project", a.getAssignmentCategory());
        assertEquals(100, a.getMaxPoints());
        assertEquals(0, a.getSubmissions().size());
    }

    /**
     * Tests constructor with minimum valid max points.
     */
    @Test
    public void testConstructorMinMaxPoints() {
        Assignment a = new Assignment("Lab", "Lab", 1);
        assertEquals(1, a.getMaxPoints());
    }

    /**
     * Tests constructor rejects null assignment name.
     */
    @Test
    public void testConstructorNullName() {
        assertThrows(IllegalArgumentException.class,
                () -> new Assignment(null, "Project", 50));
    }

    /**
     * Tests constructor rejects empty assignment name.
     */
    @Test
    public void testConstructorEmptyName() {
        assertThrows(IllegalArgumentException.class,
                () -> new Assignment("", "Project", 50));
    }

    /**
     * Tests constructor rejects null category.
     */
    @Test
    public void testConstructorNullCategory() {
        assertThrows(IllegalArgumentException.class,
                () -> new Assignment("Name", null, 50));
    }

    /**
     * Tests constructor rejects empty category.
     */
    @Test
    public void testConstructorEmptyCategory() {
        assertThrows(IllegalArgumentException.class,
                () -> new Assignment("Name", "", 50));
    }

    /**
     * Tests constructor rejects max points of 0.
     */
    @Test
    public void testConstructorZeroMaxPoints() {
        assertThrows(IllegalArgumentException.class,
                () -> new Assignment("Name", "Lab", 0));
    }

    /**
     * Tests constructor rejects max points greater than 100.
     */
    @Test
    public void testConstructorMaxPointsOver100() {
        assertThrows(IllegalArgumentException.class,
                () -> new Assignment("Name", "Lab", 101));
    }

    /**
     * Tests constructor rejects negative max points.
     */
    @Test
    public void testConstructorNegativeMaxPoints() {
        assertThrows(IllegalArgumentException.class,
                () -> new Assignment("Name", "Lab", -1));
    }

    // ---------------------------------------------------------------
    // addSubmission and sorting tests
    // ---------------------------------------------------------------

    /**
     * Tests adding submissions maintains sorted order by id.
     */
    @Test
    public void testAddSubmissionSortedOrder() {
        Assignment a = new Assignment("Project 1", "Project", 100);
        a.addSubmission("Bob", "b1");
        a.addSubmission("Alice", "a1");

        assertEquals(2, a.getSubmissions().size());
        assertEquals(0, a.getSubmissions().get(0).getId());
        assertEquals(1, a.getSubmissions().get(1).getId());
        assertEquals("Bob", a.getSubmissions().get(0).getName());
        assertEquals("Alice", a.getSubmissions().get(1).getName());
    }

    /**
     * Tests adding a Submission object with a specific id inserts in sorted order.
     */
    @Test
    public void testAddSubmissionObjectSortedOrder() {
        Assignment a = new Assignment("Project 1", "Project", 100);
        a.addSubmission("Bob", "b1");

        Submission high = new Submission(10, "Upload", "Charlie", "c1", false, "Not Done", false, "", "");
        a.addSubmission(high);

        Submission mid = new Submission(5, "Upload", "Dave", "d1", false, "Not Done", false, "", "");
        a.addSubmission(mid);

        assertEquals(3, a.getSubmissions().size());
        assertEquals(0, a.getSubmissions().get(0).getId());
        assertEquals(5, a.getSubmissions().get(1).getId());
        assertEquals(10, a.getSubmissions().get(2).getId());
    }

    /**
     * Tests adding a submission with a duplicate id throws IllegalArgumentException.
     */
    @Test
    public void testAddSubmissionDuplicateId() {
        Assignment a = new Assignment("Project 1", "Project", 100);
        Submission s = new Submission(10, "Upload", "Charlie", "c1", false, "Not Done", false, "", "");
        a.addSubmission(s);

        Submission dup = new Submission(10, "Upload", "Dave", "d1", false, "Not Done", false, "", "");
        assertThrows(IllegalArgumentException.class, () -> a.addSubmission(dup));
    }

    /**
     * Tests addSubmission returns the correct id of the added submission.
     */
    @Test
    public void testAddSubmissionReturnsId() {
        Assignment a = new Assignment("Test", "Lab", 10);
        int id = a.addSubmission("Eve", "e1");
        assertEquals(0, id);
    }

    // ---------------------------------------------------------------
    // getSubmissionById tests
    // ---------------------------------------------------------------

    /**
     * Tests getSubmissionById returns the correct submission.
     */
    @Test
    public void testGetSubmissionById() {
        Assignment a = new Assignment("Project 1", "Project", 100);
        a.addSubmission("Bob", "b1");
        a.addSubmission("Alice", "a1");

        assertEquals("Bob", a.getSubmissionById(0).getName());
        assertEquals("Alice", a.getSubmissionById(1).getName());
    }

    /**
     * Tests getSubmissionById returns null for a non-existent id.
     */
    @Test
    public void testGetSubmissionByIdNotFound() {
        Assignment a = new Assignment("Project 1", "Project", 100);
        assertNull(a.getSubmissionById(99));
    }

    // ---------------------------------------------------------------
    // deleteSubmissionById tests
    // ---------------------------------------------------------------

    /**
     * Tests deleting an existing submission removes it from the list.
     */
    @Test
    public void testDeleteSubmissionById() {
        Assignment a = new Assignment("Project 1", "Project", 100);
        a.addSubmission("Bob", "b1");
        a.addSubmission("Alice", "a1");

        a.deleteSubmissionById(0);
        assertNull(a.getSubmissionById(0));
        assertEquals(1, a.getSubmissions().size());
        assertEquals("Alice", a.getSubmissions().get(0).getName());
    }

    /**
     * Tests deleting a non-existent id does not change the list.
     */
    @Test
    public void testDeleteSubmissionByIdNotFound() {
        Assignment a = new Assignment("Project 1", "Project", 100);
        a.addSubmission("Bob", "b1");
        a.deleteSubmissionById(99);
        assertEquals(1, a.getSubmissions().size());
    }

    // ---------------------------------------------------------------
    // executeCommand tests
    // ---------------------------------------------------------------

    /**
     * Tests executeCommand updates the submission's state.
     */
    @Test
    public void testExecuteCommand() {
        Assignment a = new Assignment("Project 1", "Project", 100);
        a.addSubmission("Bob", "b1");

        a.executeCommand(0, new Command(CommandValue.ASSIGN, "grader1"));
        assertEquals("Feedback", a.getSubmissionById(0).getState());
    }

    /**
     * Tests executeCommand with a non-existent id does nothing.
     */
    @Test
    public void testExecuteCommandNotFound() {
        Assignment a = new Assignment("Project 1", "Project", 100);
        a.addSubmission("Bob", "b1");
        a.executeCommand(99, new Command(CommandValue.ASSIGN, "grader1"));
        assertEquals("Upload", a.getSubmissionById(0).getState());
    }

    // ---------------------------------------------------------------
    // setSubmissionId tests
    // ---------------------------------------------------------------

    /**
     * Tests setSubmissionId sets the counter to one past the max id.
     */
    @Test
    public void testSetSubmissionId() {
        Assignment a = new Assignment("Test", "Lab", 10);
        Submission s = new Submission(7, "Upload", "Frank", "f1", false, "Not Done", false, "", "");
        a.addSubmission(s);
        a.setSubmissionId();
        Submission next = new Submission("Grace", "g1");
        assertEquals(8, next.getId());
    }

    /**
     * Tests setSubmissionId on an empty list sets counter to 0.
     */
    @Test
    public void testSetSubmissionIdEmptyList() {
        Assignment a = new Assignment("Test", "Lab", 10);
        Submission.setCounter(50);
        a.setSubmissionId();
        Submission next = new Submission("Henry", "h1");
        assertEquals(0, next.getId());
    }

    // ---------------------------------------------------------------
    // toString test
    // ---------------------------------------------------------------

    /**
     * Tests toString produces the correct file format.
     */
    @Test
    public void testToString() {
        Assignment a = new Assignment("Lab 1", "Lab", 10);
        a.addSubmission("Ivan", "i1");
        String result = a.toString();
        assertTrue(result.startsWith("# Lab 1,Lab,10\n"));
        assertTrue(result.contains("* 0,Upload,Ivan,i1,false,Not Done,false,,"));
    }
}