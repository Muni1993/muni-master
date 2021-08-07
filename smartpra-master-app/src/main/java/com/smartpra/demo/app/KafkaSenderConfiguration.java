package com.smartpra.demo.app;
/* 
 * Kafka sender configuration class 
 * this is manual configuration
 * */

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class KafkaSenderConfiguration {

	//need to configure in application.yml file
	@Value("${spring.kafka.bootstrap.servers}")
	private String bootstrapServers;

	@Bean
	public Map<String, Object> producerFactory() {

		Map<String, Object> configs = new HashMap<>();
		configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1.:9092");
		configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return configs;

	}

	@Bean
	public ProducerFactory<String, Aircraft> producerFactoryAircraft() {
		return new DefaultKafkaProducerFactory<>(producerFactory());
	}

	public KafkaTemplate<String, Aircraft> kafkaTemplateAircraft() {
		return new KafkaTemplate<>(producerFactoryAircraft());
	}

}
