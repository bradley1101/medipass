package bj.ifri.medipass.model;

import java.util.Objects;

public class Medecin {
    private Long id;
    private String nom;
    private String prenom;
    private String specialite;
    private String telephone;

    public Medecin() { }

    public Medecin(String nom, String prenom, String specialite, String telephone) {
        this.nom = nom;
        this.prenom = prenom;
        this.specialite = specialite;
        this.telephone = telephone;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    @Override public boolean equals(Object o) {
        return o instanceof Medecin m && id != null && id.equals(m.id);
    }
    @Override public int hashCode() { return Objects.hashCode(id); }
    @Override public String toString() {
        return String.format("#%d Dr %s %s — %s", id, nom.toUpperCase(), prenom, specialite);
    }
}
