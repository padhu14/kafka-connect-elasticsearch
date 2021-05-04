/*
 * Copyright (C) ThermoFisher Scientific Inc.- All Rights Reserved
 * Unauthorized use or copying of this file, via any medium is strictly prohibited and will be subject to legal action.
 * Proprietary and confidential
 *
 */
package com.okta.javakafka.controller;

import com.okta.javakafka.dto.ProductDTO;
import com.okta.javakafka.service.ElasticSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/elasticSearch")
public class ElasticSearchController {

    @Autowired
    private ElasticSearchService elasticSearchService;

    @GetMapping("/search")
    public List<String> fetchSuggestions(@RequestParam String key) {

        return elasticSearchService.fetchSuggestions(key);
    }

    @GetMapping("/index")
    public void createIndex(@RequestParam String indexName) {
        if (!elasticSearchService.indexExists(indexName)) {
            elasticSearchService.createIndex();
        }
    }

    @GetMapping("/allValues")
    public List<ProductDTO> getALlValues() {
        return elasticSearchService.getALlValues();
    }
}
