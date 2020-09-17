package thro.inf.client;

import thro.inf.allgemein.ereignis.Eingabe;
import thro.inf.allgemein.konfiguration.NetzwerkKonfiguration;

import java.util.logging.Logger;

/*
Sensoren können mit verschiedenen Konfigurationen gestartet werden.
Diese Klasse ermöglicht das Parsen der Argumente als Eingabe.
 */

public class SensorKonfiguration extends NetzwerkKonfiguration {

    private Eingabe eingabe;

    //Sensor Logger Instanz
    private Logger sensorLog;

    SensorKonfiguration(String[] args, Logger sensorLog) {
        super(args);
        this.sensorLog = sensorLog;
        parseEingabe(args);
    }

    //Parst die Programm Argumente als Eingabe
    private void parseEingabe(String[] args) {
        if (args == null || args.length < 3) {
            String hinweis = "Keine passende Eingabe, die Standardeingabe wird verwendet: " + Eingabe.Links.toString();
            eingabe = Eingabe.Links;
            sensorLog.warning(hinweis);
            return;
        }
        eingabe = Eingabe.parseEingabe(args[ 2 ]);
        if (eingabe == null) {
            eingabe = Eingabe.Links;
            String output = " Argmuent3 konnte nicht als Eingabe geparst werden: " + args[ 2 ] + ", stattdessen wird 'Links' als Eingabe verwendet.";
            sensorLog.warning(output);
        } else {
            sensorLog.info("Geparste Eingabe: " + eingabe.toString());
        }
    }

    //Gibt Eingabe zurück
    final Eingabe getEingabe() {
        return eingabe;
    }

    //Gibt aktuelle Konfigurationsdaten als String zurück
    @Override
    public final String toString() {
        return super.toString() + "," + eingabe.toString();
    }
}
