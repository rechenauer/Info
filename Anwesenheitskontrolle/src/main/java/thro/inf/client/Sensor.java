package thro.inf.client;

import com.google.gson.Gson;
import thro.inf.allgemein.ereignis.Eingabe;
import thro.inf.allgemein.ereignis.Ereignis;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
/*
Diese Klasse verbindet sich anfangs mit dem spezifizierten Server.
Danach erhält es ein zufälliges Testpaket alle 2 Sekunden.
 */

public class Sensor {
    //Random Instanz
    private final Random random;
    //Gson Instanz
    private final Gson gson;
    //Socket Instanz
    private Socket socket;
    //Eingabe des Servers
    private Eingabe eingabe;
    //Sensor-Logger Instanz
    private static final Logger sensorLog = Logger.getLogger(Sensor.class.getName());
    //Konsoleneingabe Instanz
    private KonsolenEingabe konsolenEingabe;

    private Sensor(String[] args) {
        SensorKonfiguration sensorKonfig = null;
        random = new Random();
        gson = new Gson();

        try {
            sensorLog.addHandler(new FileHandler("sensor_" + hashCode() + ".log"));
            sensorKonfig = new SensorKonfiguration(args, sensorLog);
            socket = new Socket(sensorKonfig.getInetAddress(), sensorKonfig.getPort());
        } catch (ConnectException e) {
            String output = sensorKonfig.getInetAddress().toString() + ":" + sensorKonfig.getPort();
            sensorLog.severe("Es konnte keine verbindung zum Server aufgebaut werden " + output + ". Verbindung verweigert.");
            return;
        } catch (IOException e) {
            System.err.println("Etwas unerwartetes ist passiert.\n" + e.getStackTrace().toString());
            return;
        }
        eingabe = sensorKonfig.getEingabe();
    }

    //Legt KonsolenEingabe Instanz fest
    void setKonsolenEingabe(KonsolenEingabe konsolenEingabe) {
        this.konsolenEingabe = konsolenEingabe;
    }

    //Erstellt eine zufällige MAC Adresse und gibt diese als String zurück
    private String getZufälligeID() {
        StringBuilder ergebnis = new StringBuilder();
        for (byte i = 0; i < 5; ++i) {
            int randomInt = random.nextInt() % 256;
            randomInt = Math.abs(randomInt);
            ergebnis.append(Integer.toHexString(randomInt));
            if (i < 4) {
                ergebnis.append(':');
            }
        }
        return ergebnis.toString();
    }


    //Erstellt ein zufälliges Ereignis
    private String getRandomEreignis() {

        Ereignis ereignis = new Ereignis(eingabe.toString(), getZufälligeID());

        return gson.toJson(ereignis);
    }

    //Alle 2 Sekunden wird ein zufälliges Ereignis zum Server geschickt
    private void start() {
        if (socket == null) {
            sensorLog.severe("Verbindung zum Server konnte nicht hergestellt werden. Verbindung untersagt.");
            return;
        }

        while (true) {
            try {
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                String randomEreignis = getRandomEreignis();
                pw.println(randomEreignis);
                pw.flush();
                if (pw.checkError()) {
                    sensorLog.info("Herunterfahren des Sensors...");
                    konsolenEingabe.shutdown();
                    return;
                }
                Thread.sleep(2000);
            } catch (InterruptedException | IOException e) {
                return;
            }
        }
    }

    //Schließen des Sockets
    void close() {
        try {
            socket.close();
        } catch (IOException e) {
            sensorLog.severe("Etwas unerwartetes ist passiert.\n" + Arrays.toString(e.getStackTrace()));
        }
    }

    //Startet Sensor und versucht eine Verbindung zum Server herzustellen. Danach wird auf Tastatureingaben geachtet
    public static void main(String[] args) {
        Sensor sensor = new Sensor(args);
        KonsolenEingabe konsolenEingabe = new KonsolenEingabe(sensor);
        konsolenEingabe.start();
        sensor.setKonsolenEingabe(konsolenEingabe);
        sensor.start();
    }

}
