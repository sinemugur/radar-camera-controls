import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class world extends JPanel {
	private int tower1X, tower1Y;
	private int tower2X, tower2Y;
	private int targetX, targetY;
	private int panelWidth, panelHeight;
	private int cameraX, cameraY;

	private Timer timer;
	private Timer timerConsumer;

	public world(int tower1X, int tower1Y, int tower2X, int tower2Y, int targetX, int targetY, int panelWidth,
			int panelHeight, int cameraX, int cameraY) {
		this.tower1X = tower1X;
		this.tower1Y = panelHeight - 60;
		this.tower2X = tower2X;
		this.tower2Y = panelHeight - 60;
		this.targetX = targetX;
		this.targetY = panelHeight - targetY;
		this.panelWidth = panelWidth;
		this.panelHeight = panelHeight;
		this.cameraX = cameraX;
		this.cameraY = cameraY;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(Color.RED);
		g.fillRect(tower1X, tower1Y, 20, 20);
		g.fillRect(tower2X, tower2Y, 20, 20);

		g.setColor(Color.BLUE);
		g.fillOval(targetX - 5, targetY - 5, 10, 10);

		g.setColor(Color.GREEN);
		g.fillRect(70, 0, cameraX, cameraY);
	}

	public void moveTarget(int x) {
		if (x == 0) {
			timer.cancel();
			timerConsumer.cancel();
			return;
		}

		Properties consumerProperties = new Properties();
		consumerProperties.put("bootstrap.servers", "localhost:9092");
		consumerProperties.put("group.id", "test-group");
		consumerProperties.put("key.deserializer", StringDeserializer.class.getName());
		consumerProperties.put("value.deserializer", StringDeserializer.class.getName());
		consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProperties);

		String topicNameConsumer = "CameraLossStatus";
		consumer.subscribe(Arrays.asList(topicNameConsumer));

		Properties properties = new Properties();
		properties.put("bootstrap.servers", "localhost:9092");
		properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		properties.put("value.serializer", TowerPositionModel.class.getName());

		Properties properties2 = new Properties();
		properties2.put("bootstrap.servers", "localhost:9092");
		properties2.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		properties2.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		KafkaProducer<String, String> producer = new KafkaProducer<>(properties2);
		KafkaProducer<String, byte[]> producer2 = new KafkaProducer<>(properties);

		String targetTopicName = "TargetPointPosition";

		String towerTopicName = "TowerPosition";

		String message = targetX + " " + targetY;

		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				targetX++;
				repaint();
				Point2D.Double double1 = new Point2D.Double(targetX, targetY);
				TargetPositionModel targetPositionModel = new TargetPositionModel();
				targetPositionModel.setTargetPosition(double1);
				String message = targetX + " " + targetY;
				ProducerRecord<String, String> record1 = new ProducerRecord<>(targetTopicName, message);
				producer.send(record1);
				producer.flush();

				Point2D.Double radar = new Point2D.Double(tower1X, tower1Y);
				Point2D.Double camera = new Point2D.Double(tower2X, tower2Y);
				TowerPositionModel towerPositionModel = new TowerPositionModel();
				towerPositionModel.setRadarTowerPosition(radar);
				towerPositionModel.setCameraTowerPosition(camera);
				byte[] serializedModel = SerializationUtils.serialize(towerPositionModel);
				ProducerRecord<String, byte[]> record2 = new ProducerRecord<>(towerTopicName, serializedModel);
				producer2.send(record2);
				producer2.flush();
			}
		};

		timer = new Timer();
		timer.scheduleAtFixedRate(task, 0, 1000);

		TimerTask task2 = new TimerTask() {
			@Override
			public void run() {
				cameraX += 10;
				cameraY += 10;
				repaint();

				ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
				records.forEach(record -> {
					System.out.println(record.value());
				});
			}
		};
		timerConsumer = new Timer();
		timerConsumer.scheduleAtFixedRate(task2, 0, 1000);

	}

	public static void main(String[] args) {
		int panelWidth = 400;
		int panelHeight = 400;

		int tower1X = 0, tower1Y = 0;
		int tower2X = 200, tower2Y = 0;

		int targetX = 10, targetY = 150;
		int cameraX = 80, cameraY = 80;

		JFrame frame = new JFrame("Tower and Target Positions");
		world panel = new world(tower1X, tower1Y, tower2X, tower2Y, targetX, targetY, panelWidth, panelHeight, 8,
				cameraY);

		JButton playButton = new JButton("Play");
		playButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				panel.moveTarget(1);
			}
		});

		JButton StopButton = new JButton("Stop");
		StopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				panel.moveTarget(0);
			}
		});

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(playButton);
		frame.add(buttonPanel, BorderLayout.WEST);
		frame.add(panel, BorderLayout.CENTER);

		JPanel buttonPanel1 = new JPanel();
		buttonPanel1.add(StopButton);
		frame.add(buttonPanel1, BorderLayout.EAST);
		frame.add(panel, BorderLayout.CENTER);

		frame.setSize(panelWidth, panelHeight + 50);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

	}
}
