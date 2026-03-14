package edu.ncsu.csc216.wolf_grader.model.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import edu.ncsu.csc216.wolf_grader.model.manager.Assignment;

/**
 * Handles saving the current assignment data back to a file.
 * Uses the toString methods of the model to format the output.
 */
public class AssignmentWriter {

    /**
     * Takes the list of assignments and writes them to the specified file.
     * @param fileName where to save the data
     * @param assignments list of data to be saved
     */
    public static void writeAssignmentsToFile(String fileName, List<Assignment> assignments) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new File(fileName));
            for (int i = 0; i < assignments.size(); i++) {
                out.print(assignments.get(i).toString());
            }
            out.close();
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Unable to write to file " + fileName);
        }
    }
}