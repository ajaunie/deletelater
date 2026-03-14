package edu.ncsu.csc216.wolf_grader.model.manager;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import edu.ncsu.csc216.wolf_grader.model.command.Command;
import edu.ncsu.csc216.wolf_grader.model.command.Command.CommandValue;
import edu.ncsu.csc216.wolf_grader.model.submission.Submission;

/**
 * Provides tests for the WolfGrader singleton manager and global application state.
 * @author Ajaunie White
 */
public class WolfGraderTest {

    /**
     * Resets the WolfGrader manager and submission counter before each test.
     */
    @BeforeEach
    public void setUp() {
        WolfGrader.getInstance().resetManager();
        Submission.setCounter(0);
    }

    /**
     * Helper method to create a temporary test file with the given content.
     * @param text the text to write into the file.
     * @return the absolute path of the created file.
     */
    private String createTempFile(String text) {
        try {
            File f = File.createTempFile("wg_test", ".txt");
            FileWriter fw = new FileWriter(f);
            fw.write(text);
            fw.close();
            return f.getAbsolutePath();
        } catch (IOException e) {
            fail("File setup failed.");
            return null;
        }
    }

    // ---------------------------------------------------------------
    // Singleton tests
    // ---------------------------------------------------------------

    /**
     * Tests that getInstance always returns the same object.
     */
    @Test
    public void testSingleton() {
        WolfGrader wg1 = WolfGrader.getInstance();
        WolfGrader wg2 = WolfGrader.getInstance();
        assertSame(wg1, wg2);
    }

    /**
     * Tests that resetManager causes the next getInstance to return a fresh object.
     */
    @Test
    public void testResetManager() {
        WolfGrader wg = WolfGrader.getInstance();
        wg.addNewAssignment("Lab 1", "Lab", 10);
        wg.resetManager();
        WolfGrader fresh = WolfGrader.getInstance();
        assertNull(fresh.getActiveAssignmentName());
        assertEquals(0, fresh.getAssignmentList().length);
    }

    // ---------------------------------------------------------------
    // getActiveAssignmentName and getActiveAssignment tests
    // ---------------------------------------------------------------

    /**
     * Tests that active assignment name is null on a fresh manager.
     */
    @Test
    public void testActiveAssignmentNullInitially() {
        WolfGrader wg = WolfGrader.getInstance();
        assertNull(wg.getActiveAssignmentName());
        assertNull(wg.getActiveAssignment());
    }

    // ---------------------------------------------------------------
    // addNewAssignment tests
    // ---------------------------------------------------------------

    /**
     * Tests that addNewAssignment creates an assignment and sets it active.
     */
    @Test
    public void testAddNewAssignment() {
        WolfGrader wg = WolfGrader.getInstance();
        wg.addNewAssignment("Lab 1", "Lab", 10);
        assertEquals("Lab 1", wg.getActiveAssignmentName());
        assertNotNull(wg.getActiveAssignment());
    }

    /**
     * Tests that addNewAssignment with a duplicate name throws IllegalArgumentException.
     */
    @Test
    public void testAddNewAssignmentDuplicate() {
        WolfGrader wg = WolfGrader.getInstance();
        wg.addNewAssignment("Lab 1", "Lab", 10);
        assertThrows(IllegalArgumentException.class,
                () -> wg.addNewAssignment("Lab 1", "Lab", 10));
    }

    /**
     * Tests that addNewAssignment duplicate check is case-insensitive.
     */
    @Test
    public void testAddNewAssignmentDuplicateCaseInsensitive() {
        WolfGrader wg = WolfGrader.getInstance();
        wg.addNewAssignment("Lab 1", "Lab", 10);
        assertThrows(IllegalArgumentException.class,
                () -> wg.addNewAssignment("LAB 1", "Lab", 10));
    }

    /**
     * Tests that addNewAssignment with a null name throws IllegalArgumentException.
     */
    @Test
    public void testAddNewAssignmentNullName() {
        assertThrows(IllegalArgumentException.class,
                () -> WolfGrader.getInstance().addNewAssignment(null, "Lab", 10));
    }

    /**
     * Tests that addNewAssignment with an empty name throws IllegalArgumentException.
     */
    @Test
    public void testAddNewAssignmentEmptyName() {
        assertThrows(IllegalArgumentException.class,
                () -> WolfGrader.getInstance().addNewAssignment("", "Lab", 10));
    }

    /**
     * Tests that addNewAssignment with invalid maxPoints throws IllegalArgumentException.
     */
    @Test
    public void testAddNewAssignmentInvalidMaxPoints() {
        assertThrows(IllegalArgumentException.class,
                () -> WolfGrader.getInstance().addNewAssignment("Name", "Lab", 0));
        assertThrows(IllegalArgumentException.class,
                () -> WolfGrader.getInstance().addNewAssignment("Name2", "Lab", 101));
    }

    // ---------------------------------------------------------------
    // getAssignmentList tests
    // ---------------------------------------------------------------

