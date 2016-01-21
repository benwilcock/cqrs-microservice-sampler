package com.soagrowers.product.data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by ben on 07/10/15.
 */

@Entity
public class Product {

    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO) - Left off as this demo is read-only
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
