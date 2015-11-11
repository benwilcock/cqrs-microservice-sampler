package com.soagrowers.todo.data;


import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Created by ben on 07/10/15.
 */
@RepositoryRestResource(collectionResourceRel = "todo", path = "todo")
public interface TodoRepository extends ReadOnlyPagingAndSortingRepository {

    List<Todo> findByStatus(@Param("status") boolean status);

}
