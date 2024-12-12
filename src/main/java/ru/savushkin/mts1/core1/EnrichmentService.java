package ru.savushkin.mts1.core1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EnrichmentService {
    private final EnrichmentProcessor processor;
    private final MessageValidator validator;

    public String enrich(Message message) {

        if(!validator.validate(message)) {
            return message.getContent();
        }
        if(message.getEnrichmentType() == Message.EnrichmentType.MSISDN){
            return processor.enrich(message.getContent());
        }
        return message.getContent();
    }
    public EnrichmentService(EnrichmentProcessor processor, MessageValidator validator) {
        this.processor = processor;
        this.validator = validator;
    }
}
