package thro.inf.RestApplikationKleiderkreisel.server.kleidung;

import javax.persistence.Embeddable;

/*
 * Dieses Enum bietet dem RESTful WebService verschiedene KleidungsArten, wie z.B 'Hose', 'Kleid', 'Hemd' und 'Bluse'.
 * Die Annotation '@Embeddable' signalisiert, dass dieses Enum in eine andere Klasse eingefügt werden muss.
 * */

@Embeddable
public enum KleidungsArt {
    Hose, Kleid, Hemd, Bluse
}
