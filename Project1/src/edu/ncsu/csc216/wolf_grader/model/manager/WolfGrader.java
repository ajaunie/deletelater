package edu.ncsu.csc216.wolf_grader.model.manager;

import java.util.ArrayList;
import java.util.List;
import edu.ncsu.csc216.wolf_grader.model.command.Command;
import edu.ncsu.csc216.wolf_grader.model.submission.Submission;
import edu.ncsu.csc216.wolf_grader.model.io.AssignmentReader;
import edu.ncsu.csc216.wolf_grader.model.io.AssignmentWriter;

/**
 * Main manager class for the application.
 */
public class WolfGrader {
    private static WolfGrader singleton;
    private Assignment activeAssignment;
    private List<Assignment> assignments;

    private WolfGrader() { assignments = new ArrayList<Assignment>(); }

    public static synchronized WolfGrader getInstance() {
        if (singleton == null) { singleton = new WolfGrader(); }
        return singleton;
    }

    public void saveAssignmentsToFile(String fileName) { 
        if (assignments.size() == 0) { throw new IllegalArgumentException("Unable to save file."); }
        AssignmentWriter.writeAssignmentsToFile(fileName, assignments);
    }

    public void loadAssignmentsFromFile(String fileName) { 
        assignments = AssignmentReader.readAssignmentFile(fileName);
        if (assignments.size() > 0) { activeAssignment = assignments.get(0); }
    }

    public void addNewAssignment(String name, String category, int maxPoints) { 
        for (int i = 0; i < assignments.size(); i++) {
            if (assignments.get(i).getAssignmentName().equalsIgnoreCase(name)) {
                throw new IllegalArgumentException("Assignment already exists.");
            }
        }
        Assignment a = new Assignment(name, category, maxPoints);
        assignments.add(a);
        loadAssignment(name);
    }

    public void loadAssignment(String name) { 
        for (int i = 0; i < assignments.size(); i++) {
            if (assignments.get(i).getAssignmentName().equals(name)) {
                activeAssignment = assignments.get(i);
                activeAssignment.setSubmissionId();
                return;
            }
        }
        throw new IllegalArgumentException("Assignment not found.");
    }

    public String getActiveAssignmentName() { 
        return (activeAssignment == null) ? null : activeAssignment.getAssignmentName();
    }

    public String[] getAssignmentList() { 
        String[] list = new String[assignments.size()];
        for (int i = 0; i < assignments.size(); i++) {
            list[i] = assignments.get(i).getAssignmentName();
        }
        return list;
    }

    public void addSubmissionToAssignment(String name, String unityId) { 
        if (activeAssignment != null) { activeAssignment.addSubmission(name, unityId); }
    }

    public void executeCommand(int id, Command c) { 
        if (activeAssignment != null) { activeAssignment.executeCommand(id, c); }
    }

    public void deleteSubmissionById(int id) { 
        if (activeAssignment != null) { activeAssignment.deleteSubmissionById(id); }
    }

    public String[][] getSubmissionsAsArray(String filter) { 
        if (activeAssignment == null) return new String[0][0];
        List<Submission> allSubs = activeAssignment.getSubmissions();
        ArrayList<Submission> filtered = new ArrayList<Submission>();
        for (int i = 0; i < allSubs.size(); i++) {
            if ("All".equals(filter) || allSubs.get(i).getState().equals(filter)) {
                filtered.add(allSubs.get(i));
            }
        }
        String[][] results = new String[filtered.size()][4];
        for (int i = 0; i < filtered.size(); i++) {
            Submission s = filtered.get(i);
            results[i][0] = "" + s.getId();
            results[i][1] = s.getState();
            results[i][2] = s.getName() + " (" + s.getUnityId() + ")";
            results[i][3] = s.getCheckResult();
        }
        return results;
    }

    public Submission getSubmissionById(int id) { 
        return (activeAssignment == null) ? null : activeAssignment.getSubmissionById(id);
    }

    protected void resetManager() { 
        singleton = null;
        Submission.setCounter(0);
    }
}