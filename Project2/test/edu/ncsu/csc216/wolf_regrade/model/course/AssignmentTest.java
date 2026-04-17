package edu.ncsu.csc216.wolf_regrade.model.course;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.ncsu.csc216.wolf_regrade.model.regrade.ClarifyRequest;
import edu.ncsu.csc216.wolf_regrade.model.regrade.RegradeRequest;
import edu.ncsu.csc216.wolf_regrade.model.regrade.ReviewRequest;

/**
 * Tests for Assignment logic and RegradeRequest integration.
 */
public class AssignmentTest {

    /** Assignment object for testing */
    private Assignment discography;

    /**
     * Generates a ReviewRequest for testing.
     * @param artist the artist name
     * @return a new ReviewRequest
     */
    private ReviewRequest spawnReview(String artist) {
        return new ReviewRequest(artist, artist.toLowerCase().replace(" ", ""), "Rick Rubin", "Verse 1", "Mix sounds thin.");
    }

    /**
     * Generates a ClarifyRequest for testing.
     * @param artist the artist name
     * @return a new ClarifyRequest
     */
    private ClarifyRequest spawnClarify(String artist) {
        return new ClarifyRequest(artist, artist.toLowerCase().replace(" ", ""), "Quincy Jones", "Sample Clearance", "Is this licensed?");
    }

    /**
     * Initializes a fresh Assignment before every test case.
     */
    @BeforeEach
    public void setup() {
        discography = new Assignment("The Blueprint");
    }

    @Test
    public void testInitialization() {
        assertEquals("The Blueprint", discography.getAssignmentName());
        assertEquals(0, discography.getPendingSize());
        assertEquals(0, discography.getCompletedSize());
        assertEquals(0, discography.getTotalSize());
        assertEquals(0, discography.getCompletedPercentage());
    }

    @Test
    public void testInvalidConstruction() {
        assertThrows(IllegalArgumentException.class, () -> new Assignment(null));
        assertThrows(IllegalArgumentException.class, () -> new Assignment(""));
    }

    @Test
    public void testNameModification() {
        discography.setAssignmentName("Illmatic");
        assertEquals("Illmatic", discography.getAssignmentName());
        
        assertThrows(IllegalArgumentException.class, () -> discography.setAssignmentName(null));
        assertThrows(IllegalArgumentException.class, () -> discography.setAssignmentName("   "));
    }

    @Test
    public void testPendingAddition() {
        ReviewRequest r = spawnReview("Jay-Z");
        discography.addPendingRequest(r);
        assertEquals(1, discography.getPendingSize());
        assertSame(r, discography.getPendingRequest(0));
        assertSame(discography, r.getAssignment());
        assertEquals("The Blueprint", r.getAssignmentName());
    }

    @Test
    public void testMultipleQueueOrder() {
        ReviewRequest r1 = spawnReview("Nas");
        ReviewRequest r2 = spawnReview("Eminem");
        ReviewRequest r3 = spawnReview("Kendrick");
        discography.addPendingRequest(r1);
        discography.addPendingRequest(r2);
        discography.addPendingRequest(r3);
        
        assertEquals(3, discography.getPendingSize());
        assertSame(r1, discography.getPendingRequest(0));
        assertSame(r2, discography.getPendingRequest(1));
        assertSame(r3, discography.getPendingRequest(2));
    }

