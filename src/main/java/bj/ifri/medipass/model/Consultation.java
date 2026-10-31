package bj.ifri.medipass.model;

import java.time.LocalDate;
import java.util.Objects;

/** One entry of the medical pass: a visit of a patient to a doctor. */
public class Consultation {
    private Long id;
    private Long patientId;
    private Long medecinId;
    private LocalDate date;
    private String motif;
    private String diagnostic;
    private String prescription;

    public Consultation() { }

    public Consultation(Long patientId, Long medecinId, LocalDate date,
                        String motif, String diagnostic, String prescription) {
        this.patientId = patientId;
        this.medecinId = medecinId;
        this.date = date;
        this.motif = motif;
        this.diagnostic = diagnostic;
        this.prescription = prescription;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public Long getMedecinId() { return medecinId; }
    public void setMedecinId(Long medecinId) { this.medecinId = medecinId; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }
    public String getDiagnostic() { return diagnostic; }
    public void setDiagnostic(String diagnostic) { this.diagnostic = diagnostic; }
    public String getPrescription() { return prescription; }
    public void setPrescription(String prescription) { this.prescription = prescription; }

    @Override public boolean equals(Object o) {
        return o instanceof Consultation c && id != null && id.equals(c.id);
    }
    @Override public int hashCode() { return Objects.hashCode(id); }
}
