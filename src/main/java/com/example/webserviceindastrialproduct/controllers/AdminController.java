package com.example.webserviceindastrialproduct.controllers;

import com.example.webserviceindastrialproduct.models.Product;
import com.example.webserviceindastrialproduct.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class AdminController {
    ProductService productService;

    @GetMapping("/add-product")
    public String addProduct(Model model){
        model.addAttribute("product", new Product());
        return "admin/addProduct";
    }
    @PostMapping("/add-product")
    public String saveProduct(@ModelAttribute("product") Product product,
                              @RequestParam(value = "images", required = false) MultipartFile[] files){
        List<String> photos = new ArrayList<>();
        for (MultipartFile file: files){
            if(!file.isEmpty()){
                try {
                    String filename = file.getOriginalFilename();
                    File upload = new File("/Users/ilyashamrilo/Downloads/курсовая 2 сем/WebServiceIndastrialProduct/src/main/resources/static/images/" + filename);
                    file.transferTo(upload);
                    photos.add(filename);
                } catch (IOException e){
                    System.out.println("Файл не найден");
                }
            }
        }
        product.setPhotos(photos);
        productService.addProduct(product);
        return "redirect:/add-product";
    }
}
