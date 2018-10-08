package com.pankesh.productcommand.configuration;

import org.axonframework.common.jpa.EntityManagerProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@Configuration
public class MyEntityManagerProvider implements EntityManagerProvider {
@Autowired
private EntityManager entityManager;
    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }
}
