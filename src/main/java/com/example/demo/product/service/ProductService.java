package com.example.demo.product.service;

import com.example.demo.product.awss3.BucketName;
import com.example.demo.product.dto.ProductDto;
import com.example.demo.product.dto.ProductResponse;
import com.example.demo.product.model.Product;
import com.example.demo.product.repository.ProductRepository;
import com.example.demo.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.http.entity.ContentType.*;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final FileStore fileStore;

    @Autowired
    public ProductService(ProductRepository productRepository, FileStore fileStore) {
        this.productRepository = productRepository;
        this.fileStore = fileStore;
    }

    public List<ProductResponse> getAllProduct() {
        List<ProductResponse> productResponses = new ArrayList<>();
        productResponses.addAll(productRepository.findAll().stream().map(product -> copyProduct(product)).collect(Collectors.toList()));
        return productResponses;
    }

    public ProductResponse getProduct(UUID id) {
        Product product = productRepository.findById(id);
        return copyProduct(product);
    }

    private ProductResponse copyProduct(Product product) {
        ProductResponse pResponse = new ProductResponse();
        pResponse.setImage(product.getImageLink().get());
        pResponse.setId(product.getId());
        pResponse.setDescription(product.getDescription());
        pResponse.setCost(product.getCost());
        pResponse.setName(product.getName());
        pResponse.setProductCategory(product.getProductCategory());
        return pResponse;
    }

    public void uploadUserProfileImage(ProductDto productDto, MultipartFile file) {
        // 1. Check if image is not empty
        ImageUtils.isFileEmpty(file);
        // 2. If file is an image
        ImageUtils.isImage(file);

        // 3. The product exists in our database


        // 4. Grab some metadata from file if any
        Map<String, String> metadata = ImageUtils.extractMetadata(file);

        Product product = new Product();
        product.setName(productDto.getName());
        product.setCost(productDto.getCost());
        product.setDescription(productDto.getDescription());
        product.setProductCategory(productDto.getProductCategory());
        product.setId(UUID.randomUUID());
        // 5. Store the image in s3 and update database (productProfileImageLink) with s3 image link
        String path = String.format("%s/%s", BucketName.PRODUCT_IMAGE.getBucketName(), product.getId());
        String filename = String.format("%s", file.getOriginalFilename());
//      Exact Product to save


        product.setImageLink(Product.ORIGINAL_PATH + product.getId() + "/" + filename);
        try {
            //save img on AWS
            fileStore.save(path, filename, Optional.of(metadata), file.getInputStream());
            //save product to database
            productRepository.save(product);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

    }

    public List<ProductResponse> getSuggestionProduct() {
        List<ProductResponse> productResponses = new ArrayList<>();
        productResponses.addAll(productRepository.getSuggestionProduct("").stream().map(product -> copyProduct(product)).collect(Collectors.toList()));
        return productResponses;
    }

    public void updateProduct(UUID id,ProductDto productDto, MultipartFile file) {
        Product product = productRepository.findById(id);
        // 1. Check if image is not empty
        ImageUtils.isFileEmpty(file);
        // 2. If file is an image
        ImageUtils.isImage(file);
        // 3. The product exists in our database
        // 4. Grab some metadata from file if any
        Map<String, String> metadata = ImageUtils.extractMetadata(file);
        String path = String.format("%s/%s", BucketName.PRODUCT_IMAGE.getBucketName(), product.getId());
        String filename = String.format("%s", file.getOriginalFilename());
        try {
            String oldFilename = product.getImageLink().get().substring(Product.ORIGINAL_PATH.length(), product.getImageLink().get().length());
            fileStore.deleteFile( oldFilename);
            //save img on AWS
            fileStore.save(path, filename, Optional.of(metadata), file.getInputStream());
            //save product to database
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        product.setName(productDto.getName());
        product.setCost(productDto.getCost());
        product.setDescription(productDto.getDescription());
        product.setProductCategory(productDto.getProductCategory());
        product.setImageLink(Product.ORIGINAL_PATH + product.getId() + "/" + filename);
        productRepository.save(product);

    }
}
