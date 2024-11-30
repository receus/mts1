package ru.savushkin.mts1.core1;

public class Message {

    private String content;
    private EnrichmentType enrichmentType;

    public Message(String content, EnrichmentType enrichmentType) {
        this.content = content;
        this.enrichmentType = enrichmentType;
    }

    public enum EnrichmentType {
        MSISDN;
    }

    public String getContent() {
        return content;
    }

    public EnrichmentType getEnrichmentType() {
        return enrichmentType;
    }
}
