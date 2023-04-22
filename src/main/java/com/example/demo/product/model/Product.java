package com.example.demo.product.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Entity
@Data
public class Product {
    @Transient
    public static final String ORIGINAL_PATH="https://magic-coffee-img.s3.amazonaws.com/";
    @Id
    UUID id;
    private String imageLink;
    String name;
    double cost;
    String description;
    @Enumerated(EnumType.STRING)
    private ProductCategory productCategory;





    public Optional<String> getImageLink() {
        return Optional.ofNullable(imageLink);
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product that = (Product) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(imageLink, that.imageLink);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, imageLink);
    }
}
