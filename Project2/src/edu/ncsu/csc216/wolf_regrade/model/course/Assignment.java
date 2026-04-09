package edu.ncsu.csc216.wolf_regrade.model.course;

import edu.ncsu.csc216.wolf_regrade.model.regrade.RegradeRequest;
import edu.ncsu.csc216.wolf_regrade.model.util.BackLog;
import edu.ncsu.csc216.wolf_regrade.model.util.CompletedRegradesStack;
import edu.ncsu.csc216.wolf_regrade.model.util.IBackLog;
import edu.ncsu.csc216.wolf_regrade.model.util.IStack;

/**
 * Represents an assignment containing pending and completed regrade requests.
 */
public class Assignment implements Comparable<Assignment> {

    private String assignmentName;
    private IBackLog<RegradeRequest> pendingRequests;
    private IStack<RegradeRequest> completedRequests;

    public Assignment(String name) {
        this.assignmentName = name;
        this.pendingRequests = new BackLog<>();
        this.completedRequests = new CompletedRegradesStack<>();
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public void setAssignmentName(String assignmentName) {
        this.assignmentName = assignmentName;
    }

    public void addPendingRequest(RegradeRequest request) { }

    public RegradeRequest getPendingRequest(int idx) { return null; }

    public RegradeRequest removePendingRequest(int idx) { return null; }

    public void editPendingRequest(int idx, RegradeRequest request) { }

    public void completeRequest(int idx, String resolution, boolean gradeChanged) { }

    /**
     * Adds a completed request.
     * @param request request to add
     */
    public void addCompletedRequest(RegradeRequest request) {
        completedRequests.push(request);
    }

    public void undoLastCompletion() { }

    public int getPendingSize() { return 0; }

    public int getCompletedSize() { return 0; }

    public int getTotalSize() { return 0; }

    public int getCompletedPercentage() { return 0; }

    public String[][] getPendingRequestsAsArray() { return null; }

    public String[][] getCompletedRequestsAsArray() { return null; }

    @Override
    public int compareTo(Assignment other) { return 0; }

    @Override
    public String toString() { return null; }
}