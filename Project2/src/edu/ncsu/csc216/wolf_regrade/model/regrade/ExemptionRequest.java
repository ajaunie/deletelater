package edu.ncsu.csc216.wolf_regrade.model.regrade;

/**
 * Concrete class for regrade requests for policy exemptions.
 * @author Ajaunie White
 */
public class ExemptionRequest extends RegradeRequest {

    /** Identifier for this request type */
    public static final String TYPE = "Exemption";
    /** Label for the description field */
    public static final String OPEN_TEXT_NAME = "Circumstance Description";
    /** The policy being referenced */
    private String policyReference;

    /**
     * Constructs an ExemptionRequest with the provided details.
     * @param studentName name of the student
     * @param unityId unity ID of the student
     * @param grader name of the grader
     * @param policyReference referenced policy
     * @param circumstances description of circumstances
     */
    public ExemptionRequest(String studentName, String unityId, String grader, String policyReference, String circumstances) {
        super(studentName, unityId, grader, circumstances);
        setPolicyReference(policyReference);
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
     * Retrieves the policy reference.
     * @return the policy reference
     */
    public String getPolicyReference() {
        return policyReference;
    }

    /**
     * Sets the policy reference.
     * @param policyReference the reference to set
     * @throws IllegalArgumentException if the reference is null or blank
     */
    public void setPolicyReference(String policyReference) {
        if (policyReference == null || policyReference.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid regrade request.");
        }
        this.policyReference = policyReference.trim();
    }

    @Override
    public String[] getSubtypeFields() {
        return new String[] { policyReference };
    }
}