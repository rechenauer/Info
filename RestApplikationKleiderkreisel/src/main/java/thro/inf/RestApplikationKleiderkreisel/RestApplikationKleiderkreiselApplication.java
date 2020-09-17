package thro.inf.RestApplikationKleiderkreisel;

import org.h2.tools.Server;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import thro.inf.RestApplikationKleiderkreisel.server.konto.Konto;
import thro.inf.RestApplikationKleiderkreisel.server.konto.KontoSpeicher;
import thro.inf.RestApplikationKleiderkreisel.server.mitglied.Adresse;
import thro.inf.RestApplikationKleiderkreisel.server.mitglied.Mitglied;
import thro.inf.RestApplikationKleiderkreisel.server.mitglied.MitgliederSpeicher;

import java.sql.SQLException;

@SpringBootApplication
public class RestApplikationKleiderkreiselApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestApplikationKleiderkreiselApplication.class, args);
    }


    @Bean
    InitializingBean init(KontoSpeicher kontoSpeicher, MitgliederSpeicher mitgliederSpeicher){
        return () -> {
            Konto test = new Konto("TEST", "test", true);
            kontoSpeicher.save(test);
            Adresse testAdresse = new Adresse("Taxistraße", (short) 1, 12312, "Rosenheim" );
            Mitglied testMitglied = new Mitglied("hansMaier", "test", "Hans",
                    "Maier", 8763.D, "hansMaier@gmx.de" );
            mitgliederSpeicher.save(testMitglied);
        };
    }
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server server() throws SQLException{
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "4001");
    }
}
