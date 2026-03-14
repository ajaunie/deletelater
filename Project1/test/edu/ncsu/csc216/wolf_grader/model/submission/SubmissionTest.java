package edu.ncsu.csc216.wolf_grader.model.submission;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import edu.ncsu.csc216.wolf_grader.model.command.Command;
import edu.ncsu.csc216.wolf_grader.model.command.Command.CommandValue;

/**
 * Provides comprehensive tests for the Submission class, focusing on the correctness
 * of the state machine logic and error handling for all transitions.
 * @author Ajaunie White
 */
public class SubmissionTest {

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
     * Tests the short constructor with valid name and unityId.
     */
    @Test
    public void testShortConstructorValid() {
        Submission s = new Submission("Alice Smith", "asmith");
        assertEquals(0, s.getId());
        assertEquals(Submission.UPLOAD_NAME, s.getState());
        assertEquals("Alice Smith", s.getName());
        assertEquals("asmith", s.getUnityId());
        assertEquals(Submission.PC_NONE, s.getCheckResult());
        assertFalse(s.isFeedbackProcessed());
        assertFalse(s.isPublished());
        assertEquals("", s.getGrader());
        assertEquals("", s.getGrade());
    }

    /**
     * Tests that the counter increments across multiple submissions.
     */
    @Test
    public void testCounterIncrements() {
        Submission s1 = new Submission("Bob", "b1");
        Submission s2 = new Submission("Carol", "c1");
        assertEquals(0, s1.getId());
        assertEquals(1, s2.getId());
    }

    /**
     * Tests the short constructor rejects null or empty name/unityId.
     */
    @Test
    public void testShortConstructorInvalid() {
        assertThrows(IllegalArgumentException.class, () -> new Submission(null, "u1"));
        assertThrows(IllegalArgumentException.class, () -> new Submission("", "u1"));
        assertThrows(IllegalArgumentException.class, () -> new Submission("Name", null));
        assertThrows(IllegalArgumentException.class, () -> new Submission("Name", ""));
    }

    /**
     * Tests the full constructor creates a valid Upload submission.
     */
    @Test
    public void testFullConstructorUpload() {
        Submission s = new Submission(5, "Upload", "Dan", "d1", false, "Not Done", false, "", "");
        assertEquals(5, s.getId());
        assertEquals(Submission.UPLOAD_NAME, s.getState());
        assertEquals("Dan", s.getName());
        assertEquals("d1", s.getUnityId());
        assertFalse(s.isFeedbackProcessed());
        assertEquals(Submission.PC_NONE, s.getCheckResult());
        assertFalse(s.isPublished());
        assertEquals("", s.getGrader());
        assertEquals("", s.getGrade());
    }

    /**
     * Tests full constructor for a valid Feedback submission.
     */
    @Test
    public void testFullConstructorFeedback() {
        Submission s = new Submission(2, "Feedback", "Eve", "e1", false, "Not Done", false, "grader1", "");
        assertEquals(Submission.FEEDBACK_NAME, s.getState());
        assertEquals("grader1", s.getGrader());
        assertFalse(s.isFeedbackProcessed());
    }

    /**
     * Tests full constructor for a valid Submitted submission.
     */
    @Test
    public void testFullConstructorSubmitted() {
        Submission s = new Submission(3, "Submitted", "Frank", "f1", true, "Not Done", false, "", "");
        assertEquals(Submission.SUBMITTED_NAME, s.getState());
        assertTrue(s.isFeedbackProcessed());
        assertEquals("", s.getGrader());
    }

    /**
     * Tests full constructor for a valid Grade submission (with Failed check result).
     */
    @Test
    public void testFullConstructorGradeWithFailed() {
        Submission s = new Submission(4, "Grade", "Gina", "g1", true, "Failed", false, "grader1", "");
        assertEquals(Submission.GRADE_NAME, s.getState());
        assertEquals(Submission.PC_FAIL, s.getCheckResult());
    }

    /**
     * Tests full constructor for a valid Check submission.
     */
    @Test
    public void testFullConstructorCheck() {
        Submission s = new Submission(6, "Check", "Hank", "h1", true, "Not Done", false, "grader1", "B");
        assertEquals(Submission.CHECK_NAME, s.getState());
        assertEquals("B", s.getGrade());
    }

    /**
     * Tests full constructor for a valid Regrade submission (must have Passed check result).
     */
    @Test
    public void testFullConstructorRegrade() {
        Submission s = new Submission(7, "Regrade", "Iris", "i1", true, "Passed", false, "grader1", "C");
        assertEquals(Submission.REGRADE_NAME, s.getState());
        assertEquals(Submission.PC_PASS, s.getCheckResult());
    }

    /**
     * Tests full constructor for a valid Return submission with Passed check result and letter grade.
     */
    @Test
    public void testFullConstructorReturnPassed() {
        Submission s = new Submission(8, "Return", "Jake", "j1", true, "Passed", false, "grader1", "A");
        assertEquals(Submission.RETURN_NAME, s.getState());
        assertEquals("A", s.getGrade());
        assertFalse(s.isPublished());
    }

