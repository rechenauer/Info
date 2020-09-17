package clientproxy;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import thro.inf.RestApplikationKleiderkreisel.server.mitglied.Mitglied;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
    Diese Klasse setzt Java-Aufrufe in entsprechende Aufrufe des RESTful WebServices um.
    Mit dieser Klasse lassen sich Mitglieder verwalten.
    Operationen wie löschen, updaten, erstellen oder suchen eines Mitglieds sind möglich.
    Genauso wie das Auflisten aller existierenden Mitglieder oder das Updaten des Kontostandes.
 */

public class Mitgliederverwaltung {

    private static final String basisURI = "http://localhost:8080/";

    private static final String API = "api/version1/";

    //Diese Methode ermöglicht es Mitglieder über die entsprechende ID zu finden.
    //Bei Erfolg, also wenn Statuscode 200 OK, wird der Body der Entity zurückgegeben.

    public Mitglied findMitgliedPerId(Long id) {
        RestTemplate template = new RestTemplate();
        ResponseEntity<Mitglied> response = template.getForEntity(basisURI + API + "mitglied" + id, Mitglied.class);
        if (response.getStatusCode().equals(HttpStatus.OK)) {
            return response.getBody();
        }
        return null;
    }

    //Diese Methode ermöglicht es alle Mitglieder, die bereits in der Liste gespeichert sind, auszugeben.
    //Ist die Liste leer, so wird eine neue, leere ArrayList zurückgegeben.

    public List<Mitglied> findAllMitglieder() {
        RestTemplate template = new RestTemplate();
        ResponseEntity<List<Mitglied>> response = template.exchange(basisURI + API + "mitglied",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Mitglied>>() {
                });
        if (response.getStatusCode().equals(HttpStatus.OK)) {
            return response.getBody();
        }
        return new ArrayList<>();
    }

    //Ermöglicht das Erstellen eines neuen Mitglieds.
    //Bei Statuscode 200 OK wird der Body der ResponseEntity zurückgegeben.

    public Mitglied createMitglied(Mitglied mitglied) {
        RestTemplate template = new RestTemplate();
        URI uri = template.postForLocation(basisURI + API + "/mitglied", mitglied, Mitglied.class);
        ResponseEntity<Mitglied> response = template.getForEntity(uri, Mitglied.class);

        if (response.getStatusCode().equals(HttpStatus.OK)) {
            return response.getBody();
        }
        return null;
    }

    //Ermöglicht das Löschen von Mitgliedern. Bei Erfolg wird der return-Wert "true" zurückgegeben.

    public boolean deleteMitglied(Mitglied mitglied) {
        RestTemplate template = new RestTemplate();
        HttpHeaders http = new HttpHeaders();
        http.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Mitglied> httpEntity = new HttpEntity<>(mitglied, http);
        ResponseEntity<Void> response = template.exchange(basisURI + API + "mitglied" +
                mitglied.getBenutzerName(), HttpMethod.DELETE, httpEntity, Void.class);
        return response.getStatusCode().equals(HttpStatus.NO_CONTENT);
    }

    //Diese Methode ermöglicht das Updaten von Mitgliedern.
    // Bei Statuscode 200 OK wird der Body der ResponseEntity zurückgegeben

    public Mitglied updateMitglied(Mitglied mitglied) {
        RestTemplate template = new RestTemplate();
        HttpHeaders http = new HttpHeaders();
        http.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        http.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Mitglied> httpEntity = new HttpEntity<>(mitglied, http);
        ResponseEntity<Mitglied> responseEntity = template.exchange(basisURI + API + "mitglied" + mitglied.getBenutzerName(),
                HttpMethod.PUT, httpEntity, Mitglied.class);

        if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return responseEntity.getBody();
        }
        return null;
    }

    //Diese Methode ermöglicht das Updaten des Kontostands.
    //Return-Wert ist "true", wenn ResponseEntity mit Statuscode 200 OK übereinstimmt.

    public boolean updateKontostand(Mitglied mitglied) {
        RestTemplate template = new RestTemplate();
        HttpHeaders http = new HttpHeaders();
        http.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        http.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Mitglied> httpEntity = new HttpEntity<>(mitglied, http);
        ResponseEntity<Mitglied> response = template.exchange(basisURI + API + "mitglied" + mitglied.getBenutzerName(),
                HttpMethod.PATCH, httpEntity, Mitglied.class);
        return response.getStatusCode().equals(HttpStatus.OK);
    }
}

