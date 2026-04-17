package edu.ncsu.csc216.wolf_regrade.model.io;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.File;
import edu.ncsu.csc216.wolf_regrade.model.course.Course;

/**
 * Tests reading from files.
 */
public class RegradeReaderTest {

    @Test
    public void testReadInvalidFiles() {
        // Test missing file
        assertThrows(IllegalArgumentException.class, () -> RegradeReader.readRegradeFile(new File("ghost_file.txt")));
        
        // Test empty/bad header (assuming your reader checks for # )
        File bad = new File("test-files/bad_header.txt");
        assertThrows(IllegalArgumentException.class, () -> RegradeReader.readRegradeFile(bad));
    }
    
    @Test
    public void testReadValidSkeleton() {
        // You'll need an actual file in test-files/ to get high coverage
        // For now, testing that a properly formatted empty course loads
        File valid = new File("test-files/course_only.txt");
        if (valid.exists()) {
            Course c = RegradeReader.readRegradeFile(valid);
            assertNotNull(c);
            assertEquals(0, c.getAssignmentCount());
        }
    }
}