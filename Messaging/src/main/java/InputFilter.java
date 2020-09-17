import javax.jms.*;
import javax.naming.NamingException;
import java.util.Arrays;
import java.util.logging.Logger;

public class InputFilter extends ActiveMQComponent {

    private static Logger logger = Logger.getLogger(InputFilter.class.getName());

    private InputFilter() {
        super();
        init();
    }

    @Override
    public void init() {
        super.init();
        try {
            Destination destination = (Destination) initialContext.lookup("dynamicQueues/fahrt_daten");
            messageConsumer = session.createConsumer(destination);

            //Produzenten einrichten
            Topic topic = session.createTopic("distributor");
            messageProducer = session.createProducer(topic);
            messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            destination = session.createQueue("alarms");
            alarmMessageProducer = session.createProducer(destination);
            alarmMessageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        } catch (JMSException | NamingException e) {
            logger.severe("Initialisieren der Klasse fehlgeschlagen: " + getClass().getName() + ".");
            logger.severe(Arrays.toString(e.getStackTrace()));
        }
    }

    private void startHandlingData() {
        isRunning.set(true);
        while (isRunning.get()) {
            try {
                Thread.sleep(10);
                ObjectMessage receivedObjectMessage = (ObjectMessage) messageConsumer.receive();
                Nachricht nachricht = (Nachricht)receivedObjectMessage.getObject();
                MessageProducer temp;
                if (nachricht.isAlarm()) {
                    temp = alarmMessageProducer;
                } else {
                    temp = messageProducer;
                }
                temp.send(receivedObjectMessage);
            } catch (InterruptedException | JMSException e) {
                logger.severe("Fehler beim Empfangen der Daten");
                logger.severe(Arrays.toString(e.getStackTrace()));
            }
        }
    }

    public static void main(String[] args) {
        final Thread mainThread = Thread.currentThread();
        InputFilter inputFilter = new InputFilter();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            inputFilter.shutdown();
            inputFilter.dispose();
            try {
                mainThread.join();
            } catch (InterruptedException e) {
                logger.severe("Haupt-Thread konnte nicht beigetreten werden.");
                logger.severe(Arrays.toString(e.getStackTrace()));
            }
        }));
        inputFilter.startHandlingData();
        inputFilter.dispose();
    }
}
