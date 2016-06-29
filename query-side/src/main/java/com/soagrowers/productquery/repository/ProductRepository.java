package com.soagrowers.productquery.repository;


import com.soagrowers.productquery.domain.Product;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Created by ben on 07/10/15.
 */
@SuppressWarnings("unchecked")
@RepositoryRestResource(collectionResourceRel = "products", path = "products")
public interface ProductRepository extends ReadOnlyPagingAndSortingRepository {
    public List<Product> findBySaleable(@Param("saleable") boolean saleable);
}