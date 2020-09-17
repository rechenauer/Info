package thro.inf.server.mitarbeiterverwaltung;

import org.junit.Assert;
import org.junit.Test;
import thro.inf.allgemein.ereignis.Eingabe;


public class MitarbeiterTest {
    private Mitarbeiter maxMustermann;

    @Test
    public void schalte() {
        maxMustermann = new Mitarbeiter("00-14-22-01-23-45");
        //'zustand' wird der Zustand 'Abwesend' zugewiesen.
        Mitarbeiter.Zustand zustand = maxMustermann.getZustand();

        //Testen des Startzustands
        Assert.assertEquals(Mitarbeiter.Zustand.Abwesend, zustand);

        //Testen, ob der Zustand 'zustand' auf 'Fehler' schaltet nachdem 'Rechts' eingegeben wurde.
        zustand = maxMustermann.schalte(Eingabe.Rechts);
        Assert.assertEquals(Mitarbeiter.Zustand.Fehler, zustand);
    }

    @Test
    public void getZustand() {
        //Erstelle neuen Mitarbeiter. Anschließend wird mit der Methode 'getZustand() 'getestet, ob der Startzustand 'Abwesend' ist.
        maxMustermann = new Mitarbeiter("00-14-22-01-23-46");
        Assert.assertEquals(Mitarbeiter.Zustand.Abwesend, maxMustermann.getZustand());
    }

    @Test
    public void getID() {
        //Testen der Methode 'getID()'.
        final String id = "00-14-22-01-23-47";
        maxMustermann = new Mitarbeiter(id);
        Assert.assertEquals(id, maxMustermann.getID());
    }


    @Test
    public void uebergangsfunktion() {
        //Testen des Zustands 'Anwesend'.
        Assert.assertEquals(Mitarbeiter.Zustand.Fehler, Mitarbeiter.uebergangsfunktion(Mitarbeiter.Zustand.Anwesend, Eingabe.Links));
        Assert.assertEquals(Mitarbeiter.Zustand.ImGang, Mitarbeiter.uebergangsfunktion(Mitarbeiter.Zustand.Anwesend, Eingabe.Rechts));
        //Testen des Zustands 'ImGang'.
        Assert.assertEquals(Mitarbeiter.Zustand.Abwesend, Mitarbeiter.uebergangsfunktion(Mitarbeiter.Zustand.ImGang, Eingabe.Links));
        Assert.assertEquals(Mitarbeiter.Zustand.Anwesend, Mitarbeiter.uebergangsfunktion(Mitarbeiter.Zustand.ImGang, Eingabe.Rechts));
        //Testen des Zustands 'Abwesend'.
        Assert.assertEquals(Mitarbeiter.Zustand.ImGang, Mitarbeiter.uebergangsfunktion(Mitarbeiter.Zustand.Abwesend, Eingabe.Links));
        Assert.assertEquals(Mitarbeiter.Zustand.Fehler, Mitarbeiter.uebergangsfunktion(Mitarbeiter.Zustand.Abwesend, Eingabe.Rechts));
        //Testen des Fehler-Zustands. Test ist erfolgreich, wenn der Zustand bei jeder Eingabe ('Links' & 'Rechts') 'Fehler' bleibt.
        Assert.assertEquals(Mitarbeiter.Zustand.Fehler, Mitarbeiter.uebergangsfunktion(Mitarbeiter.Zustand.Fehler, Eingabe.Links));
        Assert.assertEquals(Mitarbeiter.Zustand.Fehler, Mitarbeiter.uebergangsfunktion(Mitarbeiter.Zustand.Fehler, Eingabe.Rechts));

    }
}