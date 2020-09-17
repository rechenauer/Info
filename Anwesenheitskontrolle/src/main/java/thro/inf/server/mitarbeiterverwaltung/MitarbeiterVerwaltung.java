package thro.inf.server.mitarbeiterverwaltung;

import thro.inf.allgemein.ereignis.Eingabe;
import thro.inf.allgemein.ereignis.Ereignis;

import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
Diese Klasse verwaltet alle Mitarbeiter und neue Eingaben.
Außerdem werden die Logger der Klassen Ereignis und MitarbeiterVerwaltung initialisiert.
 */
public class MitarbeiterVerwaltung {
    // Eine (versteckte) Klassenvariable vom Typ der eigene Klasse
    private static MitarbeiterVerwaltung instance;

    private LinkedList<Mitarbeiter> mitarbeiterListe;

    //Logger der Klasse MitarbeiterVerwaltung
    private static final Logger mitarbeiterVerwaltungsLog = Logger.getLogger(MitarbeiterVerwaltung.class.getName());

    //Logger der Klasse Ereignis
    private static final Logger ereignisLog = Logger.getLogger(Ereignis.class.getName());


    //Mitarbeiterliste und Logger werden initialisiert
    private MitarbeiterVerwaltung() {
        mitarbeiterListe = new LinkedList<>();
        initLogger();
    }

    //Logger der Klassen Ereignis und MitarbeiterVerwaltung werden initialisiert
    private void initLogger() {
        try {
            mitarbeiterVerwaltungsLog.addHandler(new FileHandler("allgemein.log"));
            ereignisLog.addHandler(new FileHandler("ereignis.log"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mitarbeiterVerwaltungsLog.setLevel(Level.ALL);
        ereignisLog.setLevel(Level.ALL);
    }

    /*
    Wenn es keinen Mitarbeiter in der Liste mit bestimmter 'mitarbeiterID' gibt,
    dann wird dieser neu erstellt und zur Liste hinzugefügt.
    Anschließend erfolgt ein Zustandsübergang. Wenn der neue Zustand ein Fehler Zustand ist,
    dann wird er auf 'Abwesend' gesetzt.
    */
    public final void notify(Ereignis ereignis) {

        if (ereignis == null) {
            return;
        }
        ereignisLog.info("Neues Ereignis : " + ereignis.toString());

        //Durchsuchen der MitarbeiterListe nach bereits existierendem Mitarbeiter
        Mitarbeiter mitarbeiterExistent = null;
        for (Mitarbeiter mitarbeiter : mitarbeiterListe) {
            final String id = mitarbeiter.getID();
            if (id != null && id.equals(ereignis.getMitarbeiterID())) {
                mitarbeiterExistent = mitarbeiter;

                //Loggt, dass Mitarbeiter bereits in Liste ist
                mitarbeiterVerwaltungsLog.info("Mitarbeiter gefunden mit ID: " + mitarbeiterExistent.getID());
                break;
            }
        }

        //Erstellen des Mitarbeiters und Hinzufügen zur Liste
        if (mitarbeiterExistent == null) {
            mitarbeiterVerwaltungsLog.info("Es konnte kein Mitarbeiter mit  ID " + ereignis.getMitarbeiterID() + " gefunden werden.");

            mitarbeiterExistent = new Mitarbeiter(ereignis.getMitarbeiterID());
            mitarbeiterListe.add(mitarbeiterExistent);

            mitarbeiterVerwaltungsLog.info("Mitarbeiter mit ID: " + mitarbeiterExistent.getID() + " wurde erstellt.");
        }

        //Ändert den Status des Mitarbeiters
        Mitarbeiter.Zustand vorherigerZustand = mitarbeiterExistent.getZustand();
        mitarbeiterExistent.schalte(Eingabe.parseEingabe(ereignis.getEingabe()));

        //Loggt den Zustandsübergang des Mitarbeiters
        String log = "Mitarbeiter mit ID " + mitarbeiterExistent.getID();
        log += " ändert den Zustand von " + vorherigerZustand.name();
        log += " zu " + mitarbeiterExistent.getZustand();
        mitarbeiterVerwaltungsLog.info(log);

        //Wenn Zustand eines Mitarbeiters fehlerhaft, wird dieser auf 'Abwesend' gesetzt.
        if (mitarbeiterExistent.getZustand() == Mitarbeiter.Zustand.Fehler) {
            log = "Mitarbeiter mit ID " + mitarbeiterExistent.getID();
            log += " ist im Zustand 'Fehler', der vorherige Zustand war:  " + vorherigerZustand.toString();
            mitarbeiterVerwaltungsLog.severe(log);
            mitarbeiterExistent.setzeZustandZurueck();
            mitarbeiterVerwaltungsLog.severe("Status des Mitarbeiters mit ID " + mitarbeiterExistent.getID() + " auf 'Abwesend' gesetzt.");
        }
    }

    //Gibt aktuelle Mitarbeiterliste zurück.
    public LinkedList<Mitarbeiter> getMitarbeiterListe() {
        return mitarbeiterListe;
    }

    //Gibt Singleton Instanz von MitarbeiterVerwaltung zurück.
    public static MitarbeiterVerwaltung getInstance() {
        if (instance == null) {
            instance = new MitarbeiterVerwaltung();
        }
        return instance;
    }
}
