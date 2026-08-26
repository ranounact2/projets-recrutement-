package com.centoria.jobmaroc.dao.mongodb;

import com.centoria.jobmaroc.common.context.ApplicationContext;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.connection.SocketSettings;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.concurrent.TimeUnit;

/**
 * Fabrique de gestionnaire d'accès mongodb
 * Un par domain
 *
 * @author pc
 */
public class MongoDBManagerFactory {
    /**
     * Using thread local and proxies for storing database
     * <p>
     * One thread = one db connection = one transaction
     */

    /*
     * Singleton
     * Replace by Spring, seam or other later on
     */
    private static MongoDBManagerFactory instance = new MongoDBManagerFactory();

    private IMongoManager mongoManager = null;

    private MongoDBManagerFactory() {
    }

    public static MongoDBManagerFactory getInstance() {
        return instance;
    }

    public IMongoManager getManager() {
        if (mongoManager == null) {
            mongoManager = new MongoDBManager();
        }
        return mongoManager;
    }

    /**
     * Inner class implementing IMongoManager
     */
    private static class MongoDBManager implements IMongoManager {
        private final MongoClient client;
        private final MongoDatabase database;

        public MongoDBManager() {
            // Configure POJO codec
            CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
            CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                    CodecRegistries.fromProviders(pojoCodecProvider));
            int port = ApplicationContext.getInstance().getProps().getIntegerValue("db.port");
            String connectionString = ApplicationContext.getInstance().getProps().getValue("db.cnnection.string");//"mongodb://localhost:27017";

            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(connectionString))
                    .codecRegistry(pojoCodecRegistry)
                    .applyToSocketSettings(builder ->
                            builder.applySettings(SocketSettings.builder()
                                    .connectTimeout(30, TimeUnit.SECONDS)
                                    .readTimeout(30, TimeUnit.SECONDS)
                                    .build()
                            )
                    )
                    .build();


            this.client = MongoClients.create(settings);
            this.database = client.getDatabase("emplois-maroc").withCodecRegistry(pojoCodecRegistry);
        }

        @Override
        public MongoDatabase getDatabase() {
            return database;
        }
    }


}
