package ru.anotherworld.server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;

import javax.sql.DataSource;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DatabaseSynchronizationService {

    private final JdbcTemplate primaryJdbcTemplate;
    private final JdbcTemplate secondaryJdbcTemplate;
    private final ObjectMapper objectMapper;

    // Внедряем sync.origin из application.properties
    @Value("${sync.origin}")
    private String currentOrigin;

    @KafkaListener(topicPattern = "server[12]\\.public\\..*", groupId = "${kafka.group-id}")
    public void listen(@Payload String message) {
        try {
            log.debug("Received message: {}", message);
            
            // Десериализуем JSON строку в Map
            Map<String, Object> payload = objectMapper.readValue(message, Map.class);
            
            String operation = (String) payload.get("op");

            // КЛЮЧЕВАЯ ПРОВЕРКА: пропускаем СВОИ изменения
            String syncOrigin = (String) payload.get("sync_origin");
            if (syncOrigin != null && syncOrigin.equals(currentOrigin)) {
                log.debug("Skipping own change from origin: {}", syncOrigin);
                return;
            }

            Map<String, Object> source = (Map<String, Object>) payload.get("source");
            if (source == null || operation == null) {
                log.warn("Received message with missing source or operation: {}", message);
                return;
            }

            String sourceDb = (String) source.get("db");
            String table = (String) source.get("table");

            if (sourceDb == null || table == null) {
                return;
            }

            log.info("Applying {} operation on table {} from origin {}",
                    operation, table, syncOrigin != null ? syncOrigin : sourceDb);

            // Определяем, в какую БД писать
            JdbcTemplate targetJdbcTemplate = "server1".equals(sourceDb)
                    ? secondaryJdbcTemplate
                    : primaryJdbcTemplate;

            switch (operation) {
                case "c" -> handleCreate(targetJdbcTemplate, table, (Map<String, Object>) payload.get("after"));
                case "u" -> handleUpdate(targetJdbcTemplate, table,
                        (Map<String, Object>) payload.get("before"),
                        (Map<String, Object>) payload.get("after"));
                case "d" -> handleDelete(targetJdbcTemplate, table, (Map<String, Object>) payload.get("before"));
                default -> log.warn("Unhandled operation: {}", operation);
            }

        } catch (Exception e) {
            log.error("Error processing Kafka event: {}", e.getMessage(), e);
        }
    }

    private void handleCreate(JdbcTemplate jdbcTemplate, String table, Map<String, Object> data) {
        if (data == null || data.isEmpty()) return;

        try {
            StringBuilder columns = new StringBuilder();
            StringBuilder values = new StringBuilder();
            Object[] params = new Object[data.size()];
            int i = 0;

            for (Map.Entry<String, Object> entry : data.entrySet()) {
                if (i > 0) {
                    columns.append(", ");
                    values.append(", ");
                }
                columns.append("\"").append(entry.getKey()).append("\"");
                values.append("?");
                params[i] = entry.getValue();
                i++;
            }

            String sql = String.format("INSERT INTO %s (%s) VALUES (%s) ON CONFLICT DO NOTHING",
                table, columns, values);

            jdbcTemplate.update(sql, params);
            log.debug("Inserted record into {}: {}", table, data);
        } catch (Exception e) {
            log.error("Error handling create operation for table {}: {}", table, e.getMessage(), e);
        }
    }

    private void handleUpdate(JdbcTemplate jdbcTemplate, String table,
                             Map<String, Object> before, Map<String, Object> after) {
        if (after == null || after.isEmpty()) return;

        try {
            StringBuilder setClause = new StringBuilder();
            StringBuilder whereClause = new StringBuilder();
            Object[] params = new Object[after.size() + before.size()];
            int i = 0;

            // Build SET clause
            for (Map.Entry<String, Object> entry : after.entrySet()) {
                if (i > 0) {
                    setClause.append(", ");
                }
                setClause.append("\"").append(entry.getKey()).append("\" = ?");
                params[i] = entry.getValue();
                i++;
            }

            // Build WHERE clause using the before values (primary keys)
            int whereStart = i;
            for (Map.Entry<String, Object> entry : before.entrySet()) {
                if (i > whereStart) {
                    whereClause.append(" AND ");
                } else {
                    whereClause.append(" WHERE ");
                }
                whereClause.append("\"").append(entry.getKey()).append("\" = ?");
                params[i] = entry.getValue();
                i++;
            }

            String sql = String.format("UPDATE %s SET %s%s", table, setClause, whereClause);

            int updated = jdbcTemplate.update(sql, params);
            log.debug("Updated {} records in {}: {}", updated, table, after);
        } catch (Exception e) {
            log.error("Error handling update operation for table {}: {}", table, e.getMessage(), e);
        }
    }

    private void handleDelete(JdbcTemplate jdbcTemplate, String table, Map<String, Object> data) {
        if (data == null || data.isEmpty()) return;

        try {
            StringBuilder whereClause = new StringBuilder(" WHERE ");
            Object[] params = new Object[data.size()];
            int i = 0;

            for (Map.Entry<String, Object> entry : data.entrySet()) {
                if (i > 0) {
                    whereClause.append(" AND ");
                }
                whereClause.append("\"").append(entry.getKey()).append("\" = ?");
                params[i] = entry.getValue();
                i++;
            }

            String sql = String.format("DELETE FROM %s%s", table, whereClause);

            int deleted = jdbcTemplate.update(sql, params);
            log.debug("Deleted {} records from {}: {}", deleted, table, data);
        } catch (Exception e) {
            log.error("Error handling delete operation for table {}: {}", table, e.getMessage(), e);
        }
    }
}
