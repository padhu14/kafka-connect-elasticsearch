/*
 * Copyright (C) ThermoFisher Scientific Inc.- All Rights Reserved
 * Unauthorized use or copying of this file, via any medium is strictly prohibited and will be subject to legal action.
 * Proprietary and confidential
 *
 */
package com.okta.javakafka.Repository;

import com.okta.javakafka.Entity.ProductEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends ElasticsearchRepository<ProductEntity, String> {
    List<ProductEntity> findByName(String name);

    List<ProductEntity> findByNameContaining(String name);

    List<ProductEntity> findByManufacturerAndCategory(String manufacturer, String category);

}
