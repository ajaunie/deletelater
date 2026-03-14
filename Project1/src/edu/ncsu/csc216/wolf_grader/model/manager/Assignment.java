package edu.ncsu.csc216.wolf_grader.model.manager;

import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc216.wolf_grader.model.command.Command;
import edu.ncsu.csc216.wolf_grader.model.submission.Submission;

/**
 * Stores details for an assignment and manages its student submissions.
 * Keeps everything organized and in the right order.
 */
public class Assignment {

    /** The name of this assignment. */
    private String assignmentName;
    /** The category type of this assignment. */
    private String assignmentCategory;
    /** The maximum number of points for this assignment. */
    private int maxPoints;
    /** The sorted list of submissions belonging to this assignment. */
    private List<Submission> submissions;

    /**
     * Creates a new assignment category.
     * @param assignmentName name of the work.
     * @param assignmentCategory type of work.
     * @param maxPoints point total for the work.
     */
    public Assignment(String assignmentName, String assignmentCategory, int maxPoints) {
        if (assignmentName == null || assignmentName.isEmpty()) {
            throw new IllegalArgumentException("Assignment cannot be created.");
        }
        if (assignmentCategory == null || assignmentCategory.isEmpty()) {
            throw new IllegalArgumentException("Assignment cannot be created.");
        }
        if (maxPoints < 1 || maxPoints > 100) {
            throw new IllegalArgumentException("Assignment cannot be created.");
        }
        this.assignmentName = assignmentName;
        this.assignmentCategory = assignmentCategory;
        this.maxPoints = maxPoints;
        this.submissions = new ArrayList<Submission>();
    }

    /** * Adjusts the submission counter to match the current list.
     * If the list is empty, the counter is reset to 0.
     */
    public void setSubmissionId() {
        // Removed the "if (submissions.isEmpty()) return;" block.
        // By initializing high to -1, an empty list will call setCounter(0).
        int high = -1;
        for (int i = 0; i < submissions.size(); i++) {
            if (submissions.get(i).getId() > high) {
                high = submissions.get(i).getId();
            }
        }
        Submission.setCounter(high + 1);
    }

    /**
     * Returns the assignment name.
     * @return assignment name.
     */
    public String getAssignmentName() {
        return assignmentName;
    }

    /**
     * Returns the assignment category.
     * @return assignment category.
     */
    public String getAssignmentCategory() {
        return assignmentCategory;
    }

    /**
     * Returns the maximum points for this assignment.
     * @return max points.
     */
    public int getMaxPoints() {
        return maxPoints;
    }

    /**
     * Creates a new Submission and adds it in sorted order.
     * @param name the submission author name.
     * @param unityId the submission author unity id.
     * @return the id of the newly added submission.
     */
    public int addSubmission(String name, String unityId) {
        return addSubmission(new Submission(name, unityId));
    }

    /**
     * Adds the submission to the list in sorted order by id.
     * Throws IllegalArgumentException if a submission with the same id 
     * or Unity ID already exists.
     * @param submission the submission to add.
     * @return the id of the added submission.
     */
    public int addSubmission(Submission submission) {
        for (int i = 0; i < submissions.size(); i++) {
            // Check for both duplicate numeric ID and duplicate Unity ID.
            if (submissions.get(i).getId() == submission.getId() ||
                submissions.get(i).getUnityId().equals(submission.getUnityId())) {
                throw new IllegalArgumentException("Submission cannot be created.");
            }
        }
        int idx = 0;
        while (idx < submissions.size() && submissions.get(idx).getId() < submission.getId()) {
            idx++;
        }
        submissions.add(idx, submission);
        return submission.getId();
    }

    /**
     * Returns the list of submissions for this assignment.
     * @return list of submissions.
     */
    public List<Submission> getSubmissions() {
        return submissions;
    }

    /**
     * Returns the submission with the given id, or null if not found.
     * @param id the submission id to look for.
     * @return the matching submission or null.
     */
    public Submission getSubmissionById(int id) {
        for (int i = 0; i < submissions.size(); i++) {
            if (submissions.get(i).getId() == id) {
                return submissions.get(i);
            }
        }
        return null;
    }

    /**
     * Executes the given command on the submission with the specified id.
     * @param id the id of the submission to update.
     * @param c the command to execute.
     */
    public void executeCommand(int id, Command c) {
        Submission s = getSubmissionById(id);
        if (s != null) {
            s.update(c);
        }
    }

    /**
     * Deletes the submission with the given id from the list.
     * @param id the id of the submission to delete.
     */
    public void deleteSubmissionById(int id) {
        for (int i = 0; i < submissions.size(); i++) {
            if (submissions.get(i).getId() == id) {
                submissions.remove(i);
                return;
            }
        }
    }

    /**
     * Returns the string representation of the assignment for saving.
     * @return formatted assignment string.
     */
    @Override
    public String toString() {
        String output = "# " + assignmentName + "," + assignmentCategory + "," + maxPoints + "\n";
        for (int i = 0; i < submissions.size(); i++) {
            output = output + submissions.get(i).toString() + "\n";
        }
        return output;
    }
}