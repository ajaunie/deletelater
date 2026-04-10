package edu.ncsu.csc216.wolf_regrade.model.regrade;

/**
 * request type for reviewing a grading item
 * 
 * @author Ajaunie White
 */
public class ReviewRequest extends RegradeRequest {

    public static final String TYPE = "Review";
    public static final String OPEN_TEXT_NAME = "Rationale";
    private String reviewItem;

    public ReviewRequest(String studentName, String unityId, String grader, String reviewItem, String rationale) {
        super(studentName, unityId, grader, rationale);
        setReviewItem(reviewItem);
    }

    public String getReviewItem() {
        return reviewItem;
    }

    /**
     * Sets the review item
     * @param reviewItem item to set
     */
    public void setReviewItem(String reviewItem) {
        this.reviewItem = reviewItem;
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