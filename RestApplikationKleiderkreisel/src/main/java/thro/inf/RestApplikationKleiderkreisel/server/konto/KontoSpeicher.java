package thro.inf.RestApplikationKleiderkreisel.server.konto;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/*
 * Dieses Interface bietet dem RESTful WebService die Möglichkeit die Konten abzuspeichern.
 * Dies wird mithilfe der Klasse 'Optional<T>' umgesetzt. 'Optional<T>' stellt Methoden bereit,
 * um den oft lästigen und aufgeblähten Code produzierenden Umgang mit null-Werten deutlich zu vereinfachen.
 * */

public interface KontoSpeicher extends CrudRepository<Konto, Long> {
    Optional<Konto> findByBenutzername(String benutzerName);
}
