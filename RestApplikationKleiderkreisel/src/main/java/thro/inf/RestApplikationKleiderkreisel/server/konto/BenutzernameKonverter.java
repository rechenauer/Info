package thro.inf.RestApplikationKleiderkreisel.server.konto;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;
import java.security.Key;
import java.util.Base64;

public class BenutzernameKonverter implements AttributeConverter<String, String> {
    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";
    private static final byte[] KEY = "%36^Vfxqj3LUo&L%".getBytes();

    //Verschlüssele Daten mit AES Verschlüsselung
    @Override
    public String convertToDatabaseColumn(String entschluesselteDaten) {
        Key schluessel = new SecretKeySpec(KEY, "AES");
        try {
            Cipher ziffer = Cipher.getInstance(ALGORITHM);
            ziffer.init(Cipher.ENCRYPT_MODE, schluessel);
            return Base64.getEncoder().encodeToString(ziffer.doFinal(entschluesselteDaten.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //Entschlüsselung der verschlüsselten Daten mit dem "AES" Algorithmus
    @Override
    public String convertToEntityAttribute(String verschluesselteDaten) {
        Key schluessel = new SecretKeySpec(KEY, "AES");
        try {
            Cipher ziffer = Cipher.getInstance(ALGORITHM);
            ziffer.init(Cipher.DECRYPT_MODE, schluessel);
            return new String(ziffer.doFinal(Base64.getDecoder().decode(verschluesselteDaten)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
