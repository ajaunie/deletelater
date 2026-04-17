package edu.ncsu.csc216.wolf_regrade.model.course;

import java.io.File;

import edu.ncsu.csc216.wolf_regrade.model.regrade.RegradeRequest;
import edu.ncsu.csc216.wolf_regrade.model.util.ISortedList;
import edu.ncsu.csc216.wolf_regrade.model.util.SortedList;

/**
 * Manages regrade requests for a course, organized by assignment.
 * Contains an ISortedList of Assignments and operations to manage assignments,
 * pending requests, completed requests, filtering, and file saving.
 * 
 * @author Ajaunie White
 */
public class Course {

    /** The name of the course */
    private String courseName;
    /** Whether the course data has been modified since the last save */
    private boolean isChanged;
    /** The sorted list of assignments */
    private ISortedList<Assignment> assignments;

    /**
     * Constructs a Course with the given name and an empty list of assignments.
     * isChanged is initialized to true.
     * 
     * @param courseName the name of the course
     * @throws IllegalArgumentException if courseName is null or empty
     */
    public Course(String courseName) {
        if (courseName == null || courseName.isEmpty()) {
            throw new IllegalArgumentException("Invalid name.");
        }
        this.courseName = courseName;
        this.assignments = new SortedList<>();
        this.isChanged = true;
    }

    /**
     * Returns the course name.
     * 
     * @return the course name
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * Sets the course name.
     * 
     * @param courseName the course name to set
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    /**
     * Returns whether the course data has been modified since the last save.
     * 
     * @return true if changed
     */
    public boolean isChanged() {
        return isChanged;
    }

    /**
     * Sets the changed flag.
     * 
     * @param isChanged the changed status to set
     */
    public void setChanged(boolean isChanged) {
        this.isChanged = isChanged;
    }

    /**
     * Creates a new Assignment with the given name and adds it to the sorted list.
     * 
     * @param assignmentName the name of the new assignment
     * @throws IllegalArgumentException if the name is null, empty, or a duplicate
     */
    public void addAssignment(String assignmentName) {
    }

    /**
     * Renames the assignment at the given index. Removes, renames, and re-adds
     * to maintain sorted order.
     * 
     * @param idx     the index of the assignment to edit
     * @param newName the new assignment name
     * @throws IllegalArgumentException  if the new name is null, empty, or a duplicate
     * @throws IndexOutOfBoundsException if idx is out of bounds
     */
    public void editAssignment(int idx, String newName) {
    }

    /**
     * Removes and returns the assignment at the given index along with all of its requests.
     * 
     * @param idx the index of the assignment to remove
     * @return the removed Assignment
     * @throws IndexOutOfBoundsException if idx is out of bounds
     */
    public Assignment removeAssignment(int idx) {
        return null;
    }

    /**
     * Returns the assignment at the given index.
     * 
     * @param idx the index of the assignment to retrieve
     * @return the assignment at the given index
     * @throws IndexOutOfBoundsException if idx is out of bounds
     */
    public Assignment getAssignment(int idx) {
        return null;
    }

    /**
     * Returns the number of assignments.
     * 
     * @return assignment count
     */
    public int getAssignmentCount() {
        return assignments.size();
    }

    /**
     * Returns a String array of assignment names in sorted order.
     * 
     * @return array of assignment names
     */
    public String[] getAssignmentsAsArray() {
        String[] names = new String[assignments.size()];
        for (int i = 0; i < assignments.size(); i++) {
            names[i] = assignments.get(i).getAssignmentName();
        }
        return names;
    }

    /**
     * Returns a String[][] with five columns per assignment:
     * name, pending count, completed count, total count, and completion percentage.
     * 
     * @return 2D string array of assignment detail data
     */
    public String[][] getAssignmentsAsDetailArray() {
        return null;
    }

    /**
     * Adds the given request to the assignment at the given index.
     * 
     * @param assignmentIdx the index of the assignment
     * @param request       the request to add
     * @throws IndexOutOfBoundsException if assignmentIdx is out of bounds
     */
    public void addPendingRequest(int assignmentIdx, RegradeRequest request) {
    }

    /**
     * Replaces the pending request at the given index within the assignment.
     * 
     * @param assignmentIdx the index of the assignment
     * @param requestIdx    the index of the request to replace
     * @param replacement   the replacement request
     * @throws IndexOutOfBoundsException if either index is out of bounds
     */
    public void editPendingRequest(int assignmentIdx, int requestIdx, RegradeRequest replacement) {
    }

    /**
     * Removes and returns the pending request at the given index within the assignment.
     * 
     * @param assignmentIdx the index of the assignment
     * @param requestIdx    the index of the request to remove
     * @return the removed RegradeRequest
     * @throws IndexOutOfBoundsException if either index is out of bounds
     */
    public RegradeRequest removePendingRequest(int assignmentIdx, int requestIdx) {
        return null;
    }

    /**
     * Completes the pending request at the given index within the assignment.
     * 
     * @param assignmentIdx the index of the assignment
     * @param requestIdx    the index of the request to complete
     * @param resolution    the resolution to assign
     * @param gradeChanged  whether the grade was changed
     * @throws IndexOutOfBoundsException if either index is out of bounds
     * @throws IllegalArgumentException  if the resolution or gradeChanged is invalid
     */
    public void completeRequest(int assignmentIdx, int requestIdx,
            String resolution, boolean gradeChanged) {
    }

    /**
     * Undoes the last completion for the assignment at the given index.
     * 
     * @param assignmentIdx the index of the assignment
     * @throws IndexOutOfBoundsException if assignmentIdx is out of bounds
     * @throws IllegalArgumentException  if there are no completed requests to undo
     */
    public void undoLastCompletion(int assignmentIdx) {
    }

    /**
     * Returns a String[][] of all pending requests across all assignments
     * that match the given grader, sorted by assignment name then student name.
     * Columns match those of Assignment.getPendingRequestsAsArray().
     * 
     * @param grader the grader name to filter by
     * @return 2D string array of filtered pending request data
     */
    public String[][] filterByGrader(String grader) {
        return null;
    }

    /**
     * Saves the current Course to the given file using RegradeWriter.
     * Sets isChanged to false on success.
     * 
     * @param file the destination file
     * @throws IllegalArgumentException if the file cannot be written
     */
    public void saveCourseRegrades(File file) {
    }
}