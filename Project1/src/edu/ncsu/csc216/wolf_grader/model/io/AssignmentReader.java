package edu.ncsu.csc216.wolf_grader.model.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import edu.ncsu.csc216.wolf_grader.model.manager.Assignment;
import edu.ncsu.csc216.wolf_grader.model.submission.Submission;

/** Reads files into assignment data objects. */
public class AssignmentReader {

    /**
     * Reads the given file and returns a list of valid Assignments with their Submissions.
     * Throws IllegalArgumentException if the file cannot be found.
     * @param fileName the path of the file to read
     * @return ArrayList of valid Assignment objects
     */
    public static ArrayList<Assignment> readAssignmentFile(String fileName) {
        Scanner fileReader = null;
        try {
            fileReader = new Scanner(new FileInputStream(fileName));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Unable to load file " + fileName);
        }

        String fileContent = "";
        while (fileReader.hasNextLine()) {
            fileContent = fileContent + fileReader.nextLine() + "\n";
        }
        fileReader.close();

        fileContent = fileContent.trim();

        if (fileContent.isEmpty() || fileContent.charAt(0) != '#') {
            return new ArrayList<Assignment>();
        }

        ArrayList<Assignment> assignments = new ArrayList<Assignment>();
        Scanner assignmentScanner = new Scanner(fileContent);
        assignmentScanner.useDelimiter("\\r?\\n?[#]");

        while (assignmentScanner.hasNext()) {
            String token = assignmentScanner.next().trim();
            if (!token.isEmpty()) {
                try {
                    Assignment a = processAssignment(token);
                    if (a != null) {
                        assignments.add(a);
                    }
                } catch (Exception e) {
                    // Skip invalid assignment blocks
                }
            }
        }
        assignmentScanner.close();

        return assignments;
    }

    /**
     * Processes a single assignment text block (without the leading '#').
     * Returns null if the block produces no valid submissions.
     * @param assignmentText the text block for one assignment
     * @return a valid Assignment or null
     */
    private static Assignment processAssignment(String assignmentText) {
        Scanner s = new Scanner(assignmentText);

        if (!s.hasNextLine()) {
            s.close();
            return null;
        }

        String header = s.nextLine().trim();
        Assignment a = processAssignmentLine(header);

        if (a == null) {
            s.close();
            return null;
        }

        while (s.hasNextLine()) {
            String line = s.nextLine().trim();
            if (line.startsWith("*")) {
                String data = line.substring(1).trim();
                Submission sub = processSubmission(data);
                if (sub != null) {
                    try {
                        a.addSubmission(sub);
                    } catch (Exception e) {
                        // Skip duplicate or invalid submissions
                    }
                }
            }
        }
        s.close();

        if (a.getSubmissions().isEmpty()) {
            return null;
        }

        return a;
    }

    /**
     * Parses the first line of an assignment block into an Assignment.
     * @param assignmentLine the comma-separated assignment header line
     * @return a valid Assignment or null if the line is malformed
     */
    private static Assignment processAssignmentLine(String assignmentLine) {
        try {
            String[] parts = assignmentLine.split(",", -1);
            if (parts.length != 3) {
                return null;
            }
            String name = parts[0].trim();
            String category = parts[1].trim();
            int maxPoints = Integer.parseInt(parts[2].trim());
            return new Assignment(name, category, maxPoints);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Parses a single submission line (without the leading '*') into a Submission.
     * @param submissionLine the comma-separated submission data
     * @return a valid Submission or null if invalid
     */
    private static Submission processSubmission(String submissionLine) {
        try {
            String[] p = submissionLine.split(",", -1);
            if (p.length != 9) {
                return null;
            }
            int id = Integer.parseInt(p[0].trim());
            String state = p[1].trim();
            String name = p[2].trim();
            String unityId = p[3].trim();
            boolean processed = Boolean.parseBoolean(p[4].trim());
            String checkResult = p[5].trim();
            boolean published = Boolean.parseBoolean(p[6].trim());
            String grader = p[7].trim();
            String grade = p[8].trim();
            return new Submission(id, state, name, unityId, processed,
                                  checkResult, published, grader, grade);
        } catch (Exception e) {
            return null;
        }
    }
}