    /**
     * Tests getAssignmentList returns an empty array when no assignments exist.
     */
    @Test
    public void testGetAssignmentListEmpty() {
        WolfGrader wg = WolfGrader.getInstance();
        assertEquals(0, wg.getAssignmentList().length);
    }

    /**
     * Tests getAssignmentList returns names in insertion order.
     */
    @Test
    public void testGetAssignmentList() {
        WolfGrader wg = WolfGrader.getInstance();
        wg.addNewAssignment("Lab 1", "Lab", 10);
        wg.addNewAssignment("Project 1", "Project", 100);

        String[] list = wg.getAssignmentList();
        assertEquals(2, list.length);
        assertEquals("Lab 1", list[0]);
        assertEquals("Project 1", list[1]);
    }

    // ---------------------------------------------------------------
    // loadAssignment tests
    // ---------------------------------------------------------------

    /**
     * Tests loadAssignment switches the active assignment.
     */
    @Test
    public void testLoadAssignment() {
        WolfGrader wg = WolfGrader.getInstance();
        wg.addNewAssignment("Lab 1", "Lab", 10);
        wg.addNewAssignment("Project 1", "Project", 100);
        wg.loadAssignment("Lab 1");
        assertEquals("Lab 1", wg.getActiveAssignmentName());
    }

    /**
     * Tests loadAssignment with a non-existent name throws IllegalArgumentException.
     */
    @Test
    public void testLoadAssignmentNotFound() {
        WolfGrader wg = WolfGrader.getInstance();
        assertThrows(IllegalArgumentException.class,
                () -> wg.loadAssignment("NoSuchAssignment"));
    }

    // ---------------------------------------------------------------
    // addSubmissionToAssignment tests
    // ---------------------------------------------------------------

    /**
     * Tests addSubmissionToAssignment adds a submission to the active assignment.
     */
    @Test
    public void testAddSubmissionToAssignment() {
        WolfGrader wg = WolfGrader.getInstance();
        wg.addNewAssignment("Lab 1", "Lab", 10);
        wg.addSubmissionToAssignment("Devin", "d1");

        String[][] data = wg.getSubmissionsAsArray("All");
        assertEquals(1, data.length);
        assertTrue(data[0][2].contains("Devin"));
    }

    /**
     * Tests addSubmissionToAssignment does nothing when there is no active assignment.
     */
    @Test
    public void testAddSubmissionNoActiveAssignment() {
        WolfGrader wg = WolfGrader.getInstance();
        wg.addSubmissionToAssignment("Ghost", "g1");
        assertEquals(0, wg.getAssignmentList().length);
    }

    // ---------------------------------------------------------------
    // getSubmissionsAsArray tests
    // ---------------------------------------------------------------

    /**
     * Tests getSubmissionsAsArray returns null when no active assignment.
     */
    @Test
    public void testGetSubmissionsAsArrayNoActiveAssignment() {
        WolfGrader wg = WolfGrader.getInstance();
        assertNull(wg.getSubmissionsAsArray("All"));
    }

    /**
     * Tests getSubmissionsAsArray with filter "All" returns all submissions.
     */
    @Test
    public void testGetSubmissionsAsArrayAll() {
        WolfGrader wg = WolfGrader.getInstance();
        wg.addNewAssignment("Lab 1", "Lab", 10);
        wg.addSubmissionToAssignment("Alice", "a1");
        wg.addSubmissionToAssignment("Bob", "b1");

        String[][] data = wg.getSubmissionsAsArray("All");
        assertEquals(2, data.length);
    }

    /**
     * Tests getSubmissionsAsArray with a state filter returns only matching submissions.
     */
    @Test
    public void testGetSubmissionsAsArrayFiltered() {
        WolfGrader wg = WolfGrader.getInstance();
        wg.addNewAssignment("Lab 1", "Lab", 10);
        wg.addSubmissionToAssignment("Alice", "a1");
        wg.addSubmissionToAssignment("Bob", "b1");

        wg.executeCommand(0, new Command(CommandValue.ASSIGN, "grader1"));

        String[][] uploadData = wg.getSubmissionsAsArray("Upload");
        assertEquals(1, uploadData.length);
        assertTrue(uploadData[0][2].contains("Bob"));

        String[][] feedbackData = wg.getSubmissionsAsArray("Feedback");
        assertEquals(1, feedbackData.length);
        assertTrue(feedbackData[0][2].contains("Alice"));
    }

    /**
     * Tests the columns of getSubmissionsAsArray are in the correct order.
     */
    @Test
    public void testGetSubmissionsAsArrayColumns() {
        WolfGrader wg = WolfGrader.getInstance();
        wg.addNewAssignment("Test", "Lab", 10);
        wg.addSubmissionToAssignment("Frank", "f1");

        String[][] data = wg.getSubmissionsAsArray("All");
        assertEquals("0", data[0][0]);
        assertEquals("Upload", data[0][1]);
        assertTrue(data[0][2].contains("Frank"));
        assertTrue(data[0][2].contains("f1"));
        assertEquals("Frank(f1)", data[0][2]);
        assertEquals("Not Done", data[0][3]);
    }

