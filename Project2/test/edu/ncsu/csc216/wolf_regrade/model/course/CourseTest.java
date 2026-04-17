package edu.ncsu.csc216.wolf_regrade.model.course;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.ncsu.csc216.wolf_regrade.model.regrade.RegradeRequest;
import edu.ncsu.csc216.wolf_regrade.model.regrade.ReviewRequest;

/**
 * Tests for the Course management logic.
 */
public class CourseTest {

    /** The target course instance */
    private Course testCourse;

    /**
     * Utility method to generate a ReviewRequest.
     */
    private ReviewRequest buildRequest(String student, String marker) {
        return new ReviewRequest(student, student.toLowerCase().replace(" ", ""), marker, "Task-A", "Detailed Rationale");
    }

    /**
     * Initializes the testing environment.
     */
    @BeforeEach
    public void setup() {
        testCourse = new Course("MUS200");
    }

    @Test
    public void testInit() {
        assertEquals("MUS200", testCourse.getCourseName());
        assertTrue(testCourse.isChanged());
        assertEquals(0, testCourse.getAssignmentCount());
    }

    @Test
    public void testInitFailure() {
        assertThrows(IllegalArgumentException.class, () -> new Course(null));
        assertThrows(IllegalArgumentException.class, () -> new Course(""));
    }

    @Test
    public void testStateAndAccessors() {
        testCourse.setCourseName("MUS300");
        assertEquals("MUS300", testCourse.getCourseName());
        testCourse.setChanged(false);
        assertFalse(testCourse.isChanged());
        testCourse.setChanged(true);
        assertTrue(testCourse.isChanged());
    }

    @Test
    public void testAssignmentOrdering() {
        testCourse.addAssignment("World Tour");
        testCourse.addAssignment("Studio Session");
        testCourse.addAssignment("Album Release");
        
        assertEquals(3, testCourse.getAssignmentCount());
        assertEquals("Album Release", testCourse.getAssignment(0).getAssignmentName());
        assertEquals("Studio Session", testCourse.getAssignment(1).getAssignmentName());
        assertEquals("World Tour", testCourse.getAssignment(2).getAssignmentName());
    }

    @Test
    public void testDuplicatePrevention() {
        testCourse.addAssignment("Section.80");
        assertThrows(IllegalArgumentException.class, () -> testCourse.addAssignment("Section.80"));
    }

    @Test
    public void testInvalidAssignmentAddition() {
        assertThrows(IllegalArgumentException.class, () -> testCourse.addAssignment(null));
        assertThrows(IllegalArgumentException.class, () -> testCourse.addAssignment("   "));
    }

    @Test
    public void testAssignmentModification() {
        testCourse.addAssignment("DAMN");
        testCourse.addAssignment("Rodeo");
        testCourse.setChanged(false);
        
        testCourse.editAssignment(0, "Graduation");
        assertTrue(testCourse.isChanged());
        
        boolean isFound = false;
        for (int i = 0; i < testCourse.getAssignmentCount(); i++) {
            if ("Graduation".equals(testCourse.getAssignment(i).getAssignmentName())) {
                isFound = true;
            }
        }
        assertTrue(isFound);
    }

    @Test
    public void testConflictDuringEdit() {
        testCourse.addAssignment("Take Care");
        testCourse.addAssignment("Views");
        assertThrows(IllegalArgumentException.class, () -> testCourse.editAssignment(0, "Views"));
    }

    @Test
    public void testInvalidRename() {
        testCourse.addAssignment("Scorpion");
        assertThrows(IllegalArgumentException.class, () -> testCourse.editAssignment(0, null));
        assertThrows(IllegalArgumentException.class, () -> testCourse.editAssignment(0, ""));
    }

    @Test
    public void testAssignmentRemoval() {
        testCourse.addAssignment("Astroworld");
        testCourse.addAssignment("Utopia");
        testCourse.setChanged(false);
        
        Assignment deleted = testCourse.removeAssignment(0);
        assertEquals("Astroworld", deleted.getAssignmentName());
        assertEquals(1, testCourse.getAssignmentCount());
        assertTrue(testCourse.isChanged());
    }

