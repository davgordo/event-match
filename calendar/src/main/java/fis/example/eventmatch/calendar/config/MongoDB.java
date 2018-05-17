package fis.example.eventmatch.calendar.config;

import java.io.IOException;
import java.net.UnknownHostException;

import com.mongodb.MongoClient;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.mongodb.ServerAddress.defaultHost;
import static de.flapdoodle.embed.mongo.distribution.Version.Main.PRODUCTION;
import static de.flapdoodle.embed.process.runtime.Network.localhostIsIPv6;
import static org.springframework.util.SocketUtils.findAvailableTcpPort;

@Configuration
public class MongoDB {

    private static final int PORT = findAvailableTcpPort();

    static {
        try {
            IMongodConfig mongodConfig = new MongodConfigBuilder()
                    .version(PRODUCTION)
                    .net(new Net(PORT, localhostIsIPv6()))
                    .build();
            MongodExecutable mongodExecutable = MongodStarter.getDefaultInstance().prepare(mongodConfig);
            mongodExecutable.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public MongoClient calendarDb() throws UnknownHostException {
        return new MongoClient(defaultHost(), PORT);
    }

}