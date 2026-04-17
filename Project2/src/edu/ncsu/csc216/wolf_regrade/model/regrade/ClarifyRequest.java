package edu.ncsu.csc216.wolf_regrade.model.regrade;

/**
 * Concrete class for regrade requests seeking feedback clarification.
 * @author Ajaunie White
 */
public class ClarifyRequest extends RegradeRequest {

    /** Identifier for this request type */
    public static final String TYPE = "Clarify";
    /** Label for the description field */
    public static final String OPEN_TEXT_NAME = "Question";
    /** The specific feedback item to clarify */
    private String feedbackItem;

    /**
     * Constructs a ClarifyRequest with the provided details.
     * @param studentName name of the student
     * @param unityId unity ID of the student
     * @param grader name of the grader
     * @param feedbackItem item to clarify
     * @param question the clarification question
     */
    public ClarifyRequest(String studentName, String unityId, String grader, String feedbackItem, String question) {
        super(studentName, unityId, grader, question);
        setFeedbackItem(feedbackItem);
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public String getOpenTextName() {
        return OPEN_TEXT_NAME;
    }

    /**
     * Retrieves the feedback item.
     * @return the feedback item
     */
    public String getFeedbackItem() {
        return feedbackItem;
    }

    /**
     * Sets the feedback item.
     * @param feedbackItem the item to set
     * @throws IllegalArgumentException if the item is null or blank
     */
    public void setFeedbackItem(String feedbackItem) {
        if (feedbackItem == null || feedbackItem.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid regrade request.");
        }
        this.feedbackItem = feedbackItem.trim();
    }

    /**
     * ClarifyRequests cannot have a 'Closed' resolution.
     * @param resolution the resolution to set
     * @throws IllegalArgumentException if resolution is 'Closed'
     */
    @Override
    public void setResolution(String resolution) {
        if (RESOLUTION_CLOSED.equals(resolution)) {
            throw new IllegalArgumentException("Invalid resolution.");
        }
        super.setResolution(resolution);
    }

    @Override
    public String[] getSubtypeFields() {
        return new String[] { feedbackItem };
    }
}