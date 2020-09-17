package thro.inf.allgemein.konfiguration;


import thro.inf.server.server.Server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Logger;

//Implementiert IConfiguration und parst die Programm Argumente
public class NetzwerkKonfiguration implements IConfiguration {

    private InetAddress inetAddress;
    private int port;
    private final Logger serverLog = Server.getLogger();

    public NetzwerkKonfiguration(String[] args) {
        try {
            inetAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        port = 4001;
        //Parsen der Argumente
        parseInetAdress(args);
        parsePort(args);
    }

    //Gibt Host Adresse der Socket Verbindung zurück
    @Override
    public final InetAddress getInetAddress() {
        return inetAddress;
    }

    //Parst die Argumente von String zu einer InetAdress Instanz
    public final void parseInetAdress(String[] args) {
        String hinweis;
        if (args == null || args.length < 2) {
            hinweis = "Keine passenden Argumente übergeben, die Standard-Host Adresse wird verwendet: " + inetAddress.toString();
            serverLog.warning(hinweis);
            return;
        }
        try {
            inetAddress = InetAddress.getByName(args[ 0 ]);
            hinweis = "Geparste Host Adresse: " + inetAddress.toString();
            serverLog.info(hinweis);
        } catch (Exception e) {
            e.printStackTrace();
            hinweis = "Argument1 konnte nicht zu InetAdress geparst werden: " + args[ 0 ] + ", der Localhost wird stattdessen verwendet.";
            serverLog.warning(hinweis);
        }
    }

    //Gibt Port der Socket Verbindung zurück
    //Eingabe muss im Intervall [0, 65535] sein
    @Override
    public final int getPort() {
        return port;
    }

    //Parst die Argumente von String zu Port
    @Override
    public void parsePort(String[] args) {
        String hinweis;
        if (args == null || args.length < 2) {
            hinweis = "Keine passenden Argumente eingegeben, der Standard-Port wird verwendet: " + port;
            serverLog.warning(hinweis);
            return;
        }
        try {
            port = Integer.parseInt(args[ 1 ]);
            //Checkt, ob Port zwischen 0 und 65535 liegt
            if (port <= 0 || port > 65535) {
                hinweis = "Eingegebener Port ist ungültig: " + port;
                hinweis += "Der Port muss im Intervall [0, 65535] sein, verwende Port: " + port + " stattdessen.";
                serverLog.warning(hinweis);
            } else {
                hinweis = "Port geparst: " + port;
                serverLog.info(hinweis);
            }
        } catch (Exception e) {
            hinweis = "Konnte Argument2 nicht als Port parsen: " + args[ 1 ] + ", verwende " + port + " stattdessen";
            serverLog.warning(hinweis);
        }


    }

    //Gibt aktuelle Instanz als String zurück
    @Override
    public String toString() {
        return "InetAddress: " + inetAddress.toString() + ", port: " + port;
    }
}
