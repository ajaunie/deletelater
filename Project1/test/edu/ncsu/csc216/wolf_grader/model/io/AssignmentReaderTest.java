package edu.ncsu.csc216.wolf_grader.model.io;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import edu.ncsu.csc216.wolf_grader.model.manager.Assignment;

/**
 * Provides tests for the AssignmentReader class, which parses files into objects.
 * @author Ajaunie White
 */
public class AssignmentReaderTest {

	/**
     * Helper method to create a temporary test file.
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
     * Checks reading a valid file with multiple student records.
     */
    @Test
    public void testReadValidFile() {
        String content = "# Lab 2,Lab,50\n" +
                         "* 0,Upload,Bob,b1,false,Not Done,false,,\n" +
                         "# Project 1,Project,100\n" +
                         "* 1,Submitted,Alice,a1,true,Passed,false,graderX,\n";

        String path = createTempFile(content);
        ArrayList<Assignment> list = AssignmentReader.readAssignmentFile(path);

        assertEquals(2, list.size());
        assertEquals("Lab 2", list.get(0).getAssignmentName());
        assertEquals(1, list.get(0).getSubmissions().size());
    }

    /**
     * Checks reader error handling for missing files.
     */
    @Test
    public void testMissingFile() {
        assertThrows(IllegalArgumentException.class, () -> AssignmentReader.readAssignmentFile("invalid_file_name.txt"));
    }
}