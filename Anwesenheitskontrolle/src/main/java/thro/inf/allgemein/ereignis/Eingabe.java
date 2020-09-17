package thro.inf.allgemein.ereignis;

/*
Dieses Enum bietet zwei Möglichkeiten als Eingabe ('Links' oder 'Rechts') und beschreibt den Sensor-Typ.
 */
public enum Eingabe {
    //Möglichkeiten des Enums
    Links, Rechts;

    //Gibt Sensortypen als String zurück
    @Override
    public String toString() {
        if (this == Links) {
            return "links";
        }
        if (this == Rechts) {
            return "rechts";
        }
        return "null";
    }

    //Parst String 'eingabe' als Eingabe('Links' oder 'Rechts') und gibt diesen String zurück
    public static Eingabe parseEingabe(String eingabe) {
        if (eingabe == null) {
            return null;
        }
        eingabe = eingabe.toLowerCase();
        if (eingabe.equals("links")) {
            return Eingabe.Links;
        }
        if (eingabe.equals("rechts")) {
            return Eingabe.Rechts;
        }
        return null;
    }
}
