package thro.inf.RestApplikationKleiderkreisel.server.kleidung;

import org.springframework.data.repository.CrudRepository;

public interface KleidungsLager extends CrudRepository<Kleidung, Long> {
}
