package thro.inf.RestApplikationKleiderkreisel.server.tausch;

import thro.inf.RestApplikationKleiderkreisel.server.mitglied.Mitglied;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

/*
 * Diese Klasse ermöglicht dem RESTful WebService das Tauschen auf der Plattform.
 * Jedem Tausch werden Käufer, Verkäufer, Datum und eine ID zugeordnet.
 * Die Provision der Plattform wird durch das Attribut 'plattformProvision' beschrieben.
 *
 * Annotationen:
 * '@Id' markiert das Attribut 'id' als Primärschlüssel.
 * '@GeneratedValue' spezifiziert, dass der Wert des Primärschlüssels automatisch zugeordnet wird.
 * '@ManyToOne' bedeutet, dass zum einen viele Verkäufer zu einem Käufer zugeordnet werden und umgekehrt.
 * '@Entity' ist lediglich eine Markierung der Klasse und definiert, dass diese auch in eine Tabelle gemappt werden kann.
 * */

@Entity
public class Tausch {
    @GeneratedValue
    @Id
    private Long id;
    @ManyToOne
    private Mitglied kaeufer;
    @ManyToOne
    private Mitglied verkaeufer;
    private LocalDate datum;
    private Long kleidungsId;
    private static double plattformProvision = 0;

    public Tausch() {

    }

    public Tausch(Mitglied kaeufer, Mitglied verkaeufer) {
        this.kaeufer = kaeufer;
        this.verkaeufer = verkaeufer;
    }

    //Standard Getter und Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Mitglied getKaeufer() {
        return kaeufer;
    }

    public void setKaeufer(Mitglied kaeufer) {
        this.kaeufer = kaeufer;
    }

    public Mitglied getVerkaeufer() {
        return verkaeufer;
    }

    public void setVerkaeufer(Mitglied verkaeufer) {
        this.verkaeufer = verkaeufer;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    public Long getKleidungsId() {
        return kleidungsId;
    }

    public void setKleidungsId(Long kleidungsId) {
        this.kleidungsId = kleidungsId;
    }

    public static double getPlattformProvision() {
        return plattformProvision;
    }

    public static void setPlattformProvision(double plattformProvision) {
        Tausch.plattformProvision = plattformProvision;
    }
}
