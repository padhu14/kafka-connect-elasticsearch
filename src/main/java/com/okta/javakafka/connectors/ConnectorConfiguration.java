/*
 * Copyright (C) ThermoFisher Scientific Inc.- All Rights Reserved
 * Unauthorized use or copying of this file, via any medium is strictly prohibited and will be subject to legal action.
 * Proprietary and confidential
 *
 */
package com.okta.javakafka.connectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class ConnectorConfiguration {

    @Autowired
    private RestTemplate restTemplate;

    public void createConnector() {
        final String uri = "http://localhost:8083/connectors/";
        String body = "{\n" +
                "  \"name\": \"SINK_ELASTIC_TEST_KEY\",\n" +
                "  \"config\": {\n" +
                "    \"connector.class\": \"io.confluent.connect.elasticsearch.ElasticsearchSinkConnector\",\n" +
                "    \"tasks.max\": \"1\",\n" +
                "    \"topics\": \"productindex\",\n" +
                "    \"key.ignore\": \"false\",\n" +
                "    \"value.converter.schemas.enable\": \"false\",\n" +
                "    \"key.converter\": \"org.apache.kafka.connect.storage.StringConverter\",\n" +
                "    \"value.converter\": \"org.apache.kafka.connect.json.JsonConverter\",\n" +
                "    \"schema.ignore\": \"true\",\n" +
                "    \"connection.url\": \"http://elasticsearch:9200\",\n" +
                "    \"type.name\": \"_doc\",\n" +
                "    \"name\": \"SINK_ELASTIC_TEST_KEY\",\n" +
                "    \"behavior.on.null.values\": \"DELETE\",\n" +
                "    \"errors.tolerance\": \"all\",\n" +
                "    \"errors.log.enable\": true,\n" +
                "    \"errors.log.include.messages\": true\n" +
                "  }\n" +
                "}";

        ResponseEntity<String> result = restTemplate.postForEntity(uri, body, String.class);
        System.out.println(result);
    }
}
