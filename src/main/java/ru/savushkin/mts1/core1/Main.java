package ru.savushkin.mts1.core1;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        Map<String, Map<String, String>> userData = new HashMap<>();
        userData.put("88005553535", Map.of("firstName", "Vasya", "lastName", "Ivanov"));
        userData.put("1234567890", Map.of("firstName", "John", "lastName", "Doe"));

        EnrichmentProcessor msisdnProcessor = new MSISDNProcessor(userData);

        EnrichmentService enrichmentService = new EnrichmentService(
                msisdnProcessor,
                new MessageValidatorImpl()
        );

        Message message = new Message(
                "{\"action\": \"button_click\", \"page\": \"book_card\", \"msisdn\": \"88005553535\"}",
                Message.EnrichmentType.MSISDN
        );

        String enrichedMessage = enrichmentService.enrich(message);

        System.out.println("Исходное сообщение: " + message.getContent());
        System.out.println("Обогащенное сообщение: " + enrichedMessage);
    }
}