/*
 * Copyright (C) ThermoFisher Scientific Inc.- All Rights Reserved
 * Unauthorized use or copying of this file, via any medium is strictly prohibited and will be subject to legal action.
 * Proprietary and confidential
 *
 */
package com.okta.javakafka.service;

import com.okta.javakafka.constants.IKafkaConstants;
import com.okta.javakafka.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class KafkaService {

    private final List<ProductDTO> messages = new ArrayList<>();
    @Autowired
    private KafkaTemplate<String, ProductDTO> template;


    public void produce(ProductDTO productDTO, String key) {
        template.executeInTransaction(operations -> operations.send(IKafkaConstants.TOPIC_NAME, key, productDTO));

    }

    public void producerSetNullValue(String key) {
        template.executeInTransaction(operations -> operations.send(IKafkaConstants.TOPIC_NAME, key, null));

    }

    @KafkaListener(topics = IKafkaConstants.TOPIC_NAME, groupId = IKafkaConstants.GROUP_ID_CONFIG)
    public void listen(@Payload(required = false) ProductDTO productDTO, @Headers MessageHeaders headers) {
        synchronized (messages) {
            System.out.println("Message headers : " + headers);
            messages.add(productDTO);
        }
    }

    public List<ProductDTO> getMessages() {
        return messages;
    }
}
