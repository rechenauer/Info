package thro.inf.RestApplikationKleiderkreisel.apiversion1;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thro.inf.RestApplikationKleiderkreisel.server.kleidung.Kleidung;
import thro.inf.RestApplikationKleiderkreisel.server.kleidung.KleidungsLager;
import thro.inf.RestApplikationKleiderkreisel.server.mitglied.Mitglied;
import thro.inf.RestApplikationKleiderkreisel.server.mitglied.MitgliederSpeicher;
import thro.inf.RestApplikationKleiderkreisel.server.tausch.Tausch;
import thro.inf.RestApplikationKleiderkreisel.server.tausch.TauschSpeicher;

import java.time.LocalDate;

@Api(produces = MediaType.APPLICATION_JSON_VALUE)
@RequestMapping(value = "api/version1")
@RestController
public class TauschController {
    @Autowired
    private TauschSpeicher tauschSpeicher;
    @Autowired
    private MitgliederSpeicher mitgliederSpeicher;
    @Autowired
    private KleidungsLager kleidungsLager;

    @ApiOperation(value = "Tausche Kleidung", response = Tausch.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Tausch erfolgreich"),
            @ApiResponse(code = 404, message = "Tausch fehlgeschlagen")}
    )
    @RequestMapping(value = "tausch/erstelleen/{id}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Tausch> erstelleTausch(@PathVariable("id") Long id, @RequestBody Mitglied kaeufer) {
        Kleidung kleidung = kleidungsLager.findById(id).orElse(null);

        //Wenn kein Kleidungsstück mit solcher ID existiert
        if (kleidung == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        //Erstelle neues Tausch-Objekt
        Tausch tausch = new Tausch();
        tausch.setKleidungsId(kleidung.getId());
        tausch.setDatum(LocalDate.now());
        tausch.setKaeufer(kaeufer);
        tausch.setVerkaeufer(kleidung.getVerkaeufer());

        //Beide Tauschpartner ausfindig machen
        Mitglied kaeuferMitglied = mitgliederSpeicher.findById(kaeufer.getId()).orElse(null);
        Mitglied verkaeuferMitglied = mitgliederSpeicher.findById(kleidung.getVerkaeufer().getId()).orElse(null);

        //Wenn Käufer oder Verkäufer Mitglied gefunden wurde
        if (kaeuferMitglied == null || verkaeuferMitglied == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        //Wenn Käufer nicht genug Geld hat
        if (kaeufer.getKontoStand() < kleidung.getTauschWert() + 1) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        //Geldaustausch wird vollzogen, die Provision der Plattform wird automatisch abgezogen
        verkaeuferMitglied.setKontoStand(verkaeuferMitglied.getKontoStand() + kleidung.getTauschWert() - 1);
        kaeuferMitglied.setKontoStand(kaeuferMitglied.getKontoStand() - kleidung.getTauschWert() - 1);
        verkaeuferMitglied.getKleidungsListe().remove(kleidung);

        //Speichern aller Entitäten
        tauschSpeicher.save(tausch);
        mitgliederSpeicher.save(verkaeuferMitglied);
        mitgliederSpeicher.save(kaeuferMitglied);

        //Löschen des Kleidungsstücks aus dem Lager/Speicher
        kleidungsLager.delete(kleidung);

        return new ResponseEntity<>(tausch, HttpStatus.OK);
    }
}
