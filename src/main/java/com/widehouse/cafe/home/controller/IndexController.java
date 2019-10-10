package com.widehouse.cafe.home.controller;

import com.widehouse.cafe.cafe.entity.Category;
import com.widehouse.cafe.cafe.service.CategoryService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by kiel on 2017. 2. 21..
 */
@Controller
public class IndexController {
    @Autowired
    private CategoryService categoryService;

    /**
     * index home.
     * @param model {@link Model}
     * @return
     */
    @GetMapping("/")
    public String index(Model model) {
        List<Category> categories = categoryService.findAll(CategoryService.ORDER);
        model.addAttribute("categories", categories);

        return "index.vue";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/createCafe")
    public String createCafe() {
        return "create_cafe";
    }
}