    /**
     * Tests full constructor for a valid published Return submission.
     */
    @Test
    public void testFullConstructorReturnPublished() {
        Submission s = new Submission(9, "Return", "Kim", "k1", true, "Passed", true, "grader1", "B");
        assertTrue(s.isPublished());
    }

    /**
     * Tests full constructor for a Return submission with Failed check result and IN grade.
     */
    @Test
    public void testFullConstructorReturnFailedIN() {
        Submission s = new Submission(10, "Return", "Leo", "l1", true, "Failed", false, "grader1", "IN");
        assertEquals(Submission.PC_FAIL, s.getCheckResult());
        assertEquals("IN", s.getGrade());
    }

    /**
     * Tests full constructor for a Return submission with Failed check result and F-AIV grade.
     */
    @Test
    public void testFullConstructorReturnFailedAIV() {
        Submission s = new Submission(11, "Return", "Mia", "m1", true, "Failed", false, "grader1", "F-AIV");
        assertEquals("F-AIV", s.getGrade());
    }

    /**
     * Tests full constructor counter update when id is higher than current counter.
     */
    @Test
    public void testFullConstructorUpdatesCounter() {
        new Submission(99, "Upload", "Ned", "n1", false, "Not Done", false, "", "");
        Submission next = new Submission("Olive", "o1");
        assertEquals(100, next.getId());
    }

    // ---------------------------------------------------------------
    // Full constructor invalid cases
    // ---------------------------------------------------------------

    /**
     * Tests full constructor rejects null or invalid state.
     */
    @Test
    public void testFullConstructorInvalidState() {
        assertThrows(IllegalArgumentException.class,
                () -> new Submission(1, null, "A", "a", false, "Not Done", false, "", ""));
        assertThrows(IllegalArgumentException.class,
                () -> new Submission(1, "BadState", "A", "a", false, "Not Done", false, "", ""));
    }

    /**
     * Tests full constructor rejects null or empty name.
     */
    @Test
    public void testFullConstructorInvalidName() {
        assertThrows(IllegalArgumentException.class,
                () -> new Submission(1, "Upload", null, "a1", false, "Not Done", false, "", ""));
        assertThrows(IllegalArgumentException.class,
                () -> new Submission(1, "Upload", "", "a1", false, "Not Done", false, "", ""));
    }

    /**
     * Tests full constructor rejects null or empty unityId.
     */
    @Test
    public void testFullConstructorInvalidUnityId() {
        assertThrows(IllegalArgumentException.class,
                () -> new Submission(1, "Upload", "Name", null, false, "Not Done", false, "", ""));
        assertThrows(IllegalArgumentException.class,
                () -> new Submission(1, "Upload", "Name", "", false, "Not Done", false, "", ""));
    }

    /**
     * Tests full constructor rejects Feedback state with feedbackProcessed=true.
     */
    @Test
    public void testFullConstructorFeedbackAlreadyProcessed() {
        assertThrows(IllegalArgumentException.class,
                () -> new Submission(1, "Feedback", "A", "a1", true, "Not Done", false, "gr", ""));
    }

    /**
     * Tests full constructor rejects Submitted/Grade/Check/Return/Regrade with processed=false.
     */
    @Test
    public void testFullConstructorRequiresProcessed() {
        assertThrows(IllegalArgumentException.class,
                () -> new Submission(1, "Submitted", "A", "a1", false, "Not Done", false, "", ""));
        assertThrows(IllegalArgumentException.class,
                () -> new Submission(1, "Grade", "A", "a1", false, "Failed", false, "gr", ""));
        assertThrows(IllegalArgumentException.class,
                () -> new Submission(1, "Check", "A", "a1", false, "Not Done", false, "gr", "A"));
        assertThrows(IllegalArgumentException.class,
                () -> new Submission(1, "Return", "A", "a1", false, "Passed", false, "gr", "A"));
        assertThrows(IllegalArgumentException.class,
                () -> new Submission(1, "Regrade", "A", "a1", false, "Passed", false, "gr", "A"));
    }

    /**
     * Tests full constructor rejects Upload/Feedback/Submitted/Check with Passed or Failed check result.
     */
    @Test
    public void testFullConstructorInvalidCheckResultForState() {
        assertThrows(IllegalArgumentException.class,
                () -> new Submission(1, "Upload", "A", "a1", false, "Passed", false, "", ""));
        assertThrows(IllegalArgumentException.class,
                () -> new Submission(1, "Upload", "A", "a1", false, "Failed", false, "", ""));
        assertThrows(IllegalArgumentException.class,
                () -> new Submission(1, "Feedback", "A", "a1", false, "Passed", false, "gr", ""));
        assertThrows(IllegalArgumentException.class,
                () -> new Submission(1, "Submitted", "A", "a1", true, "Passed", false, "", ""));
        assertThrows(IllegalArgumentException.class,
                () -> new Submission(1, "Check", "A", "a1", true, "Passed", false, "gr", "A"));
    }

