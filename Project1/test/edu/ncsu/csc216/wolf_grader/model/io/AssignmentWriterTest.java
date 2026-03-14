package edu.ncsu.csc216.wolf_grader.model.io;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Scanner;
import edu.ncsu.csc216.wolf_grader.model.manager.Assignment;
import edu.ncsu.csc216.wolf_grader.model.submission.Submission;

/**
 * Provides tests for the system's ability to save assignment data back to a file.
 * @author Ajaunie White
 */
public class AssignmentWriterTest {

    /**
     * Resets the submission counter before each test.
     */
    @BeforeEach
    public void setUp() {
        Submission.setCounter(0);
    }

    /**
     * Checks that a valid file is written without throwing an exception.
     * Max points must be between 1 and 100.
     */
    @Test
    public void testWriteFile() {
        ArrayList<Assignment> assignments = new ArrayList<Assignment>();
        assignments.add(new Assignment("Final Project", "Project", 100));

        try {
            AssignmentWriter.writeAssignmentsToFile("test_save_output.txt", assignments);
        } catch (Exception e) {
            fail("Should not throw an exception.");
        }
    }

    /**
     * Checks that the written file contains the correct assignment header line.
     */
    @Test
    public void testWriteFileContents() {
        ArrayList<Assignment> assignments = new ArrayList<Assignment>();
        Assignment a = new Assignment("Homework 1", "Homework", 20);
        a.addSubmission("Alice", "a1");
        assignments.add(a);

        String fileName = "test_writer_contents.txt";
        AssignmentWriter.writeAssignmentsToFile(fileName, assignments);

        try {
            Scanner scanner = new Scanner(new FileInputStream(fileName));
            String firstLine = scanner.nextLine();
            scanner.close();
            assertEquals("# Homework 1,Homework,20", firstLine);
        } catch (Exception e) {
            fail("Could not read output file: " + e.getMessage());
        }
    }

    /**
     * Checks that writing multiple assignments produces multiple header lines.
     */
    @Test
    public void testWriteMultipleAssignments() {
        ArrayList<Assignment> assignments = new ArrayList<Assignment>();
        Assignment a1 = new Assignment("Lab 1", "Lab", 10);
        a1.addSubmission("Bob", "b1");
        Assignment a2 = new Assignment("Lab 2", "Lab", 10);
        a2.addSubmission("Carol", "c1");
        assignments.add(a1);
        assignments.add(a2);

        String fileName = "test_writer_multi.txt";
        AssignmentWriter.writeAssignmentsToFile(fileName, assignments);

        try {
            Scanner scanner = new Scanner(new FileInputStream(fileName));
            String content = "";
            while (scanner.hasNextLine()) {
                content = content + scanner.nextLine() + "\n";
            }
            scanner.close();
            assertTrue(content.contains("# Lab 1,Lab,10"));
            assertTrue(content.contains("# Lab 2,Lab,10"));
        } catch (Exception e) {
            fail("Could not read output file: " + e.getMessage());
        }
    }

    /**
     * Checks that writing to an invalid file path throws IllegalArgumentException.
     */
    @Test
    public void testWriteInvalidPath() {
        ArrayList<Assignment> assignments = new ArrayList<Assignment>();
        assignments.add(new Assignment("Test", "Lab", 10));

        assertThrows(IllegalArgumentException.class,
                () -> AssignmentWriter.writeAssignmentsToFile(
                        "/nonexistent_directory/bad_path/file.txt", assignments));
    }

    /**
     * Checks that the written file can be read back by AssignmentReader.
     */
    @Test
    public void testWriteThenRead() {
        ArrayList<Assignment> assignments = new ArrayList<Assignment>();
        Assignment a = new Assignment("Round Trip", "Project", 50);
        a.addSubmission("Dan", "d1");
        assignments.add(a);

        String fileName = "test_round_trip.txt";
        AssignmentWriter.writeAssignmentsToFile(fileName, assignments);

        Submission.setCounter(0);
        ArrayList<Assignment> read = AssignmentReader.readAssignmentFile(fileName);
        assertEquals(1, read.size());
        assertEquals("Round Trip", read.get(0).getAssignmentName());
        assertEquals(1, read.get(0).getSubmissions().size());

        new File(fileName).delete();
    }
}