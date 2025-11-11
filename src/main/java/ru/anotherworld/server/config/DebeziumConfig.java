package ru.anotherworld.server.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.debezium.config.Configuration;
import io.debezium.embedded.Connect;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.RecordChangeEvent;
import io.debezium.engine.format.ChangeEventFormat;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Slf4j
@org.springframework.context.annotation.Configuration
public class DebeziumConfig {

    private final KafkaTemplate<?, ?> kafkaTemplate;
    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${kafka.topic}")
    private String topic;

    @Value("${debezium.server1.host}")
    private String server1Host;

    @Value("${debezium.server1.port}")
    private String server1Port;

    @Value("${debezium.server1.database}")
    private String server1Database;

    @Value("${debezium.server1.user}")
    private String server1User;

    @Value("${debezium.server1.password}")
    private String server1Password;

    @Value("${debezium.server2.host}")
    private String server2Host;

    @Value("${debezium.server2.port}")
    private String server2Port;

    @Value("${debezium.server2.database}")
    private String server2Database;

    @Value("${debezium.server2.user}")
    private String server2User;

    @Value("${debezium.server2.password}")
    private String server2Password;

    @Value("${debezium.server1.slot.name}")
    private String server1SlotName;

    @Value("${debezium.server2.slot.name}")
    private String server2SlotName;

    @Value("${debezium.server1.database.server.name}")
    private String server1ServerName;

    @Value("${debezium.server2.database.server.name}")
    private String server2ServerName;

    public DebeziumConfig(KafkaTemplate<?, ?> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Bean
    @Lazy
    public io.debezium.config.Configuration server1Connector() {
        return io.debezium.config.Configuration.create()
                .with("name", "server1-connector")
                .with("connector.class", "io.debezium.connector.postgresql.PostgresConnector")
                .with("offset.storage", "org.apache.kafka.connect.storage.MemoryOffsetBackingStore")
                .with("offset.flush.interval.ms", 60000)
                .with("database.hostname", server1Host)
                .with("database.port", server1Port)
                .with("database.user", server1User)
                .with("database.password", server1Password)
                .with("database.dbname", server1Database)
                .with("topic.prefix", server1ServerName)  // Add this line
                .with("table.include.list", "public.*")
                .with("plugin.name", "pgoutput")
                .with("publication.name", "dbz_publication")
                .with("publication.autocreate.mode", "filtered")
                .with("tombstones.on.delete", "false")
                .with("database.server.name", server1ServerName)
                .with("slot.name", server1SlotName)
                .build();
    }

    @Bean
    @Lazy
    public io.debezium.config.Configuration server2Connector() {
        return io.debezium.config.Configuration.create()
                .with("name", "server2-connector")
                .with("connector.class", "io.debezium.connector.postgresql.PostgresConnector")
                .with("offset.storage", "org.apache.kafka.connect.storage.MemoryOffsetBackingStore")
                .with("offset.flush.interval.ms", 60000)
                .with("database.hostname", server2Host)
                .with("database.port", server2Port)
                .with("database.user", server2User)
                .with("database.password", server2Password)
                .with("database.dbname", server2Database)
                .with("table.include.list", "public.*")
                .with("plugin.name", "pgoutput")
                .with("publication.name", "dbz_publication")
                .with("publication.autocreate.mode", "filtered")
                .with("tombstones.on.delete", "false")
                .with("database.server.name", server2ServerName)
                .with("topic.prefix", server2ServerName)
                .with("slot.name", server2SlotName)
                .build();
    }

    @Bean
    @Lazy
    @Qualifier("debeziumEngine1")
    public DebeziumEngine<RecordChangeEvent<SourceRecord>> debeziumEngine1() {
        return createEngine(server1Connector(), "server1-connector");
    }

    @Bean
    @Lazy
    @Qualifier("debeziumEngine2")
    public DebeziumEngine<RecordChangeEvent<SourceRecord>> debeziumEngine2() {
        return createEngine(server2Connector(), "server2-connector");
    }

    private DebeziumEngine<RecordChangeEvent<SourceRecord>> createEngine(
            Configuration config, String connectorName) {
        Properties props = config.asProperties();
        props.setProperty("transforms", "unwrap");
        props.setProperty("transforms.unwrap.type", "io.debezium.transforms.ExtractNewRecordState");
        props.setProperty("transforms.unwrap.drop.tombstones", "false");
        ObjectMapper objectMapper = new ObjectMapper();
        return DebeziumEngine.create(ChangeEventFormat.of(Connect.class))
                .using(props)
                .notifying(recordEvent -> {
                    SourceRecord sourceRecord = recordEvent.record();
                    String topic = sourceRecord.topic();

//                    Object valueObj = sourceRecord.value();
//                    if (valueObj != null && topic.contains("server2")) {
//                        try {
//                            String jsonValue;
//
//                            // Если пришёл Struct (тип Debezium), конвертируем в Map и сериализуем
//                            if (valueObj instanceof Struct struct) {
//                                Map<String, Object> map = new HashMap<>();
//                                struct.schema().fields().forEach(f -> map.put(f.name(), struct.get(f)));
//                                jsonValue = new ObjectMapper().writeValueAsString(map);
//                            } else {
//                                // Для любых других типов — просто сериализуем напрямую
//                                jsonValue = new ObjectMapper().writeValueAsString(valueObj);
//                            }
//
//                            // Отправляем как String (т.к. KafkaTemplate ожидает строку)
//                            ((KafkaTemplate<String, String>) kafkaTemplate).send(topic, jsonValue);
//
//                            log.info("✅ Sent to Kafka topic {}: {}", topic, jsonValue);
//
//                        } catch (Exception e) {
//                            log.error("❌ Error serializing Debezium event for topic {}: {}", topic, e.getMessage(), e);
//                        }
//                    } else {
//                        log.debug("⚠️ Skipping null value for topic {}", topic);
//                    }

                    log.debug("Received change event for {}: {}", topic, sourceRecord);
                })
                .build();
    }

    private Map<String, Object> structToMap(Struct struct) {
        Map<String, Object> map = new HashMap<>();
        struct.schema().fields().forEach(f -> map.put(f.name(), struct.get(f)));
        return map;
    }
}