    /**
     * Tests full constructor rejects Regrade with Failed or Not Done check result.
     */
    @Test
    public void testFullConstructorRegradeInvalidCheckResult() {
        assertThrows(IllegalArgumentException.class,
                () -> new Submission(1, "Regrade", "A", "a1", true, "Failed", false, "gr", "A"));
        assertThrows(IllegalArgumentException.class,
                () -> new Submission(1, "Regrade", "A", "a1", true, "Not Done", false, "gr", "A"));
    }

    /**
     * Tests full constructor rejects Return with Not Done check result.
     */
    @Test
    public void testFullConstructorReturnNoDone() {
        assertThrows(IllegalArgumentException.class,
                () -> new Submission(1, "Return", "A", "a1", true, "Not Done", false, "gr", "A"));
    }

    /**
     * Tests full constructor rejects Upload/Submitted with a grader.
     */
    @Test
    public void testFullConstructorNoGraderAllowed() {
        assertThrows(IllegalArgumentException.class,
                () -> new Submission(1, "Upload", "A", "a1", false, "Not Done", false, "grader", ""));
        assertThrows(IllegalArgumentException.class,
                () -> new Submission(1, "Submitted", "A", "a1", true, "Not Done", false, "grader", ""));
    }

    /**
     * Tests full constructor rejects Feedback/Grade/Check/Return/Regrade without a grader.
     */
    @Test
    public void testFullConstructorGraderRequired() {
        assertThrows(IllegalArgumentException.class,
                () -> new Submission(1, "Feedback", "A", "a1", false, "Not Done", false, "", ""));
        assertThrows(IllegalArgumentException.class,
                () -> new Submission(1, "Grade", "A", "a1", true, "Failed", false, "", ""));
        assertThrows(IllegalArgumentException.class,
                () -> new Submission(1, "Check", "A", "a1", true, "Not Done", false, "", "A"));
        assertThrows(IllegalArgumentException.class,
                () -> new Submission(1, "Return", "A", "a1", true, "Passed", false, "", "A"));
        assertThrows(IllegalArgumentException.class,
                () -> new Submission(1, "Regrade", "A", "a1", true, "Passed", false, "", "A"));
    }

    /**
     * Tests full constructor rejects Upload/Feedback/Submitted with a grade.
     */
    @Test
    public void testFullConstructorNoGradeAllowed() {
        assertThrows(IllegalArgumentException.class,
                () -> new Submission(1, "Upload", "A", "a1", false, "Not Done", false, "", "A"));
        assertThrows(IllegalArgumentException.class,
                () -> new Submission(1, "Feedback", "A", "a1", false, "Not Done", false, "gr", "B"));
        assertThrows(IllegalArgumentException.class,
                () -> new Submission(1, "Submitted", "A", "a1", true, "Not Done", false, "", "C"));
    }

    /**
     * Tests full constructor rejects Check/Regrade/Return without a grade.
     */
    @Test
    public void testFullConstructorGradeRequired() {
        assertThrows(IllegalArgumentException.class,
                () -> new Submission(1, "Check", "A", "a1", true, "Not Done", false, "gr", ""));
        assertThrows(IllegalArgumentException.class,
                () -> new Submission(1, "Regrade", "A", "a1", true, "Passed", false, "gr", ""));
        assertThrows(IllegalArgumentException.class,
                () -> new Submission(1, "Return", "A", "a1", true, "Passed", false, "gr", ""));
    }

    /**
     * Tests full constructor rejects Grade/Check state with F-AIV or IN grade.
     */
    @Test
    public void testFullConstructorGradeCheckInvalidGrades() {
        assertThrows(IllegalArgumentException.class,
                () -> new Submission(1, "Check", "A", "a1", true, "Not Done", false, "gr", "F-AIV"));
        assertThrows(IllegalArgumentException.class,
                () -> new Submission(1, "Check", "A", "a1", true, "Not Done", false, "gr", "IN"));
    }

    /**
     * Tests full constructor rejects Grade state with Not Done check result and a grade.
     */
    @Test
    public void testFullConstructorGradeNoDoneCheckWithGrade() {
        assertThrows(IllegalArgumentException.class,
                () -> new Submission(1, "Grade", "A", "a1", true, "Not Done", false, "gr", "A"));
    }

    /**
     * Tests full constructor rejects Return with Failed check result and a letter grade.
     */
    @Test
    public void testFullConstructorReturnFailedWithLetterGrade() {
        assertThrows(IllegalArgumentException.class,
                () -> new Submission(1, "Return", "A", "a1", true, "Failed", false, "gr", "A"));
        assertThrows(IllegalArgumentException.class,
                () -> new Submission(1, "Return", "A", "a1", true, "Failed", false, "gr", "B"));
        assertThrows(IllegalArgumentException.class,
                () -> new Submission(1, "Return", "A", "a1", true, "Failed", false, "gr", "C"));
        assertThrows(IllegalArgumentException.class,
                () -> new Submission(1, "Return", "A", "a1", true, "Failed", false, "gr", "F"));
    }

