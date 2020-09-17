package thro.inf.RestApplikationKleiderkreisel.server.mitglied;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import thro.inf.RestApplikationKleiderkreisel.server.kleidung.Kleidung;

import javax.persistence.*;
import java.util.LinkedList;

/*
 * Diese Klasse ermöglicht dem RESTful WebService das Erstellen von Mitgliedern.
 * Das Passwort des erstellten Mitglieds wird mithilfe des Interfaces 'PasswordEncoder' gehasht und verschlüsselt abgespeichert.
 * Die erstellten Mitglieder werden in einer LinkedList abgelegt.
 *
 * Annotationen:
 *
 * '@Id' markiert das Attribut 'id' als Primärschlüssel.
 * '@GeneratedValue' spezifiziert, dass der Wert des Primärschlüssels automatisch zugeordnet wird.
 * '@Embedded' wird verwendet, um eine Klasse in eine andere einzufügen.
 * '@Version' ermöglicht das Versionieren.
 *
 */

@Entity
public class Mitglied {
    @GeneratedValue
    @Id
    private Long id;
    private String benutzerName;
    private String passwort;
    private String vorname;
    private String nachname;
    private double kontoStand;
    private String emailAdresse;
    @Embedded
    private Adresse adresse;
    @Version
    private Long version;
    private LinkedList<Kleidung> kleidungsListe;
    private static PasswordEncoder passwortVerschluesselung = new BCryptPasswordEncoder();


    public Mitglied() {

    }

    public Mitglied(String benutzerName, String passwort, String vorname,
                    String nachname, double kontoStand, String emailAdresse) {
        this.benutzerName = benutzerName;
        setPasswort(passwort);
        this.vorname = vorname;
        this.nachname = nachname;
        this.kontoStand = kontoStand;
        this.emailAdresse = emailAdresse;
    }

    //Getter und Setter für das Passwort. Hier muss das Password gehasht abgespeichert werden,
    // das garantiert die Methode 'encode()' des Interfaces 'PasswordEncoder'.
    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwortVerschluesselung.encode(passwort);
    }

    public void setPasswortHash(String passwordHash) {
        this.passwort = passwordHash;
    }

    //Standard Getter und Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBenutzerName() {
        return benutzerName;
    }

    public void setBenutzerName(String benutzerName) {
        this.benutzerName = benutzerName;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public double getKontoStand() {
        return kontoStand;
    }

    public void setKontoStand(double kontoStand) {
        this.kontoStand = kontoStand;
    }

    public String getEmailAdresse() {
        return emailAdresse;
    }

    public void setEmailAdresse(String emailAdresse) {
        this.emailAdresse = emailAdresse;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    public LinkedList<Kleidung> getKleidungsListe() {
        return kleidungsListe;
    }

    public void setKleidungsListe(LinkedList<Kleidung> kleidungsListe) {
        this.kleidungsListe = kleidungsListe;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
