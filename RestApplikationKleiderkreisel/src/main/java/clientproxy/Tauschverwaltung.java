package clientproxy;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import thro.inf.RestApplikationKleiderkreisel.server.tausch.Tausch;

import java.net.URI;

/*
    Diese Klasse dient der Erstellung verschiedener Täusche.
 */
public class Tauschverwaltung {

    private static final String basisURI = "http://localhost:8080/";

    private static final String API = "api/version1/";

    //Diese Methode ermöglicht es einen Tausch zu erstellen.
    //Bei Statuscode 200 OK wird der Body des ResponseEntities zurückgegeben.

    public Tausch createTausch(Tausch tausch) {

        RestTemplate template = new RestTemplate();
        URI uri = template.postForLocation(basisURI + API + "tausch/{id}" + tausch.getId(), Tausch.class);
        ResponseEntity<Tausch> response = template.getForEntity(uri, Tausch.class);

        if (response.getStatusCode().equals(HttpStatus.OK)) {
            return response.getBody();
        }
        return null;

    }
}
