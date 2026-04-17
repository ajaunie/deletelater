package edu.ncsu.csc216.wolf_regrade.model.regrade;

/**
 * Concrete class for regrade requests focusing on reviewing a specific item.
 * @author Ajaunie White
 */
public class ReviewRequest extends RegradeRequest {

    /** Identifier for this request type */
    public static final String TYPE = "Review";
    /** Label for the description field */
    public static final String OPEN_TEXT_NAME = "Rationale";
    /** The specific item to be reviewed */
    private String reviewItem;

    /**
     * Constructs a ReviewRequest with the provided details.
     * @param studentName name of the student
     * @param unityId unity ID of the student
     * @param grader name of the grader
     * @param reviewItem item to be reviewed
     * @param rationale text explaining the rationale
     */
    public ReviewRequest(String studentName, String unityId, String grader, String reviewItem, String rationale) {
        super(studentName, unityId, grader, rationale);
        setReviewItem(reviewItem);
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
     * Retrieves the item under review.
     * @return the review item
     */
    public String getReviewItem() {
        return reviewItem;
    }

    /**
     * Sets the item to be reviewed.
     * @param reviewItem the item to set
     * @throws IllegalArgumentException if the item is null or blank
     */
    public void setReviewItem(String reviewItem) {
        if (reviewItem == null || reviewItem.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid regrade request.");
        }
        this.reviewItem = reviewItem.trim();
    }

    @Override
    public String[] getSubtypeFields() {
        return new String[] { reviewItem };
    }
}