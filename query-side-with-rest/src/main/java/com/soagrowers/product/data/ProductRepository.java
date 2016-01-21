package com.soagrowers.product.data;


import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Created by ben on 07/10/15.
 */
@RepositoryRestResource(collectionResourceRel = "product", path = "product")
public interface ProductRepository extends ReadOnlyPagingAndSortingRepository {

    List<Product> findBySaleable(@Param("saleable") boolean saleable);

}
