package edu.ncsu.csc216.wolf_regrade.model.regrade;

/**
 * Specific request type for clarifying feedback.
 */
public class ClarifyRequest extends RegradeRequest {

    public static final String TYPE = "Clarify";
    public static final String OPEN_TEXT_NAME = "Question";
    private String feedbackItem;

    public ClarifyRequest(String studentName, String unityId, String grader, String feedbackItem, String question) {
        super(studentName, unityId, grader, question);
        setFeedbackItem(feedbackItem);
    }

    public String getFeedbackItem() {
        return feedbackItem;
    }

    /**
     * Sets the feedback item.
     * @param feedbackItem item to set
     */
    public void setFeedbackItem(String feedbackItem) {
        this.feedbackItem = feedbackItem;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public String getOpenTextName() {
        return OPEN_TEXT_NAME;
    }

    @Override
    public String[] getSubtypeFields() {
        return null;
    }
}