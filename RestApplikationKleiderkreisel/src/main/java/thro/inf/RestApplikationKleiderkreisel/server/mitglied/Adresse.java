package thro.inf.RestApplikationKleiderkreisel.server.mitglied;

import javax.persistence.Embeddable;

/*
 * Die Klasse Adresse ermöglicht es dem RESTful WebService Adressen abzuspeichern. Die Annotation '@Embeddable' signalisiert,
 * dass die Klasse in eine andere eingefügt werden muss.
 *
 */

@Embeddable
public class Adresse {
    private String strasse;
    private short nummer;
    private int postLeitZahl;
    private String stadt;

    public Adresse() {
    }

    public Adresse(String strasse, short nummer, int postLeitZahl, String stadt) {
        this.strasse = strasse;
        this.nummer = nummer;
        this.postLeitZahl = postLeitZahl;
        this.stadt = stadt;
    }

    //Standard Getter und Setter
    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public short getNummer() {
        return nummer;
    }

    public void setNummer(short nummer) {
        this.nummer = nummer;
    }

    public int getPostLeitZahl() {
        return postLeitZahl;
    }

    public void setPostLeitZahl(int postLeitZahl) {
        this.postLeitZahl = postLeitZahl;
    }

    public String getStadt() {
        return stadt;
    }

    public void setStadt(String stadt) {
        this.stadt = stadt;
    }
}
