package edu.ncsu.csc216.wolf_regrade.model.io;

import java.io.File;
import java.util.Scanner;

import edu.ncsu.csc216.wolf_regrade.model.course.Assignment;
import edu.ncsu.csc216.wolf_regrade.model.course.Course;
import edu.ncsu.csc216.wolf_regrade.model.regrade.ClarifyRequest;
import edu.ncsu.csc216.wolf_regrade.model.regrade.ExemptionRequest;
import edu.ncsu.csc216.wolf_regrade.model.regrade.RegradeRequest;
import edu.ncsu.csc216.wolf_regrade.model.regrade.ResubmitRequest;
import edu.ncsu.csc216.wolf_regrade.model.regrade.ReviewRequest;

/**
 * Reads course regrade data from a file and returns a fully populated Course.
 * Invalid records are silently skipped.
 * Contains only static methods and is not instantiated.
 *
 * @author Ajaunie White
 */
public class RegradeReader {

    /**
     * Reads the file and returns a Course.
     *
     * @param file the file to read
     * @return the fully populated Course
     * @throws IllegalArgumentException if the file cannot be found or is invalid
     */
    public static Course readRegradeFile(File file) {
        String fullText;
        try (Scanner fileScanner = new Scanner(file)) {
            fileScanner.useDelimiter("\\Z"); // read entire file
            if (!fileScanner.hasNext()) {
                throw new IllegalArgumentException("Unable to load file.");
            }
            fullText = fileScanner.next();
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to load file.");
        }

        // Split into assignment sections using \n# as delimiter
        // First token is the course name (possibly with trailing whitespace)
        Scanner assignmentScanner = new Scanner(fullText);
        assignmentScanner.useDelimiter("\\r?\\n[#] ");

        if (!assignmentScanner.hasNext()) {
            assignmentScanner.close();
            throw new IllegalArgumentException("Unable to load file.");
        }

        String courseNameToken = assignmentScanner.next().trim();
        if (courseNameToken.isEmpty()) {
            assignmentScanner.close();
            throw new IllegalArgumentException("Unable to load file.");
        }

        Course course;
        try {
            course = new Course(courseNameToken);
        } catch (IllegalArgumentException e) {
            assignmentScanner.close();
            throw new IllegalArgumentException("Unable to load file.");
        }

        // Process each assignment section
        while (assignmentScanner.hasNext()) {
            String assignmentToken = assignmentScanner.next();
            processAssignmentToken(course, assignmentToken);
        }
        assignmentScanner.close();

        return course;
    }

    /**
     * Processes a single assignment token and adds it (with its requests) to the course.
     * Invalid data is silently skipped.
     *
     * @param course          the course to add to
     * @param assignmentToken the token containing the assignment name and requests
     */
    private static void processAssignmentToken(Course course, String assignmentToken) {
        // Split into request tokens using \n* as delimiter
        Scanner requestScanner = new Scanner(assignmentToken);
        requestScanner.useDelimiter("\\r?\\n[*] ");

        if (!requestScanner.hasNext()) {
            requestScanner.close();
            return;
        }

        String assignmentName = requestScanner.next().trim();
        if (assignmentName.isEmpty()) {
            requestScanner.close();
            return;
        }

        // Try to add the assignment; skip if duplicate
        Assignment assignment = null;
        try {
            course.addAssignment(assignmentName);
            assignment = course.getAssignment(course.getAssignmentCount() - 1);
            // Find the correct assignment by name (sorted list may reorder)
            for (int i = 0; i < course.getAssignmentCount(); i++) {
                if (course.getAssignment(i).getAssignmentName().equals(assignmentName)) {
                    assignment = course.getAssignment(i);
                    break;
                }
            }
        } catch (IllegalArgumentException e) {
            // Duplicate assignment name — skip this section
            requestScanner.close();
            return;
        }

        // Process each request token
        while (requestScanner.hasNext()) {
            String requestToken = requestScanner.next();
            processRequestToken(assignment, requestToken);
        }
        requestScanner.close();
    }

    /**
     * Processes a single request token and adds it to the assignment.
     * Invalid data is silently skipped.
     *
     * @param assignment   the assignment to add the request to
     * @param requestToken the token starting with P| or C| followed by fields and open text
     */
    private static void processRequestToken(Assignment assignment, String requestToken) {
        try {
            // Split into first line (pipe-delimited fields) and open text
            // requestToken starts with "P|" or "C|"
            int newlineIdx = requestToken.indexOf('\n');
            if (newlineIdx < 0) {
                return; // no open text line
            }
            String firstLine = requestToken.substring(0, newlineIdx).trim();
            String openText = requestToken.substring(newlineIdx + 1).trim();
            if (openText.isEmpty()) {
                return;
            }

            // Split first line by |
            String[] parts = firstLine.split("\\|", -1);
            if (parts.length < 5) {
                return;
            }

            String status = parts[0]; // P or C
            String type = parts[1];
            String studentName = parts[2];
            String unityId = parts[3];
            String grader = parts[4];

            RegradeRequest request = null;

            switch (type) {
                case ReviewRequest.TYPE:
                    // P|Review|studentName|unityId|grader|reviewItem
                    // C|Review|studentName|unityId|grader|reviewItem|resolution|gradeChanged
                    if (parts.length < 6) return;
                    String reviewItem = parts[5];
                    request = new ReviewRequest(studentName, unityId, grader, reviewItem, openText);
                    if ("C".equals(status)) {
                        if (parts.length < 8) return;
                        applyCompletion(request, parts[6], parts[7]);
                    }
                    break;

                case ResubmitRequest.TYPE:
                    // P|Resubmit|studentName|unityId|grader|repoLink|commitHash
                    if (parts.length < 7) return;
                    String repoLink = parts[5];
                    String commitHash = parts[6];
                    request = new ResubmitRequest(studentName, unityId, grader, repoLink, commitHash, openText);
                    if ("C".equals(status)) {
                        if (parts.length < 9) return;
                        applyCompletion(request, parts[7], parts[8]);
                    }
                    break;

                case ClarifyRequest.TYPE:
                    // P|Clarify|studentName|unityId|grader|feedbackItem
                    if (parts.length < 6) return;
                    String feedbackItem = parts[5];
                    request = new ClarifyRequest(studentName, unityId, grader, feedbackItem, openText);
                    if ("C".equals(status)) {
                        if (parts.length < 8) return;
                        applyCompletion(request, parts[6], parts[7]);
                    }
                    break;

                case ExemptionRequest.TYPE:
                    // P|Exemption|studentName|unityId|grader|policyReference
                    if (parts.length < 6) return;
                    String policyRef = parts[5];
                    request = new ExemptionRequest(studentName, unityId, grader, policyRef, openText);
                    if ("C".equals(status)) {
                        if (parts.length < 8) return;
                        applyCompletion(request, parts[6], parts[7]);
                    }
                    break;

                default:
                    return;
            }

            if (request == null) {
                return;
            }

            if ("P".equals(status)) {
                assignment.addPendingRequest(request);
            } else if ("C".equals(status)) {
                assignment.addCompletedRequest(request);
            }

        } catch (Exception e) {
            // Silently skip invalid records
        }
    }

    /**
     * Applies resolution and gradeChanged to a completed request.
     *
     * @param request      the request to update
     * @param resolution   the resolution string
     * @param gradeChanged the gradeChanged string ("true" or "false")
     * @throws IllegalArgumentException if values are invalid
     */
    private static void applyCompletion(RegradeRequest request, String resolution, String gradeChanged) {
        request.setResolution(resolution);
        request.setGradeChanged(Boolean.parseBoolean(gradeChanged));
    }
}