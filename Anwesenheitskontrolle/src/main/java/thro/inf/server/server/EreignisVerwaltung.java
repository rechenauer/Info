package thro.inf.server.server;

import thro.inf.allgemein.ereignis.Ereignis;
import thro.inf.server.mitarbeiterverwaltung.MitarbeiterVerwaltung;

import java.util.concurrent.LinkedBlockingQueue;

/*
Diese Klasse verwaltet neue Ereignisse und informiert die Mitarbeiterverwaltung, wenn ein neues Ereignis vorliegt.
 */
public class EreignisVerwaltung implements Runnable {

    private final MitarbeiterVerwaltung mitarbeiterVerwaltung = MitarbeiterVerwaltung.getInstance();

    private static final LinkedBlockingQueue<Ereignis> ereignisQueue = new LinkedBlockingQueue<>();

    //Wenn ein neues Ereignis in der EreignisQueue vorhanden ist, wird dieses zur MitarbeiterVerwaltung geschickt.
    @Override
    public void run() {
        while (true) {
            try {
                mitarbeiterVerwaltung.notify(ereignisQueue.take());
            } catch (InterruptedException ignored) {
            }
        }
    }

    //Fügt ein neues Ereignis in die EreignisQueue ein
    public static void hinzufügenEreignis(Ereignis ereignis) {
        ereignisQueue.offer(ereignis);
    }
}
