package com.ln.training.app.controller.api;

import com.ln.training.app.repository.entity.BrandEntity;
import com.ln.training.app.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(value = { "/home"})
public class BrandControllerApi {

    BrandRepository brandRepository;

    @Autowired
    public BrandControllerApi(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @GetMapping
    public String getAll(Model model){
        List<BrandEntity> list = brandRepository.findAll();
        model.addAttribute("list", list);
        return "index";
    }
}
