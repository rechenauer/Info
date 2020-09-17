package thro.inf.server.mitarbeiterverwaltung;

import org.junit.Assert;
import org.junit.Test;
import thro.inf.allgemein.ereignis.Eingabe;
import thro.inf.allgemein.ereignis.Ereignis;

import java.util.LinkedList;


public class MitarbeiterVerwaltungTest {


    @Test
    public void notify() {
        MitarbeiterVerwaltung mitarbeiterVerwaltung = MitarbeiterVerwaltung.getInstance();
        LinkedList<Mitarbeiter> mitarbeiter = mitarbeiterVerwaltung.getMitarbeiterListe();
        Assert.assertEquals(0, mitarbeiter.size());

        mitarbeiterVerwaltung.notify(new Ereignis(Eingabe.Links.toString(), "00-14-22-01-23-45"));
        mitarbeiter = mitarbeiterVerwaltung.getMitarbeiterListe();
        Assert.assertEquals(1, mitarbeiter.size());

        mitarbeiterVerwaltung.notify(new Ereignis(Eingabe.Links.toString(), "00-14-22-01-23-45"));
        mitarbeiter = mitarbeiterVerwaltung.getMitarbeiterListe();
        Assert.assertEquals(1, mitarbeiter.size());
    }
}
