package com.example.demo.product.awss3;

public enum BucketName {

    PRODUCT_IMAGE("magic-coffee-img");

    private final String bucketName;

    BucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }
}
