package com.example.demo.product.controller;

import com.example.demo.product.dto.ProductDto;
import com.example.demo.product.dto.ProductResponse;
import com.example.demo.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public List<ProductResponse> getSuggestionProduct(){
        return productService.getSuggestionProduct();
    }

    @PostMapping(
            path = "add/",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public  ResponseEntity.BodyBuilder addProduct(@ModelAttribute ProductDto productDto, @RequestParam("file") MultipartFile file) {
        productService.uploadUserProfileImage(productDto, file);
        return ResponseEntity.ok();

    }
    @PostMapping(
            path = "update/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity.BodyBuilder updateProduct(@PathVariable ("id") UUID id,
                                                    @ModelAttribute ProductDto productDto, @RequestParam("file") MultipartFile file) {
        productService.updateProduct(id,productDto, file);
        return ResponseEntity.ok();
    }



}
