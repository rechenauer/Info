package thro.inf.server.server;


import thro.inf.allgemein.konfiguration.NetzwerkKonfiguration;
import thro.inf.server.clientverwaltung.Client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
/*
Diese Klasse implementiert das Interface Runnable und verwendet das Executor Framework,
um mehrere Threads laufen lassen zu können.
 */

public class Server implements Runnable {

    private ServerSocket socket;

    //ExecutorService startet neue Threads
    private static final ExecutorService executorService = Executors.newFixedThreadPool(5);

    //Server Logger loggt wichtige Ereignisse
    private static final Logger serverLog = Logger.getLogger(Server.class.getName());

    //Parst die Argumente und versucht ein Server Socket zu starten
    //Argumente müssen im Format: Host-Adresse Port, also z.B. '127.0.0.1 4001', sein
    Server(String[] args) {
        NetzwerkKonfiguration configuration = new NetzwerkKonfiguration(args);
        try {
            socket = new ServerSocket(configuration.getPort(), 0, configuration.getInetAddress());
            serverLog.addHandler(new FileHandler("server.log"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        serverLog.setLevel(Level.ALL);

    }

    //Startet den Server Thread und Ereignisverwaltung asynchron
    void start() {
        executorService.execute(new EreignisVerwaltung());
        executorService.execute(this);

        /* Print general system information */
        serverLog.info(System.getProperty("java.vendor") + "Java " + System.getProperty("java.version"));
        serverLog.info(System.getProperty("os.name") + " " + System.getProperty("os.arch"));
        serverLog.info("Benutzer: " + System.getProperty("user.name"));
        serverLog.info(System.getProperty("java.class.path"));
    }

    //Enthält Hauptschleife und akzeptiert neue Clients
    @Override
    public void run() {
        //Wartet auf neue Clients
        while (true) {
            try {
                Socket akzeptiertesSocket = socket.accept();
                if (akzeptiertesSocket != null) {
                    executorService.execute(new Client(akzeptiertesSocket));
                }
            } catch (Exception e) {
                //todo LOG
                return;
            }
        }
    }

    //Schließt Server Socket und beendet alle laufenden Threads
    void close() {
        try {
            executorService.shutdownNow();
            executorService.awaitTermination(500L, TimeUnit.MILLISECONDS);
            socket.close();
        } catch (IOException | InterruptedException e) {
            serverLog.severe("Etwas ist falsch gelaufen.\n" + Arrays.toString(e.getStackTrace()));
        }
    }

    //Gibt die Instanz des Server-Loggers zurück
    public static Logger getLogger() {
        return serverLog;
    }

}
