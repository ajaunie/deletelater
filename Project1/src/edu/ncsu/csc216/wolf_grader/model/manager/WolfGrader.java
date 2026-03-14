package edu.ncsu.csc216.wolf_grader.model.manager;

import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc216.wolf_grader.model.command.Command;
import edu.ncsu.csc216.wolf_grader.model.io.AssignmentReader;
import edu.ncsu.csc216.wolf_grader.model.io.AssignmentWriter;
import edu.ncsu.csc216.wolf_grader.model.submission.Submission;

/**
 * Main manager class for the application.
 */
public class WolfGrader {

    /** The single instance of WolfGrader enforced by the Singleton pattern. */
    private static WolfGrader singleton;
    /** The assignment currently selected as active. */
    private Assignment activeAssignment;
    /** The full list of assignments loaded into the system. */
    private List<Assignment> assignments;

    /**
     * Private constructor initializes an empty list and null active assignment.
     */
    private WolfGrader() {
        assignments = new ArrayList<Assignment>();
        activeAssignment = null;
    }

    /**
     * Returns the single WolfGrader instance, creating it if it does not yet exist.
     * @return the WolfGrader singleton
     */
    public static synchronized WolfGrader getInstance() {
        if (singleton == null) {
            singleton = new WolfGrader();
        }
        return singleton;
    }

    /**
     * Saves all assignments to the specified file.
     * Throws IllegalArgumentException if there is no active assignment.
     * @param fileName the path of the file to save to
     */
    public void saveAssignmentsToFile(String fileName) {
        if (activeAssignment == null) {
            throw new IllegalArgumentException("Unable to save file.");
        }
        AssignmentWriter.writeAssignmentsToFile(fileName, assignments);
    }

    /**
     * Loads assignments from the specified file and appends them to the current list.
     * Makes the first loaded assignment active.
     * @param fileName the path of the file to load from
     */
    public void loadAssignmentsFromFile(String fileName) {
        List<Assignment> loaded = AssignmentReader.readAssignmentFile(fileName);
        assignments.addAll(loaded);
        if (!loaded.isEmpty()) {
            loadAssignment(loaded.get(0).getAssignmentName());
        }
    }

    /**
     * Creates a new Assignment and adds it to the end of the assignments list,
     * then makes it the active assignment.
     * Throws IllegalArgumentException with "Assignment cannot be created." if the name
     * is null, empty, or a case-insensitive duplicate of an existing assignment.
     * @param name      the assignment name
     * @param category  the assignment category
     * @param maxPoints the maximum points (1-100)
     */
    public void addNewAssignment(String name, String category, int maxPoints) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Assignment cannot be created.");
        }
        for (int i = 0; i < assignments.size(); i++) {
            if (assignments.get(i).getAssignmentName().equalsIgnoreCase(name)) {
                throw new IllegalArgumentException("Assignment cannot be created.");
            }
        }
        Assignment a = new Assignment(name, category, maxPoints);
        assignments.add(a);
        loadAssignment(name);
    }

    /**
     * Finds the assignment with the given name, makes it active, and sets the submission counter.
     * Throws IllegalArgumentException with "Assignment not available." if not found.
     * @param assignmentName the name of the assignment to load
     */
    public void loadAssignment(String assignmentName) {
        for (int i = 0; i < assignments.size(); i++) {
            if (assignments.get(i).getAssignmentName().equals(assignmentName)) {
                activeAssignment = assignments.get(i);
                activeAssignment.setSubmissionId();
                return;
            }
        }
        throw new IllegalArgumentException("Assignment not available.");
    }

    /**
     * Returns the currently active assignment.
     * @return the active Assignment, or null if none is set
     */
    public Assignment getActiveAssignment() {
        return activeAssignment;
    }

    /**
     * Returns the name of the active assignment.
     * @return the active assignment name, or null if none is set
     */
    public String getActiveAssignmentName() {
        if (activeAssignment == null) {
            return null;
        }
        return activeAssignment.getAssignmentName();
    }

    /**
     * Returns a String array of all assignment names in list order.
     * @return array of assignment names
     */
    public String[] getAssignmentList() {
        String[] list = new String[assignments.size()];
        for (int i = 0; i < assignments.size(); i++) {
            list[i] = assignments.get(i).getAssignmentName();
        }
        return list;
    }

    /**
     * Adds a new submission to the active assignment.
     * Does nothing if there is no active assignment.
     * @param name    the submission author name
     * @param unityId the submission author unity id
     */
    public void addSubmissionToAssignment(String name, String unityId) {
        if (activeAssignment != null) {
            activeAssignment.addSubmission(name, unityId);
        }
    }

    /**
     * Executes the given command on the submission with the specified id
     * in the active assignment. Does nothing if there is no active assignment.
     * @param id the submission id
     * @param c  the command to execute
     */
    public void executeCommand(int id, Command c) {
        if (activeAssignment != null) {
            activeAssignment.executeCommand(id, c);
        }
    }

    /**
     * Deletes the submission with the given id from the active assignment.
     * Does nothing if there is no active assignment.
     * @param id the submission id to delete
     */
    public void deleteSubmissionById(int id) {
        if (activeAssignment != null) {
            activeAssignment.deleteSubmissionById(id);
        }
    }

    /**
     * Returns a 2D String array of submission data for the active assignment,
     * filtered by the given state name. Pass "All" to return every submission.
     * Columns: [0] id, [1] state, [2] name(unityId), [3] checkResult.
     * @param filter the state name to filter by, or "All"
     * @return 2D array of submission display data
     */
    public String[][] getSubmissionsAsArray(String filter) {
        if (activeAssignment == null) {
            return null;
        }
        List<Submission> allSubs = activeAssignment.getSubmissions();
        ArrayList<Submission> filtered = new ArrayList<Submission>();
        for (int i = 0; i < allSubs.size(); i++) {
            if ("All".equals(filter) || allSubs.get(i).getState().equals(filter)) {
                filtered.add(allSubs.get(i));
            }
        }
        String[][] results = new String[filtered.size()][4];
        for (int i = 0; i < filtered.size(); i++) {
            Submission s = filtered.get(i);
            results[i][0] = "" + s.getId();
            results[i][1] = s.getState();
            results[i][2] = s.getName() + "(" + s.getUnityId() + ")";
            results[i][3] = s.getCheckResult();
        }
        return results;
    }

    /**
     * Returns the submission with the given id from the active assignment.
     * @param id the submission id to look up
     * @return the matching Submission, or null if not found
     */
    public Submission getSubmissionById(int id) {
        if (activeAssignment == null) {
            return null;
        }
        return activeAssignment.getSubmissionById(id);
    }

    /**
     * Resets the singleton to null. Protected for use in testing only.
     */
    protected void resetManager() {
        singleton = null;
        Submission.setCounter(0);
    }
}