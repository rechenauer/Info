package thro.inf.RestApplikationKleiderkreisel.server.konto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/*
 * Diese Klasse ermöglicht dem RESTful WebService Benutzer durch den Namen im Kontospeicher auszulesen.
 * Falls der Benutzer noch nicht existiert, so wird eine 'UsernameNotFoundException' ausgelöst.
 *Annotations:
 *
 *'@Override':   Die Methode 'loadUserByUsername()' muss überschrieben werden,
 *               da das Interface 'UserDetailsService' implementiert wird.
 * '@Autowired': Eine Instanz von KontoSpeicher wird dem Konstruktor eingeführt (injected),
 *               wenn KontoDetailsService erstellt wird.
 */

public class KontoDetailsService implements UserDetailsService {
    private final KontoSpeicher kontoSpeicher;

    @Autowired
    public KontoDetailsService(KontoSpeicher kontoSpeicher) {
        this.kontoSpeicher = kontoSpeicher;
    }

    @Override
    public UserDetails loadUserByUsername(String benutzerName) throws UsernameNotFoundException {
        Konto konto = kontoSpeicher.findByBenutzername(benutzerName).orElseThrow(() ->
                new UsernameNotFoundException("Der Benutzer " + benutzerName + " konnte nicht gefunden werden."));
        UserDetails benutzerDetails = User.builder().username(benutzerName).password(konto.getPasswort())
                .roles("ADMINISTRATOR", "BENUTZER").build();
        return benutzerDetails;
    }
}
