package com.example.demo.product.controller;

import com.example.demo.product.dto.ProductDto;
import com.example.demo.product.dto.ProductResponse;
import com.example.demo.product.model.Product;
import com.example.demo.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products/")
@CrossOrigin("*")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductResponse> getAllProduct() {
        return productService.getAllProduct();
    }
    @GetMapping("{id}/")
    public ProductResponse getProduct(@PathVariable ("id") UUID id) {
        return productService.getProduct(id);
    }

    @GetMapping("suggestion/")
    public List<Product> getSuggestionProduct(){
        return productService.getSuggestionProduct();
    }

    @PostMapping(
            path = "add/",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public void addProduct(@ModelAttribute ProductDto productDto, @RequestParam("file") MultipartFile file) {
        productService.uploadUserProfileImage(productDto, file);
    }
    @PostMapping(
            path = "update/",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public void updateProduct(@ModelAttribute ProductDto productDto, @RequestParam("file") MultipartFile file) {
//        productService.updateProduct(productDto, file);
    }



}
