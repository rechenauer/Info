package thro.inf.allgemein.ereignis;

import com.google.gson.Gson;

/*
 * Diese Klasse hat die Attribute 'eingabe' und 'mitarbeiterID'.
 * Sie wird instanziiert, wenn der Sensor ein neues Ereignis sendet.
 * Die Eingabe 'eingabe' beschreibt den Sensortyp (Links oder Rechts).
 * Der String 'mitarbeiterID' beschreibt die MAC-Adresse des Mitarbeiters.
 */

public class Ereignis {

    //Links oder Rechts als Eingabe
    private String eingabe;

    //MAC Adresse als MitarbeiterID
    private String mitarbeiterID;

    //Initialisiert die Klasse indem 'eingabe' & 'mitarbeiterID' auf 'null' gesetzt werden.
    public Ereignis() {
        setMitarbeiterID(null);
        setEingabe(null);
    }

    //Initialisiert die Klasse indem 'eingabe' & 'mitarbeiterID' von der Ereignis Instanz kopiert werden.
    public Ereignis(Ereignis ereignis) {
        this.eingabe = ereignis.eingabe;
        this.mitarbeiterID = ereignis.mitarbeiterID;
    }

    public Ereignis(String eingabe, String mitarbeiterID) {
        setEingabe(eingabe);
        setMitarbeiterID(mitarbeiterID);
    }

    public final String getEingabe() {
        return eingabe;
    }

    public final void setEingabe(String eingabe) {
        this.eingabe = eingabe;
    }

    public final String getMitarbeiterID() {
        return mitarbeiterID;
    }

    public final void setMitarbeiterID(String id) {
        this.mitarbeiterID = id;
    }

    //Gibt aktuelle Instanz als String zurück
    @Override
    public final String toString() {
        return "Eingabe: " + eingabe + " ID: " + mitarbeiterID;
    }

    //Parst String 'parseString' als Ereignis Objekt und gibt dieses Ereignis zurück
    public static Ereignis parse(String parseString) {
        if (parseString == null) {
            return null;
        }
        try {
            Gson gson = new Gson();
            Ereignis ereignis = gson.fromJson(parseString, Ereignis.class);
            if (ereignis.eingabe == null || ereignis.mitarbeiterID == null) {
                return null;
            }
            return ereignis;
        } catch (Exception e) {
            return null;
        }
    }

}