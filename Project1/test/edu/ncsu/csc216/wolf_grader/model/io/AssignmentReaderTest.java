package edu.ncsu.csc216.wolf_grader.model.io;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import edu.ncsu.csc216.wolf_grader.model.manager.Assignment;
import edu.ncsu.csc216.wolf_grader.model.submission.Submission;

/**
 * Provides tests for the AssignmentReader class, which parses files into objects.
 * @author Ajaunie White
 */
public class AssignmentReaderTest {

    /**
     * Resets the submission counter before each test.
     */
    @BeforeEach
    public void setUp() {
        Submission.setCounter(0);
    }

    /**
     * Helper method to create a temporary test file with the given content.
     * @param text the text to write into the file.
     * @return the absolute path of the created file.
     */
    private String createTempFile(String text) {
        try {
            File f = File.createTempFile("reader_test", ".txt");
            FileWriter fw = new FileWriter(f);
            fw.write(text);
            fw.close();
            return f.getAbsolutePath();
        } catch (IOException e) {
            fail("File setup failed.");
            return null;
        }
    }

    /**
     * Checks reading a valid file with two assignments each having one valid submission.
     * Submitted state requires processed=true, Not Done checkResult, no grader, no grade.
     * Upload state requires processed=false, Not Done checkResult, no grader, no grade.
     */
    @Test
    public void testReadValidFile() {
        String content = "# Lab 2,Lab,50\n"
                + "* 0,Upload,Bob,b1,false,Not Done,false,,\n"
                + "# Project 1,Project,100\n"
                + "* 1,Submitted,Alice,a1,true,Not Done,false,,\n";

        String path = createTempFile(content);
        ArrayList<Assignment> list = AssignmentReader.readAssignmentFile(path);

        assertEquals(2, list.size());
        assertEquals("Lab 2", list.get(0).getAssignmentName());
        assertEquals(1, list.get(0).getSubmissions().size());
        assertEquals("Project 1", list.get(1).getAssignmentName());
        assertEquals(1, list.get(1).getSubmissions().size());
    }

    /**
     * Checks that the reader correctly parses all fields of a valid submission.
     */
    @Test
    public void testReadSubmissionFields() {
        String content = "# Homework 1,Homework,20\n"
                + "* 3,Return,Kathleen Parker,kparker,true,Passed,true,sallen,A\n";

        String path = createTempFile(content);
        ArrayList<Assignment> list = AssignmentReader.readAssignmentFile(path);

        assertEquals(1, list.size());
        Submission s = list.get(0).getSubmissions().get(0);
        assertEquals(3, s.getId());
        assertEquals("Return", s.getState());
        assertEquals("Kathleen Parker", s.getName());
        assertEquals("kparker", s.getUnityId());
        assertTrue(s.isFeedbackProcessed());
        assertEquals("Passed", s.getCheckResult());
        assertTrue(s.isPublished());
        assertEquals("sallen", s.getGrader());
        assertEquals("A", s.getGrade());
    }

    /**
     * Checks reading a file with multiple assignments and multiple submissions.
     */
    @Test
    public void testReadMultipleAssignmentsAndSubmissions() {
        String content = "# Assignment A,Project,100\n"
                + "* 1,Upload,Bob,b1,false,Not Done,false,,\n"
                + "* 2,Submitted,Carol,c1,true,Not Done,false,,\n"
                + "# Assignment B,Lab,50\n"
                + "* 5,Return,Dave,d1,true,Passed,false,grader1,B\n";

        String path = createTempFile(content);
        ArrayList<Assignment> list = AssignmentReader.readAssignmentFile(path);

        assertEquals(2, list.size());
        assertEquals(2, list.get(0).getSubmissions().size());
        assertEquals(1, list.get(1).getSubmissions().size());
    }

    /**
     * Checks that invalid submissions within a valid assignment are skipped.
     */
    @Test
    public void testSkipsInvalidSubmissions() {
        String content = "# Course Work,Project,75\n"
                + "* 1,Upload,Good,g1,false,Not Done,false,,\n"
                + "* 2,BadState,Bad,b1,false,Not Done,false,,\n";

        String path = createTempFile(content);
        ArrayList<Assignment> list = AssignmentReader.readAssignmentFile(path);

        assertEquals(1, list.size());
        assertEquals(1, list.get(0).getSubmissions().size());
    }

    /**
     * Checks that a duplicate submission id keeps only the first occurrence.
     */
    @Test
    public void testDuplicateIdKeepsFirst() {
        String content = "# Test,Homework,10\n"
                + "* 3,Upload,First,f1,false,Not Done,false,,\n"
                + "* 3,Upload,Second,s1,false,Not Done,false,,\n";

        String path = createTempFile(content);
        ArrayList<Assignment> list = AssignmentReader.readAssignmentFile(path);

        assertEquals(1, list.size());
        assertEquals(1, list.get(0).getSubmissions().size());
        assertEquals("First", list.get(0).getSubmissions().get(0).getName());
    }