    /**
     * Tests full constructor rejects Return with Passed check result and F-AIV or IN grade.
     */
    @Test
    public void testFullConstructorReturnPassedWithBadGrade() {
        assertThrows(IllegalArgumentException.class,
                () -> new Submission(1, "Return", "A", "a1", true, "Passed", false, "gr", "F-AIV"));
        assertThrows(IllegalArgumentException.class,
                () -> new Submission(1, "Return", "A", "a1", true, "Passed", false, "gr", "IN"));
    }

    /**
     * Tests full constructor rejects published submission outside Return state.
     */
    @Test
    public void testFullConstructorPublishedNotReturn() {
        assertThrows(IllegalArgumentException.class,
                () -> new Submission(1, "Grade", "A", "a1", true, "Failed", true, "gr", ""));
    }

    /**
     * Tests full constructor rejects published submission with IN grade.
     */
    @Test
    public void testFullConstructorPublishedWithIN() {
        assertThrows(IllegalArgumentException.class,
                () -> new Submission(1, "Return", "A", "a1", true, "Failed", true, "gr", "IN"));
    }

    /**
     * Tests full constructor rejects invalid check result string.
     */
    @Test
    public void testFullConstructorBadCheckResult() {
        assertThrows(IllegalArgumentException.class,
                () -> new Submission(1, "Upload", "A", "a1", false, "Maybe", false, "", ""));
    }

    /**
     * Tests full constructor rejects invalid grade string.
     */
    @Test
    public void testFullConstructorBadGrade() {
        assertThrows(IllegalArgumentException.class,
                () -> new Submission(1, "Check", "A", "a1", true, "Not Done", false, "gr", "Z"));
    }

    // ---------------------------------------------------------------
    // toString
    // ---------------------------------------------------------------

    /**
     * Tests toString for a fresh Upload submission.
     */
    @Test
    public void testToStringUpload() {
        Submission s = new Submission("Eva", "e1");
        assertEquals("* 0,Upload,Eva,e1,false,Not Done,false,,", s.toString());
    }

    /**
     * Tests toString for a Return submission with all fields populated.
     */
    @Test
    public void testToStringReturn() {
        Submission s = new Submission(3, "Return", "Frank", "f1", true, "Passed", true, "grader1", "A");
        assertEquals("* 3,Return,Frank,f1,true,Passed,true,grader1,A", s.toString());
    }

    // ---------------------------------------------------------------
    // Upload state transitions
    // ---------------------------------------------------------------

    /**
     * Tests ASSIGN command in Upload state moves to Feedback.
     */
    @Test
    public void testUploadAssign() {
        Submission s = new Submission("Alice", "a1");
        s.update(new Command(CommandValue.ASSIGN, "grader1"));
        assertEquals(Submission.FEEDBACK_NAME, s.getState());
        assertEquals("grader1", s.getGrader());
    }

    /**
     * Tests ASSIGN command in Upload state fails if already processed (feedback given once).
     */
    @Test
    public void testUploadAssignAlreadyProcessed() {
        Submission s = new Submission("Alice", "a1");
        s.update(new Command(CommandValue.ASSIGN, "grader1"));
        s.update(new Command(CommandValue.PROVIDE, null));
        assertThrows(UnsupportedOperationException.class,
                () -> s.update(new Command(CommandValue.ASSIGN, "grader2")));
    }

    /**
     * Tests REUPLOAD command in Upload state when processed is true stays in Upload.
     */
    @Test
    public void testUploadReupload() {
        Submission s = new Submission("Alice", "a1");
        s.update(new Command(CommandValue.ASSIGN, "grader1"));
        s.update(new Command(CommandValue.PROVIDE, null));
        s.update(new Command(CommandValue.REUPLOAD, null));
        assertEquals(Submission.UPLOAD_NAME, s.getState());
    }

    /**
     * Tests REUPLOAD command in Upload state fails if not yet processed.
     */
    @Test
    public void testUploadReuploadNotProcessed() {
        Submission s = new Submission("Alice", "a1");
        assertThrows(UnsupportedOperationException.class,
                () -> s.update(new Command(CommandValue.REUPLOAD, null)));
    }

    /**
     * Tests MOVE command in Upload state moves to Submitted after feedback processed.
     */
    @Test
    public void testUploadMove() {
        Submission s = new Submission("Alice", "a1");
        s.update(new Command(CommandValue.ASSIGN, "grader1"));
        s.update(new Command(CommandValue.PROVIDE, null));
        s.update(new Command(CommandValue.MOVE, null));
        assertEquals(Submission.SUBMITTED_NAME, s.getState());
    }

    /**
     * Tests MOVE command in Upload state fails if not yet processed.
     */
    @Test
    public void testUploadMoveNotProcessed() {
        Submission s = new Submission("Alice", "a1");
        assertThrows(UnsupportedOperationException.class,
                () -> s.update(new Command(CommandValue.MOVE, null)));
    }

