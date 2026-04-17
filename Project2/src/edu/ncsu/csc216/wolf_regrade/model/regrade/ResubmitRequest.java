package edu.ncsu.csc216.wolf_regrade.model.regrade;

/**
 * Concrete class for regrade requests involving work resubmission.
 * @author Ajaunie White
 */
public class ResubmitRequest extends RegradeRequest {

    /** Identifier for this request type */
    public static final String TYPE = "Resubmit";
    /** Label for the description field */
    public static final String OPEN_TEXT_NAME = "Resubmit Reason";
    /** URL to the repository */
    private String repoLink;
    /** The specific Git commit hash */
    private String commitHash;

    /**
     * Constructs a ResubmitRequest with the provided details.
     * @param studentName name of the student
     * @param unityId unity ID of the student
     * @param grader name of the grader
     * @param repoLink repository link
     * @param commitHash commit hash
     * @param reason reason for resubmitting
     */
    public ResubmitRequest(String studentName, String unityId, String grader, String repoLink, String commitHash, String reason) {
        super(studentName, unityId, grader, reason);
        setRepoLink(repoLink);
        setCommitHash(commitHash);
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
     * Retrieves the repository link.
     * @return the repo link
     */
    public String getRepoLink() {
        return repoLink;
    }

    /**
     * Sets the repository link.
     * @param repoLink the link to set
     * @throws IllegalArgumentException if the link is null or blank
     */
    public void setRepoLink(String repoLink) {
        if (repoLink == null || repoLink.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid regrade request.");
        }
        this.repoLink = repoLink.trim();
    }

    /**
     * Retrieves the commit hash.
     * @return the commit hash
     */
    public String getCommitHash() {
        return commitHash;
    }

    /**
     * Sets the commit hash.
     * @param commitHash the hash to set
     * @throws IllegalArgumentException if the hash is null or blank
     */
    public void setCommitHash(String commitHash) {
        if (commitHash == null || commitHash.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid regrade request.");
        }
        this.commitHash = commitHash.trim();
    }

    @Override
    public String[] getSubtypeFields() {
        return new String[] { repoLink, commitHash };
    }
}