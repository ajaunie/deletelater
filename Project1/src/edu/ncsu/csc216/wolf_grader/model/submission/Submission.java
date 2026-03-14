package edu.ncsu.csc216.wolf_grader.model.submission;

import edu.ncsu.csc216.wolf_grader.model.command.Command;

/**
 * Represents a student submission in the WolfGrader system.
 * Uses the State Pattern to manage transitions through the grading workflow FSM.
 * Contains the SubmissionState inner interface and all seven concrete state inner classes.
 * @author Ajaunie White
 */
public class Submission {

    /**
     * Represents the possible results of a plagiarism check on a submission.
     */
    public enum PlagiarismCheckResult { PASS, FAIL, NONE }

    /** Plagiarism Check Result is passed. */
    public static final String PC_PASS = "Passed";
    /** Plagiarism Check Result is failed. */
    public static final String PC_FAIL = "Failed";
    /** Plagiarism Check Result is not done yet. */
    public static final String PC_NONE = "Not Done";

    /** A constant string for the upload state's name. */
    public static final String UPLOAD_NAME = "Upload";
    /** A constant string for the feedback state's name. */
    public static final String FEEDBACK_NAME = "Feedback";
    /** A constant string for the submitted state's name. */
    public static final String SUBMITTED_NAME = "Submitted";
    /** A constant string for the grade state's name. */
    public static final String GRADE_NAME = "Grade";
    /** A constant string for the regrade state's name. */
    public static final String REGRADE_NAME = "Regrade";
    /** A constant string for the check state's name. */
    public static final String CHECK_NAME = "Check";
    /** A constant string for the return state's name. */
    public static final String RETURN_NAME = "Return";

    /** A constant string for the grade A. */
    public static final String GRADE_A_RETURN = "A";
    /** A constant string for the grade B. */
    public static final String GRADE_B_RETURN = "B";
    /** A constant string for the grade C. */
    public static final String GRADE_C_RETURN = "C";
    /** A constant string for the grade F. */
    public static final String GRADE_F_RETURN = "F";
    /** A constant string for the grade IN. */
    public static final String GRADE_IN_RETURN = "IN";
    /** A constant string for the grade F-AIV. */
    public static final String GRADE_F_AIV_RETURN = "F-AIV";

    /** Unique id for a submission. */
    private int submissionId;
    /** Current state for the submission. */
    private SubmissionState currentState;
    /** Author name of the submission. */
    private String name;
    /** UnityId of the submission author. */
    private String unityId;
    /** True if the submission's feedback has been processed, false otherwise. */
    private boolean feedbackProcessed;
    /** Results of the plagiarism check. */
    private PlagiarismCheckResult checkResult;
    /** True if the grades have been finalized and published, false otherwise. */
    private boolean published;
    /** Grader assigned to review or grade the submission. Null if not yet assigned. */
    private String grader;
    /** Assigned grade on the submission. Null if not yet graded. */
    private String grade;

    /** Shared counter used to generate unique submission ids. */
    private static int counter = 0;

    /** Final instance of the UploadState inner class. */
    private final SubmissionState uploadState = new UploadState();
    /** Final instance of the FeedbackState inner class. */
    private final SubmissionState feedbackState = new FeedbackState();
    /** Final instance of the SubmittedState inner class. */
    private final SubmissionState submittedState = new SubmittedState();
    /** Final instance of the GradeState inner class. */
    private final SubmissionState gradeState = new GradeState();
    /** Final instance of the RegradeState inner class. */
    private final SubmissionState regradeState = new RegradeState();
    /** Final instance of the CheckState inner class. */
    private final SubmissionState checkState = new CheckState();
    /** Final instance of the ReturnState inner class. */
    private final SubmissionState returnState = new ReturnState();

    /**
	 * Interface for states in the Submission State Pattern.  All 
	 * concrete Submission states must implement the SubmissionState interface.
	 * The SubmissionState interface should be a private interface of the 
	 * Submission class.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 * @author Dr. Chandrika Satyavolu (jsatyav@ncsu.edu)
	 */
	private interface SubmissionState {
		
