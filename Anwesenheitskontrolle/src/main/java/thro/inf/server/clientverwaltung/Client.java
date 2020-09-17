package thro.inf.server.clientverwaltung;

import thro.inf.allgemein.ereignis.Ereignis;
import thro.inf.server.server.EreignisVerwaltung;
import thro.inf.server.server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Logger;

/*
Diese Klasse wird instanziiert, wenn sich ein neuer Client verbindet. Danach
startet ein Thread der horcht, ob neue Pakete ankommen. Wenn ein Client ein
Paket sendet, dann muss es im JSON-Format sein {"eingabe": XY, "id": YZ},
wobei beide Werte Strings sein müssen und die 'eingabe' zwingend 'links' oder
'rechts' sein muss. Wenn das Format ungültig ist, dann wird es ignoriert.
Wenn der Client die Verbindung unterbricht, dann wird auch der dazugehörige
Thread geschlossen.
 */
public class Client implements Runnable {

    //Socket des verbundenen Clients
    private final Socket clientSocket;

    //Logger des Servers
    private static final Logger serverLog = Server.getLogger();

    public Client(Socket socket) {
        this.clientSocket = socket;
        serverLog.info("Client verbunden : " + toString());
    }

    /*
    Startet Client Hauptschleife. Bei Empfangen eines Pakets versucht die Methode
    dieses zu parsen und ignoriert es, falls es nicht in das erwartete Format
    passt.
    */
    public final void run() {
        serverLog.info("Client (" + toString() + ") Thread gestartet.");
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            serverLog.severe("Etwas unerwartetes ist passiert.\n" + Arrays.toString(e.getStackTrace()));
        }
        //Horcht, ob neue Pakete empfangen werden
        while (true) {
            String empfangenesPaket = empfangePaket(bufferedReader);
            //Wenn Paket 'null', dann trennt der Client die Verbindung
            if (empfangenesPaket == null) {
                schließen();
                return;
            } else {
                //Parst Ereignis und fügt es zur Ereignisqueue hinzu
                Ereignis ereignis = Ereignis.parse(empfangenesPaket);
                if (ereignis != null) {
                    EreignisVerwaltung.hinzufügenEreignis(ereignis);
                }
            }
        }
    }

    //Wartet bis Client ein neues Paket sendet und gibt dieses als String zurück.
    //Gibt 'null' zurück, wenn der Client die Verbindung abbricht.
    private String empfangePaket(BufferedReader bufferedReader) {
        final char[] speicher = new char[ 512 ];
        int speicherLänge;

        try {
            speicherLänge = bufferedReader.read(speicher, 0, 512);
        } catch (IOException e) {
            return null;
        }

        //Bei Speicherlänge von -1 bricht der Client die Verbindung ab.
        if (speicherLänge == -1) {
            return null;
        }

        //Wandelt Speicher in einen String um.
        return new String(speicher, 0, speicherLänge);
    }

    //Schließt das Socket
    private void schließen() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        serverLog.info("Client geschlossen: " + toString());
    }


    //Vergleicht, ob spezifiziertes Objekt gleich der aktuellen Instanz ist.
    @Override
    public final boolean equals(Object objekt) {
        if (objekt == null || !Client.class.isAssignableFrom(objekt.getClass())) {
            return false;
        }
        if (objekt == this) {
            return true;
        }
        Client client = (Client) objekt;
        if (clientSocket.getPort() != client.clientSocket.getPort()
                || !clientSocket.getInetAddress().equals(client.clientSocket.
                        getInetAddress())) {
            return true;
        }
        return false;
    }

    /*Gibt Host-Adresse und Port des Clients als String zurück.
    Im Format: Host-Adresse:Port
    */
    @Override
    public final String toString() {
        return clientSocket.getInetAddress().toString()
                + ":" + clientSocket.getPort();
    }
}