    /**
     * Tests invalid command in Upload state throws UnsupportedOperationException.
     */
    @Test
    public void testUploadInvalidCommand() {
        Submission s = new Submission("Alice", "a1");
        assertThrows(UnsupportedOperationException.class,
                () -> s.update(new Command(CommandValue.PASS, null)));
    }

    // ---------------------------------------------------------------
    // Feedback state transitions
    // ---------------------------------------------------------------

    /**
     * Tests PROVIDE command in Feedback state moves back to Upload and sets processed=true.
     */
    @Test
    public void testFeedbackProvide() {
        Submission s = new Submission("Bob", "b1");
        s.update(new Command(CommandValue.ASSIGN, "grader1"));
        assertEquals(Submission.FEEDBACK_NAME, s.getState());
        s.update(new Command(CommandValue.PROVIDE, null));
        assertEquals(Submission.UPLOAD_NAME, s.getState());
        assertTrue(s.isFeedbackProcessed());
    }

    /**
     * Tests invalid command in Feedback state throws UnsupportedOperationException.
     */
    @Test
    public void testFeedbackInvalidCommand() {
        Submission s = new Submission("Bob", "b1");
        s.update(new Command(CommandValue.ASSIGN, "grader1"));
        assertThrows(UnsupportedOperationException.class,
                () -> s.update(new Command(CommandValue.MOVE, null)));
    }

    // ---------------------------------------------------------------
    // Submitted state transitions
    // ---------------------------------------------------------------

    /**
     * Tests ASSIGN command in Submitted state moves to Grade.
     */
    @Test
    public void testSubmittedAssign() {
        Submission s = new Submission("Carol", "c1");
        s.update(new Command(CommandValue.ASSIGN, "grader1"));
        s.update(new Command(CommandValue.PROVIDE, null));
        s.update(new Command(CommandValue.MOVE, null));
        assertEquals(Submission.SUBMITTED_NAME, s.getState());
        s.update(new Command(CommandValue.ASSIGN, "grader2"));
        assertEquals(Submission.GRADE_NAME, s.getState());
        assertEquals("grader2", s.getGrader());
    }

    /**
     * Tests invalid command in Submitted state throws UnsupportedOperationException.
     */
    @Test
    public void testSubmittedInvalidCommand() {
        Submission s = new Submission("Carol", "c1");
        s.update(new Command(CommandValue.ASSIGN, "grader1"));
        s.update(new Command(CommandValue.PROVIDE, null));
        s.update(new Command(CommandValue.MOVE, null));
        assertThrows(UnsupportedOperationException.class,
                () -> s.update(new Command(CommandValue.PASS, null)));
    }

    // ---------------------------------------------------------------
    // Grade state transitions
    // ---------------------------------------------------------------

    /**
     * Helper that brings a submission to Grade state.
     * @return the submission in the Grade state.
     */
    private Submission getToGradeState() {
        Submission s = new Submission("Dan", "d1");
        s.update(new Command(CommandValue.ASSIGN, "grader1"));
        s.update(new Command(CommandValue.PROVIDE, null));
        s.update(new Command(CommandValue.MOVE, null));
        s.update(new Command(CommandValue.ASSIGN, "grader1"));
        return s;
    }

    /**
     * Tests ANALYZE command in Grade state moves to Check with the given grade.
     */
    @Test
    public void testGradeAnalyze() {
        Submission s = getToGradeState();
        s.update(new Command(CommandValue.ANALYZE, "B"));
        assertEquals(Submission.CHECK_NAME, s.getState());
        assertEquals("B", s.getGrade());
    }

    /**
     * Tests ANALYZE command in Grade state fails if already analyzed (checkResult not None).
     */
    @Test
    public void testGradeAnalyzeAlreadyAnalyzed() {
        Submission s = getToGradeState();
        s.update(new Command(CommandValue.ANALYZE, "A"));
        s.update(new Command(CommandValue.FAIL, null));
        assertThrows(UnsupportedOperationException.class,
                () -> s.update(new Command(CommandValue.ANALYZE, "B")));
    }

    /**
     * Tests SUBMIT command in Grade state with Not Done check result throws.
     */
    @Test
    public void testGradeSubmitNoDone() {
        Submission s = getToGradeState();
        assertThrows(UnsupportedOperationException.class,
                () -> s.update(new Command(CommandValue.SUBMIT, "A")));
    }

    /**
     * Tests SUBMIT in Grade state after failed check with letter grade resets checkResult to Passed.
     */
    @Test
    public void testGradeSubmitAfterFailWithLetterGrade() {
        Submission s = getToGradeState();
        s.update(new Command(CommandValue.ANALYZE, "A"));
        s.update(new Command(CommandValue.FAIL, null));
        s.update(new Command(CommandValue.SUBMIT, "B"));
        assertEquals(Submission.RETURN_NAME, s.getState());
        assertEquals("B", s.getGrade());
        assertEquals(Submission.PC_PASS, s.getCheckResult());
    }

