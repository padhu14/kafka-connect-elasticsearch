/*
 * Copyright (C) ThermoFisher Scientific Inc.- All Rights Reserved
 * Unauthorized use or copying of this file, via any medium is strictly prohibited and will be subject to legal action.
 * Proprietary and confidential
 *
 */
package com.okta.javakafka.service;

import com.okta.javakafka.Entity.ProductEntity;
import com.okta.javakafka.Repository.ProductRepository;
import com.okta.javakafka.dto.ProductDTO;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ElasticSearchService {

    public static final String PRODUCT_INDEX = "productindex";

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;


    public void createProductIndex(final ProductDTO product) {
        ProductEntity productEntity = new ProductEntity();
        BeanUtils.copyProperties(product, productEntity);
        productRepository.save(productEntity);
    }

    public boolean createIndex() {
        IndexQuery indexQuery = new IndexQuery();
        elasticsearchOperations.index(indexQuery,IndexCoordinates.of(""));
        IndexOperations indexOperations =  elasticsearchOperations.indexOps(IndexCoordinates.of(""));
        Document document = Document.create();
        return indexOperations.create();
    }

    public boolean indexExists(String indexName) {
        return elasticsearchOperations.indexExists(indexName);
    }

    public List<String> createProductIndexBulk(final List<ProductEntity> products) {

        List<IndexQuery> queries = products.stream()
                .map(product ->
                        new IndexQueryBuilder()
                                .withId(product.getId())
                                .withObject(product).build())
                .collect(Collectors.toList());

        return elasticsearchOperations.bulkIndex(queries, IndexCoordinates.of(PRODUCT_INDEX));
    }

    public String createProductIndex(ProductEntity product) {

        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(product.getId())
                .withObject(product).build();

        String documentId = elasticsearchOperations
                .index(indexQuery, IndexCoordinates.of(PRODUCT_INDEX));

        return documentId;
    }

    public List<ProductEntity> findProductsByBrand(final String brandName) {

        MatchQueryBuilder queryBuilder = QueryBuilders
                .matchQuery("manufacturer", brandName);

        Query searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder).build();

        SearchHits<ProductEntity> productHits = elasticsearchOperations
                .search(searchQuery, ProductEntity.class, IndexCoordinates.of(PRODUCT_INDEX));
        return productHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
    }

    public List<ProductEntity> findByProductName(final String productName) {
        Query searchQuery = new StringQuery(
                "{\"match\":{\"name\":{\"query\":" + productName + "}}}");

        SearchHits<ProductEntity> productHits = elasticsearchOperations.search(
                searchQuery, ProductEntity.class, IndexCoordinates.of(PRODUCT_INDEX));
        return productHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
    }


    public List<String> fetchSuggestions(String query) {
        QueryBuilder queryBuilder = QueryBuilders
                .wildcardQuery("name", "*" + query + "*");

        Query searchQuery = new NativeSearchQueryBuilder()
                .withFilter(queryBuilder)
                .withPageable(PageRequest.of(0, 10))
                .build();

        SearchHits<ProductEntity> searchSuggestions =
                elasticsearchOperations.search(searchQuery,
                        ProductEntity.class,
                        IndexCoordinates.of(PRODUCT_INDEX));

        List<String> suggestions = new ArrayList<>();

        searchSuggestions.getSearchHits().forEach(searchHit -> {
            suggestions.add(searchHit.getContent().getName());
        });
        return suggestions;
    }

    public void getIndex() {
        IndexOperations indexOperations = elasticsearchOperations.indexOps(ProductEntity.class);
        System.out.println(indexOperations.getMapping());
    }

    public ProductDTO getObjectById(String id) {
        ProductDTO productDTO = new ProductDTO();
        Optional<ProductEntity> productEntityOptional = productRepository.findById(id);
        productEntityOptional.ifPresent(productEntity -> BeanUtils.copyProperties(productEntity, productDTO));
        return productDTO;
    }

    public List<ProductDTO> getALlValues() {
        List<ProductDTO> productDTOS = new ArrayList<>();
        Iterable<ProductEntity> productEntities = productRepository.findAll();
        productEntities.forEach(productEntity -> {
            ProductDTO productDTO = new ProductDTO();
            BeanUtils.copyProperties(productEntity, productDTO);
            productDTOS.add(productDTO);
        });

        return productDTOS;
    }
}