		/**
		 * Update the Submission from the given Command.
		 * An UnsupportedOperationException is thrown if the Command
		 * is not a valid action for the given state.  
		 * @param command Command describing the action that will update the Submission's
		 * state.
		 * @throws UnsupportedOperationException if the Command is not a valid action
		 * for the given state.
		 */
		void updateState(Command command);
		
		/**
		 * Returns the name of the current state as a String.
		 * @return the name of the current state as a String.
		 */
		String getStateName();
	}

    /**
     * Constructs a new Submission from the name and unity id provided by the user interface.
     * The submission starts in the Upload state. Throws IllegalArgumentException
     * if name or unityId is null or empty.
     * @param name author name of the submission
     * @param unityId unity id of the submission author
     */
    public Submission(String name, String unityId) {
        if (name == null || name.isEmpty() || unityId == null || unityId.isEmpty()) {
            throw new IllegalArgumentException("Submission cannot be created.");
        }
        this.submissionId = counter;
        incrementCounter();
        this.name = name;
        this.unityId = unityId;
        this.currentState = uploadState;
        this.checkResult = PlagiarismCheckResult.NONE;
        this.feedbackProcessed = false;
        this.published = false;
        this.grader = null;
        this.grade = null;
    }

    /**
     * Constructs a Submission from data loaded from a file.
     * All parameter values are validated against the data format rules.
     * Throws IllegalArgumentException with "Submission cannot be created." for any invalid value.
     * @param id              unique submission id
     * @param state           state name string
     * @param name            author name
     * @param unityId         author unity id
     * @param processed       whether feedback has been processed
     * @param checkResult     plagiarism check result string
     * @param published       whether grades have been published
     * @param grader          grader id string (empty string means no grader)
     * @param grade           assigned grade string (empty string means no grade)
     */
    public Submission(int id, String state, String name, String unityId, boolean processed,
                      String checkResult, boolean published, String grader, String grade) {

        setState(state);
        setFeedbackProcessed(processed);

        String stateName = currentState.getStateName();

        if (FEEDBACK_NAME.equals(stateName) && feedbackProcessed) {
            throw new IllegalArgumentException("Submission cannot be created.");
        }

        if (SUBMITTED_NAME.equals(stateName) && !feedbackProcessed) {
            throw new IllegalArgumentException("Submission cannot be created.");
        }

        if (GRADE_NAME.equals(stateName) && !feedbackProcessed) {
            throw new IllegalArgumentException("Submission cannot be created.");
        }

        if (CHECK_NAME.equals(stateName) && !feedbackProcessed) {
            throw new IllegalArgumentException("Submission cannot be created.");
        }

        if (RETURN_NAME.equals(stateName) && !feedbackProcessed) {
            throw new IllegalArgumentException("Submission cannot be created.");
        }

        if (REGRADE_NAME.equals(stateName) && !feedbackProcessed) {
            throw new IllegalArgumentException("Submission cannot be created.");
        }

        setName(name);
        setUnityId(unityId);
        setCheckResult(checkResult);

        if (UPLOAD_NAME.equals(stateName) && this.checkResult != PlagiarismCheckResult.NONE) {
            throw new IllegalArgumentException("Submission cannot be created.");
        }

        if (FEEDBACK_NAME.equals(stateName) && this.checkResult != PlagiarismCheckResult.NONE) {
            throw new IllegalArgumentException("Submission cannot be created.");
        }

        if (SUBMITTED_NAME.equals(stateName) && this.checkResult != PlagiarismCheckResult.NONE) {
            throw new IllegalArgumentException("Submission cannot be created.");
        }

        if (CHECK_NAME.equals(stateName) && this.checkResult != PlagiarismCheckResult.NONE) {
            throw new IllegalArgumentException("Submission cannot be created.");
        }

        if (REGRADE_NAME.equals(stateName) && this.checkResult != PlagiarismCheckResult.PASS) {
            throw new IllegalArgumentException("Submission cannot be created.");
        }

        if (RETURN_NAME.equals(stateName) && this.checkResult == PlagiarismCheckResult.NONE) {
            throw new IllegalArgumentException("Submission cannot be created.");
        }

        setGrader(grader);

        if (UPLOAD_NAME.equals(stateName) && this.grader != null) {
            throw new IllegalArgumentException("Submission cannot be created.");
        }

        if (SUBMITTED_NAME.equals(stateName) && this.grader != null) {
            throw new IllegalArgumentException("Submission cannot be created.");
        }

        if (FEEDBACK_NAME.equals(stateName) && this.grader == null) {
            throw new IllegalArgumentException("Submission cannot be created.");
        }

        if (GRADE_NAME.equals(stateName) && this.grader == null) {
            throw new IllegalArgumentException("Submission cannot be created.");
        }

        if (CHECK_NAME.equals(stateName) && this.grader == null) {
            throw new IllegalArgumentException("Submission cannot be created.");
        }

        if (RETURN_NAME.equals(stateName) && this.grader == null) {
            throw new IllegalArgumentException("Submission cannot be created.");
        }

        if (REGRADE_NAME.equals(stateName) && this.grader == null) {
            throw new IllegalArgumentException("Submission cannot be created.");
        }

        if (grade == null) {
            throw new IllegalArgumentException("Submission cannot be created.");
        }
        boolean hasGrade = !grade.isEmpty();

        if (UPLOAD_NAME.equals(stateName) && hasGrade) {
            throw new IllegalArgumentException("Submission cannot be created.");
        }

        if (FEEDBACK_NAME.equals(stateName) && hasGrade) {
            throw new IllegalArgumentException("Submission cannot be created.");
        }

        if (SUBMITTED_NAME.equals(stateName) && hasGrade) {
            throw new IllegalArgumentException("Submission cannot be created.");
        }

        if (CHECK_NAME.equals(stateName) && !hasGrade) {
            throw new IllegalArgumentException("Submission cannot be created.");
        }

        if (REGRADE_NAME.equals(stateName) && !hasGrade) {
            throw new IllegalArgumentException("Submission cannot be created.");
        }

        if (RETURN_NAME.equals(stateName) && !hasGrade) {
            throw new IllegalArgumentException("Submission cannot be created.");
        }

        if (hasGrade) {
            if (!GRADE_A_RETURN.equals(grade) && !GRADE_B_RETURN.equals(grade)
                    && !GRADE_C_RETURN.equals(grade) && !GRADE_F_RETURN.equals(grade)
                    && !GRADE_IN_RETURN.equals(grade) && !GRADE_F_AIV_RETURN.equals(grade)) {
                throw new IllegalArgumentException("Submission cannot be created.");
            }

            if (GRADE_NAME.equals(stateName) && this.checkResult == PlagiarismCheckResult.NONE) {
                throw new IllegalArgumentException("Submission cannot be created.");
            }

            if (CHECK_NAME.equals(stateName) && !GRADE_A_RETURN.equals(grade)
                    && !GRADE_B_RETURN.equals(grade) && !GRADE_C_RETURN.equals(grade)
                    && !GRADE_F_RETURN.equals(grade)) {
                throw new IllegalArgumentException("Submission cannot be created.");
            }

            if (GRADE_NAME.equals(stateName) && GRADE_F_AIV_RETURN.equals(grade)) {
                throw new IllegalArgumentException("Submission cannot be created.");
            }

            if (GRADE_NAME.equals(stateName) && GRADE_IN_RETURN.equals(grade)) {
                throw new IllegalArgumentException("Submission cannot be created.");
            }

            if (RETURN_NAME.equals(stateName) && this.checkResult == PlagiarismCheckResult.FAIL
                    && (GRADE_A_RETURN.equals(grade) || GRADE_B_RETURN.equals(grade)
                        || GRADE_C_RETURN.equals(grade) || GRADE_F_RETURN.equals(grade))) {
                throw new IllegalArgumentException("Submission cannot be created.");
            }

            if (RETURN_NAME.equals(stateName) && this.checkResult == PlagiarismCheckResult.PASS
                    && (GRADE_F_AIV_RETURN.equals(grade) || GRADE_IN_RETURN.equals(grade))) {
                throw new IllegalArgumentException("Submission cannot be created.");
            }

            this.grade = grade;
        } else {
            this.grade = null;
        }

        if (published) {
            if (!RETURN_NAME.equals(stateName)) {
                throw new IllegalArgumentException("Submission cannot be created.");
            }
            if (GRADE_IN_RETURN.equals(this.grade)) {
                throw new IllegalArgumentException("Submission cannot be created.");
            }
            if (this.grade == null) {
                throw new IllegalArgumentException("Submission cannot be created.");
            }
        }
        this.published = published;

        if (id < 0) {
            throw new IllegalArgumentException("Submission cannot be created.");
        }
        this.submissionId = id;
        if (id >= counter) {
            setCounter(id + 1);
        }
    }