    /**
     * Tests SUBMIT in Grade state after failed check with IN grade stays Failed.
     */
    @Test
    public void testGradeSubmitAfterFailWithIN() {
        Submission s = getToGradeState();
        s.update(new Command(CommandValue.ANALYZE, "A"));
        s.update(new Command(CommandValue.FAIL, null));
        s.update(new Command(CommandValue.SUBMIT, "IN"));
        assertEquals(Submission.RETURN_NAME, s.getState());
        assertEquals("IN", s.getGrade());
        assertEquals(Submission.PC_FAIL, s.getCheckResult());
    }

    /**
     * Tests SUBMIT in Grade state after failed check with F-AIV grade stays Failed.
     */
    @Test
    public void testGradeSubmitAfterFailWithFAIV() {
        Submission s = getToGradeState();
        s.update(new Command(CommandValue.ANALYZE, "C"));
        s.update(new Command(CommandValue.FAIL, null));
        s.update(new Command(CommandValue.SUBMIT, "F-AIV"));
        assertEquals(Submission.RETURN_NAME, s.getState());
        assertEquals("F-AIV", s.getGrade());
        assertEquals(Submission.PC_FAIL, s.getCheckResult());
    }

    /**
     * Tests SUBMIT in Grade state after regrade (Passed checkResult) with letter grade succeeds.
     */
    @Test
    public void testGradeSubmitAfterRegradeWithLetterGrade() {
        Submission s = getToGradeState();
        s.update(new Command(CommandValue.ANALYZE, "A"));
        s.update(new Command(CommandValue.PASS, null));
        s.update(new Command(CommandValue.REQUEST, null));
        s.update(new Command(CommandValue.PROCESS, null));
        s.update(new Command(CommandValue.SUBMIT, "C"));
        assertEquals(Submission.RETURN_NAME, s.getState());
        assertEquals("C", s.getGrade());
    }

    /**
     * Tests SUBMIT in Grade state after Passed check with non-letter grade throws.
     */
    @Test
    public void testGradeSubmitPassedWithNonLetter() {
        Submission s = getToGradeState();
        s.update(new Command(CommandValue.ANALYZE, "A"));
        s.update(new Command(CommandValue.PASS, null));
        s.update(new Command(CommandValue.REQUEST, null));
        s.update(new Command(CommandValue.PROCESS, null));
        assertThrows(UnsupportedOperationException.class,
                () -> s.update(new Command(CommandValue.SUBMIT, "IN")));
    }

    /**
     * Tests invalid command in Grade state throws UnsupportedOperationException.
     */
    @Test
    public void testGradeInvalidCommand() {
        Submission s = getToGradeState();
        assertThrows(UnsupportedOperationException.class,
                () -> s.update(new Command(CommandValue.PASS, null)));
    }

    // ---------------------------------------------------------------
    // Check state transitions
    // ---------------------------------------------------------------

    /**
     * Helper that brings a submission to Check state with grade A.
     * @return the submission in the Check state.
     */
    private Submission getToCheckState() {
        Submission s = getToGradeState();
        s.update(new Command(CommandValue.ANALYZE, "A"));
        return s;
    }

    /**
     * Tests PASS command in Check state moves to Return with Passed checkResult.
     */
    @Test
    public void testCheckPass() {
        Submission s = getToCheckState();
        s.update(new Command(CommandValue.PASS, null));
        assertEquals(Submission.RETURN_NAME, s.getState());
        assertEquals(Submission.PC_PASS, s.getCheckResult());
    }

    /**
     * Tests FAIL command in Check state moves to Grade with Failed checkResult.
     */
    @Test
    public void testCheckFail() {
        Submission s = getToCheckState();
        s.update(new Command(CommandValue.FAIL, null));
        assertEquals(Submission.GRADE_NAME, s.getState());
        assertEquals(Submission.PC_FAIL, s.getCheckResult());
    }

    /**
     * Tests invalid command in Check state throws UnsupportedOperationException.
     */
    @Test
    public void testCheckInvalidCommand() {
        Submission s = getToCheckState();
        assertThrows(UnsupportedOperationException.class,
                () -> s.update(new Command(CommandValue.MOVE, null)));
    }

    // ---------------------------------------------------------------
    // Return state transitions
    // ---------------------------------------------------------------

    /**
     * Helper that brings a submission to Return state with grade A and Passed checkResult.
     * @return the submission in the Return state.
     */
    private Submission getToReturnState() {
        Submission s = getToCheckState();
        s.update(new Command(CommandValue.PASS, null));
        return s;
    }

    /**
     * Tests REQUEST command in Return state with letter grade moves to Regrade.
     */
    @Test
    public void testReturnRequest() {
        Submission s = getToReturnState();
        s.update(new Command(CommandValue.REQUEST, null));
        assertEquals(Submission.REGRADE_NAME, s.getState());
    }

