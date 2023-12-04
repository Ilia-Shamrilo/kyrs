package com.example.webserviceindastrialproduct.controllers;

import com.example.webserviceindastrialproduct.models.Product;
import com.example.webserviceindastrialproduct.models.User;
import com.example.webserviceindastrialproduct.services.ProductService;
import com.example.webserviceindastrialproduct.services.UserService;
import lombok.AllArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class AdminController {
    ProductService productService;
    UserService userService;

    @GetMapping("/add-product")
    public String addProduct(Model model){
        model.addAttribute("product", new Product());
        return "admin/addProduct";
    }
    @PostMapping("/add-product")
    public String saveProduct(@ModelAttribute("product") Product product,
                              @RequestParam("characteristics") String characteristics,
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
        List<String> characteristicsOfProduct = List.of(characteristics.split(","));
        product.setCharacteristics(characteristicsOfProduct);
        product.setPhotos(photos);
        productService.addProduct(product);
        return "redirect:add-product";
    }

    @GetMapping("/admin-panel")
    public String adminPanel(Model model){
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "admin/panelAdmin";
    }

    @GetMapping("/product-edit")
    public String productEdit(Model model, @RequestParam("productIdToEdit") Long id){
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        String characteristics = product.getCharacteristics().get(0);
        for(int i=1;i < product.getCharacteristics().size();i++){
            characteristics = characteristics + "," + product.getCharacteristics().get(i);
        }
        model.addAttribute("characteristics",characteristics);
        return "admin/adminProductEdit";
    }
    @PostMapping("/delete-photo")
    public String deletePhoto(@RequestParam("photo") String photo,
                              @RequestParam("productId") Long id,
                              RedirectAttributes redirectAttributes){
        Product product = productService.getProductById(id);
        List<String> photos = product.getPhotos();
        if(photos.size() != 1){
            photos.remove(photo);
            product.setPhotos(photos);
            Path path = Paths.get("/Users/ilyashamrilo/Downloads/курсовая 2 сем/WebServiceIndastrialProduct/src/main/resources/static/images/", photo);
            productService.addProduct(product);
            try {
                Files.delete(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            redirectAttributes.addFlashAttribute("errorOfLastPhoto", "Для начала надо добавить новую фотку");
        }
        redirectAttributes.addAttribute("productIdToEdit",id);
        return "redirect:/product-edit";
    }
    @PostMapping("/product-edit")
    public String productEdit(@ModelAttribute("product") Product product,
                              @RequestParam("characteristics") String characteristics,
                              @RequestParam("id") Long id,
                              @RequestParam(value = "images", required = false) MultipartFile[] files,
                              RedirectAttributes redirectAttributes) {
        Product pastProduct = productService.getProductById(id);
        List<String> photos = pastProduct.getPhotos();
        if(files != null) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    try {
                        String filename = file.getOriginalFilename();
                        File upload = new File("/Users/ilyashamrilo/Downloads/курсовая 2 сем/WebServiceIndastrialProduct/src/main/resources/static/images/" + filename);
                        file.transferTo(upload);
                        photos.add(filename);
                    } catch (IOException e) {
                        System.out.println("Файл не найден");
                    }
                }
            }
        }
        product.setId(id);
        product.setPhotos(photos);
        product.setOrders(pastProduct.getOrders());
        product.setUsers(pastProduct.getUsers());
        List<String> characteristicsOfProduct = List.of(characteristics.split(","));
        product.setCharacteristics(characteristicsOfProduct);
        productService.addProduct(product);
        redirectAttributes.addAttribute("productIdToEdit", id);
        return "redirect:/product-edit";
    }
    @PostMapping("/delete-product")
    public String deleteProduct(@RequestParam("productIdToDelete") Long id){
        productService.deletePhotos(productService.getProductById(id));
        productService.deleteProduct(id);
        return "redirect:/admin-panel";
    }
    @PostMapping("/delete-user")
    public String deleteUser(@RequestParam("userIdToDelete") Long id){
        userService.deleteUser(id);
        return "redirect:/admin-panel";
    }
}
