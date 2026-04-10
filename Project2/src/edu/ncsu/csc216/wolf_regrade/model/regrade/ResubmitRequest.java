package edu.ncsu.csc216.wolf_regrade.model.regrade;

/**
 * Specific request type for resubmitting work.
 * 
 * @author Ajaunie White
 */
public class ResubmitRequest extends RegradeRequest {

    public static final String TYPE = "Resubmit";
    public static final String OPEN_TEXT_NAME = "Resubmit Reason";
    private String repoLink;
    private String commitHash;

    public ResubmitRequest(String studentName, String unityId, String grader, String repoLink, String commitHash, String reason) {
        super(studentName, unityId, grader, reason);
        setRepoLink(repoLink);
        setCommitHash(commitHash);
    }

    public String getRepoLink() {
        return repoLink;
    }

    /**
     * Sets repo link
     * @param repoLink link to set
     */
    public void setRepoLink(String repoLink) {
        this.repoLink = repoLink;
    }

    public String getCommitHash() {
        return commitHash;
    }

    /**
     * Sets the commit hash
     * @param commitHash hash to set
     */
    public void setCommitHash(String commitHash) {
        this.commitHash = commitHash;
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