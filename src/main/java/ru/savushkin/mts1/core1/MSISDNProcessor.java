package ru.savushkin.mts1.core1;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class MSISDNProcessor implements EnrichmentProcessor{
    private final Map<String, Map<String, String>> enrichmentData;

    public MSISDNProcessor(Map<String, Map<String, String>> enrichmentData) {
        this.enrichmentData = enrichmentData;
    }

    @Override
    public String enrich(String content) {
        try{JSONObject jsonObject = new JSONObject(content);
            if (jsonObject.has("msisdn")){
                String msisdn = jsonObject.getString("msisdn");
                Map<String, String> userData = enrichmentData.get(msisdn);
                if (userData != null) {
                    JSONObject enrichment = new JSONObject(userData);
                    jsonObject.put("enrichment", enrichment);
                }
            }

            return jsonObject.toString();

        } catch (JSONException e) {
            return content;
        }
    }
}
