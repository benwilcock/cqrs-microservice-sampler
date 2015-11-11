package com.soagrowers.todo.data;


import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * Created by ben on 07/10/15.
 */
@NoRepositoryBean
public interface ReadOnlyPagingAndSortingRepository extends PagingAndSortingRepository<Todo, String> {


    @Override
    @RestResource(exported = false) //true means the capability will be offered
    Todo save(Todo entity);

    @Override
    @RestResource(exported = false) //false restricts the capability
    void delete(String aLong);

    @Override
    @RestResource(exported = false)
    void delete(Todo entity);
}
