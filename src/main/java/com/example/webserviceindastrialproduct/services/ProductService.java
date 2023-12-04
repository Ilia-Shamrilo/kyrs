package com.example.webserviceindastrialproduct.services;

import com.example.webserviceindastrialproduct.models.Product;
import com.example.webserviceindastrialproduct.models.User;
import com.example.webserviceindastrialproduct.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

//    public List<Product> getAllProducts() {
//        return productRepository.findAll();
//    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found with id: " + id));
    }
    public void addProduct(Product product) {
        productRepository.save(product);
    }

//    public void updateProduct(Long id, Product updatedProduct) {
//        Product existingProduct = getProductById(id);
//        existingProduct.setName(updatedProduct.getName());
//        existingProduct.setDescription(updatedProduct.getDescription());
//        existingProduct.setPrice(updatedProduct.getPrice());
//        // Другие обновления полей, если необходимо
//        productRepository.save(existingProduct);
//    }
    public List<Product> findAll(){
        return productRepository.findAll();
    }
    @Transactional
    public void deleteProduct(Long id) {
        for(User user: productRepository.findById(id).get().getUsers()){
            user.getBasketProduct().remove(productRepository.findById(id).get());
        }
        productRepository.deleteById(id);
    }
    public void deletePhotos(Product product){
        for(String photo: product.getPhotos()){
            Path path = Paths.get("/Users/ilyashamrilo/Downloads/курсовая 2 сем/WebServiceIndastrialProduct/src/main/resources/static/images/", photo);
            try{
                Files.delete(path);
            } catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }
}