    /**
     * Sets the current state from a state name string.
     * @param stateValue the name of the target state
     */
    private void setState(String stateValue) {
        if (stateValue == null) {
            throw new IllegalArgumentException("Submission cannot be created.");
        }
        if (UPLOAD_NAME.equals(stateValue)) {
            currentState = uploadState;
        } else if (FEEDBACK_NAME.equals(stateValue)) {
            currentState = feedbackState;
        } else if (SUBMITTED_NAME.equals(stateValue)) {
            currentState = submittedState;
        } else if (GRADE_NAME.equals(stateValue)) {
            currentState = gradeState;
        } else if (REGRADE_NAME.equals(stateValue)) {
            currentState = regradeState;
        } else if (CHECK_NAME.equals(stateValue)) {
            currentState = checkState;
        } else if (RETURN_NAME.equals(stateValue)) {
            currentState = returnState;
        } else {
            throw new IllegalArgumentException("Submission cannot be created.");
        }
    }

    /**
     * Sets the submission author name.
     * @param name the author name to set
     */
    private void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Submission cannot be created.");
        }
        this.name = name;
    }

    /**
     * Sets the unity id.
     * @param unityId the unity id to set
     */
    private void setUnityId(String unityId) {
        if (unityId == null || unityId.isEmpty()) {
            throw new IllegalArgumentException("Submission cannot be created.");
        }
        this.unityId = unityId;
    }

    /**
     * Sets the feedbackProcessed field.
     * @param processed the processed value to set
     */
    private void setFeedbackProcessed(boolean processed) {
        this.feedbackProcessed = processed;
    }

    /**
     * Sets checkResult from its string representation.
     * @param checkResult the string check result value
     */
    private void setCheckResult(String checkResult) {
        if (PC_PASS.equals(checkResult)) {
            this.checkResult = PlagiarismCheckResult.PASS;
        } else if (PC_FAIL.equals(checkResult)) {
            this.checkResult = PlagiarismCheckResult.FAIL;
        } else if (PC_NONE.equals(checkResult)) {
            this.checkResult = PlagiarismCheckResult.NONE;
        } else {
            throw new IllegalArgumentException("Submission cannot be created.");
        }
    }

    /**
     * Sets the grader field. Sets to null if the string is empty.
     * Throws IllegalArgumentException if grader is null, since null is not
     * a valid file format value — only empty string represents no grader.
     * @param grader the grader id string
     */
    private void setGrader(String grader) {
        if (grader == null) {
            throw new IllegalArgumentException("Submission cannot be created.");
        }
        if (grader.isEmpty()) {
            this.grader = null;
        } else {
            this.grader = grader;
        }
    }

    /**
     * Increments the shared submission id counter.
     */
    public static void incrementCounter() {
        counter++;
    }

    /**
     * Sets the shared counter to a specific value.
     * @param newCount the new counter value
     */
    public static void setCounter(int newCount) {
        counter = newCount;
    }

    /**
     * Returns the submission id.
     * @return submission id
     */
    public int getId() {
        return submissionId;
    }

    /**
     * Returns the name of the current state.
     * @return current state name
     */
    public String getState() {
        return currentState.getStateName();
    }

    /**
     * Returns the author name.
     * @return author name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the unity id.
     * @return unity id
     */
    public String getUnityId() {
        return unityId;
    }

    /**
     * Returns the string representation of the plagiarism check result.
     * @return check result string
     */
    public String getCheckResult() {
        if (checkResult == PlagiarismCheckResult.PASS) {
            return PC_PASS;
        }
        if (checkResult == PlagiarismCheckResult.FAIL) {
            return PC_FAIL;
        }
        return PC_NONE;
    }

    /**
     * Returns whether feedback has been processed.
     * @return true if feedback was processed
     */
    public boolean isFeedbackProcessed() {
        return feedbackProcessed;
    }

    /**
     * Returns whether grades have been published.
     * @return true if published
     */
    public boolean isPublished() {
        return published;
    }

    /**
     * Returns the grader id, or an empty string if no grader is assigned.
     * @return grader id or empty string
     */
    public String getGrader() {
        if (grader == null) {
            return "";
        }
        return grader;
    }

    /**
     * Returns the grade, or an empty string if no grade is assigned.
     * @return grade or empty string
     */
    public String getGrade() {
        if (grade == null) {
            return "";
        }
        return grade;
    }

    /**
     * Delegates a command to the current state for processing.
     * @param c the command to execute
     * @throws UnsupportedOperationException if the command is invalid for the current state
     */
    public void update(Command c) {
        currentState.updateState(c);
    }

    /**
     * Returns the file-format string representation of this submission.
     * @return formatted submission string
     */
    @Override
    public String toString() {
        return "* " + submissionId + "," + getState() + "," + name + "," + unityId + ","
                + feedbackProcessed + "," + getCheckResult() + "," + published + ","
                + getGrader() + "," + getGrade();
    }

    /**
     * Represents the Upload state of the WolfGrader FSM.
     * A submission in this state can be assigned to a grader, re-uploaded, or moved to Submitted.
     */
    private class UploadState implements SubmissionState {

        /**
         * Handles commands valid in the Upload state.
         * ASSIGN moves to Feedback if feedback has not been given before.
         * REUPLOAD stays in Upload if feedback has already been processed.
         * MOVE transitions to Submitted if feedback has already been processed.
         * @param command the command to execute
         * @throws UnsupportedOperationException if the command is not valid in this state
         */
        @Override
        public void updateState(Command command) {
            if (command.getCommand() == Command.CommandValue.ASSIGN) {
                if (feedbackProcessed) {
                    throw new UnsupportedOperationException("Invalid command.");
                }
                grader = command.getCommandInformation();
                currentState = feedbackState;
            } else if (command.getCommand() == Command.CommandValue.REUPLOAD) {
                if (!feedbackProcessed) {
                    throw new UnsupportedOperationException("Invalid command.");
                }
            } else if (command.getCommand() == Command.CommandValue.MOVE) {
                if (!feedbackProcessed) {
                    throw new UnsupportedOperationException("Invalid command.");
                }
                currentState = submittedState;
            } else {
                throw new UnsupportedOperationException("Invalid command.");
            }
        }

        /**
         * Returns the name of the Upload state.
         * @return Upload state name constant
         */
        @Override
        public String getStateName() {
            return UPLOAD_NAME;
        }
    }

    /**
     * Represents the Feedback state of the WolfGrader FSM.
     * A submission in this state is awaiting grader feedback before returning to Upload.
     */
    private class FeedbackState implements SubmissionState {

        /**
         * Handles commands valid in the Feedback state.
         * PROVIDE moves the submission back to Upload and sets feedbackProcessed to true.
         * @param command the command to execute
         * @throws UnsupportedOperationException if the command is not valid in this state
         */
        @Override
        public void updateState(Command command) {
            if (command.getCommand() == Command.CommandValue.PROVIDE) {
                feedbackProcessed = true;
                grader = null;
                currentState = uploadState;
            } else {
                throw new UnsupportedOperationException("Invalid command.");
            }
        }

        /**
         * Returns the name of the Feedback state.
         * @return Feedback state name constant
         */
        @Override
        public String getStateName() {
            return FEEDBACK_NAME;
        }
    }

    /**
     * Represents the Submitted state of the WolfGrader FSM.
     * A submission in this state is waiting to be assigned a grader for evaluation.
     */
    private class SubmittedState implements SubmissionState {

        /**
         * Handles commands valid in the Submitted state.
         * ASSIGN moves the submission to Grade with the given grader.
         * @param command the command to execute
         * @throws UnsupportedOperationException if the command is not valid in this state
         */
        @Override
        public void updateState(Command command) {
            if (command.getCommand() == Command.CommandValue.ASSIGN) {
                grader = command.getCommandInformation();
                currentState = gradeState;
            } else {
                throw new UnsupportedOperationException("Invalid command.");
            }
        }

        /**
         * Returns the name of the Submitted state.
         * @return Submitted state name constant
         */
        @Override
        public String getStateName() {
            return SUBMITTED_NAME;
        }
    }

    /**
     * Represents the Grade state of the WolfGrader FSM.
     * A submission in this state is being graded or re-evaluated after a plagiarism result.
     */
    private class GradeState implements SubmissionState {

        /**
         * Handles commands valid in the Grade state.
         * ANALYZE assigns a letter grade and moves to Check only if check result is Not Done.
         * SUBMIT assigns a grade and moves to Return based on the current check result rules.
         * @param command the command to execute
         * @throws UnsupportedOperationException if the command is not valid in this state
         */
        @Override
        public void updateState(Command command) {
            if (command.getCommand() == Command.CommandValue.ANALYZE) {
                if (checkResult != PlagiarismCheckResult.NONE) {
                    throw new UnsupportedOperationException("Invalid command.");
                }
                grade = command.getCommandInformation();
                currentState = checkState;
            } else if (command.getCommand() == Command.CommandValue.SUBMIT) {
                String submitGrade = command.getCommandInformation();
                if (checkResult == PlagiarismCheckResult.NONE) {
                    throw new UnsupportedOperationException("Invalid command.");
                }
                if (checkResult == PlagiarismCheckResult.FAIL) {
                    if (GRADE_A_RETURN.equals(submitGrade) || GRADE_B_RETURN.equals(submitGrade)
                            || GRADE_C_RETURN.equals(submitGrade) || GRADE_F_RETURN.equals(submitGrade)) {
                        checkResult = PlagiarismCheckResult.PASS;
                    } else if (!GRADE_IN_RETURN.equals(submitGrade) && !GRADE_F_AIV_RETURN.equals(submitGrade)) {
                        throw new UnsupportedOperationException("Invalid command.");
                    }
                } else {
                    if (!GRADE_A_RETURN.equals(submitGrade) && !GRADE_B_RETURN.equals(submitGrade)
                            && !GRADE_C_RETURN.equals(submitGrade) && !GRADE_F_RETURN.equals(submitGrade)) {
                        throw new UnsupportedOperationException("Invalid command.");
                    }
                }
                grade = submitGrade;
                currentState = returnState;
            } else {
                throw new UnsupportedOperationException("Invalid command.");
            }
        }

        /**
         * Returns the name of the Grade state.
         * @return Grade state name constant
         */
        @Override
        public String getStateName() {
            return GRADE_NAME;
        }
    }

    /**
     * Represents the Check state of the WolfGrader FSM.
     * A submission in this state is undergoing plagiarism analysis.
     */
    private class CheckState implements SubmissionState {

        /**
         * Handles commands valid in the Check state.
         * PASS sets checkResult to Passed and moves to Return.
         * FAIL sets checkResult to Failed and moves back to Grade for manual review.
         * @param command the command to execute
         * @throws UnsupportedOperationException if the command is not valid in this state
         */
        @Override
        public void updateState(Command command) {
            if (command.getCommand() == Command.CommandValue.PASS) {
                checkResult = PlagiarismCheckResult.PASS;
                currentState = returnState;
            } else if (command.getCommand() == Command.CommandValue.FAIL) {
                checkResult = PlagiarismCheckResult.FAIL;
                currentState = gradeState;
            } else {
                throw new UnsupportedOperationException("Invalid command.");
            }
        }

        /**
         * Returns the name of the Check state.
         * @return Check state name constant
         */
        @Override
        public String getStateName() {
            return CHECK_NAME;
        }
    }

    /**
     * Represents the Regrade state of the WolfGrader FSM.
     * A submission in this state has an active regrade request pending review.
     */
    private class RegradeState implements SubmissionState {

        /**
         * Handles commands valid in the Regrade state.
         * PROCESS moves the submission back to Grade so it can be re-evaluated.
         * @param command the command to execute
         * @throws UnsupportedOperationException if the command is not valid in this state
         */
        @Override
        public void updateState(Command command) {
            if (command.getCommand() == Command.CommandValue.PROCESS) {
                currentState = gradeState;
            } else {
                throw new UnsupportedOperationException("Invalid command.");
            }
        }

        /**
         * Returns the name of the Regrade state.
         * @return Regrade state name constant
         */
        @Override
        public String getStateName() {
            return REGRADE_NAME;
        }
    }

    /**
     * Represents the Return state of the WolfGrader FSM.
     * A submission in this state has completed grading and may be published, resolved, or regraded.
     */
    private class ReturnState implements SubmissionState {

        /**
         * Handles commands valid in the Return state.
         * REQUEST moves to Regrade only if the current grade is a letter grade.
         * RESOLVE changes an IN grade to a new grade and may revert the check result to Passed.
         * PUBLISH sets published to true if not already published and the grade is not IN.
         * @param command the command to execute
         * @throws UnsupportedOperationException if the command is not valid in this state
         */
        @Override
        public void updateState(Command command) {
            if (command.getCommand() == Command.CommandValue.REQUEST) {
                if (!GRADE_A_RETURN.equals(grade) && !GRADE_B_RETURN.equals(grade)
                        && !GRADE_C_RETURN.equals(grade) && !GRADE_F_RETURN.equals(grade)) {
                    throw new UnsupportedOperationException("Invalid command.");
                }
                currentState = regradeState;
            } else if (command.getCommand() == Command.CommandValue.RESOLVE) {
                if (!GRADE_IN_RETURN.equals(grade)) {
                    throw new UnsupportedOperationException("Invalid command.");
                }
                String resolveGrade = command.getCommandInformation();
                if (GRADE_A_RETURN.equals(resolveGrade) || GRADE_B_RETURN.equals(resolveGrade)
                        || GRADE_C_RETURN.equals(resolveGrade) || GRADE_F_RETURN.equals(resolveGrade)) {
                    checkResult = PlagiarismCheckResult.PASS;
                }
                grade = resolveGrade;
            } else if (command.getCommand() == Command.CommandValue.PUBLISH) {
                if (published || GRADE_IN_RETURN.equals(grade)) {
                    throw new UnsupportedOperationException("Invalid command.");
                }
                published = true;
            } else {
                throw new UnsupportedOperationException("Invalid command.");
            }
        }

        /**
         * Returns the name of the Return state.
         * @return Return state name constant
         */
        @Override
        public String getStateName() {
            return RETURN_NAME;
        }
    }
}