import javax.jms.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

public class TelematicUnit extends ActiveMQComponent {
    private int id;

    private static Random random = new Random();

    private TelematicUnit(int id) {
        super();
        init();
        this.id = id;
    }

    @Override
    public void init() {
        super.init();
        try {
            //Erzeuger aufsetzen
            Destination destination = session.createQueue("fahrt_daten");
            messageProducer = session.createProducer(destination);
            messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            alarmMessageProducer = session.createProducer(destination);
            alarmMessageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        } catch (JMSException e) {
            logger.severe("Initialisieren der Klasse fehlgeschlagen: " + getClass().getName() + ".");
            logger.severe(Arrays.toString(e.getStackTrace()));
        }
    }

    private void startSendingData(int sendIntervalSeconds) {
        isRunning.set(true);
        double entfernung = 0.0D;
        while (isRunning.get()) {
            try {
                Thread.sleep(sendIntervalSeconds * 1000);
                double breite = randomCoordinate(-90.0D, 90.0D);
                double laenge = randomCoordinate(-180.0D, 180.0D);

                double randomAlarm = randomDouble(0.0D, 100.0D);
                //Entfernung zwischen 100 und 180 km pro Stunde (in Meter)
                entfernung += randomDouble(28.77D, 50.0D) * sendIntervalSeconds;
                Nachricht nachricht = new Nachricht(id, breite, laenge, entfernung);

                MessageProducer temp;
                if (randomAlarm <= 20.0D) {
                    temp = alarmMessageProducer;
                    nachricht.setIsAlarm(true);
                } else {
                    temp = messageProducer;
                    nachricht.setIsAlarm(false);
                }
                temp.send(session.createObjectMessage(nachricht));
            } catch (InterruptedException | JMSException e) {
                logger.severe("Senden der Nachricht an Queue fehlgeschlagen.");
                logger.severe(Arrays.toString(e.getStackTrace()));
            }
        }
    }

    private static double randomDouble(double min, double max) {
        return (random.nextDouble() * (max - min)) + min;
    }

    private static double randomCoordinate(double min, double max) {
       //Die Genauigkeit von 7, da Breite und Länge nicht genauer als Zentimeter sind
        DecimalFormat decimalFormat = new DecimalFormat("####.#######");
        decimalFormat.setRoundingMode(RoundingMode.DOWN);
        return Double.parseDouble(decimalFormat.format(randomDouble(min, max)));
    }

    public static void main(String[] args) {
        final Thread mainThread = Thread.currentThread();
        TelematicUnit telematicUnit = new TelematicUnit(UUID.randomUUID().hashCode());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            telematicUnit.shutdown();
            try {
                mainThread.join();
            } catch (InterruptedException e) {
                logger.severe("Fehler beim Beitreten zum Haupt-Thread.");
                logger.severe(Arrays.toString(e.getStackTrace()));
            }
        }));

        telematicUnit.startSendingData(3);
        telematicUnit.dispose();
    }
}