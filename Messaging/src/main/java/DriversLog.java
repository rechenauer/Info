import javax.jms.*;
import java.util.*;
import java.util.logging.Logger;

public class DriversLog extends ActiveMQComponent {
    private HashMap<Integer, LinkedList<Nachricht>> messageMap;

    private static Logger logger = Logger.getLogger(DriversLog.class.getName());

    private DriversLog() {
        super();
        init();
    }

    @Override
    public void init() {
        super.init();
        messageMap = new HashMap<>();
        try {
            Topic topic = session.createTopic("distributor");
            messageConsumer = session.createDurableSubscriber(topic, "DriversLog");
        } catch (JMSException e) {
            logger.severe("Initialisieren der Klasse fehlgeschlagen: " + getClass().getName() + ".");
            logger.severe(Arrays.toString(e.getStackTrace()));
        }
    }

    private void addToMap(ObjectMessage objectMessage) {
        try {
            Nachricht nachricht = (Nachricht) objectMessage.getObject();
            LinkedList<Nachricht> nachrichtenListe;
            if (!messageMap.containsKey(nachricht.getID())) {
                nachrichtenListe = new LinkedList<>();
                messageMap.put(nachricht.getID(), nachrichtenListe);
            } else {
                nachrichtenListe = messageMap.get(nachricht.getID());
            }
            nachrichtenListe.add(nachricht);
        } catch (JMSException e) {
            logger.severe("Hinzufügen der Nachricht zur Liste ist fehlgeschlagen.");
            logger.severe(Arrays.toString(e.getStackTrace()));
        }
    }

    private void startHandlingData() {
        isRunning.set(true);
        while (isRunning.get()) {
            try {
                ObjectMessage objectMessage = (ObjectMessage) messageConsumer.receive();
                logger.info("Empfangene Nachricht: " + objectMessage.toString());
                addToMap(objectMessage);
            } catch (JMSException e) {
                logger.severe("Empfangen der Nachricht fehlgeschlagen.");
                logger.severe(Arrays.toString(e.getStackTrace()));
            }
        }
    }

    public static void main(String[] args) {
        DriversLog fahrtenbuch = new DriversLog();
        Thread mainThread = Thread.currentThread();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            fahrtenbuch.shutdown();
            try {
                mainThread.join();
            } catch (InterruptedException e) {
                logger.severe("Haupt-Thread konnte nicht beigetreten werden.");
                logger.severe(Arrays.toString(e.getStackTrace()));
            }
        }));
        fahrtenbuch.startHandlingData();
        fahrtenbuch.dispose();
    }
}
