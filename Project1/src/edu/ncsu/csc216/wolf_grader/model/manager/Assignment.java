package edu.ncsu.csc216.wolf_grader.model.manager;

import java.util.ArrayList;
import java.util.List;
import edu.ncsu.csc216.wolf_grader.model.submission.Submission;
import edu.ncsu.csc216.wolf_grader.model.command.Command;

/**
 * Stores details for an assignment and manages its student submissions.
 * Keeps everything organized and in the right order.
 */
public class Assignment {
    private String assignmentName;
    private String assignmentCategory;
    private int maxPoints;
    private List<Submission> submissions;

    /**
     * Creates a new assignment category.
     * @param assignmentName name of the work.
     * @param assignmentCategory type of work.
     * @param maxPoints point total for the work.
     */
    public Assignment(String assignmentName, String assignmentCategory, int maxPoints) { 
        if (assignmentName == null || assignmentName.isEmpty() || assignmentCategory == null || assignmentCategory.isEmpty() || maxPoints <= 0) {
            throw new IllegalArgumentException("Invalid assignment details.");
        }
        this.assignmentName = assignmentName;
        this.assignmentCategory = assignmentCategory;
        this.maxPoints = maxPoints;
        this.submissions = new ArrayList<Submission>();
    }

    /** Adjusts the submission counter to match the current list. */
    public void setSubmissionId() { 
        int high = -1;
        for (int i = 0; i < submissions.size(); i++) {
            if (submissions.get(i).getId() > high) {
                high = submissions.get(i).getId();
            }
        }
        Submission.setCounter(high + 1);
    }

    public String getAssignmentName() { return assignmentName; }
    public String getAssignmentCategory() { return assignmentCategory; }
    public int getMaxPoints() { return maxPoints; }

    public int addSubmission(String name, String unityId) { 
        return addSubmission(new Submission(name, unityId));
    }

    public int addSubmission(Submission submission) { 
        for (int i = 0; i < submissions.size(); i++) {
            if (submissions.get(i).getId() == submission.getId() || 
                submissions.get(i).getUnityId().equals(submission.getUnityId())) {
                throw new IllegalArgumentException("Submission already exists.");
            }
        }
        
        int idx = 0;
        while (idx < submissions.size() && submissions.get(idx).getId() < submission.getId()) {
            idx++;
        }
        submissions.add(idx, submission);
        return submission.getId();
    }

    public List<Submission> getSubmissions() { return submissions; }

    public Submission getSubmissionById(int id) { 
        for (int i = 0; i < submissions.size(); i++) {
            if (submissions.get(i).getId() == id) {
                return submissions.get(i);
            }
        }
        return null;
    }

    public void executeCommand(int id, Command c) { 
        Submission s = getSubmissionById(id);
        if (s != null) {
            s.update(c);
        }
    }

    public void deleteSubmissionById(int id) { 
        for (int i = 0; i < submissions.size(); i++) {
            if (submissions.get(i).getId() == id) {
                submissions.remove(i);
                return;
            }
        }
    }

    @Override
    public String toString() { 
        String output = "# " + assignmentName + "," + assignmentCategory + "," + maxPoints + "\n";
        for (int i = 0; i < submissions.size(); i++) {
            output += submissions.get(i).toString() + "\n";
        }
        return output;
    }
}