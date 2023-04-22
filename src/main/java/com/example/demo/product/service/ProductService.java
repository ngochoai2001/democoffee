package com.example.demo.product.service;

import com.example.demo.product.awss3.BucketName;
import com.example.demo.product.dto.ProductDto;
import com.example.demo.product.dto.ProductResponse;
import com.example.demo.product.model.Product;
import com.example.demo.product.repository.ProductRepository;
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
    public ProductService(ProductRepository productRepository,
                          FileStore fileStore) {
        this.productRepository = productRepository;
        this.fileStore = fileStore;
    }

    public List<ProductResponse> getAllProduct() {
        List<ProductResponse> productResponses = new ArrayList<>();
        productResponses.addAll(
                productRepository.findAll().stream().map(product -> copyProduct(product))
                        .collect(Collectors.toList()));
        return  productResponses;
    }
    public ProductResponse getProduct(UUID id){
        Product product = productRepository.findById(id);
        return copyProduct(product);
    }
    private ProductResponse copyProduct(Product product){
        ProductResponse pResponse = new ProductResponse();
        pResponse.setImage(product.getImageLink().get());
        pResponse.setId(product.getId());
        pResponse.setDescription(product.getDescription());
        pResponse.setCost(product.getCost());
        pResponse.setName(product.getName());
        pResponse.setProductCategory(product.getProductCategory());
        return pResponse;
    }

    public void uploadUserProfileImage(ProductDto productDto, MultipartFile file ) {
        // 1. Check if image is not empty
        isFileEmpty(file);
        // 2. If file is an image
        isImage(file);

        // 3. The product exists in our database


        // 4. Grab some metadata from file if any
        Map<String, String> metadata = extractMetadata(file);

        Product product = new Product();
        product.setName(productDto.getName());
        product.setCost(productDto.getCost());
        product.setDescription(productDto.getDescription());
        product.setProductCategory(productDto.getProductCategory());
        product.setId(UUID.randomUUID());
        // 5. Store the image in s3 and update database (productProfileImageLink) with s3 image link
        String path = String.format("%s/%s", BucketName.PRODUCT_IMAGE.getBucketName(), product.getId());
        String filename = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());
//      Exact Product to save


        product.setImageLink(Product.ORIGINAL_PATH+ UUID.randomUUID()+"/"+filename);
        try {
            //save img on AWS
            fileStore.save(path, filename, Optional.of(metadata), file.getInputStream());
           //save product to database
            productRepository.save(product);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

    }

    public byte[] downloadUserProfileImage(Product product) {

        String path = String.format("%s/%s",
                BucketName.PRODUCT_IMAGE.getBucketName(),
                product.getId());

        return product.getImageLink()
                .map(key -> fileStore.download(path, key))
                .orElse(new byte[0]);

    }

    private Map<String, String> extractMetadata(MultipartFile file) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        return metadata;
    }

    private Product getUserProfileOrThrow(UUID productId) {
        return productRepository
                .findAll()
                .stream()
                .filter(productProfile -> productProfile.getImageLink().equals(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("User profile %s not found", productId)));
    }

    private void isImage(MultipartFile file) {
        if (!Arrays.asList(
                IMAGE_JPEG.getMimeType(),
                IMAGE_PNG.getMimeType(),
                IMAGE_GIF.getMimeType(),
                IMAGE_SVG.getMimeType()
                ).contains(file.getContentType())) {
            throw new IllegalStateException("File must be an image [" + file.getContentType() + "]");
        }
    }

    private void isFileEmpty(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalStateException("Cannot upload empty file [ " + file.getSize() + "]");
        }
    }

    public List<Product> getSuggestionProduct() {
        return productRepository.getSuggestionProduct("");
    }
}
