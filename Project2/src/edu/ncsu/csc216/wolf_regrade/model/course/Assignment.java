package edu.ncsu.csc216.wolf_regrade.model.course;

import edu.ncsu.csc216.wolf_regrade.model.regrade.RegradeRequest;
import edu.ncsu.csc216.wolf_regrade.model.util.BackLog;
import edu.ncsu.csc216.wolf_regrade.model.util.CompletedRegradesStack;

/**
 * Represents an assignment containing pending and completed regrade requests.
 * Manages the lifecycle of requests: adding, completing, editing, removing, and undoing.
 * 
 * @author Your Name
 */
public class Assignment implements Comparable<Assignment> {

    /** The name of the assignment */
    private String assignmentName;
    /** The list of pending regrade requests */
    private BackLog<RegradeRequest> pendingRequests;
    /** The stack of completed regrade requests */
    private CompletedRegradesStack completedRequests;

    /**
     * Constructs an Assignment with the given name and empty request collections.
     * 
     * @param assignmentName the name of the assignment
     * @throws IllegalArgumentException if assignmentName is null or empty
     */
    public Assignment(String assignmentName) {
        if (assignmentName == null || assignmentName.isEmpty()) {
            throw new IllegalArgumentException("Invalid assignment name.");
        }
        this.assignmentName = assignmentName;
        this.pendingRequests = new BackLog<>();
        this.completedRequests = new CompletedRegradesStack();
    }

    /**
     * Returns the assignment name.
     * 
     * @return the assignment name
     */
    public String getAssignmentName() {
        return assignmentName;
    }

    /**
     * Sets the assignment name.
     * 
     * @param assignmentName the assignment name to set
     * @throws IllegalArgumentException if assignmentName is null or empty
     */
    public void setAssignmentName(String assignmentName) {
        if (assignmentName == null || assignmentName.isEmpty()) {
            throw new IllegalArgumentException("Invalid assignment name.");
        }
        this.assignmentName = assignmentName;
    }

    /**
     * Sets the request's back-reference to this Assignment and adds it to the pending list.
     * 
     * @param request the request to add
     */
    public void addPendingRequest(RegradeRequest request) {
    }

    /**
     * Returns the pending request at the given index.
     * 
     * @param idx the index of the request
     * @return the pending request at the given index
     * @throws IndexOutOfBoundsException if idx is out of bounds
     */
    public RegradeRequest getPendingRequest(int idx) {
        return null;
    }

    /**
     * Removes and returns the pending request at the given index.
     * 
     * @param idx the index of the request to remove
     * @return the removed pending request
     * @throws IndexOutOfBoundsException if idx is out of bounds
     */
    public RegradeRequest removePendingRequest(int idx) {
        return null;
    }

    /**
     * Replaces the pending request at the given index with the replacement request.
     * 
     * @param idx         the index of the request to replace
     * @param replacement the replacement request
     * @throws IndexOutOfBoundsException if idx is out of bounds
     */
    public void editPendingRequest(int idx, RegradeRequest replacement) {
    }

    /**
     * Removes the pending request at the given index, sets its resolution and
     * grade-changed flag, and pushes it onto the completed stack.
     * 
     * @param idx          the index of the pending request to complete
     * @param resolution   the resolution to assign
     * @param gradeChanged whether the grade was changed
     * @throws IndexOutOfBoundsException if idx is out of bounds
     * @throws IllegalArgumentException  if the resolution or gradeChanged is invalid
     */
    public void completeRequest(int idx, String resolution, boolean gradeChanged) {
    }

    /**
     * Sets the request's back-reference to this Assignment and pushes it onto the completed stack.
     * 
     * @param request the request to add as completed
     */
    public void addCompletedRequest(RegradeRequest request) {
        completedRequests.push(request);
    }

    /**
     * Pops the most recently completed request from the stack, clears its resolution
     * and grade-changed flag, and adds it back to the pending list.
     * 
     * @throws IllegalArgumentException if there are no completed requests to undo
     */
    public void undoLastCompletion() {
    }

    /**
     * Returns the number of pending requests.
     * 
     * @return pending request count
     */
    public int getPendingSize() {
        return 0;
    }

    /**
     * Returns the number of completed requests.
     * 
     * @return completed request count
     */
    public int getCompletedSize() {
        return 0;
    }

    /**
     * Returns the total number of requests (pending + completed).
     * 
     * @return total request count
     */
    public int getTotalSize() {
        return 0;
    }

    /**
     * Returns the completion percentage as an integer (0–100).
     * 
     * @return the completion percentage
     */
    public int getCompletedPercentage() {
        return 0;
    }

    /**
     * Returns the BackLog of pending regrade requests.
     * 
     * @return the pending requests BackLog
     */
    public BackLog<RegradeRequest> getPendingRequests() {
        return pendingRequests;
    }

    /**
     * Returns the CompletedRegradesStack of completed regrade requests.
     * 
     * @return the completed requests stack
     */
    public CompletedRegradesStack getCompletedRequests() {
        return completedRequests;
    }

    /**
     * Returns a String[][] of pending requests with five columns:
     * type, assignment name, student name, unity ID, grader.
     * 
     * @return 2D string array of pending request data
     */
    public String[][] getPendingRequestsAsArray() {
        return null;
    }

    /**
     * Returns a String[][] of completed requests with seven columns:
     * type, assignment name, student name, unity ID, grader, resolution, grade changed.
     * 
     * @return 2D string array of completed request data
     */
    public String[][] getCompletedRequestsAsArray() {
        return null;
    }

    /**
     * Compares assignments by name (case-insensitive).
     * 
     * @param other the other Assignment to compare to
     * @return negative, zero, or positive integer
     */
    @Override
    public int compareTo(Assignment other) {
        return 0;
    }

    /**
     * Returns a string representation of this Assignment.
     * 
     * @return the assignment name
     */
    @Override
    public String toString() {
        return null;
    }
}