package thro.inf.RestApplikationKleiderkreisel.server.kleidung;

import thro.inf.RestApplikationKleiderkreisel.server.mitglied.Mitglied;

import javax.persistence.*;
/*
 * Diese Klasse ermöglicht es dem RESTful WebService ein Kleidungsstück zu erstellen und diverse Attribute
 * zuzuweisen.
 * Die Annotation '@Entity' ist lediglich eine Markierung der Klasse und definiert, dass diese auch in eine Tabelle gemappt werden kann.
 * '@Id' markiert das Attribut 'id' als Primärschlüssel.
 * '@GeneratedValue' spezifiziert, dass der Wert des Primärschlüssels automatisch zugeordnet wird.
 * '@Embedded' wird verwendet, um eine Klasse in eine andere einzufügen.
 * '@OneToOne' garantiert, dass jedem Kleidungsstück nur ein Verkäufer zugeordnet wird. (1:1 Beziehung)
 * '@Version' ermöglicht das Versionieren.
 * */

@Entity
public class Kleidung {
    @Id
    @GeneratedValue
    private Long id;

    private float neuPreis;

    private float tauschWert;

    private byte groesse;

    private String geschlecht;

    @Embedded
    private KleidungsArt marke;

    private String hersteller;

    @OneToOne(fetch = FetchType.EAGER)
    private Mitglied verkaeufer;
    @Version
    private Long version;

    public Kleidung() {

    }

    public Kleidung(float neuPreis, float tauschWert, byte groesse, String geschlecht,
                    KleidungsArt marke, String hersteller, Mitglied verkaeufer) {
        this.neuPreis = neuPreis;
        this.tauschWert = tauschWert;
        this.groesse = groesse;
        this.geschlecht = geschlecht;
        this.marke = marke;
        this.hersteller = hersteller;
        this.verkaeufer = verkaeufer;
    }

    //Standard Getter und Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getNeuPreis() {
        return neuPreis;
    }

    public void setNeuPreis(float neuPreis) {
        this.neuPreis = neuPreis;
    }

    public float getTauschWert() {
        return tauschWert;
    }

    public void setTauschWert(float tauschWert) {
        this.tauschWert = tauschWert;
    }

    public byte getGroesse() {
        return groesse;
    }

    public void setGroesse(byte groesse) {
        this.groesse = groesse;
    }

    public String getGeschlecht() {
        return geschlecht;
    }

    public void setGeschlecht(String geschlecht) {
        this.geschlecht = geschlecht;
    }

    public KleidungsArt getMarke() {
        return marke;
    }

    public void setMarke(KleidungsArt marke) {
        this.marke = marke;
    }

    public String getHersteller() {
        return hersteller;
    }

    public void setHersteller(String hersteller) {
        this.hersteller = hersteller;
    }

    public Mitglied getVerkaeufer() {
        return verkaeufer;
    }

    public void setVerkaeufer(Mitglied verkaeufer) {
        this.verkaeufer = verkaeufer;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
