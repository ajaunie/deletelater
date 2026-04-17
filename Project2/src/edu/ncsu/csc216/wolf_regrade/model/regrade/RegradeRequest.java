package edu.ncsu.csc216.wolf_regrade.model.regrade;

import edu.ncsu.csc216.wolf_regrade.model.course.Assignment;

/**
 * Abstract core of the regrade request system.
 * @author Ajaunie White
 */
public abstract class RegradeRequest implements Comparable<RegradeRequest> {
    public static final String RESOLUTION_CLOSED = "Closed";
    public static final String RESOLUTION_DUPLICATE = "Duplicate";
    public static final String RESOLUTION_ADDITIONAL_INFO = "Additional Info";

    private String studentName;
    private String unityId;
    private String grader;
    private String resolution;
    private boolean gradeChanged;
    private String openText;
    private Assignment assignment;

    public RegradeRequest(String studentName, String unityId, String grader, String openText) {
        setStudentName(studentName);
        setUnityId(unityId);
        setGrader(grader);
        setOpenText(openText);
    }

    public abstract String getType();
    public abstract String getOpenTextName();
    public abstract String[] getSubtypeFields();

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) {
        if (studentName == null || studentName.trim().isEmpty()) throw new IllegalArgumentException("Invalid regrade request.");
        this.studentName = studentName.trim();
    }

    public String getUnityId() { return unityId; }
    public void setUnityId(String unityId) {
        if (unityId == null || unityId.trim().isEmpty()) throw new IllegalArgumentException("Invalid regrade request.");
        this.unityId = unityId.trim();
    }

    public String getGrader() { return grader; }
    public void setGrader(String grader) {
        if (grader == null || grader.trim().isEmpty()) throw new IllegalArgumentException("Invalid regrade request.");
        this.grader = grader.trim();
    }

    public String getResolution() { return resolution; }
    public void setResolution(String resolution) {
        if (resolution != null && !RESOLUTION_CLOSED.equals(resolution) && 
            !RESOLUTION_DUPLICATE.equals(resolution) && !RESOLUTION_ADDITIONAL_INFO.equals(resolution)) {
            throw new IllegalArgumentException("Invalid resolution.");
        }
        this.resolution = resolution;
    }

    public boolean isGradeChanged() { return gradeChanged; }
    public void setGradeChanged(boolean gradeChanged) {
        if (gradeChanged && !RESOLUTION_CLOSED.equals(resolution)) {
            throw new IllegalArgumentException("Grade can only be changed with a Closed resolution.");
        }
        this.gradeChanged = gradeChanged;
    }

    public String getOpenText() { return openText; }
    public void setOpenText(String openText) {
        if (openText == null || openText.trim().isEmpty()) throw new IllegalArgumentException("Invalid regrade request.");
        this.openText = openText.trim();
    }

    public Assignment getAssignment() { return assignment; }
    public void setAssignment(Assignment assignment) {
        if (this.assignment != null && this.assignment != assignment) throw new IllegalArgumentException("Request already belongs to an assignment.");
        this.assignment = assignment;
    }

    public String getAssignmentName() { return assignment == null ? "" : assignment.getAssignmentName(); }

    @Override
    public int compareTo(RegradeRequest other) {
        int comp = this.getAssignmentName().compareToIgnoreCase(other.getAssignmentName());
        if (comp != 0) return comp;
        return this.studentName.compareToIgnoreCase(other.studentName);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getType()).append("|").append(studentName).append("|").append(unityId).append("|")
          .append(grader).append("|").append(resolution == null ? "" : resolution).append("|")
          .append(gradeChanged).append("|").append(openText);
        for (String field : getSubtypeFields()) sb.append("|").append(field);
        return sb.toString();
    }
}