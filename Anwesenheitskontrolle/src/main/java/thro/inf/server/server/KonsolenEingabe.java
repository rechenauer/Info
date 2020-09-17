package thro.inf.server.server;

import java.util.Scanner;

/*
Diese Klasse verwaltet Konsoleneingaben und terminiert den Server, falls 'stop' oder 'schließen' eingegeben wird.
 */
public class KonsolenEingabe {
    private Server server;

    //Initialisiert die Klasse indem die Server Instanz spezifiziert wird
    private KonsolenEingabe(Server server) {
        this.server = server;
    }


    //Öffnet eine Schleife und frägt nach einer Konsoleneingabe
    //Wenn 'stop' oder 'schließen' eingegeben wird, so wird der Server heruntergefahren
    //Falls die Eingabe keiner der oben genannten entspricht, so wird eine Anleitung zum Beenden des Servers ausgegeben.
    public void eingabeVerarbeitung() {
        Scanner scan = new Scanner(System.in);
        String befehl;

        //Achtet auf Eingabe
        while (true) {
            befehl = scan.next().toLowerCase();
            //Mehrere Befehle führen zum Stoppen des Servers
            if (befehl.equals("stop") || befehl.equals("schließen")) {
                System.out.println("Server wird heruntergefahren...");
                server.close();
                System.exit(0);
            } else {
                System.out.println("Bitte geben Sie entweder \"stop\" oder \"schließen\" ein, um den Server herunterzufahren.");
            }
        }
    }

    //Startet den Server (args[0] = IP-Adresse, args[1] = Port)
    public static void main(String[] args) {
        //Startet den Server
        Server server = new Server(args);
        server.start();

        KonsolenEingabe konsEingabe = new KonsolenEingabe(server);
        konsEingabe.eingabeVerarbeitung();
    }
}
