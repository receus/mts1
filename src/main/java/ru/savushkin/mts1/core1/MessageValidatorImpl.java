package ru.savushkin.mts1.core1;

public class MessageValidatorImpl implements MessageValidator{

    @Override
    public boolean validate(Message message) {
        return message != null && message.getContent() != null && message.getEnrichmentType() != null;
    }
}
