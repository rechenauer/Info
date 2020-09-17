
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Arrays;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public abstract class ActiveMQComponent {

    Connection connection;

    Session session;

    MessageProducer messageProducer;

    MessageProducer alarmMessageProducer;

    MessageConsumer messageConsumer;

    AtomicBoolean isRunning;

    InitialContext initialContext;

    static Logger logger = Logger.getLogger(ActiveMQComponent.class.getName());

    ActiveMQComponent() {
    }

    protected void init() {
        isRunning = new AtomicBoolean();
        isRunning.set(false);

        //Setup-Eigenschaften

        Properties eigenschaften = new Properties();
        eigenschaften.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        eigenschaften.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");

        try {
            //Richtet Kontext ein
            initialContext = new InitialContext(eigenschaften);
            ActiveMQConnectionFactory connectionFactory = (ActiveMQConnectionFactory) initialContext.lookup("ConnectionFactory");
            connectionFactory.setTrustAllPackages(true);

            //Setzt die Verbindung auf
            connection = connectionFactory.createConnection();
            connection.setExceptionListener(e -> logger.severe("Eine JMS Exception ist aufgetaucht. " + Arrays.toString(e.getStackTrace())));
            connection.setClientID(UUID.randomUUID().toString());
            connection.start();

            //Setzt Sitzung auf
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        } catch (NamingException | JMSException e) {
            logger.severe("Initlalisieren der Klasse fehlgeschlagen: " + getClass().getName() + ".");
            logger.severe(Arrays.toString(e.getStackTrace()));
        }
    }

    void shutdown() {
        isRunning.set(false);
    }

    void dispose() {
        try {
            if (messageProducer != null) {
                messageProducer.close();
            }
            if (alarmMessageProducer != null) {
                alarmMessageProducer.close();
            }
            if (messageConsumer != null) {
                messageConsumer.close();
            }
            if (session != null) {
                session.close();
            }
            if (connection != null) {
                connection.stop();
                connection.close();
            }
        } catch (JMSException e) {
            logger.severe("Verwerfen der Klasse fehlgeschlagen " + getClass().getName() + ".");
            logger.severe(Arrays.toString(e.getStackTrace()));
        }
    }
}