    /**
     * Tests REQUEST command in Return state with IN grade throws.
     */
    @Test
    public void testReturnRequestWithIN() {
        Submission s = getToGradeState();
        s.update(new Command(CommandValue.ANALYZE, "A"));
        s.update(new Command(CommandValue.FAIL, null));
        s.update(new Command(CommandValue.SUBMIT, "IN"));
        assertThrows(UnsupportedOperationException.class,
                () -> s.update(new Command(CommandValue.REQUEST, null)));
    }

    /**
     * Tests REQUEST command in Return state with F-AIV grade throws.
     */
    @Test
    public void testReturnRequestWithFAIV() {
        Submission s = getToGradeState();
        s.update(new Command(CommandValue.ANALYZE, "A"));
        s.update(new Command(CommandValue.FAIL, null));
        s.update(new Command(CommandValue.SUBMIT, "F-AIV"));
        assertThrows(UnsupportedOperationException.class,
                () -> s.update(new Command(CommandValue.REQUEST, null)));
    }

    /**
     * Tests RESOLVE command changes IN grade to a letter grade and resets checkResult to Passed.
     */
    @Test
    public void testReturnResolveToLetterGrade() {
        Submission s = getToGradeState();
        s.update(new Command(CommandValue.ANALYZE, "A"));
        s.update(new Command(CommandValue.FAIL, null));
        s.update(new Command(CommandValue.SUBMIT, "IN"));
        s.update(new Command(CommandValue.RESOLVE, "B"));
        assertEquals(Submission.RETURN_NAME, s.getState());
        assertEquals("B", s.getGrade());
        assertEquals(Submission.PC_PASS, s.getCheckResult());
    }

    /**
     * Tests RESOLVE command changes IN grade to F-AIV (checkResult stays Failed).
     */
    @Test
    public void testReturnResolveToFAIV() {
        Submission s = getToGradeState();
        s.update(new Command(CommandValue.ANALYZE, "A"));
        s.update(new Command(CommandValue.FAIL, null));
        s.update(new Command(CommandValue.SUBMIT, "IN"));
        s.update(new Command(CommandValue.RESOLVE, "F-AIV"));
        assertEquals("F-AIV", s.getGrade());
        assertEquals(Submission.PC_FAIL, s.getCheckResult());
    }

    /**
     * Tests RESOLVE command on a non-IN grade throws.
     */
    @Test
    public void testReturnResolveNonIN() {
        Submission s = getToReturnState();
        assertThrows(UnsupportedOperationException.class,
                () -> s.update(new Command(CommandValue.RESOLVE, "B")));
    }

    /**
     * Tests PUBLISH command sets published=true on a valid submission.
     */
    @Test
    public void testReturnPublish() {
        Submission s = getToReturnState();
        s.update(new Command(CommandValue.PUBLISH, null));
        assertEquals(Submission.RETURN_NAME, s.getState());
        assertTrue(s.isPublished());
    }

    /**
     * Tests PUBLISH command on already-published submission throws.
     */
    @Test
    public void testReturnPublishAlreadyPublished() {
        Submission s = getToReturnState();
        s.update(new Command(CommandValue.PUBLISH, null));
        assertThrows(UnsupportedOperationException.class,
                () -> s.update(new Command(CommandValue.PUBLISH, null)));
    }

    /**
     * Tests PUBLISH command on submission with IN grade throws.
     */
    @Test
    public void testReturnPublishWithIN() {
        Submission s = getToGradeState();
        s.update(new Command(CommandValue.ANALYZE, "A"));
        s.update(new Command(CommandValue.FAIL, null));
        s.update(new Command(CommandValue.SUBMIT, "IN"));
        assertThrows(UnsupportedOperationException.class,
                () -> s.update(new Command(CommandValue.PUBLISH, null)));
    }

    /**
     * Tests invalid command in Return state throws UnsupportedOperationException.
     */
    @Test
    public void testReturnInvalidCommand() {
        Submission s = getToReturnState();
        assertThrows(UnsupportedOperationException.class,
                () -> s.update(new Command(CommandValue.MOVE, null)));
    }

    // ---------------------------------------------------------------
    // Regrade state transitions
    // ---------------------------------------------------------------

    /**
     * Helper that brings a submission to Regrade state.
     * @return the submission in the Regrade state.
     */
    private Submission getToRegradeState() {
        Submission s = getToReturnState();
        s.update(new Command(CommandValue.REQUEST, null));
        return s;
    }

    /**
     * Tests PROCESS command in Regrade state moves back to Grade.
     */
    @Test
    public void testRegradeProcess() {
        Submission s = getToRegradeState();
        s.update(new Command(CommandValue.PROCESS, null));
        assertEquals(Submission.GRADE_NAME, s.getState());
    }

    /**
     * Tests invalid command in Regrade state throws UnsupportedOperationException.
     */
    @Test
    public void testRegradeInvalidCommand() {
        Submission s = getToRegradeState();
        assertThrows(UnsupportedOperationException.class,
                () -> s.update(new Command(CommandValue.PASS, null)));
    }

