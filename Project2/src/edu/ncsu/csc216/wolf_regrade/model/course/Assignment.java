package edu.ncsu.csc216.wolf_regrade.model.course;

import edu.ncsu.csc216.wolf_regrade.model.regrade.RegradeRequest;
import edu.ncsu.csc216.wolf_regrade.model.util.*;

public class Assignment implements Comparable<Assignment> {
    private String assignmentName;
    private IBackLog<RegradeRequest> pendingRequests;
    private IStack<RegradeRequest> completedRequests;

    public Assignment(String name) {
        setAssignmentName(name);
        this.pendingRequests = new BackLog<>();
        this.completedRequests = new CompletedRegradesStack();
    }

    public String getAssignmentName() { return assignmentName; }
    public void setAssignmentName(String name) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Invalid assignment name.");
        this.assignmentName = name.trim();
    }

    public void addPendingRequest(RegradeRequest r) {
        r.setAssignment(this);
        pendingRequests.add(r);
    }

    public RegradeRequest getPendingRequest(int idx) { return pendingRequests.get(idx); }
    public RegradeRequest removePendingRequest(int idx) { return pendingRequests.remove(idx); }
    public void editPendingRequest(int idx, RegradeRequest r) {
        r.setAssignment(this);
        pendingRequests.set(idx, r);
    }

    public void completeRequest(int idx, String res, boolean chg) {
        RegradeRequest r = pendingRequests.remove(idx);
        r.setResolution(res);
        r.setGradeChanged(chg);
        completedRequests.push(r);
    }

    public void addCompletedRequest(RegradeRequest r) {
        r.setAssignment(this);
        completedRequests.push(r);
    }

    public void undoLastCompletion() {
        if (completedRequests.isEmpty()) throw new IllegalArgumentException("No completed requests to undo.");
        RegradeRequest r = completedRequests.pop();
        r.setResolution(null);
        r.setGradeChanged(false);
        pendingRequests.add(r);
    }

    public int getPendingSize() { return pendingRequests.size(); }
    public int getCompletedSize() { return completedRequests.size(); }
    public int getTotalSize() { return getPendingSize() + getCompletedSize(); }
    public int getCompletedPercentage() {
        if (getTotalSize() == 0) return 0;
        return (int) (((double) getCompletedSize() / getTotalSize()) * 100);
    }

    public String[][] getPendingRequestsAsArray() {
        String[][] data = new String[getPendingSize()][5];
        Iterator<RegradeRequest> it = pendingRequests.iterator();
        for (int i = 0; it.hasNext(); i++) {
            RegradeRequest r = it.next();
            data[i] = new String[]{r.getType(), assignmentName, r.getStudentName(), r.getUnityId(), r.getGrader()};
        }
        return data;
    }

    public String[][] getCompletedRequestsAsArray() {
        String[][] data = new String[getCompletedSize()][7];
        Iterator<RegradeRequest> it = completedRequests.iterator();
        for (int i = 0; it.hasNext(); i++) {
            RegradeRequest r = it.next();
            data[i] = new String[]{r.getType(), assignmentName, r.getStudentName(), r.getUnityId(), r.getGrader(), r.getResolution(), String.valueOf(r.isGradeChanged())};
        }
        return data;
    }

    public IBackLog<RegradeRequest> getPendingRequests() { return pendingRequests; }
    public IStack<RegradeRequest> getCompletedRequests() { return completedRequests; }

    @Override
    public int compareTo(Assignment other) { return this.assignmentName.compareToIgnoreCase(other.assignmentName); }

    @Override
    public String toString() { return assignmentName; }
}