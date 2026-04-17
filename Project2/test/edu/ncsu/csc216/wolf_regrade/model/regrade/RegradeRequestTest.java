package edu.ncsu.csc216.wolf_regrade.model.regrade;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Tests polymorphism and shared logic for all Request types.
 */
public class RegradeRequestTest {

    @Test
    public void testReviewLogic() {
        ReviewRequest rev = new ReviewRequest("Kendrick", "kdot", "Sounwave", "Verse 2", "Mix is off");
        assertEquals("Review", rev.getType());
        assertEquals("Rationale", rev.getOpenTextName());
        assertEquals("Verse 2", rev.getReviewItem());
        
        rev.setResolution(RegradeRequest.RESOLUTION_CLOSED);
        rev.setGradeChanged(true);
        assertTrue(rev.isGradeChanged());
        
        assertThrows(IllegalArgumentException.class, () -> rev.setStudentName(""));
        assertThrows(IllegalArgumentException.class, () -> rev.setGradeChanged(true)); // Needs resolution set first if we reset it
    }

    @Test
    public void testResubmitLogic() {
        ResubmitRequest res = new ResubmitRequest("Cole", "cole_w", "Elite", "github.com/cole", "abc1234", "New verse");
        assertEquals("Resubmit", res.getType());
        assertEquals("github.com/cole", res.getRepoLink());
        assertEquals("abc1234", res.getCommitHash());
        
        String[] fields = res.getSubtypeFields();
        assertEquals(2, fields.length);
        assertEquals("abc1234", fields[1]);
    }

    @Test
    public void testClarifyLogic() {
        ClarifyRequest cla = new ClarifyRequest("Drake", "drizzy", "40", "Intro", "Why the delay?");
        assertEquals("Clarify", cla.getType());
        
        // Clarify specific restriction: cannot be CLOSED
        assertThrows(IllegalArgumentException.class, () -> cla.setResolution(RegradeRequest.RESOLUTION_CLOSED));
        cla.setResolution(RegradeRequest.RESOLUTION_ADDITIONAL_INFO);
        assertEquals(RegradeRequest.RESOLUTION_ADDITIONAL_INFO, cla.getResolution());
    }

    @Test
    public void testExemptionLogic() {
        ExemptionRequest ex = new ExemptionRequest("Travis", "laflame", "ChaseB", "Policy-9", "Tour conflict");
        assertEquals("Exemption", ex.getType());
        assertEquals("Policy-9", ex.getPolicyReference());
        
        String ts = ex.toString();
        assertTrue(ts.contains("Travis"));
        assertTrue(ts.contains("Policy-9"));
    }
}