    // ---------------------------------------------------------------
    // Full cycle tests
    // ---------------------------------------------------------------

    /**
     * Tests a complete cycle from Upload through Return via the Check path.
     */
    @Test
    public void testFullCycleViaCheck() {
        Submission s = new Submission("Alice Smith", "asmith");
        assertEquals(Submission.UPLOAD_NAME, s.getState());

        s.update(new Command(CommandValue.ASSIGN, "grader1"));
        assertEquals(Submission.FEEDBACK_NAME, s.getState());
        assertEquals("grader1", s.getGrader());

        s.update(new Command(CommandValue.PROVIDE, null));
        assertEquals(Submission.UPLOAD_NAME, s.getState());
        assertTrue(s.isFeedbackProcessed());

        s.update(new Command(CommandValue.REUPLOAD, null));
        assertEquals(Submission.UPLOAD_NAME, s.getState());

        s.update(new Command(CommandValue.MOVE, null));
        assertEquals(Submission.SUBMITTED_NAME, s.getState());

        s.update(new Command(CommandValue.ASSIGN, "grader1"));
        assertEquals(Submission.GRADE_NAME, s.getState());

        s.update(new Command(CommandValue.ANALYZE, "B"));
        assertEquals(Submission.CHECK_NAME, s.getState());
        assertEquals("B", s.getGrade());

        s.update(new Command(CommandValue.PASS, null));
        assertEquals(Submission.RETURN_NAME, s.getState());
        assertEquals(Submission.PC_PASS, s.getCheckResult());

        s.update(new Command(CommandValue.REQUEST, null));
        assertEquals(Submission.REGRADE_NAME, s.getState());

        s.update(new Command(CommandValue.PROCESS, null));
        assertEquals(Submission.GRADE_NAME, s.getState());

        s.update(new Command(CommandValue.SUBMIT, "A"));
        assertEquals(Submission.RETURN_NAME, s.getState());
        assertEquals("A", s.getGrade());

        s.update(new Command(CommandValue.PUBLISH, null));
        assertEquals(Submission.RETURN_NAME, s.getState());
        assertTrue(s.isPublished());
    }

    /**
     * Tests a complete cycle where submission fails plagiarism check and receives IN grade.
     */
    @Test
    public void testFullCycleFailedCheckIN() {
        Submission s = new Submission("Bob", "b1");
        s.update(new Command(CommandValue.ASSIGN, "grader1"));
        s.update(new Command(CommandValue.PROVIDE, null));
        s.update(new Command(CommandValue.MOVE, null));
        s.update(new Command(CommandValue.ASSIGN, "grader1"));
        s.update(new Command(CommandValue.ANALYZE, "C"));
        assertEquals(Submission.CHECK_NAME, s.getState());
        s.update(new Command(CommandValue.FAIL, null));
        assertEquals(Submission.GRADE_NAME, s.getState());
        assertEquals(Submission.PC_FAIL, s.getCheckResult());
        s.update(new Command(CommandValue.SUBMIT, "IN"));
        assertEquals(Submission.RETURN_NAME, s.getState());
        assertEquals("IN", s.getGrade());
        s.update(new Command(CommandValue.RESOLVE, "A"));
        assertEquals("A", s.getGrade());
        assertEquals(Submission.PC_PASS, s.getCheckResult());
        s.update(new Command(CommandValue.PUBLISH, null));
        assertTrue(s.isPublished());
    }

    /**
     * Tests all grade constants have the expected string values.
     */
    @Test
    public void testGradeConstants() {
        assertEquals("A", Submission.GRADE_A_RETURN);
        assertEquals("B", Submission.GRADE_B_RETURN);
        assertEquals("C", Submission.GRADE_C_RETURN);
        assertEquals("F", Submission.GRADE_F_RETURN);
        assertEquals("IN", Submission.GRADE_IN_RETURN);
        assertEquals("F-AIV", Submission.GRADE_F_AIV_RETURN);
    }

    /**
     * Tests all state name constants have the expected string values.
     */
    @Test
    public void testStateNameConstants() {
        assertEquals("Upload", Submission.UPLOAD_NAME);
        assertEquals("Feedback", Submission.FEEDBACK_NAME);
        assertEquals("Submitted", Submission.SUBMITTED_NAME);
        assertEquals("Grade", Submission.GRADE_NAME);
        assertEquals("Check", Submission.CHECK_NAME);
        assertEquals("Regrade", Submission.REGRADE_NAME);
        assertEquals("Return", Submission.RETURN_NAME);
    }

    /**
     * Tests all plagiarism check result constants have the expected string values.
     */
    @Test
    public void testCheckResultConstants() {
        assertEquals("Passed", Submission.PC_PASS);
        assertEquals("Failed", Submission.PC_FAIL);
        assertEquals("Not Done", Submission.PC_NONE);
    }
}