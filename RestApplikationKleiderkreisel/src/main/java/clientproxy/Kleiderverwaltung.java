package clientproxy;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import thro.inf.RestApplikationKleiderkreisel.server.kleidung.Kleidung;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/*
    Diese Klasse setzt Java-Aufrufe in entsprechende Aufrufe des RESTful WebServices um.
    Operationen wie löschen, updaten, erstellen oder suchen eines Kleidungsstücks sind möglich.
    Genauso wie das Auflisten aller existierenden Kleidungsstücke.
*/

public class Kleiderverwaltung {
    private static final String basisURI = "http://localhost:8080/";

    private static final String API = "api/version1/";

    //Methode "findeKleidungPerId" ermöglicht es, ein Kleidungsstück über die ID zu suchen.
    // Bei Erfolg wird der Statuscode 200 OK zurückgegeben. Andernfalls ist der return-Wert "null".

    public Kleidung findKleidungPerId(Long id) {
        RestTemplate template = new RestTemplate();
        ResponseEntity<Kleidung> entity = template.getForEntity(basisURI + API + "kleidung" + id, Kleidung.class);
        if (entity.getStatusCode().equals(HttpStatus.OK)) {
            return entity.getBody();
        }
        return null;
    }

    //Diese Methode ermöglicht das Auflisten aller Kleidungsstücke, die in der Liste enthalten sind.
    //Bei Statuscode 200 OK wird diese zurückgegeben. Alternativ, also wenn es noch keine Kleidungsstücke gibt,
    //dann wird eine neue ArrayList als return-Wert zurückgegeben.

    public List<Kleidung> listAllKleidungsstuecke() {
        RestTemplate template = new RestTemplate();
        ResponseEntity<List<Kleidung>> entity = template.exchange(basisURI + API + "kleidung",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Kleidung>>() {
                });
        if (entity.getStatusCode().equals(HttpStatus.OK)) {
            return entity.getBody();
        }
        return new ArrayList<>();
    }

    //Diese Methode ermöglicht es neue Kleidungsstücke anzulegen.
    //Bei Statuscode 200 OK werden diese erfolgreich gespeichert und der Body des erstellten Kleidungsstücks
    // wird zurückgegeben. Schlägt die Neuerstellung fehl, dann wird "null" als return-Wert zurückgegeben.

    public Kleidung createKleidung(Kleidung kleidung) {
        RestTemplate template = new RestTemplate();
        URI uri = template.postForLocation(basisURI + API + "kleidung", kleidung, Kleidung.class);
        ResponseEntity<Kleidung> entity = template.getForEntity(uri, Kleidung.class);

        if (entity.getStatusCode().equals(HttpStatus.OK)) {
            return entity.getBody();
        }
        return null;
    }

    //Diese Methode ermöglicht das Löschen eines Kleidungsstücks.
    //Wurde dieses erfolgreich gelöscht, so wird am Ende "true" als return-Wert übergeben.

    public boolean deleteKleidungsstueck(Kleidung kleidung) {
        RestTemplate template = new RestTemplate();
        HttpHeaders http = new HttpHeaders();
        http.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Kleidung> entity = new HttpEntity<>(kleidung, http);
        ResponseEntity<Void> responseEntity = template.exchange(basisURI + API + "kleidung" + kleidung.getId(),
                HttpMethod.DELETE, entity, Void.class);
        return responseEntity.getStatusCode().equals(HttpStatus.NO_CONTENT);
    }

    //Diese Methode ermöglicht das Updaten eines Kleidungsstücks.
    //Bei Statuscode 200 OK werden die Änderungen erfolgreich abgespeichert und der Body der ResponseEntity wird zurückgegeben.
    public Kleidung updateKleidungsstueck(Kleidung kleidung) {
        RestTemplate template = new RestTemplate();
        HttpHeaders http = new HttpHeaders();
        http.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        http.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Kleidung> httpEntity = new HttpEntity<>(kleidung, http);
        ResponseEntity<Kleidung> responseEntity = template.exchange(basisURI + API + "kleidung" + kleidung.getId(),
                HttpMethod.PUT, httpEntity, Kleidung.class);

        if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return responseEntity.getBody();
        }
        return null;
    }
}
