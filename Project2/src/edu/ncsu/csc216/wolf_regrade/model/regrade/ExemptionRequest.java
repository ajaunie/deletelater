package edu.ncsu.csc216.wolf_regrade.model.regrade;

/**
 * request type for policy exemptions
 * 
 * @author Ajaunie White
 */
public class ExemptionRequest extends RegradeRequest {

    public static final String TYPE = "Exemption";
    public static final String OPEN_TEXT_NAME = "Circumstance Description";
    private String policyReference;

    public ExemptionRequest(String studentName, String unityId, String grader, String policyReference, String circumstances) {
        super(studentName, unityId, grader, circumstances);
        setPolicyReference(policyReference);
    }

    public String getPolicyReference() {
        return policyReference;
    }

    /**
     * Sets policy reference
     * @param policyReference reference to set
     */
    public void setPolicyReference(String policyReference) {
        this.policyReference = policyReference;
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