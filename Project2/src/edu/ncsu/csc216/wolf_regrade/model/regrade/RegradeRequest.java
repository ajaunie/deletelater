package edu.ncsu.csc216.wolf_regrade.model.regrade;

import edu.ncsu.csc216.wolf_regrade.model.course.Assignment;

/**
 * Abstract class representing a general regrade request.
 * 
 * @author Your Name
 */
public abstract class RegradeRequest implements Comparable<RegradeRequest> {

    /** Resolution constant for Closed */
    public static final String RESOLUTION_CLOSED = "Closed";
    /** Resolution constant for Duplicate */
    public static final String RESOLUTION_DUPLICATE = "Duplicate";
    /** Resolution constant for Additional Info */
    public static final String RESOLUTION_ADDITIONAL_INFO = "AdditionalInfo";

    /** The student's name */
    private String studentName;
    /** The student's unity ID */
    private String unityId;
    /** The grader's name */
    private String grader;
    /** The resolution status of the request */
    private String resolution;
    /** Whether the grade was changed */
    private boolean gradeChanged;
    /** The open-ended text for the request */
    private String openText;
    /** Back-reference to the owning Assignment */
    private Assignment assignment;

    /**
     * Constructs a RegradeRequest.
     * 
     * @param studentName name of student
     * @param unityId     ID of student
     * @param grader      name of grader
     * @param openText    request text
     * @throws IllegalArgumentException if any parameter is null or empty
     */
    public RegradeRequest(String studentName, String unityId, String grader, String openText) {
        setStudentName(studentName);
        setUnityId(unityId);
        setGrader(grader);
        setOpenText(openText);
    }

    /**
     * Returns the request type identifier.
     * 
     * @return the type string
     */
    public abstract String getType();

    /**
     * Returns a descriptive label for the open-text field.
     * 
     * @return the open text label
     */
    public abstract String getOpenTextName();

    /**
     * Returns a pipe-delimited string of the subtype-specific fields for use in toString().
     * 
     * @return subtype-specific fields string
     */
    protected abstract String getSubtypeFields();

    /**
     * Returns the student name.
     * 
     * @return the student name
     */
    public String getStudentName() {
        return studentName;
    }

    /**
     * Sets the student name.
     * 
     * @param studentName the student name to set
     * @throws IllegalArgumentException if studentName is null or empty
     */
    public void setStudentName(String studentName) {
        if (studentName == null || studentName.isEmpty()) {
            throw new IllegalArgumentException("Invalid regrade request.");
        }
        this.studentName = studentName;
    }

    /**
     * Returns the unity ID.
     * 
     * @return the unity ID
     */
    public String getUnityId() {
        return unityId;
    }

    /**
     * Sets the unity ID.
     * 
     * @param unityId the unity ID to set
     * @throws IllegalArgumentException if unityId is null or empty
     */
    public void setUnityId(String unityId) {
        if (unityId == null || unityId.isEmpty()) {
            throw new IllegalArgumentException("Invalid regrade request.");
        }
        this.unityId = unityId;
    }

    /**
     * Returns the grader name.
     * 
     * @return the grader name
     */
    public String getGrader() {
        return grader;
    }

    /**
     * Sets the grader name.
     * 
     * @param grader the grader name to set
     * @throws IllegalArgumentException if grader is null or empty
     */
    public void setGrader(String grader) {
        if (grader == null || grader.isEmpty()) {
            throw new IllegalArgumentException("Invalid regrade request.");
        }
        this.grader = grader;
    }

    /**
     * Returns the resolution.
     * 
     * @return the resolution
     */
    public String getResolution() {
        return resolution;
    }

    /**
     * Sets the resolution. Valid values are "Closed", "Duplicate", "AdditionalInfo", or null.
     * 
     * @param resolution the resolution to set
     * @throws IllegalArgumentException if resolution is not a valid value
     */
    public void setResolution(String resolution) {
        if (resolution != null
                && !RESOLUTION_CLOSED.equals(resolution)
                && !RESOLUTION_DUPLICATE.equals(resolution)
                && !RESOLUTION_ADDITIONAL_INFO.equals(resolution)) {
            throw new IllegalArgumentException("Invalid resolution.");
        }
        this.resolution = resolution;
    }

    /**
     * Returns whether the grade was changed.
     * 
     * @return true if the grade was changed
     */
    public boolean isGradeChanged() {
        return gradeChanged;
    }

    /**
     * Sets whether the grade was changed.
     * 
     * @param gradeChanged true if the grade was changed
     * @throws IllegalArgumentException if gradeChanged is true but resolution is not "Closed"
     */
    public void setGradeChanged(boolean gradeChanged) {
        if (gradeChanged && !RESOLUTION_CLOSED.equals(resolution)) {
            throw new IllegalArgumentException("Grade can only be changed with a Closed resolution.");
        }
        this.gradeChanged = gradeChanged;
    }

    /**
     * Returns the open-ended text.
     * 
     * @return the open text
     */
    public String getOpenText() {
        return openText;
    }

    /**
     * Sets the open-ended text.
     * 
     * @param openText the open text to set
     * @throws IllegalArgumentException if openText is null or empty
     */
    public void setOpenText(String openText) {
        if (openText == null || openText.isEmpty()) {
            throw new IllegalArgumentException("Invalid regrade request.");
        }
        this.openText = openText;
    }

    /**
     * Returns the owning Assignment.
     * 
     * @return the assignment
     */
    public Assignment getAssignment() {
        return assignment;
    }

    /**
     * Sets the back-reference to the owning Assignment.
     * 
     * @param assignment the assignment to set
     * @throws IllegalArgumentException if the request already belongs to a different assignment
     */
    public void setAssignment(Assignment assignment) {
        if (this.assignment != null && this.assignment != assignment) {
            throw new IllegalArgumentException("Request already belongs to an assignment.");
        }
        this.assignment = assignment;
    }

    /**
     * Returns the assignment name, or an empty string if no assignment is set.
     * 
     * @return assignment name or empty string
     */
    public String getAssignmentName() {
        return assignment == null ? "" : assignment.getAssignmentName();
    }

    /**
     * Returns a pipe-delimited string representation of the request for writing to a file.
     * 
     * @return the string representation
     */
    @Override
    public String toString() {
        return null;
    }

    /**
     * Compares by assignment name first, then by student name (both case-insensitive).
     * 
     * @param other the other RegradeRequest to compare to
     * @return negative, zero, or positive integer
     */
    @Override
    public int compareTo(RegradeRequest other) {
        return 0;
    }
}