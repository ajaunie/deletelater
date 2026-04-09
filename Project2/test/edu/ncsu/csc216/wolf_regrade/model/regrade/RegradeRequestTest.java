package edu.ncsu.csc216.wolf_regrade.model.regrade;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class RegradeRequestTest {
    @Test
    public void testReviewRequest() {
        ReviewRequest r = new ReviewRequest("Student", "unityid", "grader", "Item 1", "Rationale");
        assertEquals("Review", r.getType());
    }
}