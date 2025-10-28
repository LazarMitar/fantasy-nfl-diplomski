package rs.fantasy.fantasyfootball.dto;

public class AssignInjuryRequest {
    private Long injuryId;
    private Integer estimatedRecoveryWeeks;
    private String injuryDate;

    public AssignInjuryRequest() {}

    public AssignInjuryRequest(Long injuryId, Integer estimatedRecoveryWeeks, String injuryDate) {
        this.injuryId = injuryId;
        this.estimatedRecoveryWeeks = estimatedRecoveryWeeks;
        this.injuryDate = injuryDate;
    }

    public Long getInjuryId() {
        return injuryId;
    }

    public void setInjuryId(Long injuryId) {
        this.injuryId = injuryId;
    }

    public Integer getEstimatedRecoveryWeeks() {
        return estimatedRecoveryWeeks;
    }

    public void setEstimatedRecoveryWeeks(Integer estimatedRecoveryWeeks) {
        this.estimatedRecoveryWeeks = estimatedRecoveryWeeks;
    }

    public String getInjuryDate() {
        return injuryDate;
    }

    public void setInjuryDate(String injuryDate) {
        this.injuryDate = injuryDate;
    }
}

