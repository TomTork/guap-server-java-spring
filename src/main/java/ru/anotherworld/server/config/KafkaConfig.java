package ru.anotherworld.server.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.anotherworld.server.handler.event.ApartmentEvent;
import ru.anotherworld.server.handler.event.BuildingEvent;
import ru.anotherworld.server.handler.event.ElectricityEvent;
import ru.anotherworld.server.handler.event.WaterSupplyEvent;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Autowired
    Environment environment;

    Map<String, Object> producerConfig(){
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                environment.getProperty("spring.kafka.producer.bootstrap-servers"));
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                environment.getProperty("spring.kafka.producer.key-serializer"));
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, environment
                .getProperty("spring.kafka.producer.value-serializer"));
        config.put(ProducerConfig.ACKS_CONFIG, environment.getProperty("spring.kafka.producer.acks"));

        return config;
    }

    @Bean
    ProducerFactory<String, Object> producerFactory(){
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    @Bean
    KafkaTemplate<String, Object> kafkaTemplate(){
        return new KafkaTemplate<String, Object>(producerFactory());
    }

    @Bean
    NewTopic createTopic(){
        return TopicBuilder.name("lab-work")
                .partitions(3)
                .replicas(3)
                .configs(Map.of("min.insync.replicas", "2"))
                .build();
    }

    @Bean
    ConsumerFactory<String, Object> consumerFactory(){
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                environment.getProperty("spring.kafka.consumer.bootstrap.servers"));
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                environment.getProperty("spring.kafka.consumer.key-deserializer"));
        config.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        config.put(JsonDeserializer.TRUSTED_PACKAGES,
                environment.getProperty("spring.kafka.consumer.properties.spring.json.trusted.packages"));
        config.put(ConsumerConfig.GROUP_ID_CONFIG, environment.getProperty("spring.kafka.consumer.group-id"));
        config.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "ru.anotherworld.server.handler.event.ApartmentEvent");
        config.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "ru.anotherworld.server.handler.event.BuildingEvent");
        config.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "ru.anotherworld.server.handler.event.ElectricityEvent");
        config.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "ru.anotherworld.server.handler.event.WaterSupplyEvent");
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(
                config,
                new StringDeserializer(),
                new JsonDeserializer<>(Object.class) {
                    @Override
                    public Object deserialize(String topic, Headers headers, byte[] data) {
                        if (data == null) return null;

                        String key = null;
                        if (headers.lastHeader("kafka_receivedMessageKey") != null) {
                            key = new String(headers.lastHeader("kafka_receivedMessageKey").value());
                        }

                        if (key != null) {
                            if (key.startsWith("apartment"))
                                return new JsonDeserializer<>(ApartmentEvent.class, false)
                                        .deserialize(topic, headers, data);
                            if (key.startsWith("building"))
                                return new JsonDeserializer<>(BuildingEvent.class, false)
                                        .deserialize(topic, headers, data);
                            if (key.startsWith("electricity"))
                                return new JsonDeserializer<>(ElectricityEvent.class, false)
                                        .deserialize(topic, headers, data);
                            if (key.startsWith("water-supply"))
                                return new JsonDeserializer<>(WaterSupplyEvent.class, false)
                                        .deserialize(topic, headers, data);
                        }

                        return new JsonDeserializer<>(Object.class, false)
                                .deserialize(topic, headers, data);
                    }
                }
        );
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory
            (ConsumerFactory<String, Object> consumerFactory){
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}
