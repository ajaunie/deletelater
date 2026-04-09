package edu.ncsu.csc216.wolf_regrade.model.regrade;

import edu.ncsu.csc216.wolf_regrade.model.course.Assignment;

/**
 * Abstract class representing a general regrade request.
 */
public abstract class RegradeRequest implements Comparable<RegradeRequest> {

    /** Resolution constant for Closed */
    public static final String RESOLUTION_CLOSED = "Closed";
    /** Resolution constant for Duplicate */
    public static final String RESOLUTION_DUPLICATE = "Duplicate";
    /** Resolution constant for Additional Info */
    public static final String RESOLUTION_ADDITIONAL_INFO = "Additional Info";

    private String studentName;
    private String unityId;
    private String grader;
    private String resolution;
    private boolean gradeChanged;
    private String openText;
    private Assignment assignment;

    /**
     * Constructs a RegradeRequest.
     * @param studentName name of student
     * @param unityId ID of student
     * @param grader name of grader
     * @param openText request text
     */
    public RegradeRequest(String studentName, String unityId, String grader, String openText) {
        setStudentName(studentName);
        setUnityId(unityId);
        setGrader(grader);
        setOpenText(openText);
    }

    public abstract String getType();

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getUnityId() {
        return unityId;
    }

    public void setUnityId(String unityId) {
        this.unityId = unityId;
    }

    public String getGrader() {
        return grader;
    }

    public void setGrader(String grader) {
        this.grader = grader;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public boolean isGradeChanged() {
        return gradeChanged;
    }

    public void setGradeChanged(boolean gradeChanged) {
        this.gradeChanged = gradeChanged;
    }

    public String getOpenText() {
        return openText;
    }

    public void setOpenText(String openText) {
        this.openText = openText;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    /**
     * Returns the assignment name.
     * @return assignment name or empty string
     */
    public String getAssignmentName() {
        return assignment == null ? "" : assignment.getAssignmentName();
    }

    public abstract String getOpenTextName();

    @Override
    public String toString() {
        return null;
    }

    @Override
    public int compareTo(RegradeRequest other) {
        return 0;
    }

    public abstract String[] getSubtypeFields();
}