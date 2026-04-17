package edu.ncsu.csc216.wolf_regrade.model.io;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.File;
import edu.ncsu.csc216.wolf_regrade.model.course.Course;

/**
 * Tests writing to files.
 */
public class RegradeWriterTest {

    @Test
    public void testWriteLifecycle() {
        Course studio = new Course("Studio-Session");
        studio.addAssignment("Intro");
        studio.addAssignment("Outro");
        
        File output = new File("test-files/session_save.txt");
        if (output.exists()) output.delete();
        
        RegradeWriter.writeRegradeFile(output, studio);
        assertTrue(output.exists());
        
        // Cleanup
        output.delete();
    }

    @Test
    public void testWriteErrors() {
        Course c = new Course("Error-Test");
        // Windows/Linux permission error path
        assertThrows(IllegalArgumentException.class, 
            () -> RegradeWriter.writeRegradeFile(new File("/"), c));
    }
}