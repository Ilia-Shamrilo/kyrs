package com.example.webserviceindastrialproduct.controllers;

import com.example.webserviceindastrialproduct.models.Order;
import com.example.webserviceindastrialproduct.models.Product;
import com.example.webserviceindastrialproduct.models.User;
import com.example.webserviceindastrialproduct.services.OrderService;
import com.example.webserviceindastrialproduct.services.ProductService;
import com.example.webserviceindastrialproduct.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class HomeController {
    ProductService productService;
    UserService userService;
    OrderService orderService;

    @GetMapping("/about")
    public String about(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("authentication", auth);
        List<String> roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        model.addAttribute("roles", roles);
        return "about";
    }

    @GetMapping("/inpr")
    public String home(Model model,
                       @RequestParam(name = "filterBy", required = false, defaultValue = "default") String filterBy,
                       @RequestParam(name = "nameProduct", required = false, defaultValue = "default") String nameProduct) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("authentication", auth);
        List<String> roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        model.addAttribute("roles", roles);
        List<Product> products = productService.findAll();
        List<Product> filterList = new ArrayList<>();

        if (!filterBy.equals("default")) {
            for (Product product : products) {
                if (product.getCategories().name().equals(filterBy)) {
                    filterList.add(product);
                }
            }
        } else {
            filterList.addAll(products);
        }

        if (!nameProduct.equals("default")) {
            // Используем итератор для безопасного удаления элементов
            filterList.removeIf(product -> !product.getName().contains(nameProduct));
        }

        model.addAttribute("products", filterList);
        return "home";
    }


    @PostMapping("/add-to-basket")
    public String addToBucket(@RequestParam("productId") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getName().equals("anonymousUser")) {
            User user = userService.findName(authentication.getName()).get();
            Product product = productService.getProductById(id);
            List<Product> cartProducts = user.getBasketProduct();
            cartProducts.add(product);
            user.setBasketProduct(cartProducts);
            userService.saveUser(user);
            return "redirect:/inpr";
        } else return "authorization";
    }

    @GetMapping("/product")
    public String productPage(Model model,@RequestParam("productId") Long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("authentication", auth);
        List<String> roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        model.addAttribute("roles", roles);
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "product";
    }

    @PostMapping("/delete-from-basket")
    public String deleteFromBasket(@RequestParam("productIdToDeleteFromBasket") Long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findName(auth.getName()).get();
        List<Product> productsOfUser = user.getBasketProduct();
        productsOfUser.remove(productService.getProductById(id));
        user.setBasketProduct(productsOfUser);
        userService.saveUser(user);
        return "redirect:/basket";
    }

    @PostMapping("/create-order")
    public String createOrder(@RequestParam("productIdToCreateOrder") Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findName(auth.getName()).get();
        Product product = productService.getProductById(id);
        Optional<Order> existingOrder = orderService.findOrderByUserAndProduct(user, product);
        if (existingOrder.isPresent()) {
            Order order = existingOrder.get();
            order.setAmountOfProduct(order.getAmountOfProduct() + 1);
            orderService.createOrder(order);
        } else {
            Order order = Order.builder()
                    .user(user)
                    .status("В ожидании оплаты")
                    .product(product)
                    .amountOfProduct(1)
                    .build();
            orderService.createOrder(order);
        }
        List<Product> productsOfUser = user.getBasketProduct();
        productsOfUser.remove(product);
        user.setBasketProduct(productsOfUser);
        userService.saveUser(user);
        return "redirect:/basket";
    }


    @GetMapping("/basket")
    public String yourBucket(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findName(authentication.getName()).get();
        List<Product> products = user.getBasketProduct();
        model.addAttribute("products", products);
        return "basket";
    }

    @GetMapping("/profile")
    public String profilePage(Model model,
                              @RequestParam(name = "filterBy", required = false, defaultValue = "default") String filterBy){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findName(authentication.getName()).get();
        model.addAttribute("user",user);
        List<Order> filterList = new ArrayList<>();
        List<Order> orders = orderService.findByUser(user);
        if(filterBy.equals("default")) model.addAttribute("orders", orders);
        else {
            for(Order order: orders){
                if(order.getStatus().equals(filterBy)) filterList.add(order);
            }
            model.addAttribute("orders", filterList);
        }
        return "profile";
    }

    @PostMapping("/pay-order")
    public String payOrder(@RequestParam("orderIdToPay") Long id){
        Order order = orderService.findById(id);
        order.setStatus("Успешно отправлен");
        orderService.createOrder(order);
        return "redirect:/profile?";
    }
    @PostMapping("/cancel-order")
    public String cancelOrder(@RequestParam("orderIdToCancel") Long id){
        Order order = orderService.findById(id);
        order.setStatus("Отменен");
        orderService.createOrder(order);
        return "redirect:/profile?";
    }
    @PostMapping("/delete-order")
    public String deleteOrder(@RequestParam("orderIdToDelete") Long id){
        Order order = orderService.findById(id);
        orderService.deleteOrder(order);
        return "redirect:/profile?";
    }
    @PostMapping("/restore-order")
    public String restoreOrder(@RequestParam("orderIdToRestore") Long id){
        Order order = orderService.findById(id);
        order.setStatus("В ожидании оплаты");
        orderService.createOrder(order);
        return "redirect:/profile?";
    }
}
