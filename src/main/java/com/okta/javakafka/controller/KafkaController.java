/*
 * Copyright (C) ThermoFisher Scientific Inc.- All Rights Reserved
 * Unauthorized use or copying of this file, via any medium is strictly prohibited and will be subject to legal action.
 * Proprietary and confidential
 *
 */
package com.okta.javakafka.controller;

import com.okta.javakafka.dto.ProductDTO;
import com.okta.javakafka.service.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/kafka")
public class KafkaController {


    @Autowired
    private KafkaService kafkaService;


    public KafkaController() {

    }

    @PostMapping("/produce")
    public void produce(@RequestBody ProductDTO productDTO, @RequestParam String key) {
        productDTO.setId(key);
        kafkaService.produce(productDTO, key);

    }

    @PostMapping("/producer/nullValue")
    public void ProducerSetNullValue(@RequestParam String key) {
        kafkaService.producerSetNullValue(key);
    }

    @GetMapping("/messages")
    public List<ProductDTO> getMessages() {
        return kafkaService.getMessages();
    }
}