    @Test
    public void testRetrievalBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> discography.getPendingRequest(0));
        discography.addPendingRequest(spawnReview("Drake"));
        assertThrows(IndexOutOfBoundsException.class, () -> discography.getPendingRequest(1));
    }

    @Test
    public void testRemovalLogic() {
        ReviewRequest r1 = spawnReview("Andre 3000");
        ReviewRequest r2 = spawnReview("Biggie");
        discography.addPendingRequest(r1);
        discography.addPendingRequest(r2);
        
        RegradeRequest removed = discography.removePendingRequest(0);
        assertSame(r1, removed);
        assertEquals(1, discography.getPendingSize());
        assertSame(r2, discography.getPendingRequest(0));
    }

    @Test
    public void testRequestEditing() {
        ReviewRequest r1 = spawnReview("Tupac");
        ReviewRequest r2 = spawnReview("Snoop");
        discography.addPendingRequest(r1);
        discography.editPendingRequest(0, r2);
        
        assertEquals(1, discography.getPendingSize());
        assertSame(r2, discography.getPendingRequest(0));
        assertSame(discography, r2.getAssignment());
    }

    @Test
    public void testCompletionWorkflow() {
        ReviewRequest r = spawnReview("J Cole");
        discography.addPendingRequest(r);
        discography.completeRequest(0, RegradeRequest.RESOLUTION_CLOSED, true);
        
        assertEquals(0, discography.getPendingSize());
        assertEquals(1, discography.getCompletedSize());
        
        RegradeRequest top = discography.getCompletedRequests().peek();
        assertSame(r, top);
        assertEquals(RegradeRequest.RESOLUTION_CLOSED, top.getResolution());
        assertTrue(top.isGradeChanged());
    }

    @Test
    public void testResolutionVariations() {
        ReviewRequest r = spawnReview("Pusha T");
        discography.addPendingRequest(r);
        discography.completeRequest(0, RegradeRequest.RESOLUTION_DUPLICATE, false);
        assertEquals(RegradeRequest.RESOLUTION_DUPLICATE, r.getResolution());
        assertFalse(r.isGradeChanged());
    }

    @Test
    public void testInconsistentCompletion() {
        discography.addPendingRequest(spawnReview("Busta Rhymes"));
        // Cannot have grade change without Closed status
        assertThrows(IllegalArgumentException.class,
                () -> discography.completeRequest(0, RegradeRequest.RESOLUTION_DUPLICATE, true));
        assertEquals(1, discography.getPendingSize());
    }

    @Test
    public void testDirectCompletionAdd() {
        ReviewRequest r = spawnReview("Ghostface Killah");
        r.setResolution(RegradeRequest.RESOLUTION_CLOSED);
        discography.addCompletedRequest(r);
        assertEquals(1, discography.getCompletedSize());
        assertSame(discography, r.getAssignment());
    }

    @Test
    public void testUndoMechanism() {
        ReviewRequest r = spawnReview("Raekwon");
        discography.addPendingRequest(r);
        discography.completeRequest(0, RegradeRequest.RESOLUTION_CLOSED, true);
        
        discography.undoLastCompletion();
        assertEquals(1, discography.getPendingSize());
        assertEquals(0, discography.getCompletedSize());
        assertSame(r, discography.getPendingRequest(0));
        assertNull(r.getResolution());
        assertFalse(r.isGradeChanged());
    }

    @Test
    public void testUndoStackLogic() {
        ReviewRequest r1 = spawnReview("Method Man");
        ReviewRequest r2 = spawnReview("Redman");
        discography.addPendingRequest(r1);
        discography.addPendingRequest(r2);
        
        discography.completeRequest(0, RegradeRequest.RESOLUTION_CLOSED, false);
        discography.completeRequest(0, RegradeRequest.RESOLUTION_DUPLICATE, false);
        
        discography.undoLastCompletion();
        assertEquals(1, discography.getPendingSize());
        assertEquals(1, discography.getCompletedSize());
        assertSame(r2, discography.getPendingRequest(0));
    }

    @Test
    public void testUndoEmptyError() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> discography.undoLastCompletion());
        assertEquals("No completed requests to undo.", e.getMessage());
    }

    @Test
    public void testProgressCalculation() {
        assertEquals(0, discography.getCompletedPercentage());
        discography.addPendingRequest(spawnReview("Logic"));
        discography.addPendingRequest(spawnReview("Mac Miller"));
        
        assertEquals(2, discography.getTotalSize());
        discography.completeRequest(0, RegradeRequest.RESOLUTION_CLOSED, false);
        assertEquals(50, discography.getCompletedPercentage());
        
        discography.completeRequest(0, RegradeRequest.RESOLUTION_DUPLICATE, false);
        assertEquals(100, discography.getCompletedPercentage());
    }

    @Test
    public void testPendingArrayMapping() {
        ReviewRequest r1 = spawnReview("Future");
        ClarifyRequest r2 = spawnClarify("Thugger");
        discography.addPendingRequest(r1);
        discography.addPendingRequest(r2);
        
        String[][] data = discography.getPendingRequestsAsArray();
        assertEquals(2, data.length);
        assertEquals(5, data[0].length);
        assertEquals("Review", data[0][0]);
        assertEquals("The Blueprint", data[0][1]);
        assertEquals("Future", data[0][2]);
        assertEquals("Rick Rubin", data[0][4]);
        assertEquals("Clarify", data[1][0]);
    }

    @Test
    public void testCompletedArrayMapping() {
        ReviewRequest r = spawnReview("Ye");
        discography.addPendingRequest(r);
        discography.completeRequest(0, RegradeRequest.RESOLUTION_CLOSED, true);
        
        String[][] data = discography.getCompletedRequestsAsArray();
        assertEquals(1, data.length);
        assertEquals("Review", data[0][0]);
        assertEquals("Ye", data[0][2]);
        assertEquals("Closed", data[0][5]);
        assertEquals("true", data[0][6]);
    }

    @Test
    public void testEmptyStateArrays() {
        assertEquals(0, discography.getPendingRequestsAsArray().length);
        assertEquals(0, discography.getCompletedRequestsAsArray().length);
    }

    @Test
    public void testAlphabeticalComparison() {
        Assignment a1 = new Assignment("blueprint");
        Assignment a2 = new Assignment("DAMN");
        Assignment a3 = new Assignment("BLUEPRINT");
        
        assertTrue(a1.compareTo(a2) > 0);
        assertTrue(a2.compareTo(a1) < 0);
        assertEquals(0, a1.compareTo(a3));
    }

    @Test
    public void testStringRepresentation() {
        assertEquals("The Blueprint", discography.toString());
    }

    @Test
    public void testAccessors() {
        assertNotNull(discography.getPendingRequests());
        assertNotNull(discography.getCompletedRequests());
    }
}