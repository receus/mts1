package ru.savushkin.mts1.core1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class EnrichmentServiceTest {

    public static void main(String[] args) throws InterruptedException {

        Map<String, Map<String, String>> userData = Map.of(
                "88005553535", Map.of("firstName", "Vasya", "lastName", "Ivanov"),
                "1234567890", Map.of("firstName", "John", "lastName", "Doe")
        );

        EnrichmentProcessor processor = new MSISDNProcessor(userData);
        MessageValidator validator = new MessageValidatorImpl();
        EnrichmentService enrichmentService = new EnrichmentService(processor, validator);


        List<Message> messages = List.of(
                new Message("{\"action\": \"button_click\", \"page\": \"book_card\", \"msisdn\": \"88005553535\"}", Message.EnrichmentType.MSISDN),
                new Message("{\"action\": \"page_view\", \"page\": \"home\", \"msisdn\": \"1234567890\"}", Message.EnrichmentType.MSISDN),
                new Message("{\"action\": \"login\", \"page\": \"profile\", \"msisdn\": \"9999999999\"}", Message.EnrichmentType.MSISDN)
        );


        int threadCount = messages.size();
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        List<String> results = new ArrayList<>();


        for (Message message : messages) {
            executor.submit(() -> {
                try {
                    String enrichedMessage = enrichmentService.enrich(message);
                    synchronized (results) {
                        results.add(enrichedMessage);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }


        latch.await(10, TimeUnit.SECONDS);
        executor.shutdown();


        if (results.size() != threadCount) {
            throw new IllegalStateException("Количество результатов не совпадает с количеством сообщений.");
        }

        boolean vasyaEnriched = results.stream().anyMatch(result -> result.contains("\"firstName\":\"Vasya\""));
        boolean johnEnriched = results.stream().anyMatch(result -> result.contains("\"firstName\":\"John\""));
        boolean noEnrichment = results.stream().anyMatch(result -> !result.contains("\"enrichment\""));

        if (vasyaEnriched && johnEnriched && noEnrichment) {
            System.out.println("Тест успешно пройден!");
        } else {
            throw new IllegalStateException("Тест провален: обогащение выполнено некорректно.");
        }
    }
}
