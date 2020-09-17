package thro.inf.RestApplikationKleiderkreisel.server.konto;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/*
 * Diese Klasse ermöglicht es dem RESTful WebService Konten anzulegen.
 * Die Passwörter der Konten werden mithilfe des Interfaces 'PasswordEncoder' gehasht und verschlüsselt gespeichert.
 *
 * Annotations:
 *
 * '@Entity' ist lediglich eine Markierung der Klasse und definiert, dass diese auch in eine Tabelle gemappt werden kann.
 * '@Id' markiert das Attribut 'id' als Primärschlüssel.
 * '@GeneratedValue' spezifiziert, dass der Wert des Primärschlüssels automatisch zugeordnet wird.
 * '@Convert' wandelt den 'benutzerNamen' in einen 'BenutzernameKonverter' um.
 * */

@Entity
public class Konto {
    private static PasswordEncoder passwortKodierer = new BCryptPasswordEncoder();

    @Id
    @GeneratedValue
    private Long id;

    @Convert(converter = BenutzernameKonverter.class)
    private String benutzerName;

    private String passwort;

    private boolean online;

    public Konto() {

    }

    public Konto(String benutzerName, String passwort, boolean online) {
        this.benutzerName = benutzerName;
        this.passwort = passwort;
        this.online = online;
    }

    //Standard Getter und Setter
    public String getBenutzerName() {
        return benutzerName;
    }

    public void setBenutzerName(String benutzerName) {
        this.benutzerName = benutzerName;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwortKodierer.encode(passwort);
    }

    public boolean getOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
}
