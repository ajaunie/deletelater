package edu.ncsu.csc216.wolf_regrade.model.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

import edu.ncsu.csc216.wolf_regrade.model.course.Assignment;
import edu.ncsu.csc216.wolf_regrade.model.course.Course;
import edu.ncsu.csc216.wolf_regrade.model.regrade.RegradeRequest;
import edu.ncsu.csc216.wolf_regrade.model.util.Iterator;

/**
 * Writes course regrade data to a file in the expected format.
 * Contains only static methods and is not instantiated.
 *
 * @author Ajaunie White
 */
public class RegradeWriter {

    /**
     * Writes the course data to the specified file.
     * Format:
     * CourseName
     * # AssignmentName
     * * P|Type|StudentName|UnityId|Grader|SubtypeFields
     * openText
     * * C|Type|StudentName|UnityId|Grader|SubtypeFields|Resolution|GradeChanged
     * openText
     *
     * @param file   the destination file
     * @param course the course to be saved
     * @throws IllegalArgumentException if the file cannot be written
     */
    public static void writeRegradeFile(File file, Course course) {
        try (PrintStream writer = new PrintStream(new FileOutputStream(file))) {
            writer.println(course.getCourseName());
            for (int i = 0; i < course.getAssignmentCount(); i++) {
                Assignment a = course.getAssignment(i);
                writer.println("# " + a.getAssignmentName());
                // Write pending requests
                Iterator<RegradeRequest> pendingIt = a.getPendingRequests().iterator();
                while (pendingIt.hasNext()) {
                    RegradeRequest r = pendingIt.next();
                    writer.println("* P|" + r.toString());
                }
                // Write completed requests (top of stack first)
                Iterator<RegradeRequest> completedIt = a.getCompletedRequests().iterator();
                while (completedIt.hasNext()) {
                    RegradeRequest r = completedIt.next();
                    writer.println("* C|" + r.toString());
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to save file.");
        }
    }
}