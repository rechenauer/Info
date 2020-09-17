package thro.inf.server.mitarbeiterverwaltung;

import thro.inf.allgemein.ereignis.Eingabe;

/*
Die Klasse hat die Attribute 'zustand' und 'id'.
Das Attribut 'zustand' beschreibt den aktuellen Zustand des Mitarbeiters('Fehler', 'Anwesend', 'ImGang' und 'Abwesend').
'id' entspricht der MAC-Adresse des Mitarbeiters.
*/
public class Mitarbeiter {

    //Alle Zustände, die ein Mitarbeiter annehmen kann
    public enum Zustand {
        Fehler("Fehler"), Anwesend("Anwesend"), ImGang("ImGang"), Abwesend("Abwesend");

        private String bezeichnung;

        Zustand(String bezeichnung) {
            this.bezeichnung = bezeichnung;
        }

        @Override
        public String toString() {
            return bezeichnung;
        }
    }

    //Aktueller Zustand des Mitarbeiters
    private Zustand zustand;

    //ID(MAC-Adresse) des Mitarbeiters
    private String id;

    public Mitarbeiter(String id) {
        this.id = id;
        //Startzustand
        zustand = Zustand.Abwesend;
    }

    //Zustandsübergangstabelle eines Mitarbeiters
    private static final Zustand[][] uebergang =
            {{Zustand.Fehler, Zustand.Fehler, Zustand.Abwesend, Zustand.ImGang},
                    {Zustand.Fehler, Zustand.ImGang, Zustand.Anwesend, Zustand.Fehler}};

    //Gibt Zustand zurück, nachdem ein Zustandsübergang erfolgt ist.
    public static Zustand uebergangsfunktion(Zustand zustand, Eingabe eingabe) {
        return uebergang[ eingabe.ordinal() ][ zustand.ordinal() ];
    }

    //Gibt aktuellen Zustand zurück
    public final Zustand getZustand() {
        return zustand;
    }

    //Führt einen internen Zustandsübergang durch und gibt neuen Zustand zurück.
    public final Zustand schalte(Eingabe eingabe) {
        zustand = uebergangsfunktion(zustand, eingabe);
        return zustand;
    }

    //Gibt 'id' des Mitarbeites zurück
    public final String getID() {
        return id;
    }

    //Setzt aktuellen Zustand auf 'Abwesend', da dieser der Startzustand ist.
    final void setzeZustandZurueck() {
        zustand = Zustand.Abwesend;
    }
}