    /**
     * Checks that an assignment with no valid submissions is dropped entirely.
     */
    @Test
    public void testAssignmentWithNoValidSubmissionsIsDropped() {
        String content = "# Empty,Lab,10\n"
                + "* 1,BadState,Someone,s1,false,Not Done,false,,\n";

        String path = createTempFile(content);
        ArrayList<Assignment> list = AssignmentReader.readAssignmentFile(path);

        assertEquals(0, list.size());
    }

    /**
     * Checks that a file not starting with '#' returns an empty list.
     */
    @Test
    public void testFileNotStartingWithHash() {
        String content = "Not a valid file\n# Assignment,Lab,10\n* 1,Upload,A,a1,false,Not Done,false,,\n";
        String path = createTempFile(content);
        ArrayList<Assignment> list = AssignmentReader.readAssignmentFile(path);
        assertEquals(0, list.size());
    }

    /**
     * Checks reader error handling for a file that does not exist.
     */
    @Test
    public void testMissingFile() {
        assertThrows(IllegalArgumentException.class,
                () -> AssignmentReader.readAssignmentFile("nonexistent_file_xyz.txt"));
    }

    /**
     * Checks that a submission line with fewer than 9 fields is skipped.
     */
    @Test
    public void testSubmissionLineTooFewFields() {
        String content = "# Test,Lab,10\n"
                + "* 1,Upload,Name\n"
                + "* 2,Upload,Valid,v1,false,Not Done,false,,\n";

        String path = createTempFile(content);
        ArrayList<Assignment> list = AssignmentReader.readAssignmentFile(path);

        assertEquals(1, list.size());
        assertEquals(1, list.get(0).getSubmissions().size());
    }

    /**
     * Checks that an assignment with invalid max points is skipped.
     */
    @Test
    public void testInvalidMaxPoints() {
        String content = "# Bad,Lab,0\n"
                + "* 1,Upload,A,a1,false,Not Done,false,,\n"
                + "# Good,Lab,10\n"
                + "* 2,Upload,B,b1,false,Not Done,false,,\n";

        String path = createTempFile(content);
        ArrayList<Assignment> list = AssignmentReader.readAssignmentFile(path);

        assertEquals(1, list.size());
        assertEquals("Good", list.get(0).getAssignmentName());
    }

    /**
     * Checks that the reader correctly handles a Feedback state submission.
     */
    @Test
    public void testReadFeedbackSubmission() {
        String content = "# Project,Project,100\n"
                + "* 4,Feedback,Sara,smendez1,false,Not Done,false,skumar,\n";

        String path = createTempFile(content);
        ArrayList<Assignment> list = AssignmentReader.readAssignmentFile(path);

        assertEquals(1, list.size());
        Submission s = list.get(0).getSubmissions().get(0);
        assertEquals("Feedback", s.getState());
        assertEquals("skumar", s.getGrader());
    }

    /**
     * Checks that the reader correctly handles a Grade state submission with Failed check result.
     */
    @Test
    public void testReadGradeSubmissionFailed() {
        String content = "# Project,Project,100\n"
                + "* 8,Grade,Daniel,dwright,true,Failed,false,lpatel,\n";

        String path = createTempFile(content);
        ArrayList<Assignment> list = AssignmentReader.readAssignmentFile(path);

        assertEquals(1, list.size());
        Submission s = list.get(0).getSubmissions().get(0);
        assertEquals("Grade", s.getState());
        assertEquals("Failed", s.getCheckResult());
    }

    /**
     * Checks reading a file that matches the example from the project spec.
     */
    @Test
    public void testReadSpecExampleFile() {
        String content = "# Problem Set 1,Problem Set,50\n"
                + "* 3,Return,Kathleen Parker,kparker,true,Passed,true,sallen,A\n"
                + "* 14,Upload,Oliver Grant,ogrant,false,Not Done,false,,\n"
                + "* 4,Feedback,Sara Mendez,smendez1,false,Not Done,false,skumar,\n"
                + "* 2,Submitted,Carol Smith,csmith,true,Not Done,false,,\n"
                + "* 8,Grade,Daniel Wright,dwright,true,Failed,false,lpatel,\n";

        String path = createTempFile(content);
        ArrayList<Assignment> list = AssignmentReader.readAssignmentFile(path);

        assertEquals(1, list.size());
        assertEquals("Problem Set 1", list.get(0).getAssignmentName());
        assertEquals(5, list.get(0).getSubmissions().size());
    }
}