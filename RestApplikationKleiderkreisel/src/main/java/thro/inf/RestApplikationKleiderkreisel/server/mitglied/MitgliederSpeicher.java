package thro.inf.RestApplikationKleiderkreisel.server.mitglied;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/*
 * Dieses Interface bietet dem RESTful WebService die Möglichkeit die Mitglieder abzuspeichern.
 * Dies wird mithilfe der Klasse 'Optional<T>' umgesetzt. 'Optional<T>' stellt Methoden bereit,
 * um den oft lästigen und aufgeblähten Code produzierenden Umgang mit null-Werten deutlich zu vereinfachen.
 * */
public interface MitgliederSpeicher extends CrudRepository<Mitglied, Long> {
    Optional<Mitglied> findByBenutzername(String benutzerName);
}