    @Test
    public void testRemovalBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> testCourse.removeAssignment(5));
    }

    @Test
    public void testRetrievalBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> testCourse.getAssignment(0));
    }

    @Test
    public void testDataExportArrays() {
        testCourse.addAssignment("Album B");
        testCourse.addAssignment("Album A");
        
        String[] simple = testCourse.getAssignmentsAsArray();
        assertEquals("Album A", simple[0]);
        assertEquals("Album B", simple[1]);

        testCourse.addPendingRequest(0, buildRequest("Kendrick", "Producer X"));
        testCourse.addPendingRequest(0, buildRequest("Drake", "Producer X"));
        
        String[][] complex = testCourse.getAssignmentsAsDetailArray();
        assertEquals("Album A", complex[0][0]);
        assertEquals("2", complex[0][1]); // pending count
        assertEquals("0", complex[0][2]); // completed count
    }

    @Test
    public void testPendingRequestLifecycle() {
        testCourse.addAssignment("Session 1");
        testCourse.setChanged(false);
        
        ReviewRequest kDot = buildRequest("Kendrick", "Metro");
        testCourse.addPendingRequest(0, kDot);
        assertEquals(1, testCourse.getAssignment(0).getPendingSize());
        assertTrue(testCourse.isChanged());

        ReviewRequest dzy = buildRequest("Drake", "Metro");
        testCourse.setChanged(false);
        testCourse.editPendingRequest(0, 0, dzy);
        assertSame(dzy, testCourse.getAssignment(0).getPendingRequest(0));
        assertTrue(testCourse.isChanged());

        testCourse.setChanged(false);
        RegradeRequest popped = testCourse.removePendingRequest(0, 0);
        assertSame(dzy, popped);
        assertEquals(0, testCourse.getAssignment(0).getPendingSize());
        assertTrue(testCourse.isChanged());
    }

    @Test
    public void testWorkflowCompletion() {
        testCourse.addAssignment("Mixdown");
        testCourse.addPendingRequest(0, buildRequest("Cole", "Tay Keith"));
        testCourse.setChanged(false);
        
        testCourse.completeRequest(0, 0, RegradeRequest.RESOLUTION_CLOSED, false);
        assertEquals(0, testCourse.getAssignment(0).getPendingSize());
        assertEquals(1, testCourse.getAssignment(0).getCompletedSize());
        assertTrue(testCourse.isChanged());

        testCourse.setChanged(false);
        testCourse.undoLastCompletion(0);
        assertEquals(1, testCourse.getAssignment(0).getPendingSize());
        assertTrue(testCourse.isChanged());
    }

    @Test
    public void testReversalFailure() {
        testCourse.addAssignment("Mastering");
        assertThrows(IllegalArgumentException.class, () -> testCourse.undoLastCompletion(0));
    }

    @Test
    public void testFilterFunctionality() {
        testCourse.addAssignment("Project A");
        testCourse.addAssignment("Project B");
        testCourse.addPendingRequest(0, buildRequest("Future", "Grader-1"));
        testCourse.addPendingRequest(0, buildRequest("Travis", "Grader-2"));
        testCourse.addPendingRequest(1, buildRequest("Carti", "Grader-1"));
        
        String[][] results = testCourse.filterByGrader("Grader-1");
        assertEquals(2, results.length);
        assertEquals("Project A", results[0][1]);
        assertEquals("Future", results[0][2]);
        assertEquals("Project B", results[1][1]);
        assertEquals("Carti", results[1][2]);
    }

    @Test
    public void testFilterEmptyResults() {
        testCourse.addAssignment("Singles");
        testCourse.addPendingRequest(0, buildRequest("Thugger", "YSL"));
        assertEquals(0, testCourse.filterByGrader("OVO").length);
    }

    @Test
    public void testGlobalFilterEmpty() {
        assertEquals(0, testCourse.filterByGrader("anyone").length);
    }

    @Test
    public void testPersistentStorage() {
        testCourse.addAssignment("Final Cut");
        testCourse.addPendingRequest(0, buildRequest("Ye", "Mike Dean"));
        
        File output = new File("test-files/export_log.txt");
        if (output.exists()) {
            output.delete();
        }
        output.getParentFile().mkdirs();
        
        testCourse.saveCourseRegrades(output);
        assertFalse(testCourse.isChanged());
        assertTrue(output.exists());
        output.delete();
    }

    @Test
    public void testStorageFailure() {
        assertThrows(IllegalArgumentException.class,
                () -> testCourse.saveCourseRegrades(new File("/invalid_root/directory/file.txt")));
    }
}