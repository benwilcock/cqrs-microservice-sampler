package com.soagrowers.productquery.domain;


//import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by ben on 07/10/15.
 */
@Entity
public class Product {

    @Id
    private String id;

    private String name;
    private boolean saleable;

    public Product() {
    }

    public Product(String id, String name, boolean saleable) {
        this.id = id;
        this.name = name;
        this.saleable = saleable;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSaleable() {
        return saleable;
    }

    public void setSaleable(boolean saleable) {
        this.saleable = saleable;
    }
}
