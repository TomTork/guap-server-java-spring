package ru.anotherworld.server.service;

import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.RecordChangeEvent;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DebeziumConnectorService {

    private final DebeziumEngine<RecordChangeEvent<SourceRecord>> debeziumEngine1;
    private final DebeziumEngine<RecordChangeEvent<SourceRecord>> debeziumEngine2;
    private final Executor executor = Executors.newSingleThreadExecutor();

    @PostConstruct
    public void startConnectors() {
        log.info("Starting Debezium connectors...");
        startEngine(debeziumEngine1, "server1");
        startEngine(debeziumEngine2, "server2");
    }

    private void startEngine(DebeziumEngine<RecordChangeEvent<SourceRecord>> engine, String name) {
        try {
            executor.execute(engine);
            log.info("Started Debezium connector for {}", name);
        } catch (Exception e) {
            log.error("Error starting Debezium connector for {}: {}", name, e.getMessage(), e);
        }
    }

    @PreDestroy
    public void stopConnectors() {
        log.info("Stopping Debezium connectors...");
        stopEngine(debeziumEngine1, "server1");
        stopEngine(debeziumEngine2, "server2");
    }

    private void stopEngine(DebeziumEngine<?> engine, String name) {
        if (engine != null) {
            try {
                engine.close();
                log.info("Stopped Debezium connector for {}", name);
            } catch (IOException e) {
                log.error("Error stopping Debezium connector for {}: {}", name, e.getMessage(), e);
            }
        }
    }
}
