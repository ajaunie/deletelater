package edu.ncsu.csc216.wolf_regrade.model.course;

import java.io.File;
import edu.ncsu.csc216.wolf_regrade.model.regrade.RegradeRequest;
import edu.ncsu.csc216.wolf_regrade.model.util.ISortedList;
import edu.ncsu.csc216.wolf_regrade.model.util.SortedList;

/**
 * Manages the collection of assignments for a course.
 */
public class Course {

    private String courseName;
    private boolean isChanged;
    private ISortedList<Assignment> assignments;

    public Course(String name) {
        this.courseName = name;
        this.assignments = new SortedList<>();
        this.isChanged = true;
    }

    public String getCourseName() { return courseName; }

    public void setCourseName(String courseName) { this.courseName = courseName; }

    public boolean isChanged() { return isChanged; }

    public void setChanged(boolean isChanged) { this.isChanged = isChanged; }

    public void addAssignment(String name) { }

    public void editAssignment(int idx, String name) { }

    public void removeAssignment(int idx) { }

    public Assignment getAssignment(int idx) { return null; }

    public int getAssignmentCount() { return assignments.size(); }

    /**
     * Returns a string array of assignment names.
     * @return names array
     */
    public String[] getAssignmentsAsArray() {
        String[] names = new String[assignments.size()];
        for (int i = 0; i < assignments.size(); i++) {
            names[i] = assignments.get(i).getAssignmentName();
        }
        return names;
    }

    public String[][] getAssignmentsAsDetailArray() { return null; }

    public void addPendingRequest(int assignmentIdx, RegradeRequest request) { }

    public void editPendingRequest(int assignmentIdx, int requestIdx, RegradeRequest request) { }

    public void removePendingRequest(int assignmentIdx, int requestIdx) { }

    public void completeRequest(int assignmentIdx, int requestIdx, String resolution, boolean gradeChanged) { }

    public void undoLastCompletion(int assignmentIdx) { }

    public String[][] filterByGrader(String grader) { return null; }

    public void saveCourseRegrades(File file) { }
}