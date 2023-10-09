import com.rabbitmq.client.*;

import java.io.IOException;

public class TaskConsumer {
    private final static String QUEUE_NAME = "task_queue";

    public static void main(String[] args) throws Exception {
        // Create a connection to RabbitMQ
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost"); // Replace with your RabbitMQ server's hostname
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            // Declare the queue
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);

            // Set up the consumer
            channel.basicConsume(QUEUE_NAME, true, new DeliverCallback() {
                @Override
                public void handle(String consumerTag, Delivery delivery) throws IOException {
                    String message = new String(delivery.getBody(), "UTF-8"); // Get message as String
                    System.out.println(" [x] Received '" + message + "'");
                    // Simulate task processing (you can replace this with your actual task logic)
                    simulateTaskProcessing();
                }
            }, new CancelCallback() {
                @Override
                public void handle(String consumerTag) throws IOException {
                    // Handle cancellation if needed
                }
            });

            System.out.println(" [*] Waiting for messages. To exit, press CTRL+C");
            // Keep the consumer running
            while (true) {
                Thread.sleep(1000);
            }
        }
    }

    private static void simulateTaskProcessing() {
        try {
            Thread.sleep(2000); // Simulate task processing time (2 seconds)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
