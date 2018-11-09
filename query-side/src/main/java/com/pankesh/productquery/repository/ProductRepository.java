package com.pankesh.productquery.repository;


import com.pankesh.productquery.domain.Product;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


@RepositoryRestResource(collectionResourceRel = "products", path = "products")
public interface ProductRepository extends ReadOnlyPagingAndSortingRepository {
    public List<Product> findBySaleable(@Param("saleable") boolean saleable);
    public List<Product> findByDelivered(@Param("delivered") boolean delivered);
}