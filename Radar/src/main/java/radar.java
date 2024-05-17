import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import models.AngleDistanceModel;
import models.TargetPositionModel;
import models.TowerPositionModel;

import java.awt.geom.Point2D;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class radar {
	public static void main(String[] args) {
		Properties properties = new Properties();
		properties.put("bootstrap.servers", "localhost:9092");
		properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		Properties consumerProperties = new Properties();
		consumerProperties.put("bootstrap.servers", "localhost:9092");
		consumerProperties.put("group.id", "test-group");
		consumerProperties.put("key.deserializer", StringDeserializer.class.getName());
		consumerProperties.put("value.deserializer", StringDeserializer.class.getName());
		consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		Properties consumerProperties2 = new Properties();
		consumerProperties2.put("bootstrap.servers", "localhost:9092");
		consumerProperties2.put("group.id", "test-group");
		consumerProperties2.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		consumerProperties2.put("value.deserializer", TowerPositionModel.class.getName());
		consumerProperties2.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProperties);
		KafkaConsumer<String, TowerPositionModel> consumer2 = new KafkaConsumer<>(consumerProperties2);

		String topicName = "TargetPointPosition";
		String towerTopicName = "TowerPosition";

		consumer.subscribe(Arrays.asList(topicName));
		consumer2.subscribe(Arrays.asList(towerTopicName));

		KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

		while (true) {
			ConsumerRecords<String, TowerPositionModel> towerPositions = consumer2.poll(Duration.ofMillis(100));
			for (ConsumerRecord<String, TowerPositionModel> record : towerPositions) {
				// TowerPositionModel twModel = new TowerPositionModel();

				System.out.println(record.value());

				ConsumerRecords<String, String> targetPositions = consumer.poll(Duration.ofMillis(100));
				for (ConsumerRecord<String, String> record2 : targetPositions) {
					/*
					 * TargetPositionModel targetPositionModel = record2.value(); AngleDistanceModel
					 * angleDistanceModel = new AngleDistanceModel(); angleDistanceModel =
					 * RadarControlService.getAngleDistance(towerPositionModel,
					 * targetPositionModel);
					 * System.out.println(angleDistanceModel.getCameraTowerPosition());
					 * System.out.println("hi");
					 */
				}
			}

			/*
			 * ConsumerRecords<String, String> records =
			 * consumer.poll(Duration.ofMillis(100)); records.forEach(record -> { String
			 * topicName2 = "TargetBearingPosition"; ProducerRecord<String, String> record1
			 * = new ProducerRecord<>(topicName2, record.value()); producer.send(record1);
			 * producer.flush();
			 * 
			 * 
			 * });
			 */

		}

	}
}
