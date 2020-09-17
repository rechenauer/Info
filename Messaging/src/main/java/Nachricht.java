import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Nachricht implements Serializable {
    private int telematikEinheitsID;

    private double breite;

    private double laenge;

    private double entfernung;

    private long zeitstempel;

    private boolean isAlarm;

    Nachricht(int telematikEinheitsID, double breite, double laenge, double entfernung) {
        this.telematikEinheitsID = telematikEinheitsID;
        this.breite = breite;
        this.laenge = laenge;
        this.entfernung=entfernung;
        zeitstempel = Timestamp.valueOf(LocalDateTime.now()).getTime();
    }

    int getID() {
        return telematikEinheitsID;
    }

    public double getBreite() {
        return breite;
    }

    public double getLaenge() {
        return laenge;
    }

    public double getEntfernung() {
        return entfernung;
    }

    public LocalDateTime getLocalDateTime() {
        return new Timestamp(zeitstempel).toLocalDateTime();
    }

    boolean isAlarm() {
        return isAlarm;
    }

    void setIsAlarm(boolean isAlarm) {
        this.isAlarm = isAlarm;
    }
}
