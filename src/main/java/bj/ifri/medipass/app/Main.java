package bj.ifri.medipass.app;

import bj.ifri.medipass.dao.DaoException;
import bj.ifri.medipass.dao.DaoFactory;
import bj.ifri.medipass.dao.jdbc.ConnectionManager;
import bj.ifri.medipass.model.Consultation;
import bj.ifri.medipass.model.Medecin;
import bj.ifri.medipass.model.Patient;
import bj.ifri.medipass.service.MedipassService;
import bj.ifri.medipass.service.ValidationException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/** Console application. Storage chosen by medipass.dao = JDBC | MEMORY. */
public class Main {

    private static final DateTimeFormatter FR = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final Scanner in = new Scanner(System.in);
    private final MedipassService service;

    Main(MedipassService service) { this.service = service; }

    public static void main(String[] args) {
        DaoFactory.Type type = DaoFactory.Type.valueOf(
                ConnectionManager.property("medipass.dao", "MEMORY").toUpperCase());
        MedipassService service = new MedipassService(DaoFactory.of(type));
        System.out.println("Medipass — stockage : " + type);
        if (type == DaoFactory.Type.MEMORY) DemoData.load(service);
        new Main(service).run();
    }

    void run() {
        while (true) {
            System.out.println("""

                    1. Lister les patients        5. Lister les médecins
                    2. Rechercher un patient      6. Ajouter un médecin
                    3. Ajouter un patient         7. Ajouter une consultation
                    4. Afficher un dossier        8. Supprimer un patient
                    0. Quitter""");
            String choice = ask("Choix");
            try {
                switch (choice) {
                    case "1" -> print(service.listerPatients());
                    case "2" -> print(service.rechercherPatients(ask("Nom ou prénom")));
                    case "3" -> System.out.println("Créé : " + service.enregistrerPatient(new Patient(
                            ask("Nom"), ask("Prénom"), date("Date de naissance (jj/mm/aaaa)"),
                            ask("Sexe (M/F)").toUpperCase().charAt(0), ask("Téléphone"),
                            ask("Groupe sanguin (ex. O+)").toUpperCase(), ask("Allergies"))));
                    case "4" -> System.out.print(service.dossier(number("N° patient")).format());
                    case "5" -> print(service.listerMedecins());
                    case "6" -> System.out.println("Créé : " + service.enregistrerMedecin(new Medecin(
                            ask("Nom"), ask("Prénom"), ask("Spécialité"), ask("Téléphone"))));
                    case "7" -> {
                        service.ajouterConsultation(new Consultation(number("N° patient"), number("N° médecin"),
                                date("Date (jj/mm/aaaa, vide = aujourd'hui)"), ask("Motif"),
                                ask("Diagnostic"), ask("Prescription")));
                        System.out.println("Consultation enregistrée.");
                    }
                    case "8" -> {
                        service.supprimerPatient(number("N° patient"));
                        System.out.println("Patient supprimé.");
                    }
                    case "0" -> { return; }
                    default -> System.out.println("Choix invalide.");
                }
            } catch (ValidationException | DaoException e) {
                System.out.println("Erreur : " + e.getMessage());
            } catch (StringIndexOutOfBoundsException e) {
                System.out.println("Erreur : champ vide");
            }
        }
    }

    private void print(List<?> list) {
        if (list.isEmpty()) System.out.println("(aucun résultat)");
        list.forEach(System.out::println);
    }

    private String ask(String label) {
        System.out.print(label + " : ");
        return in.hasNextLine() ? in.nextLine().trim() : "0";
    }

    private long number(String label) {
        try {
            return Long.parseLong(ask(label));
        } catch (NumberFormatException e) {
            throw new ValidationException("Nombre attendu");
        }
    }

    private LocalDate date(String label) {
        String s = ask(label);
        if (s.isEmpty()) return LocalDate.now();
        try {
            return LocalDate.parse(s, FR);
        } catch (DateTimeParseException e) {
            throw new ValidationException("Date au format jj/mm/aaaa");
        }
    }
}