    // ---------------------------------------------------------------
    // getSubmissionById tests
    // ---------------------------------------------------------------

    /**
     * Tests getSubmissionById returns the correct submission.
     */
    @Test
    public void testGetSubmissionById() {
        WolfGrader wg = WolfGrader.getInstance();
        wg.addNewAssignment("Lab 1", "Lab", 10);
        wg.addSubmissionToAssignment("Grace", "g1");

        Submission s = wg.getSubmissionById(0);
        assertNotNull(s);
        assertEquals("Grace", s.getName());
    }

    /**
     * Tests getSubmissionById returns null for a non-existent id.
     */
    @Test
    public void testGetSubmissionByIdNotFound() {
        WolfGrader wg = WolfGrader.getInstance();
        wg.addNewAssignment("Lab 1", "Lab", 10);
        assertNull(wg.getSubmissionById(99));
    }

    /**
     * Tests getSubmissionById returns null when there is no active assignment.
     */
    @Test
    public void testGetSubmissionByIdNoActive() {
        WolfGrader wg = WolfGrader.getInstance();
        assertNull(wg.getSubmissionById(0));
    }

    // ---------------------------------------------------------------
    // executeCommand tests
    // ---------------------------------------------------------------

    /**
     * Tests executeCommand updates the submission state through the manager.
     */
    @Test
    public void testExecuteCommand() {
        WolfGrader wg = WolfGrader.getInstance();
        wg.addNewAssignment("Lab 1", "Lab", 10);
        wg.addSubmissionToAssignment("Henry", "h1");
        wg.executeCommand(0, new Command(CommandValue.ASSIGN, "grader1"));
        assertEquals("Feedback", wg.getSubmissionById(0).getState());
    }

    /**
     * Tests executeCommand does nothing when there is no active assignment.
     */
    @Test
    public void testExecuteCommandNoActive() {
        WolfGrader wg = WolfGrader.getInstance();
        assertDoesNotThrow(() -> wg.executeCommand(0, new Command(CommandValue.ASSIGN, "grader1")));
    }

    // ---------------------------------------------------------------
    // deleteSubmissionById tests
    // ---------------------------------------------------------------

    /**
     * Tests deleteSubmissionById removes the submission through the manager.
     */
    @Test
    public void testDeleteSubmissionById() {
        WolfGrader wg = WolfGrader.getInstance();
        wg.addNewAssignment("Lab 1", "Lab", 10);
        wg.addSubmissionToAssignment("Iris", "i1");
        wg.deleteSubmissionById(0);
        assertNull(wg.getSubmissionById(0));
    }

    /**
     * Tests deleteSubmissionById does nothing when there is no active assignment.
     */
    @Test
    public void testDeleteSubmissionByIdNoActive() {
        WolfGrader wg = WolfGrader.getInstance();
        assertDoesNotThrow(() -> wg.deleteSubmissionById(0));
    }

    // ---------------------------------------------------------------
    // saveAssignmentsToFile tests
    // ---------------------------------------------------------------

    /**
     * Tests saveAssignmentsToFile throws when there is no active assignment.
     */
    @Test
    public void testSaveNoActiveAssignment() {
        WolfGrader wg = WolfGrader.getInstance();
        assertThrows(IllegalArgumentException.class,
                () -> wg.saveAssignmentsToFile("output.txt"));
    }

    /**
     * Tests saveAssignmentsToFile saves without error when there is an active assignment.
     */
    @Test
    public void testSaveWithActiveAssignment() {
        WolfGrader wg = WolfGrader.getInstance();
        wg.addNewAssignment("Save Test", "Lab", 10);
        wg.addSubmissionToAssignment("Jack", "j1");

        try {
            wg.saveAssignmentsToFile("test_wg_save.txt");
        } catch (Exception e) {
            fail("Should not throw: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------------
    // loadAssignmentsFromFile tests
    // ---------------------------------------------------------------

    /**
     * Tests loadAssignmentsFromFile loads assignments and sets the first active.
     */
    @Test
    public void testLoadAssignmentsFromFile() {
        String content = "# Lab A,Lab,10\n"
                + "* 1,Upload,Kim,k1,false,Not Done,false,,\n"
                + "# Lab B,Lab,10\n"
                + "* 2,Submitted,Lee,l1,true,Not Done,false,,\n";

        String path = createTempFile(content);
        WolfGrader wg = WolfGrader.getInstance();
        wg.loadAssignmentsFromFile(path);

        assertEquals("Lab A", wg.getActiveAssignmentName());
        assertEquals(2, wg.getAssignmentList().length);
    }

    /**
     * Tests loadAssignmentsFromFile with non-existent file throws IllegalArgumentException.
     */
    @Test
    public void testLoadAssignmentsFromFileNotFound() {
        assertThrows(IllegalArgumentException.class,
                () -> WolfGrader.getInstance().loadAssignmentsFromFile("no_such_file.txt"));
    }
}