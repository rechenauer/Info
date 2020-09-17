package thro.inf.RestApplikationKleiderkreisel.apiversion1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import thro.inf.RestApplikationKleiderkreisel.server.konto.Konto;
import thro.inf.RestApplikationKleiderkreisel.server.konto.KontoSpeicher;
import thro.inf.RestApplikationKleiderkreisel.server.mitglied.Mitglied;
import thro.inf.RestApplikationKleiderkreisel.server.mitglied.MitgliederSpeicher;

import java.util.ArrayList;
import java.util.List;

@Api(produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = "apiversion1")
public class MitgliedController {
    @Autowired
    private KontoSpeicher kontoSpeicher;

    @Autowired
    private MitgliederSpeicher mitgliederSpeicher;

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Mitglied gefunden"),
            @ApiResponse(code = 404, message = "Kein Mitglied gefunden")})
    @RequestMapping(value = "mitglied/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> findMitgliedPerId(@PathVariable("id") Long id) {
        Mitglied ergebnis = mitgliederSpeicher.findById(id).orElse(null);

        if (ergebnis != null) {
            return new ResponseEntity<>(ergebnis, HttpStatus.OK);
        } else {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiResponses(value = @ApiResponse(code = 200, message = "Mitglieder gefunden"))
    @RequestMapping(value = "mitglied", method = RequestMethod.GET)
    public ResponseEntity<List<Mitglied>> findAllMitglieder() {
        Iterable<Mitglied> iterator = mitgliederSpeicher.findAll();
        List<Mitglied> ergebnis = new ArrayList<>();
        iterator.forEach(ergebnis::add);
        return new ResponseEntity<>(ergebnis, HttpStatus.OK);
    }

    @ApiOperation(value = "Erstelle neues Mitglied", response = Mitglied.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Neues Mitglied wurde erfolgreich erstellt"),
            @ApiResponse(code = 400, message = "Mitglied existiert bereits")})
    @RequestMapping(value = "mitglied", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createMitglied(@RequestBody Mitglied mitglied, UriComponentsBuilder uriComponentsBuilder) {
        Mitglied tempMember = mitgliederSpeicher.findByBenutzername(mitglied.getBenutzerName()).orElse(null);
        if (tempMember != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Konto mitgliedKonto = new Konto(mitglied.getBenutzerName(), mitglied.getPasswort(), true);
        kontoSpeicher.save(mitgliedKonto);
        mitgliederSpeicher.save(mitglied);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("api/version1/mitglied/{id}").buildAndExpand(mitglied.getId()).toUri());
        return new ResponseEntity<String>(httpHeaders, HttpStatus.CREATED);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Mitglied gefunden und gelöscht"),
            @ApiResponse(code = 404, message = "Kein Mitglied gefunden")})
    @RequestMapping(value = "mitglied/{benutzerName}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteMitglied(@PathVariable("benutzerName") String benutzerName, @RequestBody Mitglied mitglied) {
        Mitglied ergebnis = mitgliederSpeicher.findByBenutzername(benutzerName).orElse(null);

        if (ergebnis == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!ergebnis.getId().equals(mitglied.getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        mitgliederSpeicher.delete(ergebnis);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Aktualisiere existierendes Mitglied", response = Mitglied.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Mitglied gefunden und aktualisiert"),
            @ApiResponse(code = 404, message = "Kein Mitglied gefunden")
    })
    @RequestMapping(value = "mitglied/{benutzerName}", method = {RequestMethod.PUT, RequestMethod.PATCH},
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateMitglied(@PathVariable("benutzerName") String benutzerName,
                                                  @RequestBody Mitglied mitglied) {
        Mitglied ergebnis = mitgliederSpeicher.findByBenutzername(benutzerName).orElse(null);

        //Wenn kein solches Mitglied existiert
        if (ergebnis == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        //Wenn Datenbank Version nicht der Version des Mitglieds entspricht
        if (ergebnis.getVersion() > ergebnis.getVersion()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        ergebnis.setVorname(mitglied.getVorname());
        ergebnis.setNachname(mitglied.getNachname());
        ergebnis.setKontoStand(mitglied.getKontoStand());
        ergebnis.setEmailAdresse(mitglied.getEmailAdresse());
        ergebnis.setPasswortHash(mitglied.getPasswort());
        ergebnis.setAdresse(mitglied.getAdresse());

        //Speichern des Mitglieds auf die Datenbank
        mitgliederSpeicher.save(ergebnis);
        return new ResponseEntity<>(ergebnis, HttpStatus.OK);
    }

    @ApiOperation(value = "Aktualisiere Kontostand", response = Mitglied.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Mitglied gefunden und Kontostand aktualisiert"),
            @ApiResponse(code = 404, message = "Kein Mitglied gefunden")})
    @RequestMapping(value = "mitglied/update/{benutzerName}/kontoStand", method = {RequestMethod.GET, RequestMethod.PATCH})
    public ResponseEntity aktualisiereKontostand(@PathVariable("benutzerName") String benutzerName, @RequestBody Mitglied mitglied) {
        Mitglied ergebnis = mitgliederSpeicher.findByBenutzername(benutzerName).orElse(null);

        //Wenn kein solches Mitglied existiert
        if (ergebnis == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        //Wenn Version der Datenbank nicht der der gegebenen Version des Mitglieds übereinstimmt
        if (ergebnis.getVersion() <= mitglied.getVersion()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        ergebnis.setKontoStand(mitglied.getKontoStand());
        mitgliederSpeicher.save(ergebnis);
        return new ResponseEntity<>("Kontostand erfolgreich aktualisiert", HttpStatus.OK);
    }

}
