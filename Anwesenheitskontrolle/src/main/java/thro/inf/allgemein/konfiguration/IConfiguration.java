package thro.inf.allgemein.konfiguration;

import java.net.InetAddress;

/*
Dieses Interface bietet eine Konfigurationsvorlage für das Arbeiten mit dem Netzwerk.
 */
public interface IConfiguration {
    //Gibt Host Adresse der Socket Verbindung zurück
    InetAddress getInetAddress();

    //Parst die Host Adresse der Argumente als InetAdress
    void parseInetAdress(String[] args);

    //Gibt den Port der Socket Verbindung zurück
    int getPort();

    //Parst die Host Adresse des Arguments als Port
    void parsePort(String[] args);
}
