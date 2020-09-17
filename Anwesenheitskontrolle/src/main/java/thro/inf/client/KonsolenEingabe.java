package thro.inf.client;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/*
Diese Klasse ermöglicht das Horchen auf Tastatureingaben.
Bei der Eingabe 'stop' oder 'schließen' terminiert der Sensor.
 */
public class KonsolenEingabe implements Runnable {

    //Instanz des Sensors
    private Sensor sensor;

    //ExecutorService öffnet einen Threadpool mit einem Thread
    private ExecutorService executorService = Executors.newFixedThreadPool(1);

    //Initialisiert Klasse indem die Sensor Instanz gesetzt wird
    KonsolenEingabe(Sensor sensor) {
        this.sensor = sensor;
    }


    /*
     Beinhaltet eine Dauerschleife, die Tastatureingaben überwacht.
     Wenn 'stop' oder 'schließen' eingetippt wird, dann terminiert der Sensor.
     */
    public void run() {
        Scanner scan = new Scanner(System.in);
        String befehl;

        //Horcht auf Tastatureingaben
        while (true) {
            befehl = scan.next().toLowerCase();

            //Schaltet Sensor ab, wenn 'stop' oder 'schließen' eingegeben wird.
            if (befehl.equals("stop") || befehl.equals("schließen")) {
                stop();
            } else {
                System.out.println("Geben Sie entweder \"stop\" oder \"schließen\" , um den Sensor abzuschalten");
            }
        }
    }

    //Startet neuen Thread für KonsolenEingabe
    void start() {
        executorService.execute(this);
    }

    //Schließt Sensor und beendet das Programm
    private void stop() {
        System.out.println("Schließe den Sensor...");
        sensor.close();
        System.exit(0);
    }

    //Beendet alle Threads, die vom ExecutorService gestartet wurden.
    void shutdown() {
        executorService.shutdownNow();
        try {
            executorService.awaitTermination(500L, TimeUnit.MILLISECONDS);
            sensor.close();
        } catch (InterruptedException ignored) {
        }
    }

}
