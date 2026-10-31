package bj.ifri.medipass.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

/** A patient and the permanent data of his medical pass. */
public class Patient {
    private Long id;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private char sexe;              // 'M' or 'F'
    private String telephone;
    private String groupeSanguin;   // e.g. "O+"
    private String allergies;       // free text, may be empty

    public Patient() { }

    public Patient(String nom, String prenom, LocalDate dateNaissance, char sexe,
                   String telephone, String groupeSanguin, String allergies) {
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.sexe = sexe;
        this.telephone = telephone;
        this.groupeSanguin = groupeSanguin;
        this.allergies = allergies;
    }

    public int getAge() {
        return dateNaissance == null ? 0 : Period.between(dateNaissance, LocalDate.now()).getYears();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public LocalDate getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }
    public char getSexe() { return sexe; }
    public void setSexe(char sexe) { this.sexe = sexe; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public String getGroupeSanguin() { return groupeSanguin; }
    public void setGroupeSanguin(String groupeSanguin) { this.groupeSanguin = groupeSanguin; }
    public String getAllergies() { return allergies; }
    public void setAllergies(String allergies) { this.allergies = allergies; }

    @Override public boolean equals(Object o) {
        return o instanceof Patient p && id != null && id.equals(p.id);
    }
    @Override public int hashCode() { return Objects.hashCode(id); }
    @Override public String toString() {
        return String.format("#%d %s %s (%d ans, %c, groupe %s)", id, nom.toUpperCase(), prenom,
                getAge(), sexe, groupeSanguin == null ? "?" : groupeSanguin);
    }
}
