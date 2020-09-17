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

import java.util.ArrayList;
import java.util.List;

@Api(produces = MediaType.APPLICATION_JSON_VALUE)
@RequestMapping(value = "api/version1")
@RestController
public class KleiderController {
    @Autowired
    private KleidungsLager kleidungsLager;

    @Autowired
    private MitgliederSpeicher mitgliederSpeicher;

    public KleiderController() {
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Kleidungsstück gefunden"),
            @ApiResponse(code = 404, message = "Kein Kleidungsstück gefunden")})
    @RequestMapping(value = "kleidung/{id}", method = RequestMethod.GET)
    public ResponseEntity findKleidungById(@PathVariable("id") Long id) {
        Kleidung kleidung = kleidungsLager.findById(id).orElse(null);
        if (kleidung != null) {
            return new ResponseEntity<>(kleidung, HttpStatus.OK);
        }
        return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    }

    @ApiResponses(value = {@ApiResponse(code = 200, message = "Kleidungsstück gefunden")})
    @RequestMapping(value = "kleidung", method = RequestMethod.GET)
    public ResponseEntity findAllKleidungsstücke() {
        Iterable<Kleidung> resultIterator = kleidungsLager.findAll();
        List<Kleidung> result = new ArrayList<>();
        resultIterator.forEach(result::add);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ApiOperation(value = "Erstelle neues Kleidungsstück", response = Kleidung.class)
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Kleidungsstück erstellt"),
            @ApiResponse(code = 403, message = "Mitglied nicht gefunden")})
    @RequestMapping(value = "kleidung", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createKleidung(@RequestBody Kleidung kleidung) {
        if (kleidung.getTauschWert() < kleidung.getNeuPreis() * 0.1F
                || kleidung.getTauschWert() > kleidung.getNeuPreis() * 0.5F) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        //Kleidungsstück wird abgespeichert
        kleidungsLager.save(kleidung);

        Mitglied verkaeufer = mitgliederSpeicher.findById(kleidung.getVerkaeufer().getId()).orElse(null);

        //Prüft, ob Verkäufer existiert und fügt das neu erstellte Kleidungsstück zum Kleidungslager hinzu,
        // falls Verkäufer existiert.
        if (verkaeufer != null && verkaeufer.getKleidungsListe().size() <= 10) {
            verkaeufer.getKleidungsListe().add(kleidung);
            mitgliederSpeicher.save(verkaeufer);
            kleidungsLager.save(kleidung);
            return new ResponseEntity<>("Kleidungsstück erfolgreich erstellt!", HttpStatus.CREATED);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Kleidungsstück gefunden und gelöscht!"),
            @ApiResponse(code = 404, message = "Kein Kleidungsstück gefunden!")})
    @RequestMapping(value = "kleidung/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteKleidung(@PathVariable("id") Long id,
                                                 @RequestBody Kleidung kleidung) {
        Kleidung gefundenesKleidungsstueck = kleidungsLager.findById(id).orElse(null);
        if (gefundenesKleidungsstueck == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        //Wenn Datenbank Version der Kleiderversion nicht übereinstimmt
        if (gefundenesKleidungsstueck.getVersion() > kleidung.getVersion()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        //Lösche Kleidungsstück von der Datenbank
        kleidungsLager.delete(gefundenesKleidungsstueck);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Update ein existierendes Kleidungsstück", response = Kleidung.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Kleidungsstück gefunden und geupdated"),
            @ApiResponse(code = 404, message = "Kein Kleidungsstück gefunden")})
    @RequestMapping(value = "kleidung/{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity updateKleidung(@PathVariable("id") Long id,
                                                @RequestBody Kleidung kleidung) {
        Kleidung kleidungsstueckErgebnis = kleidungsLager.findById(id).orElse(null);


        //Wenn ein Kleidungsstück mit der ID nicht existiert
        if (kleidungsstueckErgebnis == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        //Wenn Datenbank Version der Kleiderversion nicht übereinstimmt
        if (kleidungsstueckErgebnis.getVersion() > kleidung.getVersion()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        //Wenn Tauschwert zwischen 10 und 50 Prozent des Neupreises liegt
        if (kleidungsstueckErgebnis.getTauschWert() < kleidung.getNeuPreis() * 0.1F
                || kleidungsstueckErgebnis.getTauschWert() > kleidung.getNeuPreis() * 0.5F) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        //Erstelle neues Kleidungsstückergebnis Objekt
        kleidungsstueckErgebnis.setNeuPreis(kleidung.getNeuPreis());
        kleidungsstueckErgebnis.setMarke(kleidung.getMarke());
        kleidungsstueckErgebnis.setTauschWert(kleidung.getTauschWert());
        kleidungsstueckErgebnis.setGeschlecht(kleidung.getGeschlecht());
        kleidungsstueckErgebnis.setHersteller(kleidung.getHersteller());
        kleidungsstueckErgebnis.setGroesse(kleidung.getGroesse());

        //Speichern des Kleidungsstücks auf die Datenbank
        kleidungsLager.save(kleidungsstueckErgebnis);

        return new ResponseEntity<>(kleidungsstueckErgebnis, HttpStatus.OK);
    }
}


